/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;

/**
 *
 * @author hmueller
 */
public interface AnalysisManager {
    /*
    Analysis workflow:
    ContextListener triggers AnalysisManager.run() according to timer interval
    public void run() {
        1. generateCommandsAndSampleSheets(); 
            //configurationManager.runFoldersToAnalyze()
            //configurationManager.mkWorkSubdir(beinganalyzed);
            //configurationManager.getRunReadMode(beinganalyzed);
            //configurationManager.parseFCID(beinganalyzed);
            //String ssPath = getPreferences().getSampleSheetFolder() + beinganalyzed + ".csv";                
            //if(!ImportManager.isFileExists(ssPath)){
            //    String samplesheet = SampleSheetHelper.getSamplesheet(FCID);
            //    ImportManager.saveToFile(samplesheet, ssPath, "", false);  
            //}
            //String samplesheet = SampleSheetHelper.readSamplesheetWithPiandExpType(ssPath, FCID);                                
            //SamplesheetToCommands sc = new SamplesheetToCommandsCEMM();                 
            //sc.setAnalysisManager(this);
            //sc.setSampleSheet(samplesheet); 
            //write scripts to file
            //String path = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_bcl.sh"; 
    
            //get samplesheet depending on flowcelltype, write commands to file and submit
        2. updateData();        
    }
    
    public void generateCommandsAndSampleSheets() {
        System.out.println("Command and samplesheet generation");
        if(configurationManager == null){
            FacesContext context = FacesContext.getCurrentInstance();
            configurationManager = (ConfigurationManager) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerCEMM.class);
        }
        List<String> todo = configurationManager.runFoldersToAnalyze();
        for (String beinganalyzed : todo){
            if(beinganalyzed.length() > 0){   
                configurationManager.mkWorkSubdir(beinganalyzed);
                String readmode = configurationManager.getRunReadMode(beinganalyzed);
                String FCID = configurationManager.parseFCID(beinganalyzed);
                String ssPath = getPreferences().getSampleSheetFolder() + beinganalyzed + ".csv";
                
                if(!ImportManager.isFileExists(ssPath)){
                    String samplesheet = SampleSheetHelper.getSamplesheet(FCID);
                    ImportManager.saveToFile(samplesheet, ssPath, "", false);  
                }
                String samplesheet = SampleSheetHelper.readSamplesheetWithPiandExpType(ssPath, FCID);
                                
                SamplesheetToCommands sc = new SamplesheetToCommandsCEMM(); 
                //sc.setAnalysisManager(analysisManager);
                sc.setAnalysisManager(this);
                sc.setSampleSheet(samplesheet);
                
                
                //write scripts to file
                String path = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_bcl.sh";                
                //ImportManager.saveToFile(sc.getBclToFastQConfigString(sc.getSamplesheet(), beinganalyzed), path, "#!/bin/bash\numask 0027\n", true); 
                //ImportManager.saveToFile(sc.getBclToFastQConfigString(sc.getSamplesheet(), beinganalyzed), path, "#!/bin/bash\numask 0027\n", true); 
                             
                
                if(readmode.equals("SR")){
                    String outPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_commands.sh";
                    ImportManager.saveToFile(sc.getCommandsSR(beinganalyzed), outPath, "#!/bin/bash\n", true);  
                                        
                    //String outHtmlPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R1-side-by-side.html";
                    //ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath, "", false); 
                }else{
                    String outPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_commands.sh";
                    ImportManager.saveToFile(sc.getCommandsPE(beinganalyzed), outPath, "#!/bin/bash\n", true); 
                    
                    //String outHtmlPath1 = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R1-side-by-side.html";
                    //ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath1, "", false);
                    
                    //String outHtmlPath2 = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R2-side-by-side.html";
                    //ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath2, "", false);
                }
                
                Map<String, String> nameAndMeta = sc.getMetaNamesAndData();
                for (Map.Entry pairs : nameAndMeta.entrySet()) {
                    String metaPath = configurationManager.mkWorkSubdir(beinganalyzed + File.separator + "meta") + File.separator + pairs.getKey();
                    ImportManager.saveToFile(pairs.getValue().toString(), metaPath, "", false);                    
                }
                
                NewsHelper.publishRunFinished(beinganalyzed);
                
                //////////////////////////
                //                      //
                //    AUTOPILOT!!!!     //
                //                      //
                //////////////////////////
                runAnalysis(sc, beinganalyzed);
            }
        
    }
    }
    
    */
    
    
    public void run();
    public void generateCommandsAndSampleSheets();
    public void runAnalysis(SamplesheetToCommands scriptgenerator, String folder);
    public void sendDeliveryMail(SamplesheetToCommands scriptgenerator, String folder);
    public void updateData();
    public AnalysisManager getAnalysisManager();
    public ConfigurationManager getConfigurationManager();
    public Preferences getPreferences();
    public UpdateManager getUpdateManager();
    
}
