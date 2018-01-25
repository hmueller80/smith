/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.runCSV;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.model.parser.CSVValidationStatus;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class SampleRunsBuilder {
    
    @Inject private DTOFactory myDTOFactory;
    @Inject private ServiceFactory services;

    public ValidatedCSV<Set<SampleRunDTO>> buildSampleRunsFromCSV(File csvFile, UserDTO operator){
       
        //This objects maps sample Id -> sampleRuns)
        Map<Integer,SampleRunDTO> samples = new HashMap<>();
        Map<String,Set<String>> lanesCheck = new HashMap<>();
        Map<String,Set<Integer>> libraryToSampleInCSV = new HashMap<>(); 
        Map<String,Set<Integer>> libraryToSampleInDB = new HashMap<>(); 

        String runFolder = csvFile.getName().replace(".csv","");
                
        CSVValidationStatus validationStatus = new CSVValidationStatus();
        
        Integer previousRunId = null;
        String previousFlowcell = null;
        
        try {
            Reader reader = new FileReader(csvFile);

            CSVParser parser = new CSVParser(reader, 
                    CSVFormat.RFC4180
                            .withHeader(RunFormCSVHeader.class)
                            .withRecordSeparator(',')
                            .withSkipHeaderRecord()
                            .withIgnoreEmptyLines()
                            .withAllowMissingColumnNames());
            List<CSVRecord> records = parser.getRecords();            
            
            for (CSVRecord record: records){             
                int runId =  getIdFromField(record,RunFormCSVHeader.BSFID);
                if (previousRunId == null) {
                    previousRunId = runId ;
                } else if (previousRunId != runId) {
                    validationStatus.addFailMessage("Run ID", "Multiple runs found in the same request:" +previousRunId+","+runId);
                    break;
                }
                
                if (services.getRunService().runExists(runId)){
                    validationStatus.addFailMessage("Run ID", "A run with the id "+runId+" already exists");
                    break;
                }
                             
                Integer submissionId = getSubmissionId(record);
    
                SampleDTO sample;
                String sampleIdentifier;
                if (submissionId == null) {
                    int sampleId = getIdFromField(record, RunFormCSVHeader.Sample);
                    sampleIdentifier = "id="+String.valueOf(sampleId);
                    sample = services.getSampleService().getFullSampleById(sampleId);
                } else{
                    String sampleName = NameFilter.getSampleNameWithoutSuffix(getStringFromField(record, RunFormCSVHeader.SampleName));
                    String libraryName = NameFilter.getLibraryNameWithoutSuffix(getStringFromField(record, RunFormCSVHeader.LibraryName));
                            
                    sampleIdentifier = "request= "+submissionId +",library ="+libraryName+", name="+sampleName;
                    
                    sample = services.getSampleService().getFullSampleByRequestLibraryName(submissionId, libraryName, sampleName);
                }
                    
            
                if (sample==null){
                    validationStatus.addFailMessage("Sample ID", "Sample with "+sampleIdentifier+" not found");
                    break;
                }
                if (sample.getUser()==null){
                    validationStatus.addFailMessage("User ID", "Sample with "+sampleIdentifier+" has no user asigned");
                    break;
                }
           
                String laneName = getStringFromField(record,RunFormCSVHeader.Lane);
                
                Set<String> indexesInLane = lanesCheck.get(laneName);
                String sampleIndex = sample.getCompoundIndex();
                if (indexesInLane==null){
                    indexesInLane = new HashSet<>();
                    lanesCheck.put(laneName, indexesInLane);
                }else{
                    if (indexesInLane.contains(sampleIndex)){
                        validationStatus.addFailMessage("Index consistency", "Sample with id "+sample.getId()+" and Index "+sampleIndex+ " conflicts with another sample");
                        break;
                    }
                }
                if (!sampleIndex.equals(RequestFormDTO.NO_DEMUX_INDEX)){
                    indexesInLane.add(sampleIndex);
                }

                String flowCell= getStringFromField(record,RunFormCSVHeader.Flowcell);
                if (previousFlowcell==null){
                    previousFlowcell = flowCell;
                    List<SampleRunDTO> existingRuns = services.getRunService().getRunsByFlowCell(flowCell);
                    if (existingRuns!= null && !existingRuns.isEmpty()){
                        validationStatus.addFailMessage("Flowcell", "A run for flowcell " +flowCell+"already exists");
                        break;
                    }
                }else if (!previousFlowcell.equals(flowCell)){
                    validationStatus.addFailMessage("Flowcell", "Multiple flowcells found in the same request:" +previousFlowcell+","+flowCell);
                    break;
                }
                
                String experimentName = String.format("BSF_%04d", runId);
                if (!samples.containsKey(sample.getId())){
                    SampleRunDTO newSampleRun = myDTOFactory.getSampleRunDTO(
                            runId, 
                            sample, 
                            operator, 
                            previousFlowcell, 
                            null, 
                            runFolder,
                            false,
                            experimentName);
                    samples.put(sample.getId(),newSampleRun);
                }
                    
                SampleRunDTO currentSampleRun = samples.get(sample.getId());
                currentSampleRun.addLane(laneName);  
                
                String libraryId = sample.getLibraryName();
                Set<Integer> samplesInLibrary = libraryToSampleInCSV.get(libraryId);
                Set<Integer> samplesInDB = libraryToSampleInDB.get(libraryId);
                if (samplesInLibrary == null){
                    samplesInLibrary = new HashSet<>();
                    libraryToSampleInCSV.put(libraryId, samplesInLibrary);     
                }
                samplesInLibrary.add(sample.getId());

                if (samplesInDB == null){
                    samplesInDB = new HashSet<>();
                    libraryToSampleInDB.put(libraryId, samplesInDB);
                    for (SampleDTO pooledSample: services.getSampleService().getAllPooledSamples(sample)){
                        samplesInDB.add(pooledSample.getId());
                    }
                }
            }
            
            for (String library: libraryToSampleInCSV.keySet()){
                Set<Integer> samplesInCSV = libraryToSampleInCSV.get(library);
                Set<Integer> samplesInDB = libraryToSampleInDB.get(library);
                if (samplesInDB == null || !samplesInCSV.equals(samplesInDB)){
                    validationStatus.addFailMessage("Library Consistency", "Not all of the samples of library "+library+" are in run form");
                }
            }
  
        } catch (IOException e){
            validationStatus.addFailMessage("CSV Parsing", e.getMessage());
        } catch (ParsingException e){
            validationStatus.addFailMessage(e.getSummary(), e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            validationStatus.addFailMessage("Database Error", e.getMessage());
        }
        
        return new ValidatedCSV(new HashSet<>(samples.values()),validationStatus);
        
    }
    
    private static int getIdFromField(CSVRecord record, RunFormCSVHeader field) throws ParsingException{
        String id = record.get(field);
         if (id==null || id.trim().isEmpty()){
             throw new ParsingException("ID in field "+field.name(),"The id is empty or null");
         }
         try{
             return Integer.parseInt(id);
         }catch(NumberFormatException e){
            throw new ParsingException("ID in field "+field.name(),"The id is not an integer");
         } 
    }
    
    private static Integer getSubmissionId(CSVRecord record) throws ParsingException {
        if (record.isSet(RunFormCSVHeader.SubmissionID.toString())){
            return getIdFromField(record, RunFormCSVHeader.SubmissionID);
        }
        
        return null;
    }
    
    private static String getStringFromField(CSVRecord record, RunFormCSVHeader field) throws ParsingException{
        String value = record.get(field);
         if (value==null || value.trim().isEmpty()){
             throw new ParsingException("Value in field "+field.name(),"The value is empty or null");
         }
         
         return value;
    }
    
}
