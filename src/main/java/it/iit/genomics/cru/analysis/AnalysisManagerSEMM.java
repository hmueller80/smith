package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.mail.MailBean;
import it.iit.genomics.cru.smith.news.NewsHelper;
import it.iit.genomics.cru.smith.samplesSheetBeans.SampleSheetHelper;
import java.io.BufferedReader;
import java.io.File;
import java.util.Iterator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * @(#)AnalysisManager.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Class serves to orchestrate generation of work flow commands when a run has finished.
 * Execution is triggered by a TimerServlet that monitors the run folder every 6 hours.
 * 
 * @author Yuriy Vaskin
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
//@ManagedBean(name = "analysisManager")
//@ApplicationScoped
public class AnalysisManagerSEMM implements AnalysisManager{
    
    //private AnalysisManager             analysisManager;
    private ConfigurationManagerSEMM        configurationManager;
    private UpdateManager               updateManager;
    private MailBean                    mailBean;
  
    /**
     *
     * Class constructor
     * 
     * @author Yuriy Vaskin
     * @author Heiko Muller
     * @since 1.0
     */
    public AnalysisManagerSEMM(){
        if(Preferences.getVerbose()){
            System.out.println("init AnalysisManager");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        //Preferences prefs = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);
        configurationManager = (ConfigurationManagerSEMM) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerSEMM.class);
        updateManager = (UpdateManager) context.getApplication().evaluateExpressionGet(context, "#{updateManager}", UpdateManager.class);        
        
        //mailBean = (MailBean) context.getApplication().evaluateExpressionGet(context, "#{mailBean}", MailBean.class);
        
        //analysisManager = this;
        
        
        
    }
    
    /**
     *
     * starts an analysis run triggered by the timer servlet
     * 
     * @author Yuriy Vaskin
     * @author Heiko Muller
     * @since 1.0
     */
    public void run(){
        generateCommandsAndSampleSheets();
        updateData();
    }
    
    /**
     *
     * generates samplesheet and analysis commands when an new finished run is found
     * 
     * @author Yuriy Vaskin
     * @author Heiko Muller
     * @since 
     */
    public void generateCommandsAndSampleSheets() {
        System.out.println("Command and samplesheet generation");
        if(configurationManager == null){
            FacesContext context = FacesContext.getCurrentInstance();
            configurationManager = (ConfigurationManagerSEMM) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerSEMM.class);
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
                                
                SamplesheetToCommandsSEMM sc = new SamplesheetToCommandsSEMM(); 
                //sc.setAnalysisManager(analysisManager);
                sc.setAnalysisManager(this);
                sc.setSampleSheet(samplesheet);
                
                
                //write scripts to file
                String path = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_bcl.sh";                
                ImportManager.saveToFile(sc.getBclToFastQConfigString(sc.getSamplesheet(), beinganalyzed), path, "#!/bin/bash\numask 0027\n", true); 
                             
                
                if(readmode.equals("SR")){
                    String outPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_commands.sh";
                    ImportManager.saveToFile(sc.getCommandsSR(beinganalyzed), outPath, "#!/bin/bash\n", true);  
                                        
                    String outHtmlPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R1-side-by-side.html";
                    ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath, "", false); 
                }else{
                    String outPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_commands.sh";
                    ImportManager.saveToFile(sc.getCommandsPE(beinganalyzed), outPath, "#!/bin/bash\n", true); 
                    
                    String outHtmlPath1 = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R1-side-by-side.html";
                    ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath1, "", false);
                    
                    String outHtmlPath2 = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R2-side-by-side.html";
                    ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath2, "", false);
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
        
        List<String> tomail = configurationManager.runFoldersReadyToDeliverFastq();
        for (String beingmailed : tomail){
            System.out.println("toMail " + beingmailed);
            String FCID = configurationManager.parseFCID(beingmailed);
            String ssPath = getPreferences().getSampleSheetFolder() + beingmailed + ".csv";
            String samplesheet = SampleSheetHelper.readSamplesheetWithPiandExpType(ssPath, FCID);                                
            SamplesheetToCommandsSEMM sc = new SamplesheetToCommandsSEMM();             
            sc.setSampleSheet(samplesheet);
            if(beingmailed.length() > 0){   
                sendDeliveryMail(sc, beingmailed);
            }
        }
    }
    
    public void runAnalysis(SamplesheetToCommands scriptgenerator, String folder){
        boolean analysisHasBeenPerformed = NewsHelper.newsExists("Fastq analysis for " + folder + " has started.");
        //String command = scriptgenerator.getBclToFastQConfigString(scriptgenerator.getSamplesheet(), folder);
        if(!analysisHasBeenPerformed){
            //String command = scriptgenerator.getBclToFastQConfigString(scriptgenerator.getSamplesheet(), folder);            
            //String output = executeBclToFastQ(command);
            String output = executeBclToFastQ2(folder);
            if(output.contains("completed with no problems")){
                
                try{
                    //command = "/bin/bash -c ";
                    //command = command + Preferences.getWorkdir() + folder + "/" + folder + "_commands.sh";
                    String flowcell = configurationManager.parseFlowcell(folder);
                    if(flowcell.equals("A")){
                        URL url = new URL(Preferences.getExecuterpathflowcellA() + "?workdir=" + Preferences.getWorkdir() + "&flowcell=" + folder);
                        URLConnection conn = url.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null){
                            System.out.println(inputLine);
                        }                         
                        in.close();                    
                        NewsHelper.publishFastqAnalysisStarted(folder);
                    }else if(flowcell.equals("B")){
                        URL url = new URL(Preferences.getExecuterpathflowcellB() + "?workdir=" + Preferences.getWorkdir() + "&flowcell=" + folder);
                        URLConnection conn = url.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null){
                            System.out.println(inputLine);
                        }                         
                        in.close();                    
                        NewsHelper.publishFastqAnalysisStarted(folder);
                    }
                    //http://blade-b1-p2.iit.ieo.eu:8081/Bash/Executer?flowcell=folder&workdir=Preferences.getWorkdir() + folder
                    //Process proc=Runtime.getRuntime().exec(command);
                }catch(MalformedURLException e){            
                    e.printStackTrace();
                }catch( IOException ioe){
                    ioe.printStackTrace();
                }
                
            }else{
                NewsHelper.publishBclToFastqConfigError(folder);
            }
        } 
    }
    
    private String executeBclToFastQ(String command){
        StringBuilder output = new StringBuilder();  
        //String command = "/usr/bin/perl /opt/cluster/bin/configureBclToFastq.pl --input-dir=/data/Illumina/Runs/140715_SN880_0293_AC4Y3RACXX/Data/Intensities/BaseCalls/ --output-dir=/scratch/140715_SN880_0293_AC4Y3RACXX/ --mismatches=1 --sample-sheet=/data/Illumina/PublicData/SampleSheets/140715_SN880_0293_AC4Y3RACXX.csv";;
        try{
            
            Process proc=Runtime.getRuntime().exec(command);
            proc.waitFor();            
            BufferedReader read=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
          
            while(read.ready()){  
                output.append(read.readLine() + "\r\n");
            }            
        }catch(RuntimeException e){            
            output.append(e.getMessage());
        }
        catch(IOException e){            
            output.append(e.getMessage());
        }
        catch(InterruptedException e){            
            output.append(e.getMessage());
        }
        return output.toString();
    }
    
    private String executeBclToFastQ2(String folder){
        StringBuilder output = new StringBuilder();  
        //String command = "/usr/bin/perl /opt/cluster/bin/configureBclToFastq.pl --input-dir=/data/Illumina/Runs/140715_SN880_0293_AC4Y3RACXX/Data/Intensities/BaseCalls/ --output-dir=/scratch/140715_SN880_0293_AC4Y3RACXX/ --mismatches=1 --sample-sheet=/data/Illumina/PublicData/SampleSheets/140715_SN880_0293_AC4Y3RACXX.csv";;
        try{
            
            //Process proc=Runtime.getRuntime().exec("sudo", );
            ProcessBuilder pb = new ProcessBuilder("sudo", Preferences.getWorkdir() + folder + "/" + folder + "_bcl.sh");
            Process proc = pb.start();
            proc.waitFor();            
            BufferedReader read=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
          
            while(read.ready()){  
                output.append(read.readLine() + "\r\n");
            }            
        }catch(RuntimeException e){            
            output.append(e.getMessage());
        }
        catch(IOException e){            
            output.append(e.getMessage());
        }
        catch(InterruptedException e){            
            output.append(e.getMessage());
        }
        return output.toString();
    }
    
    private boolean testFileLinkingSucceeded(SamplesheetToCommandsSEMM scriptgenerator, String runfolder){
        ArrayList<String> foldersToTest = scriptgenerator.getFastqUserDirectories(runfolder);
        boolean result = true;        
        for(String s : foldersToTest){
            System.out.println("directory " + s);
            File f = new File(s);
            if(f.exists() && f.isDirectory()){
                String[] files = f.list();
                
                    if(files != null && files.length > 0){ 
                        System.out.println("linked files found " + files.length);
                    }else{
                        return false;
                    }
                
            }else{
                return false;
            }
        }        
        return result;
    }
    
    public void sendDeliveryMail(SamplesheetToCommands scriptgenerator, String folder){}
    
    public void sendDeliveryMail(SamplesheetToCommandsSEMM scriptgenerator, String folder){
        scriptgenerator.setFolder(folder);
        scriptgenerator.setAnalysisManager(this);
        boolean fastqDataHaveBeenDelivered = NewsHelper.newsExists("Fastq data for " + folder + " have been delivered.");
        if(!fastqDataHaveBeenDelivered){
            //System.out.println("testing file linking");
            if(testFileLinkingSucceeded(scriptgenerator, folder)){
                
                //mailBean = new MailBean();
                if(mailBean == null){
                    FacesContext context = FacesContext.getCurrentInstance();
                    try{
                        mailBean = ((MailBean) context.getApplication().evaluateExpressionGet(context, "#{mailBean}", MailBean.class));
                    }catch(NullPointerException npe){
                        mailBean = new MailBean();
                    }
                }
                if(mailBean != null){
                    //System.out.println("sending delivery mail");
                    mailBean.sendFastQDeliveryMails(folder);
                    NewsHelper.publishFastqDataHaveBeenDelivered(folder);
                }else{
                    System.out.println("problem sending delivery mail");
                }
            }
        }
    }
    
    
    /**
     *
     * launches UpdateManager
     * 
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public void updateData() {
        //update FASTQ, BAM, BIGWIG,BED,...
        updateManager.runUpdater();
    }
    
    /**
     *
     * Getter for AnalysisManager
     * 
     * @author Heiko Muller
     * @return AnalysisManager
     * @since 1.0
     */
    public AnalysisManager getAnalysisManager() {
        return (AnalysisManager)this;
    }
    
    
    /**
     *
     * Getter for ConfigurationManager
     * 
     * @author Heiko Muller
     * @return ConfigurationManager
     * @since 1.0
     */
    public ConfigurationManagerSEMM getConfigurationManager() {
        return configurationManager;
    }
    
    /**
     *
     * Getter for Preferences
     * 
     * @author Heiko Muller
     * @return Preferences
     * @since 1.0
     */
    public Preferences getPreferences() {
        return configurationManager.getPreferences();
    }

    /**
     *
     * Getter for UpdateManager
     * 
     * @author Heiko Muller
     * @return UpdateManager
     * @since 1.0
     */
    public UpdateManager getUpdateManager() {
        return updateManager;
    }
    
    
}
