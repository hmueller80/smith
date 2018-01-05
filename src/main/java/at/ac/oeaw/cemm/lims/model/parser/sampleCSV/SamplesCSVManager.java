/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author dbarreca
 */
public class SamplesCSVManager {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
   
    public static void writeToFile(RequestDTO request) throws Exception {
        File theFile = getFile(request);
        if (theFile.exists()){
            handleDelete(request);
        }
        
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(theFile);
            fileWriter.append(SampleRequestCSVHeader.getHeaderLine());
            fileWriter.append("\n");
            for (Library library: request.getLibraries()){
                String libraryName = getLibraryName(library);
                for (SampleDTO sample: ((LibraryDTO) library).getSamples()){
                    fileWriter.append(getLine(sample,libraryName));
                    fileWriter.append("\n");
                }
            }
            fileWriter.flush();
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Error while writing request sample csv", e);
        }finally{
            if (fileWriter!=null){
                try{
                    fileWriter.close();
                }catch(IOException e){}
            }
        } 
        
    }
    
    public static void handleDelete(RequestDTO request) throws Exception {
        File theFile = getFile(request);
        
        if (theFile.exists()){
            String fileName = theFile.getName();
            String extension = FilenameUtils.getExtension(fileName);
            String baseName = FilenameUtils.getBaseName(fileName);
            
            Integer counter = 0;
            String newName = baseName + "_OLD_"+counter+"."+extension;
            File newFile = new File(theFile.getParentFile(),newName);
            while (newFile.exists()){
                counter +=1;
                newName = baseName + "_OLD_"+counter+"."+extension;
                newFile = new File(theFile.getParentFile(),newName);
            }
            FileUtils.moveFile(theFile, newFile);         
        }
    }
    
    private static File getFile(RequestDTO request){
        String fileName = request.getRequestId()+"_"+request.getRequestorUser().getLogin()+".csv";
        String fileFolder = Preferences.getSampleRequestFolder();
        
        File file = new File(fileFolder,fileName);
        
        return file;
    }
            
    private static String getLine(SampleDTO sample, String libraryName) {
        char sep = SampleRequestCSVHeader.getSeparator();

        StringBuilder sb = new StringBuilder();

        /*00 UserName,*/ sb.append(sample.getUser().getUserName().replace(",", " ")).append(sep);
        /*01 UserLogin*/ sb.append(sample.getUser().getLogin().replace(",", " ")).append(sep);
        /*02 UserTel*/ sb.append(sample.getUser().getPhone().replace(",", " ")).append(sep);
        /*03 Institute*/ sb.append(sample.getUser().getAffiliation().getOrganizationName().replace(",", " ")).append(sep);
        /*04 PI*/ sb.append(sample.getCostcenter().replace(",", " ")).append(sep);
        /*05 SampleName*/ sb.append(getSampleName(sample)).append(sep);
        /*06 BarcodeByName*/ sb.append(sample.getIndex().getIndex()).append(sep);
        /*07 Application*/ sb.append(sample.getApplication().getApplicationName().replace(",", " ")).append(sep);
        /*08 ReadLength*/ sb.append(sample.getApplication().getReadLength()).append(sep);
        /*09 Receipe*/ sb.append(getRecipeString(sample.getApplication())).append(sep);
        /*10 SampleType*/ sb.append(sample.getType().replace(",", " ")).append(sep);
        /*11 LibrarySynthesis*/ sb.append(String.valueOf(sample.isSyntehsisNeeded())).append(sep);
        /*12 Organism*/ sb.append(sample.getOrganism().replace(",", " ")).append(sep);
        /*13 Antibody*/ sb.append(sample.getAntibody().replace(",", " ")).append(sep);
        /*14 SampleDescription*/ sb.append(sample.getDescription().replace(",", " ")).append(sep);
        /*15 BioAnalyzerDate*/ sb.append(sdf.format(sample.getBioanalyzerDate())).append(sep);
        /*16 BioAnalyzerNanomolarity*/ sb.append(sample.getBioAnalyzerMolarity()).append(sep);
        /*17 SampleConcentration*/ sb.append(sample.getConcentration()).append(sep);
        /*18 TotalAmount*/ sb.append(sample.getTotalAmount()).append(sep);
        /*19 BulkFragmentSize*/ sb.append(sample.getBulkFragmentSize()).append(sep);
        /*20 Comments*/ sb.append(sample.getComment().replace(",", " ")).append(sep);
        /*21 library*/ sb.append(libraryName).append(sep);
        /*22 submissionId*/ sb.append(sample.getSubmissionId()).append(sep);
        /*23 date*/ sb.append(sdf.format(sample.getRequestDate())).append(sep);
        /*24 volume */ sb.append("");

        return sb.toString();
    }
    
    private static String getRecipeString(ApplicationDTO application) {
        String recipeSep = ApplicationCSVParser.RECIPE_DELIMITER;
        StringBuilder recipeBuilder = new StringBuilder();
        recipeBuilder.append(application.getReadMode()).append(recipeSep);
        recipeBuilder.append(application.getDepth()).append(recipeSep);
        recipeBuilder.append("").append(recipeSep);
        recipeBuilder.append(application.getReadLength());
        return recipeBuilder.toString();
    }
    
    private static String getSampleName(SampleDTO sample){
        return sample.getName().replaceFirst("_S[0-9]+", "");
    }
    
    private static String getLibraryName(Library library){
        return library.getName().replaceFirst("_L[0-9]+", "");
    }
}
