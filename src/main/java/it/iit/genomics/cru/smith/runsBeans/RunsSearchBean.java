package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
//import it.iit.genomics.cru.smith.userBeans.LoginMonitor;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)RunsSearchBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for sample run searches.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
//@SessionScoped
@ViewScoped
public class RunsSearchBean implements Serializable {

    //private DataModel<SampleRun> runsList;
    private SampleRunDataModel runsList;
    private int newRunId = 1;
    private int selectedRunId = 1;
    LoggedUser loggedUserBean;
    
    List<SampleRun> allRuns = null;
    List<SampleRun> loadedRuns = null;
    RoleManager roleManager;
    String fastqhyperlink = Preferences.getFastqhyperlink();
    DataBean dataBean;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public RunsSearchBean() {
        if(Preferences.getVerbose()){
            System.out.println("init RunsSearchBean");
        }
        init();
    }
    
    /**
    * init.
    *
    * @author Heiko Muller
    * @since 1.0
    */
    public void init() {
        
        if(Preferences.getVerbose()){
            System.out.println("init initSampleRunList");
        }
        FacesContext context = FacesContext.getCurrentInstance(); 
        loggedUserBean = (LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class);  
        //LoginMonitor lm  = ((LoginMonitor) context.getApplication().evaluateExpressionGet(context, "#{loginMonitor}", LoginMonitor.class)); 
        roleManager = ((RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class)); 
        dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
        
        //allRuns = dataBean.getAllRuns();
        allRuns = RunHelper.getLazyRunsList();        
        if(allRuns.size() == dataBean.getAllRuns().size()){
            allRuns = dataBean.getAllRuns();
        }else{
            dataBean.updateRuns();
            allRuns = dataBean.getAllRuns();
        }
        //allRuns = RunHelper.getRunsList();
        
        //System.out.println("initSampleList: " + allSamples.size() + " samples loaded.");
        loadedRuns = new ArrayList<SampleRun>();
        if(!(roleManager.isAdmin() || roleManager.isTechnician())){
            for(SampleRun sr : allRuns){
                if(roleManager.hasLoadPermission(sr)){
                    //System.out.println("loading sample " + s.getName());
                    loadedRuns.add(sr);
                }
            }
        }else{
            for(SampleRun sr : allRuns){
                //if(roleManager.hasLoadPermission(sr)){
                    //System.out.println("loading sample " + s.getName());
                    loadedRuns.add(sr);
                //}
            }
        }
        this.runsList = new SampleRunDataModel(loadedRuns);
        //newRunId = RunHelper.getNextRunId();
        newRunId = 0;
        
        if(Preferences.getVerbose()){
            System.out.println("initSampleRunList...done");
        }

    }

    /**
    * init.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void init0() {
        
        FacesContext context = FacesContext.getCurrentInstance(); 
        loggedUserBean = (LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class);  
        String specifySamples = (String) context.getExternalContext().getRequestParameterMap().get("specifySamples");
        //LoggedUser loggedUser = new LoggedUser();
        List<SampleRun> templist = null;
        newRunId = RunHelper.getNextRunId();
        if (loggedUserBean.getIsTech() || loggedUserBean.getIsAdmin()) {
            System.out.println("getting run list from database ");
            templist = RunHelper.getRunsList();
            System.out.println("getting run list from database...done");
        } else {
            templist = RunHelper.getRunsListByGroupId(loggedUserBean.getPi());
                        //templist = RunHelper.getRunsList(newRunId);
            //System.out.println("Pi " + loggedUser.getPi());
        }

        //if (templist != null && templist.size() > 0) {
        //    SampleRun first = templist.get(0);
        //    newRunId = first.getId().getRunId() + 1;
        //    System.out.println("new Run ID " + newRunId);

        //} else {
        //    newRunId = 1;
        //}

        System.out.println("creating sample run data model ");
        runsList = new SampleRunDataModel(templist);
        System.out.println("creating sample run data model... done");

    }

    /**
    * Setter for runsList.
    *
    * @author Francesco Venco
    * @param runsList
    * @since 1.0
    */
    public void setRunsList(SampleRunDataModel runsList) {
        this.runsList = runsList;
    }

    /**
    * Getter for runsList.
    *
    * @author Francesco Venco
    * @return DataModel<SampleRun>
    * @since 1.0
    */
    public DataModel<SampleRun> getRunsList() {
        return this.runsList;
    }

    public int getNewRunId() {
		// init();

        return this.newRunId;
    }

    public int getSelectedRunId() {
        return this.selectedRunId;
    }

    public void setSelectedRunId(int selectedRunId) {
        this.selectedRunId = selectedRunId;
    }

    public String getFastqhyperlink() {
        return fastqhyperlink;
    }
    
    

    public boolean getUserHasAddPermission() {
        //FacesContext context = FacesContext.getCurrentInstance();       
        //LoggedUser loggeduser = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUserBean}", LoggedUserBean.class)); 
        //LoggedUser loggeduser = new LoggedUser();
        return (loggedUserBean.getIsTech() || loggedUserBean.getIsAdmin());
    }

    public void test() {

        System.out.println("RunsSearchBeanTest test() called.");

    }

}
