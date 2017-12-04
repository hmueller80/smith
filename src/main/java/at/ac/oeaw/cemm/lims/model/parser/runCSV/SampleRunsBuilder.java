/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.runCSV;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.model.parser.CSVValidationStatus;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
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
    
    public ValidatedCSV<Set<SampleRunDTO>> buildSampleRunsFromCSV(File csvFile, ServiceFactory services, UserDTO operator){
       
        //This objects maps sample Id -> sampleRuns)
        Map<Integer,SampleRunDTO> samples = new HashMap<>();
        Map<String,Set<String>> lanesCheck = new HashMap<>();
                
        String runFolder = csvFile.getName().replace(".csv","");
                
        CSVValidationStatus validationStatus = new CSVValidationStatus();
        
        Integer previousRunId = null;
        String previousFlowcell = null;
        
        try {
            Reader reader = new FileReader(csvFile);

            CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withHeader(RunFormCSVHeader.class).withRecordSeparator(',').withSkipHeaderRecord().withIgnoreEmptyLines());
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
                              
                int sampleId = getIdFromField(record,RunFormCSVHeader.Sample);
                SampleDTO sample = services.getSampleService().getFullSampleById(sampleId);
                if (sample==null){
                    validationStatus.addFailMessage("Sample ID", "Sample with id "+sampleId+" not found");
                    break;
                }
                if (sample.getUser()==null){
                    validationStatus.addFailMessage("User ID", "Sample with id "+sampleId+" has no user asigned");
                    break;
                }
           
                String laneName = getStringFromField(record,RunFormCSVHeader.Lane);
                
                Set<String> indexesInLane = lanesCheck.get(laneName);
                String sampleIndex = sample.getIndex().getIndex();
                if (indexesInLane==null){
                    indexesInLane = new HashSet<>();
                    indexesInLane.add(sampleIndex);
                    lanesCheck.put(laneName, indexesInLane);
                }else{
                    if (indexesInLane.contains(sampleIndex)){
                        validationStatus.addFailMessage("Index consistency", "Sample with id "+sampleId+" and Index "+sampleIndex+ " conflicts with another sample");
                        break;
                    }else{
                        indexesInLane.add(sampleIndex);
                    }
                }
                
                String flowCell= getStringFromField(record,RunFormCSVHeader.Flowcell);
                if (previousFlowcell==null){
                    previousFlowcell = flowCell;
                }else if (!previousFlowcell.equals(flowCell)){
                    validationStatus.addFailMessage("Flowcell", "Multiple flowcells found in the same request:" +previousFlowcell+","+flowCell);
                    break;
                }
                
                if (!samples.containsKey(sampleId)){
                    SampleRunDTO newSampleRun = myDTOFactory.getSampleRunDTO(
                            runId, 
                            sample, 
                            operator, 
                            previousFlowcell, 
                            null, 
                            runFolder,
                            false);
                    samples.put(sampleId,newSampleRun);
                }
                    
                SampleRunDTO currentSampleRun = samples.get(sampleId);
                currentSampleRun.addLane(laneName);  
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
    
    private static String getStringFromField(CSVRecord record, RunFormCSVHeader field) throws ParsingException{
        String value = record.get(field);
         if (value==null || value.trim().isEmpty()){
             throw new ParsingException("Value in field "+field.name(),"The value is empty or null");
         }
         
         return value;
    }
    
}
