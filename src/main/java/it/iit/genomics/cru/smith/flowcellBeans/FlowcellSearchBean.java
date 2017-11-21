package it.iit.genomics.cru.smith.flowcellBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)FlowcellSearchBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for virtual flow cell confirmation.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
@Deprecated
public class FlowcellSearchBean implements Serializable {

    // data model containing the loaded samples
    private DataModel sampleList;

    LoggedUser loggedUserBean;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public FlowcellSearchBean() {
        if(Preferences.getVerbose()){
            System.out.println("init FlowcellSearchBean");
        }
        FacesContext context = FacesContext.getCurrentInstance(); 
        loggedUserBean = (LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class); 
        if(loggedUserBean == null){
            loggedUserBean = new LoggedUser();
        }
        //loggedUserBean = new LoggedUser();
        
        this.initSampleList();

    }

    /**
     * Inits list of Samples.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void udDate() {
        this.initSampleList();
    }

    /**
     * Setter for.LoggedUserBean
     *
     * @author Francesco Venco
     * @param loggedUserBean
     * @since 1.0
     */
    public void setLoggedUserBean(LoggedUser loggedUserBean) {
        this.loggedUserBean = loggedUserBean;
    }

     /**
     * Getter for.SampleList
     *
     * @author Francesco Venco
     * @return DataModel
     * @since 1.0
     */
    public DataModel getSampleList() {
        return this.sampleList;
    }

    /**
     * Loads list of running samples.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    private void initSampleList() {
        this.sampleList = new ListDataModel(SampleHelper.getRunningSampleList());
         // retrieve all the samples if user is admin or a technician
        /*if(loggedUserBean.getIsTech() || loggedUserBean.getIsAdmin())
         this.sampleList =  new ListDataModel(SampleHelper.getRunningSampleList());
         // no list for normal users
         else
         this.sampleList =  new ListDataModel();*/

    }

    /**
     * Tests user permission to add new SampleRuns.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean hasAddFlowcellPermission() {
        return loggedUserBean.getIsTech() || loggedUserBean.getIsAdmin();
    }

}
