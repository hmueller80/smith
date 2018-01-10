/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl;

import at.ac.oeaw.cemm.lims.analysis.impl.model.SampleSheet;
import at.ac.oeaw.cemm.lims.analysis.impl.model.SampleSheetFactory;
import at.ac.oeaw.cemm.lims.api.analysis.AnalysisManager;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.NewsDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;


/**
 *
 * @author hmueller
 */
@ApplicationScoped
public class AnalysisManagerCEMM implements AnalysisManager {

    @Inject
    private DTOFactory dtoFactory;
    @Inject
    private ServiceFactory services;
    
    private ReentrantLock lock = new ReentrantLock();
    
    @Override
    public void run() {
        try{
            lock.lock();
            System.out.println("CEMM Command and samplesheet generation");

            List<String> todo = ConfigurationManagerCEMM.runFoldersToAnalyze();
            for (String beinganalyzed : todo) {
                String newsBody = "Run " + beinganalyzed + " has finished.";

                String FCID = ConfigurationManagerCEMM.parseFCID(beinganalyzed);

                System.out.println("runfolder being analyzed " + FCID);
                List<SampleRunDTO> run = services.getRunService().getRunsByFlowCell(FCID);
                boolean hasSampleRun = (run.size() > 0);
                if (beinganalyzed.length() > 0 && hasSampleRun) {
                    ConfigurationManagerCEMM.mkWorkSubdir(beinganalyzed);

                    boolean miseq = false;
                    boolean indexreversal = ConfigurationManagerCEMM.getFlowcellNeedsBarcodeI5ReverseComplementing(beinganalyzed);
                    if (FCID.length() != 9) {
                        //is MiSeq run, no index reversal
                        indexreversal = false;
                        miseq = true;
                    }
                    String expName = ConfigurationManagerCEMM.getExperimentName(beinganalyzed);
                    String ssPath = Preferences.getSampleSheetFolder() + expName + "_" + FCID + "_libraries.csv";

                    SampleSheet sampleSheet;
                    boolean newFile = false;
                    if (!(new File(ssPath).exists())) {
                        sampleSheet = SampleSheetFactory.createSamplesheet(run, indexreversal);
                        try {
                            sampleSheet.toFile(ssPath);
                            newFile = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Could not write the sample sheet file!");
                            return;
                        }
                    } else {
                        sampleSheet = SampleSheetFactory.readSamplesheet(ssPath);
                    }

                    if (sampleSheet == null) {
                        System.out.println("Sample sheet is null");
                        return;
                    }

                    DemuxAnalysisScript script = new DemuxAnalysisScript(beinganalyzed, miseq);
                    String path = Preferences.getRunfolderroot() + beinganalyzed + File.separator + beinganalyzed + "_submitDemux_test.sh";
                    if (!(new File(path)).exists()){
                        try {
                            script.writeToFile(path);
                            newFile = true;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Could not write the analysis script!");
                            return;
                        }
                    }

                    /*if (newFile){
                        execute("/scratch/lab_bsf/projects/BSF_runs/chown.sh");
                    }*/

                    if(!services.getNewsService().newsExists(newsBody)){
                        publishNews(beinganalyzed,newsBody);
                    }

                    //triggerAnalysis(beinganalyzed);
                }

            }
        }finally{
            lock.unlock();
        }
    }

    private void triggerAnalysis(String folder) {
        String newsBody = "Fastq analysis for " + folder + " has started.";

        boolean analysisHasBeenPerformed = services.getNewsService().newsExists(newsBody);
        if (!analysisHasBeenPerformed) {
            try {
                URL url = new URL(Preferences.getExecuterPath() + "?workdir=" + Preferences.getWorkdir() + "&flowcell=" + folder);
                URLConnection conn = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
                in.close();
                publishNews(folder,newsBody);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }
    
    private void publishNews(String header, String body){
        try {
            NewsDTO news = dtoFactory.createEmptyNews();
            news.setDate(new Date(System.currentTimeMillis()));
            news.setHeader(header);
            news.setBody(body);
            services.getNewsService().submitNews(news);
        } catch (Exception ex) {
           System.out.println("Could not publish news");
           ex.printStackTrace();
        }
    }
    
    
    private String execute(String command){
        StringBuilder output = new StringBuilder();  
     
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
    public void resetDemux(String runFolder) throws Exception {
        try {
            lock.lock();
            if (!ConfigurationManagerCEMM.folderPassesCutoff(runFolder)) {
                throw new Exception("Folder " + runFolder + " is too old! Threshold is " + Preferences.getCutoff());
            }
            String FCID = ConfigurationManagerCEMM.parseFCID(runFolder);
            String expName = ConfigurationManagerCEMM.getExperimentName(runFolder);
            String ssPath = Preferences.getSampleSheetFolder() + expName + "_" + FCID + "_libraries.csv";
            File ssPathFile = new File(ssPath);
            if (ssPathFile.exists()) {
                String newPath = Preferences.getSampleSheetFolder() + expName + "_" + FCID + "_libraries_OLD" + ".csv";
                File newFile = new File(newPath);
                Integer counter = 1;
                while (newFile.exists()) {
                    newPath = Preferences.getSampleSheetFolder() + expName + "_" + FCID + "_libraries_OLD" + counter + ".csv";
                    newFile = new File(newPath);
                    counter += 1;
                }
                Files.move(ssPathFile.toPath(), newFile.toPath());
            }

            String newsBody = "Fastq analysis for " + runFolder + " has started.";

            if (services.getNewsService().newsExists(newsBody)) {
                services.getNewsService().removeNewsWithBody(newsBody);
            }
        }catch(Exception e){
            throw new Exception(e);
        }finally {
            lock.unlock();
        }
    }
    
}
