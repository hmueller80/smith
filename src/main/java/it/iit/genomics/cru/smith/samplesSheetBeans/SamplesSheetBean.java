package it.iit.genomics.cru.smith.samplesSheetBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)SamplesSheetBean.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Backing bean for samplesheet data
 * 
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "samplesSheetBean")
@ViewScoped
public class SamplesSheetBean implements Serializable{
    
    private DataModel  runList;
    private List<SampleRun> filteredSampleRuns;
   
    LoggedUser loggedUser;
    DataBean dataBean;
    
    /**
     * Bean constructor
     * 
     * @author Francesco Venco
     * @since 1.0
     */
    public SamplesSheetBean(){ 
        if(Preferences.getVerbose()){
            System.out.println("init SamplesSheetBean");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();        
        loggedUser = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
        //loggedUser = new LoggedUser();
        this.initSampleList();
            
    }
    
    /**
     * Init.
     * 
     * @author Francesco Venco
     * @since 1.0
     */
    public void udDate(){
        this.initSampleList();
    }
    
    /**
     * Setter for logged user.
     * 
     * @author Francesco Venco
     * @param loggedUserBean
     * @since 1.0
     */
    public void setLoggedUserBean(LoggedUser loggedUserBean){
        this.loggedUser = loggedUserBean;
    }
    
    /**
     * Getter for runList.
     * 
     * @author Francesco Venco
     * @return DataModel
     * @since 1.0
     */
    public DataModel  getRunList(){
        return this.runList;
    }
    
    /**
     * Inits sample list.
     * 
     * @author Francesco Venco
     * @since 1.0
     */
    public void initSampleList(){
        List<SampleRun> runs = RunHelper.getLazyRunsList();
        List<SampleRun> bufferedRuns = dataBean.getAllRuns();
        if(bufferedRuns.size() == runs.size()){
            if(loggedUser.getIsAdmin() || loggedUser.getIsTech()){
                this.runList = new ListDataModel(bufferedRuns);
            }else{
                this.runList = new ListDataModel(RunHelper.getRunsListByGroupId(loggedUser.getPi()));
            }
        }else{
            dataBean.updateRuns();
            if(loggedUser.getIsAdmin() || loggedUser.getIsTech()){
                this.runList = new ListDataModel(bufferedRuns);
            }else{
                this.runList = new ListDataModel(RunHelper.getRunsListByGroupId(loggedUser.getPi()));
            }
        }
    }
    
    /**
     * Getter for filteredSampleRuns.
     * 
     * @author Francesco Venco
     * @return List<SampleRun>
     * @since 1.0
     */
    public List<SampleRun> getFilteredSampleRuns() {
        return filteredSampleRuns;
    }

    /**
     * Setter for filteredSampleRuns.
     * 
     * @author Francesco Venco
     * @param filteredSampleRuns
     * @since 1.0
     */
    public void setFilteredSampleRuns(List<SampleRun> filteredSampleRuns) {
        this.filteredSampleRuns = filteredSampleRuns;
    }
    
    /**
     * Creates a HashSet<String> for flow cell ids where filtered samples have been run .
     * 
     * @author Francesco Venco
     * @since 1.0
     */
    public void downloadFiltered(){
        Set<String> fcids = new HashSet<String>();
        for (SampleRun sr : filteredSampleRuns){
            String fcid = sr.getFlowcell();
            fcids.add(fcid);
        }
        if (fcids.isEmpty()){
            return;
        }        
        
        
    }
}
