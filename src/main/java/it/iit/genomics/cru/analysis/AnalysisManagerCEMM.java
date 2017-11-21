/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.mail.MailBean;
import it.iit.genomics.cru.smith.news.NewsHelper;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.samplesSheetBeans.SampleSheetHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;


/**
 *
 * @author hmueller
 */
@ManagedBean(name = "analysisManager")
@ApplicationScoped
public class AnalysisManagerCEMM implements AnalysisManager{
    
    private ConfigurationManager        configurationManager;
    private UpdateManager               updateManager;
    private MailBean                    mailBean;
    private String uploadPath;
    
    public AnalysisManagerCEMM(){
        if(Preferences.getVerbose()){
            System.out.println("init AnalysisManager");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        //Preferences prefs = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);
        configurationManager = (ConfigurationManager) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerCEMM.class);
        updateManager = (UpdateManager) context.getApplication().evaluateExpressionGet(context, "#{updateManager}", UpdateManager.class);        
        
        //mailBean = (MailBean) context.getApplication().evaluateExpressionGet(context, "#{mailBean}", MailBean.class);
        
        //analysisManager = this;
        try {
            //uploadPath = context.getExternalContext().getRealPath("/"); 
            //uploadPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            uploadPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/");
            System.out.println("upload path " + uploadPath);
            uploadPath = uploadPath + "upload" + File.separator;            
            
        } catch (UnsupportedOperationException uoe) {
            uoe.printStackTrace();
        }
        
        
        
    }

    @Override
    public void run() {
        generateCommandsAndSampleSheets();
        updateData();
        
    }

