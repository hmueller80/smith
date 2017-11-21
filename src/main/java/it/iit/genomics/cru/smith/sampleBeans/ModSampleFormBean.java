package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;

import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 * @(#)ModSampleFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for modify sample form.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "modSampleFormBean")
//@RequestScoped
@SessionScoped
public class ModSampleFormBean implements Serializable {

    private boolean permissionDenied;
    //SampleSearchBean sampleSearchBean;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public ModSampleFormBean() {
        //super();
        if(Preferences.getVerbose()){
            System.out.println("init ModSampleFormBean");
        }
        
        //init();
    }

    /**
    * init.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    
    /*
    @Override
    public void init() {

        //bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        //formId = "SampledetailsForm";

        FacesContext context = FacesContext.getCurrentInstance();
        //sampleSearchBean = (SampleSearchBean) context.getApplication().evaluateExpressionGet(context, "#{sampleSearchBean}", SampleSearchBean.class);
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        if (sid != null) {
            sampleID = Integer.parseInt(sid);
            // this should never happen and will return an error
        } else {
            sampleID = new Integer(-1);
        }

        // load all the applications
        loadApplications();
        // load Indexes
        loadIndexes();

        // load the sample informations
        permissionDenied = !load(true);
        if (permissionDenied) {
            return;
        }

        // get the user of the sample
        user = UserHelper.getUserByID(loadedSample.getUser().getId().intValue());

        // retrieve all the user's informations
        userLogin = user.getLogin();
        userName = user.getUserName();
        userEmail = user.getMailAddress();
        userTel = user.getPhone();
        pi = user.getPi();
        piLogin = UserHelper.getUserByID(pi.intValue()).getLogin();

    }
    */
   

    
    /**
    * Tests if logged user has modify sample permission.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    /* moved to super 
    public boolean getHasModifyPermission() {

        if (permissionDenied) {
            return false;
        }
        FacesContext context = FacesContext.getCurrentInstance();        
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUserBean}", LoggedUserBean.class)); 
        //LoggedUser lu = new LoggedUser();

        // if the logged user is an admin
        if (lu.getUserRole().equals(Preferences.ROLE_ADMIN)) {
            return true;
        }

        if (lu.getUserRole().equals(Preferences.ROLE_TECHNICIAN)) {
            return false;
        }

        if (loadedSample == null) {
            return false;
        }

        if (!status.equals(Sample.status_requested)) {
            return false;
        }
        // if the user of the sample and the logged user are the same
        if (user.getId().equals(lu.getUserId())) {
            return true;
        }

        // if the logged user is the PI of the sample's user
        if (lu.getLogin().equals(piLogin)) {
            return true;
        }

        return false;
    }
    */

    // TODO merge the code in new method, modify here and submit if possible
    /**
    * Persists sample modifications.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    /*moved to super
    public void modify() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // validate the form
            if (!validateForm(false)) {
                RuntimeException e = new RuntimeException("Errors in the form");
                throw e;
            }
            // load the sample in this session
            Sample s = (Sample) session.load(Sample.class, sampleID);
            // update the sample
            // Not updating status or user informations!
            SampleSpecDesc ssd = new SampleSpecDesc("", comments,sampleDescription);
            updateSampleData(s, ssd);
            Application a = getCurrentApplication();
            if (isNewApplication(a)) {
                session.save(a);
            } else {
                // load the existing application
                a = (Application) session.load(Application.class,
                        findApplicationID(a));
                session.saveOrUpdate(a);
            }
            s.setApplication(a);

            // index managing
            SequencingIndex si = (SequencingIndex) session.load(
                    SequencingIndex.class, new Integer(findIndexID(sequencingindex)));
            System.out.println("modify - loaded sample index " + si.getIndex()
                    + " " + si.getId());
            s.setSequencingIndexes(si);
            session.saveOrUpdate(s);

            tx.commit();
            NgsLimsUtility
                    .setSuccessMessage(formId, "SampleModbutton",
                            "Sample update successful", sampleName
                            + " saved correctly");

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "SampleModbutton",
                    "Saving Failed " + sampleName, fail);
        } finally {
            session.close();
        }

        loadApplications();

    }
    */

    /**
    * Deletes a sample.
    *
    * @author Francesco Venco
    * @return String - a redirect to sample deleted page
    * @since 1.0
    */
    /*moved to super
    public String delete() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // load the sample in this session
            Sample s = (Sample) session.load(Sample.class, sampleID);
            // load the multiple request, if any, 
            // and delete all of them
            Set<MultipleRequest> mrs = s.getMultipleRequests();
            for (MultipleRequest mr : mrs) {
                session.delete((MultipleRequest) session.load(MultipleRequest.class, mr.getId()));
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "SampleModbutton",
                    "Error in deleting " + sampleID, fail);
            return null;
        } finally {
            session.close();
        }
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            // load the sample in this session
            Sample s = (Sample) session.load(Sample.class, sampleID);

            // delete the sample
            this.status = "deleted";
            session.delete(s);
            tx.commit();
            NgsLimsUtility.setWarningMessage(formId, "SampleModbutton",
                    "Sample deletion successful: Sample", sampleID
                    + " have been deleted");

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "SampleModbutton",
                    "Error in deleting " + sampleID, fail);
            return null;
        } finally {
            session.close();
        }
        sampleSearchBean.initSampleList();
        return "sampleDeleted?faces-redirect=true";

    }
    */

    /**
    * Test NgsLimsUtility.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public String test() {
        System.out.println("modSampleFormBean.test() called");
        NgsLimsUtility.setFailMessage("SampledetailsForm", "SampleDeletionButton", "Test ",
                "Test");
        return "";
    }

    /**
    * Tests if sample status is "requested".
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    /*
    public boolean getIsRequested() {
        return status.equals(Sample.status_requested);
    }
    */
}
