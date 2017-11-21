package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.MultipleRequest;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.news.NewsHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)RunDetailsBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for sample run details.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "runDetailsBean")
//@SessionScoped
@ViewScoped
public class RunDetailsBean implements Serializable {

    //protected List<SampleRunFormBean> sampleruns;
    private List<SampleRun> sampleruns;
    private ArrayList<Integer> sampleToBeAdded;
    private int runID;
    private String formId;
    private boolean runIsClosed;
    private boolean allConfirmed;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public RunDetailsBean() {
        if(Preferences.getVerbose()){
            System.out.println("init RunDetailsBean");
        }
        
        formId = "rundetailsForm";
        FacesContext context = FacesContext.getCurrentInstance();
        String rid = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        if (rid != null) {
            runID = Integer.parseInt(rid);
        } else {
            runID = -1;
        }
        sampleruns = RunHelper.getRunsList(runID);
        //check for samples requested toghether but not yet
        //put in the run
        sampleToBeAdded = new ArrayList<Integer>();
        HashSet<Integer> sampleToBeAddedSet = new HashSet<Integer>();
        HashSet<Integer> sampleRequestIds = new HashSet<Integer>();
        //find all ids or requests. Check if the run
        //is still open for modifications ( i.e. not all samples
        //have been confirmed yet)
        runIsClosed = true;
        allConfirmed = true;
        for (SampleRun sr : sampleruns) {
            String status = sr.getsample().getStatus();
            if (status.equals(Sample.status_queued) || status.equals(Sample.status_confirmed)) {
                runIsClosed = false;
            }
            if (!sr.getsample().getStatus().equals(Sample.status_confirmed)) {
                allConfirmed = false;
            }
            Set<MultipleRequest> mrs = sr.getsample().getMultipleRequests();
            if (!mrs.isEmpty()) {
                for (MultipleRequest mr : mrs) {
                    sampleRequestIds.add(mr.getId().getRequestId());
                }
            }
        }
        //add ids of samples requested along with the ones in the run
        for (Integer i : sampleRequestIds) {
            List<Library> possiblyMissing = RunHelper.getPoolMembers(i);
            for (Library mr : possiblyMissing) {
                sampleToBeAddedSet.add(mr.getId().getSampleId());
            }
        }
        //remove the ones already inserted
        for (SampleRun sr : sampleruns) {
            sampleToBeAddedSet.remove(sr.getsample().getId());
        }
        System.out.println(sampleToBeAddedSet);
        for (Integer i : sampleToBeAddedSet) {
            sampleToBeAdded.add(i);
            System.out.println("Adding to list :" + i);
        }
        System.out.println(sampleToBeAdded);
        NgsLimsUtility.setFailMessage(formId, "missing", "Missing Samples in the Run ", "Other samples have been requested along with the selected ones:");

    }

    /**
     * Getter for sampleruns
     *
     * @author Francesco Venco
     * @return List<SampleRun>
     * @since 1.0
     */
    public List<SampleRun> getSampleruns() {
        return this.sampleruns;
    }

    /**
     * Getter for runID
     *
     * @author Francesco Venco
     * @return int
     * @since 1.0
     */
    public int getRunID() {
        return this.runID;
    }

    /**
     * Close run action listener.
     *
     * @author Francesco Venco
     * @return String - a redirect to the run details page
     * @since 1.0
     */
    public String closeRun() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.id.runId = '" + runID + "'");
            runsList = (List<SampleRun>) q.list();
            //for for lazy initialization
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                csr.getsample().setStatus("running");
                session.saveOrUpdate(csr.getsample());
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        session.close();
        return "runDetails.jsf?rid=" + runID + " faces-redirect=true";
    }

    /**
     * Tests if logged user has permission to add a sample run.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getUserHasAddPermission() {
        FacesContext context = FacesContext.getCurrentInstance();       
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //User loggeduser = new LoggedUser().getLoggedUser();
        User loggeduser = lu.getLoggedUser();
        return (loggeduser.getUserRole().equals(Preferences.ROLE_TECHNICIAN) || loggeduser.getUserRole().equals(Preferences.ROLE_ADMIN));
    }

    /**
     * Getter for sampleToBeAdded.
     *
     * @author Francesco Venco
     * @return ArrayList<Integer>
     * @since 1.0
     */
    public ArrayList<Integer> getSampleToBeAdded() {
        System.out.println("List: " + sampleToBeAdded);
        return sampleToBeAdded;
    }

    /**
     * Setter for sampleToBeAdded.
     *
     * @author Francesco Venco
     * @param sampleToBeAdded
     * @since 1.0
     */
    public void setSampleToBeAdded(ArrayList<Integer> sampleToBeAdded) {
        this.sampleToBeAdded = sampleToBeAdded;
    }

    /**
     * Tests if run is closed.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean isRunIsClosed() {
        return runIsClosed;
    }

    /**
     * Setter for runIsClosed.
     *
     * @author Francesco Venco
     * @param runIsClosed
     * @since 1.0
     */
    public void setRunIsClosed(boolean runIsClosed) {
        this.runIsClosed = runIsClosed;
    }

    /**
     * Tests if all samples are confirmed to be run.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean isAllConfirmed() {
        return allConfirmed;
    }
    
    public void resetRun(){
        NewsHelper.resetRun(runID);
        //System.out.println("delete news for run " + runID);
    }
}
