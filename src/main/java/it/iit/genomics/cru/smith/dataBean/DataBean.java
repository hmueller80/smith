/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.dataBean;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author hmueller
 */
@ManagedBean(name = "dataBean")
@ApplicationScoped
public class DataBean implements Serializable{
    
    List<SampleRun> allRuns;
    List<Sample> allSamples;
    List<String> libraries;
    List<String> submissions;

    /**
     * Creates a new instance of DataBean
     */
    public DataBean() {
        if(Preferences.getVerbose()){
            System.out.println("init dataBean");
        }
        allRuns = RunHelper.getRunsList();
        allSamples = SampleHelper.getSampleList();
        initLibraries();
        initSubmissions();
    }

    public List<SampleRun> getAllRuns() {
        return allRuns;
    }

    public void setAllRuns(List<SampleRun> allRuns) {
        this.allRuns = allRuns;
    }

    public List<Sample> getAllSamples() {
        return allSamples;
    }

    public void setAllSamples(List<Sample> allSamples) {
        this.allSamples = allSamples;
    }
    
    public void updateRuns(){
        allRuns = RunHelper.getRunsList();
    }
    
    public void updateSamples(){
        allSamples = SampleHelper.getSampleList();
        initLibraries();
    }
    
    public void updateRun(SampleRun sr){
        for(SampleRun x : allRuns){
            if(x.getId().getRunId() == sr.getId().getRunId() && x.getId().getSamId() == sr.getId().getSamId()){
                x = sr;
            }
        } 
    }
    
    public void updateSample(Sample s){
        for(Sample x : allSamples){
            if(x.getId().intValue() == s.getId()){
                x = s;
            }
        }
        initLibraries();
    }
    
    public void removeSample(Sample s){
        for(Sample x : allSamples){
            //System.out.println(x.getId());
            if(x.getId().intValue() == s.getId()){
                allSamples.remove(x);
                return;
            }
        }
        initLibraries();
    }
    
    public void removeSample(int id){
        for(int i = 0; i < allSamples.size(); i++){
            //System.out.println(x.getId());
            if(allSamples.get(i).getId().intValue() == id){
                allSamples.remove(i);
                return;
            }
        }
        initLibraries();
    }
    
    public void addSample(Sample s){
        allSamples.add(0, s);
        initLibraries();
    }
    
    public void initLibraries(){
        libraries = new ArrayList<String>();
        for(int i = 0; i < allSamples.size(); i++){            
            String ln = allSamples.get(i).getLibraryName();
            if(!libraries.contains(ln)){
                libraries.add(ln);
            }
        }        
    }
    
    public void initSubmissions(){
        submissions = new ArrayList<String>();
        for(int i = 0; i < allSamples.size(); i++){            
            String ln = allSamples.get(i).getSubmissionId().toString();
            if(!submissions.contains(ln)){
                submissions.add(ln); 
            }
        }        
    }
    
    public List<String> getLibraries(){
        return libraries;   
    }
    
    public List<String> getSubmissions(){
        return submissions;   
    }
    
}
