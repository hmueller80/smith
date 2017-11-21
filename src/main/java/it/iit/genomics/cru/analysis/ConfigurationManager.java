/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.util.List;

/**
 *
 * @author hmueller
 */
public interface ConfigurationManager {
    
    public boolean folderPassesCutoff(String folder);
    public Preferences getPreferences();
    public String getRunReadMode(String runfolder);
    public boolean hasDerivedFolder(String folder, String[] derivedfolders);
    public String[] listFolders(String root);
    public String mkWorkSubdir(String dirname);
    public String parseFCID(String folder);
    public boolean runFolderHasRTAComplete(String folder);
    public List<String> runFoldersToAnalyze();
    public List<String> listFastqFolders();
    public boolean getFlowcellNeedsBarcodeI5ReverseComplementing(String runfolder);
    public String getExperimentName(String folder);
    
}
