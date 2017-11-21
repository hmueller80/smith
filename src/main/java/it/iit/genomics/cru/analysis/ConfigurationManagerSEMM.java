package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.runsBeans.SampleService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * @(#)ConfigurationManager.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * ConfigurationManager class, reads context parameters and sets folder and program paths accordingly
 * 
 * @author Yuriy Vaskin
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
//@ManagedBean(name = "configurationManager")
//@ApplicationScoped
public class ConfigurationManagerSEMM implements ConfigurationManager{
    
    private Preferences preferences;
        
    /**
     *
     * Class constructor
     * 
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public ConfigurationManagerSEMM(){
        if(Preferences.getVerbose()){
            System.out.println("init ConfigurationManager");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        preferences = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);
    }
    
    /**
     *
     * get preferences
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return Preferences instance
     * @since 1.0
     */
    
    public Preferences getPreferences(){
        return preferences;
    }
              
    /**
     *
     * reads run folders, return those run folders that pass monitoring cutoff, have RTAComplete.txt, and do not have a FASTQ folder
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return List<String> of run folders
     * @since 1.0
     */
    public List<String> runFoldersToAnalyze(){
        ArrayList<String> al = new ArrayList<String>();
        String[] runfolders = listFolders(preferences.getRunfolderroot());
        String[] fastqfolders = listFolders(preferences.getFastqfolderroot());
        //System.out.println(runfolderroot);
        //System.out.println("runfolders " + runfolders.length);
        for(int i = 0; i < runfolders.length; i++){
            if(folderPassesCutoff(runfolders[i]) && runFolderHasRTAComplete(runfolders[i]) && !hasDerivedFolder(runfolders[i], fastqfolders)){
                al.add(runfolders[i]);
            }
        }
        return al;
    }
    
    /**
     *
     * reads run folders, return those run folders that pass monitoring cutoff, have RTAComplete.txt, and do not have a FASTQ folder
     * 
     * @author Heiko Muller
     * @version 1.0
     * @return List<String> of run folders
     * @since 1.0
     */
    public List<String> runFoldersReadyToDeliverFastq(){
        ArrayList<String> al = new ArrayList<String>();
        //String[] runfolders = listFolders(preferences.getRunfolderroot());
        String[] fastqfolders = listFolders(preferences.getFastqfolderroot());
        //System.out.println(runfolderroot);
        //System.out.println("runfolders " + runfolders.length);
        for(int i = 0; i < fastqfolders.length; i++){
            if(folderPassesCutoff(fastqfolders[i]) && runFolderHasRTAComplete(fastqfolders[i]) && hasDerivedFolder(fastqfolders[i], fastqfolders) && fastqFolderHasFlowcellFastQCCompleted(fastqfolders[i])){
                al.add(fastqfolders[i]);
            }
        }
        return al;
    }
   
    /**
     *
     * reads run folders
     * 
     * @author Heiko Muller
     * @version 1.0
     * @param folder - the run folder being tested
     * @return boolean - true if run folder contains file RTAComplete.txt, false otherwise
     * @since 1.0
     */
    public boolean runFolderHasRTAComplete(String folder){
        boolean result = false;
        if(folder != null && folder.length() > 0 && folder.indexOf("_") > -1){
            File f = new File(preferences.getRunfolderroot() + folder);
            String[] list = f.list();
            if(list != null && list.length > 0){
                for(int i = 0; i < list.length; i++){
                    if(list[i].equals("RTAComplete.txt")){
                        result = true;
                        break;                
                    }
                }
            }
        }
        return result;
    }
   
    /**
     *
     * tests if run was performed after the cutoff date
     * 
     * @author Heiko Muller
     * @version 1.0
     * @param folder - the run folder being tested
     * @return boolean - true if run was performed after the cutoff date, false otherwise
     * @since 1.0
     */
    public boolean folderPassesCutoff(String folder){
        boolean result = false;
        if(!folder.contains("_") || folder.startsWith(".")){
            return false;
        }
        if(folder != null && folder.length() > 0 && folder.indexOf("_") > -1){
            String[] temp = folder.split("_");
            if (temp.length > 0){
                try{
                    int date = Integer.parseInt(temp[0]);
                    if(date >= preferences.getCutoff()){
                        result = true;                             
                    }   
                }catch (NumberFormatException nfe){
                    return result;
                }
            }
        }     
        return result;
    }
    
    /**
     *
     * tests if folder contains given sub folders
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @param folder - the run folder being tested
     * @param derivedfolders - a set of folder names whose presence is tested
     * @return boolean - true if at least one sub folder is found, false otherwise
     * @since 1.0
     */
    public boolean hasDerivedFolder(String folder, String[] derivedfolders){
        boolean result = false;    
        if(folder != null && folder.length() > 0 && folder.indexOf("_") > -1){
            for(int i = 0; i < derivedfolders.length; i++){
                if(derivedfolders[i].equals(folder)){
                    result = true;
                    break;                
                }
            }    
        }   
        return result;
    }
    
    /**
     *
     * lists FASTQ folders
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return List<String> - the list of FASTQ folders
     * @since 1.0
     */
    public List<String> listFastqFolders(){
        ArrayList<String> al = new ArrayList<String>();
        String[] fastqfolders = listFolders(preferences.getFastqfolderroot());
        
        for(int i = 0; i < fastqfolders.length; i++){
            if(folderPassesCutoff(fastqfolders[i])){
                al.add(fastqfolders[i]);
            }
        }
        return al;
    }
    
    /**
     *
     * lists FASTQ folders
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return List<String> - the list of FASTQ folders
     * @since 1.0
     */
    public List<String> listAnalyzedFastqFolders(){
        ArrayList<String> al = new ArrayList<String>();
        String[] fastqfolders = listFolders(preferences.getFastqfolderroot());
        
        for(int i = 0; i < fastqfolders.length; i++){
            if(folderPassesCutoff(fastqfolders[i])){
                al.add(fastqfolders[i]);
            }
        }
        return al;
    }
    
    /**
     *
     * lists FASTQ folders
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @return List<String> - the list of FASTQ folders
     * @since 1.0
     */
    public boolean fastqFolderHasFlowcellFastQCCompleted(String fastqfolder){
        if(folderPassesCutoff(fastqfolder)){
            File f = new File(Preferences.getFastqfolderroot() + fastqfolder + File.separator + "Flowcell_FastQC" + File.separator + "all_samples_R1_fastqc" + File.separator + "R1-side-by-side.html");
            if(f.exists()){
                return true;
            }
        }
        return false;
    }
    
    /**
     *
     * creates a sub directory in the work folder
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @param dirname - the name of the folder to be created
     * @return String - the absolute path of the new sub folder
     * @since 1.0
     */
    public String mkWorkSubdir(String dirname){
        try{            
            File dir = new File(preferences.getWorkdir() + File.separator + dirname);
            if(!dir.exists()){
                dir.mkdir();
                return dir.getAbsolutePath();
            }else{
                return dir.getAbsolutePath();
            }            
        }catch(UnsupportedOperationException uoe){
            uoe.printStackTrace(); 
        }
        return "";
    }    
    
    /**
     *
     * lists folders present in directory root
     * 
     * @author Yuriy Vaskin
     * @version 1.0
     * @param root - the name of the root folder to be read
     * @return String[] - the array of sub folders found
     * @since 1.0
     */
    public String[] listFolders(String root){
        File runfolder = new File(root);
        String [] folders = runfolder.list();
        return folders;
    }
    
    /**
     *
     * reads run mode (single read or paired end) of a given run folder
     * 
     * @author Heiko Muller
     * @version 1.0
     * @param runfolder - the name of the run folder to be read
     * @return String - SR if single read, PE if paired end
     * @since 1.0
     */
    public String getRunReadMode(String runfolder){
        String result = "SR";
        try{
            File f = new File(preferences.getRunfolderroot() + runfolder + File.separator + "RunInfo.xml");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);            
            String line = "";
            while((line = br.readLine()) != null){
                if(line.contains("<Read Number=\"3\" NumCycles=")){
                    result = "PE";
                }
            }
        }catch(IOException ioe){
            
        }
        return result;
    }
    
    /**
     *
     * parses the flowcell barcode from the runfolder name
     * 
     * @author Heiko Muller
     * @version 1.0
     * @param folder - the name of the run folder to be read
     * @return String - flowcell barcode if found, empty String otherwise
     * @since 1.0
     */
    public String parseFCID(String folder){
        String result = "";
        if(folder.length() > 0){
            String[] temp = folder.split("_");
            if(temp.length > 3){
                result = temp[3].substring(1);
            }
        }
        return result;
    }
    
    public String parseFlowcell(String folder){
        String result = "";
        if(folder.length() > 0){
            String[] temp = folder.split("_");
            if(temp.length > 3){
                result = temp[3].substring(0,1);
            }
        }
        return result;
    }
    
    public void setPreferences(Preferences p){
        preferences = p;
    }
    
    public boolean getFlowcellNeedsBarcodeI5ReverseComplementing(String folder){        
        return false;
    }
    
    public String getExperimentName(String folder){
        File file = new File(preferences.getRunfolderroot() + folder + File.separator + "runParameters.xml");        
        return XMLParser.getExperimentName(file.getAbsolutePath());
    }
}
