/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl;

import at.ac.oeaw.cemm.lims.analysis.impl.illuminaxml.XMLParser;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author hmueller
 */
@ApplicationScoped
public class ConfigurationManagerCEMM {
    
  

    public static boolean folderPassesCutoff(String folder) {
        boolean result = false;
        if (!folder.contains("_") || folder.startsWith(".") || !folder.startsWith("1")) {
            return false;
        }
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {
            String[] temp = folder.split("_");
            if (temp.length > 0) {
                try {
                    int date = Integer.parseInt(temp[0]);
                    if (date >= Preferences.getCutoff()) {
                        result = true;
                    }
                } catch (NumberFormatException nfe) {
                    return result;
                }
            }
        }
        return result;
    }

    public static String getRunReadMode(String runfolder) {
        String mode = XMLParser.getReadMode(Preferences.getRunfolderroot() + runfolder + File.separator + "RunInfo.xml");
        String result = "configuration error";
        if (mode.startsWith("single end")) {
            return "SR";
        } else if (mode.startsWith("paired end")) {
            return "PE";
        }
        return mode;
    }

    public static boolean hasDerivedFolder(String folder, String[] derivedfolders) {
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

    public static boolean hasSamplesCSV(String folder) {
        boolean result = false;        
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {    
            String fcid = parseFCID(folder);
            File dir = new File(Preferences.getRunfolderroot() + "samples");
            File[] dirl = dir.listFiles();
            for (int i = 0; i < dirl.length; i++) {
                if (dirl[i].getName().endsWith(fcid + "_samples.csv"));
                result = true;
                break;
            }   
        }
        return result;
    }
    
    public static boolean hasLibrariesCSV(String folder) {
        boolean result = false;        
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {    
            String fcid = parseFCID(folder);
            File dir = new File(Preferences.getRunfolderroot() + "libraries");
            File[] dirl = dir.listFiles();
            for (int i = 0; i < dirl.length; i++) {
                if (dirl[i].getName().endsWith(fcid + "_libraries.csv"));
                result = true;
                break;
            }   
        }
        return result;
    }
    
    public static boolean getFlowcellNeedsBarcodeI5ReverseComplementing(String folder){
        File dir = new File(Preferences.getRunfolderroot() + folder + File.separator + "runParameters.xml");
        String flowcell = XMLParser.getFlowcell(dir.getAbsolutePath());
        String readmode = XMLParser.getReadMode(Preferences.getRunfolderroot() + folder + File.separator + "RunInfo.xml");
        //System.out.println(folder + " " + readmode);
        if((flowcell.contains("3000") || flowcell.contains("4000")) && readmode.equals("PE")){
            return true;
        }
        return false;
    }
    
    public static String getExperimentName(String folder){
        File file = new File(Preferences.getRunfolderroot() + folder + File.separator + "runParameters.xml");        
        return XMLParser.getExperimentName(file.getAbsolutePath());
    }

    public static String[] listFolders(String root) {
        File runfolder = new File(root);
        String[] folders = runfolder.list();
        return folders;
    }

    public static String mkWorkSubdir(String dirname) {
        try {
            File dir = new File(Preferences.getWorkdir() + File.separator + dirname);
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

    public static String parseFCID(String folder) {
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

    public static boolean runFolderHasRTAComplete(String folder) {
        boolean result = false;
        if (folder != null && folder.length() > 0 && folder.indexOf("_") > -1) {
            File f = new File(Preferences.getRunfolderroot() + folder);
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

      public static List<String> runFoldersToAnalyze() {
        ArrayList<String> al = new ArrayList<String>();
        String[] runfolders = listFolders(Preferences.getRunfolderroot());
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


}
