/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.analysis;

import java.io.File;
import java.util.Map;

/**
 *
 * @author hmueller
 */
public interface SamplesheetToCommands {
    
    public String getCommandsPE(String runfolder);
    public String getCommandsSR(String runfolder);
    public String getCommands(String runfolder);
    public String getCommands(String runfolder, boolean miseq);
    public String getRunDate(File f);
    public String getRunDate();
    public String[][] getSamplesheet();
    public String[][] parseSampleSheet(String sheet);
    public void setAnalysisManager(AnalysisManager am);
    public void setFolder(String folder);
    public void setSampleSheet(String samplesheet);
    public Map<String, String> getMetaNamesAndData();
    
}
