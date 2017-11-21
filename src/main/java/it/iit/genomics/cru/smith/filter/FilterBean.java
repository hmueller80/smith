/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.filter;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.flowcellBeans.SampleRunDataModel;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @(#)FilterBean.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 *
 * Class serves as backing bean for filtering in dataTables.
 * <p:dataTable filteredValue="#{filterBean.sampleRun}"
 * 
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "filterBean")
@SessionScoped
public class FilterBean implements Serializable{ 
    
    List<Sample> sample;
    List<SampleRun> sampleRun;
    List<Project> project;
    List<User> user;
    List<Reagent> reagent;
    List<Sample> requestedSample;
    List<SampleRun> libraryRun;
    
   

    /**
     * Creates a new instance of FilterBean
     * 
     * @author Heiko Muller
     * @since 1.0
     */     
    public FilterBean() {
        if(Preferences.getVerbose()){
            System.out.println("init FilterBean");
        }
    }

    /**
     * For filtering of List<Sample> data.
     * 
     * @author Heiko Muller
     * @return List<Sample>
     * @since 1.0
     */  
    public List<Sample> getSample() {
        return sample;
    }

    /**
     * For filtering of List<Sample> data.
     * 
     * @author Heiko Muller
     * @param sample
     * @since 1.0
     */ 
    public void setSample(List<Sample> sample) {
        this.sample = sample;
    }

    /**
     * For filtering of List<SampleRun> data.
     * 
     * @author Heiko Muller
     * @return List<SampleRun>
     * @since 1.0
     */  
    public List<SampleRun> getSampleRun() {
        return sampleRun;
    }

    /**
     * For filtering of List<SampleRun> data.
     * 
     * @author Heiko Muller
     * @param sampleRun
     * @since 1.0
     */ 
    public void setSampleRun(List<SampleRun> sampleRun) {
        this.sampleRun = sampleRun;
    }

    /**
     * For filtering of List<Project> data.
     * 
     * @author Heiko Muller
     * @return List<Project>
     * @since 1.0
     */  
    public List<Project> getProject() {
        return project;
    }

    /**
     * For filtering of List<Project> data.
     * 
     * @author Heiko Muller
     * @param project
     * @since 1.0
     */ 
    public void setProject(List<Project> project) {
        this.project = project;
    }

    /**
     * For filtering of List<User> data.
     * 
     * @author Heiko Muller
     * @return List<User>
     * @since 1.0
     */  
    public List<User> getUser() {
        return user;
    }

    /**
     * For filtering of List<User> data.
     * 
     * @author Heiko Muller
     * @param user
     * @since 1.0
     */ 
    public void setUser(List<User> user) {
        this.user = user;
    }

    /**
     * For filtering of List<Sample> data.
     * 
     * @author Heiko Muller
     * @return List<Reagent>
     * @since 1.0
     */  
    public List<Reagent> getReagent() {
        return reagent;
    }

    /**
     * For filtering of List<Reagent> data.
     * 
     * @author Heiko Muller
     * @param reagent
     * @since 1.0
     */ 
    public void setReagent(List<Reagent> reagent) {
        this.reagent = reagent;
    }

    public List<Sample> getRequestedSample() {
        return requestedSample;
    }

    public void setRequestedSample(List<Sample> requestedSample) {
        this.requestedSample = requestedSample;
    }

    public List<SampleRun> getLibraryRun() {
        return libraryRun;
    }

    public void setLibraryRun(List<SampleRun> libraryRun) {
        this.libraryRun = libraryRun;
    }

       
    
}
