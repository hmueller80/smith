/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.runCSV;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author dbarreca
 */
public class RunsCSVManager {
    private static final String NEW_LINE = "\n";
    
    public static void writeToFile(Set<SampleRunDTO> sampleRuns, String flowCellRun, Integer runId) throws Exception {
        File runFolder = getRunFolder(runId);
        
        String fileName = flowCellRun+".csv";
        File theFile = new File(runFolder,fileName);
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(theFile);
            fileWriter.append(RunFormCSVHeader.getHeaderLine());
            fileWriter.append(NEW_LINE);
            for (SampleRunDTO sampleRun: sampleRuns){
                fileWriter.append(getLines(sampleRun));
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
    
    public static void moveFolder(File theFolder) throws Exception {        
        
        if (theFolder.exists()){
            
            String baseName = theFolder.getName();
            
            Integer counter = 0;
            String newName = baseName + "_OLD_"+counter;
            File newFile = new File(theFolder.getParentFile(),newName);
            while (newFile.exists()){
                counter +=1;
                newName = baseName + "_OLD_"+counter;
                newFile = new File(theFolder.getParentFile(),newName);
            }
            FileUtils.moveDirectory(theFolder, newFile);         
        }
    }
    
    private static File getRunFolder(Integer runId) throws Exception {
        String fileFolder = Preferences.getSampleRunFolder();
        String runFolder = "BSF_"+runId;
        File filePath = new File(fileFolder,runFolder);
        if (filePath.exists()){
            moveFolder(filePath);
        }
        filePath.mkdir();
  
        return filePath;
    }
            
    private static String getLines(SampleRunDTO sampleRun) {
        char sep = RunFormCSVHeader.getSeparator();

        StringBuilder sb = new StringBuilder();
        for (String lane: sampleRun.getLanes()){
            /*00 Lane*/ sb.append(lane).append(sep);
            /*01 Sample*/ sb.append(sampleRun.getSample().getId()).append(sep);
            /*02 BSFID*/ sb.append(sampleRun.getRunId()).append(sep);
            /*03 Flowcell*/ sb.append(sampleRun.getFlowcell()).append(sep);
            /*04 Cluster*/ sb.append("").append(sep);
            /*05 Sequencing*/ sb.append("").append(sep);
            /*06 Date*/ sb.append("");
            sb.append(NEW_LINE);
        }

        return sb.toString();
    }
}