    @Override
    public void generateCommandsAndSampleSheets() {
        System.out.println("CEMM Command and samplesheet generation");
        //System.out.println("chown " + execute("/scratch/lab_bsf/projects/BSF_runs/chown.sh"));
        
        
        if(configurationManager == null){
            FacesContext context = FacesContext.getCurrentInstance();
            configurationManager = (ConfigurationManager) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerCEMM.class);
        }
        List<String> todo = configurationManager.runFoldersToAnalyze();
        //System.out.println("folders to analyze " + todo.size());
        for (String beinganalyzed : todo){
            String FCID = configurationManager.parseFCID(beinganalyzed);
            System.out.println("runfolder being analyzed " + FCID);
            List<SampleRun> run = RunHelper.getRunsByFCIDList(FCID);
            boolean hasSampleRun = (run.size() > 0);
            if(beinganalyzed.length() > 0 && hasSampleRun){   
                configurationManager.mkWorkSubdir(beinganalyzed);
                String readmode = configurationManager.getRunReadMode(beinganalyzed);
                boolean miseq = false;
                boolean indexreversal = configurationManager.getFlowcellNeedsBarcodeI5ReverseComplementing(beinganalyzed);
                if(FCID.length() != 9){
                    //is MiSeq run, no index reversal
                    indexreversal = false;
                    miseq = true;
                }
                String expName = configurationManager.getExperimentName(beinganalyzed);
                String ssPath = getPreferences().getSampleSheetFolder() + expName + "_" + FCID + "_libraries.csv";
                //System.out.println(ssPath);
                if(!ImportManager.isFileExists(ssPath)){
                    String samplesheet = SampleSheetHelper.getSamplesheetCEMM(FCID, indexreversal);
                    ImportManager.saveToFile(samplesheet, ssPath, "", false);  
                }
                //String samplesheet = SampleSheetHelper.readSamplesheetWithPiandExpType(ssPath, FCID);
                String samplesheet = SampleSheetHelper.readSamplesheetCEMM(ssPath);
                                
                SamplesheetToCommands sc = new SamplesheetToCommandsCEMM(); 
                //sc.setAnalysisManager(analysisManager);
                sc.setAnalysisManager(this);
                sc.setSampleSheet(samplesheet);
                
                
                //write scripts to file
                //String path = configurationManager.getPreferences().getRunfolderroot() + File.separator + beinganalyzed + "_submitDemux.sh";                
                //ImportManager.saveToFile(sc.getBclToFastQConfigString(sc.getSamplesheet(), beinganalyzed), path, "#!/bin/bash\numask 0027\n", true); 
                //ImportManager.saveToFile(sc.getBclToFastQConfigString(sc.getSamplesheet(), beinganalyzed), path, "#!/bin/bash\numask 0027\n", true); 
                             
                
                //if(readmode.equals("SR")){
                //    String outPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_commands.sh";
                //    ImportManager.saveToFile(sc.getCommandsSR(beinganalyzed), outPath, "#!/bin/bash\n", true);  
                                        
                    //String outHtmlPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R1-side-by-side.html";
                    //ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath, "", false); 
                //}else{
                //    String outPath = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + beinganalyzed + "_commands.sh";
                //    ImportManager.saveToFile(sc.getCommandsPE(beinganalyzed), outPath, "#!/bin/bash\n", true); 
                    
                    //String outHtmlPath1 = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R1-side-by-side.html";
                    //ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath1, "", false);
                    
                    //String outHtmlPath2 = configurationManager.mkWorkSubdir(beinganalyzed) + File.separator + "R2-side-by-side.html";
                    //ImportManager.saveToFile(sc.getFlowcellFastQCHtmlString(), outHtmlPath2, "", false);
                //}
                
                String path = configurationManager.getPreferences().getRunfolderroot() + beinganalyzed + File.separator + beinganalyzed + "_submitDemux.sh";  
                //System.out.println(path);
                if(!ImportManager.isFileExists(path)){
                //if(true){
                    ImportManager.saveToFile(sc.getCommands(beinganalyzed, miseq), path, "#!/bin/bash\n\n", true);
                }
                //System.out.println(sc.getCommands(beinganalyzed));
                
                Map<String, String> nameAndMeta = sc.getMetaNamesAndData();
                for (Map.Entry pairs : nameAndMeta.entrySet()) {
                    String metaPath = configurationManager.getPreferences().getRunfolderroot() + beinganalyzed + File.separator + "meta" + File.separator + pairs.getKey();
                    ImportManager.saveToFile(pairs.getValue().toString(), metaPath, "", false);                    
                }
                
                NewsHelper.publishRunFinished(beinganalyzed);
                
                //////////////////////////
                //                      //
                //    AUTOPILOT!!!!     //
                //                      //
                //////////////////////////
                execute("/scratch/lab_bsf/projects/BSF_runs/chown.sh");
                runAnalysis(sc, beinganalyzed);
            }
        
    }
    }

    @Override
    public void runAnalysis(SamplesheetToCommands scriptgenerator, String folder) {
        boolean analysisHasBeenPerformed = NewsHelper.newsExists("Fastq analysis for " + folder + " has started.");
        //String command = scriptgenerator.getBclToFastQConfigString(scriptgenerator.getSamplesheet(), folder);
        if(!analysisHasBeenPerformed){
            //String command = scriptgenerator.getBclToFastQConfigString(scriptgenerator.getSamplesheet(), folder);            
            //String output = executeBclToFastQ(command);
            //String output = executeBclToFastQ2(folder);
            //if(output.contains("completed with no problems")){
                
                try{
                    //command = "/bin/bash -c ";
                    //command = command + Preferences.getWorkdir() + folder + "/" + folder + "_commands.sh";
                    String flowcell = configurationManager.parseFCID(folder);
                    URL url = new URL(Preferences.getExecuterpathflowcellA() + "?workdir=" + Preferences.getWorkdir() + "&flowcell=" + folder);
                    URLConnection conn = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null){
                        System.out.println(inputLine);
                    }                         
                    in.close();                    
                    NewsHelper.publishFastqAnalysisStarted(folder);    
                        
                    
                    //http://blade-b1-p2.iit.ieo.eu:8081/Bash/Executer?flowcell=folder&workdir=Preferences.getWorkdir() + folder
                    //Process proc=Runtime.getRuntime().exec(command);
                }catch(MalformedURLException e){            
                    e.printStackTrace();
                }catch( IOException ioe){
                    ioe.printStackTrace();
                }
                
            //}else{
            //    NewsHelper.publishBclToFastqConfigError(folder);
            //}
        }
    }
    
    private String execute(String command){
        StringBuilder output = new StringBuilder();  
        //String command = "/usr/bin/perl /opt/cluster/bin/configureBclToFastq.pl --input-dir=/data/Illumina/Runs/140715_SN880_0293_AC4Y3RACXX/Data/Intensities/BaseCalls/ --output-dir=/scratch/140715_SN880_0293_AC4Y3RACXX/ --mismatches=1 --sample-sheet=/data/Illumina/PublicData/SampleSheets/140715_SN880_0293_AC4Y3RACXX.csv";;
        //chown hmueller /scratch/lab_bsf/projects/BSF_runs/libraries/*.csv
        //chown hmueller /scratch/lab_bsf/projects/BSF_runs/160322_ST-J00104_0044_AH5H77BBXX/*_submitDemux.sh

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

    @Override
    public void sendDeliveryMail(SamplesheetToCommands scriptgenerator, String folder) {
        System.out.println("sending delivery mail");
    }

    @Override
    public void updateData() {
        updateManager.runUpdater();
    }

    @Override
    public AnalysisManager getAnalysisManager() {
        return (AnalysisManager)this;
    }

    @Override
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    @Override
    public Preferences getPreferences() {
        return configurationManager.getPreferences();
    }

    @Override
    public UpdateManager getUpdateManager() {
        return updateManager;
    }
    
    
    
}
