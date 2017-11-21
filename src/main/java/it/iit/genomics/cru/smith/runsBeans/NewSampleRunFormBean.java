package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.MultipleRequest;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SampleRunId;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)NewSampleRunFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for new sample run form.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newSampleRunFormBean")
@ViewScoped
public class NewSampleRunFormBean extends SampleRunFormBean implements Serializable {

    private List<Integer> sampleRequestedToghetherIds;
    private List<SampleIndex> allSamplesIndexes;
    private int tab1Index;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public NewSampleRunFormBean() {
        super.init();
        if(Preferences.getVerbose()){
            System.out.println("init NewSampleRunFormBean");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();       
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //user = new LoggedUser().getLoggedUser();
        user = lu.getLoggedUser();
        formId = "NewSampleRunForm";
        // load the sample index
        Session session = HibernateUtil.getSessionFactory().openSession();
        sampleRequestedToghetherIds = new ArrayList<Integer>();
        allSamplesIndexes = new ArrayList<SampleIndex>();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sample = (Sample) session.get(Sample.class, sampleID);
            sequencingindex = sample.getSequencingIndexes().getIndex();
            //Set<MultipleRequest> mrs = sample.getMultipleRequests();
            Set<Library> mrl = sample.getLibrary();
            System.out.println("Multiple request for sample " + sample.getId() + " is " + new ArrayList<Library>(mrl));
            if (mrl.isEmpty()) {
                System.out.println("Sample is singleton" + sample.getId());
                sampleRequestedToghetherIds.add(sampleID);
                allSamplesIndexes.add(new SampleIndex(sample.getId(), sample.getSequencingIndexes().getIndex()));

            } else {
                for (Library l : mrl) {
                    int i = l.getId().getLibraryId();
                    List<Library> possiblyMissing = RunHelper.getPoolMembers(i);
                    for (Library l2 : possiblyMissing) {
                        Sample s = l2.getSample();
                        if (!s.getStatus().equals(Sample.status_requested)) {
                            continue;
                        }
                        sampleRequestedToghetherIds.add(s.getId());
                        allSamplesIndexes.add(new SampleIndex(s.getId(), s.getSequencingIndexes().getIndex()));
                    }
                }
            }

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
        } finally {
            session.close();
        }

    }

    /**
     * Saves a new sample run.
     *
     * @author Francesco Venco
     * @return String - a redirect to the runDetail page
     * @since 1.0
     */
    public String submit() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            boolean firstSample = true;
            for (SampleIndex si : allSamplesIndexes) {

                // reagentsList = session.createQuery("from Reagent").list();
                // make a new sample and set basic data
                SampleRun sr = new SampleRun();
                // set the id object
                id = new SampleRunId();
                id.setRunId(runID);
                id.setSamId(si.getId());
                System.out.println("sampleID " + si.getId());
                // retrieve the reagents
                boolean reagentsAreOk = true;

                if (clusterBarcode != null && !clusterBarcode.equals("")) {
                    clusterReag = (Reagent) session.get(Reagent.class,
                            clusterBarcode);
                }

                if (false == testAndUpdateReagent(clusterReag, "clusterReag",
                        "Cluster generation", !firstSample, -allSamplesIndexes.size())) {
                    reagentsAreOk = false;
                }

                if (preparationBarcode != null
                        && false == preparationBarcode.equals("")) {
                    sampleprepReag = (Reagent) session.get(Reagent.class,
                            preparationBarcode);
                }

                if (false == testAndUpdateReagent(sampleprepReag, "preparationReag",
                        "Sample preparation", !firstSample, -allSamplesIndexes.size())) {
                    reagentsAreOk = false;
                }

                if (sequencingBarcode != null
                        && false == sequencingBarcode.equals("")) {
                    sequencingReag = (Reagent) session.get(Reagent.class,
                            sequencingBarcode);
                }

                if (false == testAndUpdateReagent(sequencingReag, "sequencingReag",
                        "Sequencing", !firstSample, -allSamplesIndexes.size())) {
                    reagentsAreOk = false;
                }

                // set the sample to queued and (re)set sequencing index
                sample = (Sample) session.load(Sample.class, si.getId());
                if (false == sample.getStatus().equals(Sample.status_requested)) {
                    throw new RuntimeException(
                            "Error: sample " + si.getId() + " is not in 'requested' status");
                }

                sample.setStatus("queued");

                // sample.setSequencingindex(sequencingindex);
                // set the data of the new sample run entity object
                setSamplerunData(sr, session, si.getSequencingIndex());
                // validate the information to be inserted
                if (!(validateForm(si) && reagentsAreOk)) {
                    throw new RuntimeException("Errors in the form");
                }

                session.save(sr);
                //lane management
                ArrayList<String> lanes = this.getListOfLanes();
                for (String ln : lanes) {
                    Lane l = new Lane();
                    l.setLaneName(ln);
                    l.setSamplerun(sr);
                    System.out.println("New lane assigend to " + sr.getRunId() + " " + sr.getsamId());
                    sr.getLanes().add(l);
                    session.save(l);
                }

                // if good, try to save and commit
                session.update(sr);
                firstSample = false;

            }
            tx.commit();

            NgsLimsUtility.setSuccessMessage(formId, "submission",
                    "Sample run added", " inserted correctly");
            return "runDetails?rid=" + this.runID + " faces-redirect=true";

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            fail = fail + e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "submission", fail,
                    "Run insertion failed ");
            return null;
        } finally {
            session.close();
        }

    }
    
    
    /**
     * Tests if logged user has permission to add sample runs.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getUserHasAddPermission() {
        String role = user.getUserRole();
        return (role.equals(Preferences.ROLE_TECHNICIAN) || role.equals(Preferences.ROLE_ADMIN));
    }

    /**
     * Getter for ids of pooled samples.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getSampleIds() {
        System.out.println("Building links to samples");
        String toRet = "";
        for (int sid : sampleRequestedToghetherIds) {
            toRet = toRet + " " + sid;

        }
        return toRet;

    }

    /**
     * Getter for ids of pooled samples.
     *
     * @author Francesco Venco
     * @return List<Integer>
     * @since 1.0
     */
    public List<Integer> getSampleRequestedToghetherIds() {
        return sampleRequestedToghetherIds;
    }

    /**
     * Setter for ids of pooled samples.
     *
     * @author Francesco Venco
     * @param sampleRequestedToghetherIds
     * @since 1.0
     */
    public void setSampleRequestedToghetherIds(List<Integer> sampleRequestedToghetherIds) {
        this.sampleRequestedToghetherIds = sampleRequestedToghetherIds;
    }

    /**
     * Getter for ids of pooled samples.
     *
     * @author Francesco Venco
     * @return List<SampleIndex>
     * @since 1.0
     */
    public List<SampleIndex> getSampleRequestedToghether() {
        return allSamplesIndexes;
    }

    /**
     * Setter for ids of pooled samples.
     *
     * @author Francesco Venco
     * @param sampleRequestedToghether
     * @since 1.0
     */
    public void setSampleRequestedToghether(List<SampleIndex> sampleRequestedToghether) {
        this.allSamplesIndexes = sampleRequestedToghether;
    }

    /**
     * Getter for tab index.
     *
     * @author Francesco Venco
     * @return int
     * @since 1.0
     */
    public int getTab1Index() {
        return tab1Index;
    }

    /**
     * Setter for tab index.
     *
     * @author Francesco Venco
     * @param tab1Index
     * @since 1.0
     */
    public void setTab1Index(int tab1Index) {
        this.tab1Index = tab1Index;
    }
}
