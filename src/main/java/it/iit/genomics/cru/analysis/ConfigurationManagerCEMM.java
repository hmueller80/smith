/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
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
 *
 * @author hmueller
 */
@ManagedBean(name = "configurationManager")
@ApplicationScoped
public class ConfigurationManagerCEMM implements ConfigurationManager {
    
    /*
    run states:
    boolean variables: 8: states = 2^8 = 256 
        run has started on machine (runfolder exists)
        run passes cutoff date
        run has RTAComplete.txt
        run has been submitted to samplerun database table
        run has libraries file written to runfolderroot/libraries
        run has samples file written to runfolderroot/samples
        run has analyses started news
        run has analysis finished news
    
    1. run started on seq machine but has not been submitted to samplerun database table
    2. run started on seq machine and has been submitted to samplerun database table
    
    */

    private Preferences preferences;

    public ConfigurationManagerCEMM() {
        if (Preferences.getVerbose()) {
            System.out.println("init ConfigurationManager");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        preferences = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);
    }

    @Override
    public boolean folderPassesCutoff(String folder) {
        boolean result = false;
        if (!folder.contains("_") || folder.startsWith(".") || !folder.startsWith("1")) {
            return false;
        }
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {
            String[] temp = folder.split("_");
            if (temp.length > 0) {
                try {
                    int date = Integer.parseInt(temp[0]);
                    if (date >= preferences.getCutoff()) {
                        result = true;
                    }
                } catch (NumberFormatException nfe) {
                    return result;
                }
            }
        }
        return result;
    }

    @Override
    public Preferences getPreferences() {
        return preferences;
    }

    @Override
    public String getRunReadMode(String runfolder) {
        String mode = XMLParser.getReadMode(preferences.getRunfolderroot() + runfolder + File.separator + "RunInfo.xml");
        String result = "configuration error";
        if (mode.startsWith("single end")) {
            return "SR";
        } else if (mode.startsWith("paired end")) {
            return "PE";
        }
        return mode;
    }

    @Override
    public boolean hasDerivedFolder(String folder, String[] derivedfolders) {
        boolean result = false;
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {
            for (int i = 0; i < derivedfolders.length; i++) {
                if (derivedfolders[i].equals(folder)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    //@Override
    public boolean hasSamplesCSV(String folder) {
        boolean result = false;        
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {    
            String fcid = parseFCID(folder);
            File dir = new File(preferences.getRunfolderroot() + "samples");
            File[] dirl = dir.listFiles();
            for (int i = 0; i < dirl.length; i++) {
                if (dirl[i].getName().endsWith(fcid + "_samples.csv"));
                result = true;
                break;
            }   
        }
        return result;
    }
    
    public boolean hasLibrariesCSV(String folder) {
        boolean result = false;        
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {    
            String fcid = parseFCID(folder);
            File dir = new File(preferences.getRunfolderroot() + "libraries");
            File[] dirl = dir.listFiles();
            for (int i = 0; i < dirl.length; i++) {
                if (dirl[i].getName().endsWith(fcid + "_libraries.csv"));
                result = true;
                break;
            }   
        }
        return result;
    }
    
    public boolean getFlowcellNeedsBarcodeI5ReverseComplementing(String folder){
        File dir = new File(preferences.getRunfolderroot() + folder + File.separator + "runParameters.xml");
        String flowcell = XMLParser.getFlowcell(dir.getAbsolutePath());
        String readmode = XMLParser.getReadMode(preferences.getRunfolderroot() + folder + File.separator + "RunInfo.xml");
        //System.out.println(folder + " " + readmode);
        if((flowcell.contains("3000") || flowcell.contains("4000")) && readmode.equals("PE")){
            return true;
        }
        return false;
    }
    
    public String getExperimentName(String folder){
        File file = new File(preferences.getRunfolderroot() + folder + File.separator + "runParameters.xml");        
        return XMLParser.getExperimentName(file.getAbsolutePath());
    }

    @Override
    public String[] listFolders(String root) {
        File runfolder = new File(root);
        String[] folders = runfolder.list();
        return folders;
    }

    @Override
    public String mkWorkSubdir(String dirname) {
        try {
            File dir = new File(preferences.getWorkdir() + File.separator + dirname);
            if (!dir.exists()) {
                dir.mkdir();
                return dir.getAbsolutePath();
            } else {
                return dir.getAbsolutePath();
            }
        } catch (UnsupportedOperationException uoe) {
            uoe.printStackTrace();
        }
        return "";
    }

    @Override
    public String parseFCID(String folder) {
        String result = "";
        if(folder.contains("000000000")){
            String[] temp = folder.split("_");
            if (temp.length > 3) {
                result = temp[3];
            }
        }else
        
        if (folder.length() > 0) {
            String[] temp = folder.split("_");
            if (temp.length > 3) {
                result = temp[3].substring(1);
            }            
        }
        return result;
    }

    @Override
    public boolean runFolderHasRTAComplete(String folder) {
        boolean result = false;
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {
            File f = new File(preferences.getRunfolderroot() + folder);
            String[] list = f.list();
            if (list != null && list.length > 0) {
                for (int i = 0; i < list.length; i++) {
                    if (list[i].equals("RTAComplete.txt")) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<String> runFoldersToAnalyze() {
        ArrayList<String> al = new ArrayList<String>();
        String[] runfolders = listFolders(preferences.getRunfolderroot());
        //String[] fastqfolders = listFolders(preferences.getFastqfolderroot());
        //System.out.println(runfolderroot);
        //System.out.println("runfolders " + runfolders.length);
        for (int i = 0; i < runfolders.length; i++) {
            if (folderPassesCutoff(runfolders[i]) && runFolderHasRTAComplete(runfolders[i])){ //&& hasLibrariesCSV(runfolders[i]) && !hasSamplesCSV(runfolders[i])) {
                al.add(runfolders[i]);
            }
        }
        return al;
    }

    @Override
    public List<String> listFastqFolders() {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
