package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SampleRunId;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)ModSampleRunFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for modification of sample runs.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@ViewScoped
public class ModSampleRunFormBean extends SampleRunFormBean implements Serializable {

    private String _clusterBarcode;
    private String _preparationBarcode;
    private String _sequencingBarcode;
    private String _lane;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public ModSampleRunFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init ModSampleRunFormBean");
        }
        init();
        load();
        _lane = this.getLane();
        System.out.println("Lane is " + this.getLane() + " " + _lane);
        System.out.println("Lane and _lane are equals? " + _lane.equals(this.getLane()));

        formId = "SamplerundetailsForm";
        _clusterBarcode = clusterBarcode;
        _preparationBarcode = preparationBarcode;
        _sequencingBarcode = sequencingBarcode;
    }

    /**
     * Deletes a sample run.
     *
     * @author Francesco Venco
     * @return String - a redirect to the runs details page
     * @since 1.0
     */
    public String delete() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // load the sample in this session
            Sample s = (Sample) session.load(Sample.class, sampleID);
            //revert sample to requested
            s.setStatus("requested");
            // load sample run
            SampleRunId id = new SampleRunId();
            id.setRunId(runID);
            id.setSamId(sampleID);
            SampleRun sr = (SampleRun) session.load(SampleRun.class, id);
            session.delete(sr);
            tx.commit();
            NgsLimsUtility.setWarningMessage(formId, "save", "Sample run deletion successful", "Deleted");
            return "runDetails?rid=" + this.runID + " faces-redirect=true";
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "save", "Error in deleting ", fail);
            return null;
        } finally {
            session.close();
        }
    }

    /**
     * Saves a sample run.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void save() {
        //TODO create a method to check the reagents instead of repeating code...
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            //set the id object
            id = new SampleRunId();
            id.setRunId(runID);
            id.setSamId(sampleID);
            SampleRun sr = (SampleRun) session.get(SampleRun.class, id);
            //retrieve the reagents and update if necessary
            boolean reagentsAreOk = true;
            //cluster reagent
            if (clusterBarcode != null && !clusterBarcode.equals("")) {
                clusterReag = (Reagent) session.get(Reagent.class, clusterBarcode);
                //if the reagent has been changed, we need to update both old and new
                if (!clusterBarcode.equals(_clusterBarcode)) {
                    //Reagent old = (Reagent) session.load(Reagent.class, _clusterBarcode );
                    //old.setSupportedreactions(old.getSupportedreactions() + 1);
                    if (!testAndDecreaseReagent(clusterReag, "clusterReag", "Cluster generation")) {
                        reagentsAreOk = false;
                    }
                }
            } else {
                reagentsAreOk = false;
                NgsLimsUtility.setFailMessage(formId, "clusterReag", "Reaganet not found", "Not found ");
            }
            //sample preparation reagent
            if (preparationBarcode != null && !preparationBarcode.equals("")) {
                sampleprepReag = (Reagent) session.get(Reagent.class, preparationBarcode);
                if (!preparationBarcode.equals(_preparationBarcode)) {
                    //Reagent old = (Reagent) session.load(Reagent.class, _preparationBarcode );
                    //old.setSupportedreactions(old.getSupportedreactions() + 1);
                    if (!testAndDecreaseReagent(sampleprepReag, "preparationReag", "Sample preparation")) {
                        reagentsAreOk = false;
                    }
                }
            } else {
                reagentsAreOk = false;
                NgsLimsUtility.setFailMessage(formId, "preparationReag", "Reagent not found", "Not found ");
            }
            //sequencing reagent
            if (sequencingBarcode != null && !sequencingBarcode.equals("")) {
                sequencingReag = (Reagent) session.get(Reagent.class, sequencingBarcode);
                if (!sequencingBarcode.equals(_sequencingBarcode)) {
                    //Reagent old = (Reagent) session.load(Reagent.class, _sequencingBarcode );
                    //old.setSupportedreactions(old.getSupportedreactions() + 1);
                    if (!testAndDecreaseReagent(sequencingReag, "sequencingReag", "Sequencing")) {
                        reagentsAreOk = false;
                    }
                }
            } else {
                reagentsAreOk = false;
                NgsLimsUtility.setFailMessage(formId, "sequencingReag", "Reaganet not found", "Not found ");
            }
            //load the sample ( to set the index)
            sample = (Sample) session.get(Sample.class, sampleID);
            //validate the information to be inserted
            if (!validateForm() || !reagentsAreOk) {
                throw new RuntimeException("Errors in the form");
            }
            //manage the lanes
            System.out.println("Lane and _lane are " + this.getLane() + " " + _lane);
            System.out.println("Lanes are changed? " + lanesAreChanged());
            if (lanesAreChanged()) {
                //delete all existing lanes
                for (Lane oldLane : sr.getLanes()) {
                    oldLane = (Lane) session.get(Lane.class, oldLane.getId());
                    session.delete(oldLane);
                }
                for (String newLaneName : this.getListOfLanes()) {
                    Lane newLane = new Lane();
                    newLane.setLaneName(newLaneName);
                    newLane.setSamplerun(sr);
                    session.save(newLane);
                }
            }
            //set the data of the new sample run entity object
            setSamplerunData(sr, session);
            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "save", "Sample run updated", " saved correctly");
            _lane = this.getLane();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            fail = fail + e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "save", fail, "Run update failed ");
        } finally {
            session.close();
        }

    }

    /**
     * Tests if lanes have changed.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean lanesAreChanged() {
        Set<String> newLanes = new HashSet<String>(this.getListOfLanes());
        if (newLanes.isEmpty()) {
            return true;
        }
        Set<String> oldLanes = new HashSet<String>();
        Scanner s = new Scanner(_lane);
        while (s.hasNext()) {
            String name = s.next();
            oldLanes.add(name);
            if (!newLanes.contains(name)) {
                return true;
            }
        }
        for (String newName : newLanes) {
            if (!oldLanes.contains(newName)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Tests if logged user has sample run update permission.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getUserHasUpdatePermission() {
        FacesContext context = FacesContext.getCurrentInstance();        
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //LoggedUser lu = new LoggedUser();
        String role = lu.getUserRole();
        if (role.equals(Preferences.ROLE_ADMIN)) {
            return true;
        }
        if (!role.equals(Preferences.ROLE_TECHNICIAN)) {
            return false;
        }
        String status = this.sample.getStatus();
        return (status.equals(Sample.status_requested) || status.equals(Sample.status_queued) || status.equals(Sample.status_confirmed));
    }
}
