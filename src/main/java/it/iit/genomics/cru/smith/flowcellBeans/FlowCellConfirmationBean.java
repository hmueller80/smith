package it.iit.genomics.cru.smith.flowcellBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)FlowCellConfirmationBean.java 20 JUN 2014 Copyright 2014 Computational Research
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
public class FlowCellConfirmationBean {

    private SampleRunDataModel sampleRuns;
    private SampleRun[] selectedSampleRuns;
    private LoggedUser lu;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public FlowCellConfirmationBean() {
        if(Preferences.getVerbose()){
            System.out.println("init FlowCellConfirmationBean");
        }
        initList();

    }

    /**
     * Inits list of SampleRuns.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    private void initList() {

        FacesContext context = FacesContext.getCurrentInstance(); 
        lu = (LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class); 
        if(lu == null){
            lu = new LoggedUser();
        }
        //lu = new LoggedUser();
        if (lu.getIsAdmin() || lu.getIsTech()) {
            sampleRuns = new SampleRunDataModel(RunHelper.getQueuedSampleRunList());
        } else {
            sampleRuns = new SampleRunDataModel(RunHelper.getQueuedSampleRunListForUser(lu.getUserId()));
        }
    }

    /**
     * Set the selected runs to "queued"
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void setSelectedToRunning() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            if(sampleRuns.isRowAvailable()){
                tx = session.beginTransaction();            
                for (SampleRun selected : selectedSampleRuns) {
                    Sample s = (Sample) session.load(Sample.class, selected.getId().getSamId());
                    if (s.getStatus().equals(Sample.status_queued)) {
                        //s.setStatus("running");
                        s.setStatus(Sample.status_confirmed);
                    }
                }
                tx.commit();
            }
        } catch (RuntimeException e) {
            tx.rollback();
        } finally {
            session.close();
        }
        initList();
    }

    /**
     * Getter for sampleRuns.
     *
     * @author Francesco Venco
     * @return SampleRunDataModel - the sampleRuns
     * @since 1.0
     */
    public SampleRunDataModel getSampleRuns() {
        return sampleRuns;
    }

    /**
     * Setter for sampleRuns.
     *
     * @author Francesco Venco
     * @param sampleRuns the sampleRuns to set
     * @since 1.0
     */
    public void setSampleRuns(SampleRunDataModel sampleRuns) {
        this.sampleRuns = sampleRuns;
    }

    /**
     * Getter for selectedSampleRuns.
     *
     * @author Francesco Venco
     * @return SampleRun[] - the selectedSampleRuns
     * @since 1.0
     */
    public SampleRun[] getSelectedSampleRuns() {
        return selectedSampleRuns;
    }

    /**
     * 
     */
    /**
     * Setter for selectedSampleRuns.
     *
     * @author Francesco Venco
     * @param selectedSampleRuns the selectedSampleRuns to set
     * @since 1.0
     */
    public void setSelectedSampleRuns(SampleRun[] selectedSampleRuns) {
        this.selectedSampleRuns = selectedSampleRuns;
    }  
}
