package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.AttributeValue;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)SampleRichDataBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for management of attribute value pairs.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "sampleRichDataBean")
@RequestScoped
public class SampleRichDataBean implements Serializable {

    protected DataModel attributevalues;
    
    protected ResourceBundle bundle;
    protected String userName;
    protected String institute;
    protected Integer pi;
    protected String piLogin;
    protected String piName;
    protected Integer sampleID;
    protected Integer loadedID;
    protected Double bulkFragmentSize = new Double(200.0);
    protected String fragmentationImage;
    protected Double picoGreenQuantification;
    protected String picoGreenImage;
    protected Integer readLength;
    protected String readMode;
    protected Integer depth;
    protected String instrument = "HiSeq2000";
    protected String userLogin;
    protected String userEmail;
    protected String userTel;
    protected String fragmentationImageFileName;
    protected String picoGreenImageFileName;
    protected Date requestDate = new Date(System.currentTimeMillis());
    protected String antibody = "undefined";
    protected String applicationName = "ChIP_Seq";
    protected Application sampleApplication;
    protected String comments = "";
    protected Double sampleConcentration = new Double(100.0);
    protected String costCenter;
    protected String sampleDescription;
    protected Boolean librarySynthesis = false;
    protected String organism = "HUMAN";
    protected String sampleName = "undefined";
    protected String sampleType = "IP";
    protected String processed = "false";
    protected Double totalAmount = new Double(100.0);
    protected String status = "requested";
    protected User user;
    protected User loggeduser;
    protected String sequencingindex = "none";
    protected List<SampleSpecDesc> selectedIndexes;
    protected Date biodate = new Date(System.currentTimeMillis());
    protected Double biomolarity = new Double(100.0); 
    protected String experiment = "ChIP-Seq";
    protected String timestep;
    protected Sample loadedSample;
    private List<Application> applications;
    protected List<SampleSpecDesc> possibleIndexes;
    protected List<SequencingIndex> indexesObjectsList;
    protected String formId;
    protected String recipe = Preferences.getDefault_application();
    protected Recipes recipes;
    
    protected RoleManager roleManager;
    

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SampleRichDataBean() {
        if(Preferences.getVerbose()){
            System.out.println("init SampleRichDataBean");
        }
        
        this.init();

    }

    /**
    * init
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void init() {
        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        formId = "SampleAttributesForm";

        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class);  
        loggeduser = roleManager.getLoggedUser();
        if (sid != null) {
            sampleID = Integer.parseInt(sid);
        } // this should never happen and will return an error
        else {
            sampleID = -1;
        }

        //System.out.println("Attribute value bean: load attribute values");
        this.loadAttributeValues();

        //load and check if has loading permission for sample
        //if(!load(true)) return;
        //System.out.println("Attribute value bean: load sample");
        load(true);
        //System.out.println("Attribute value bean : loading done");

        //get the user of the sample
        user = UserHelper.getUserByID(loadedSample.getUser().getId());

        //retrieve all the user's informations
        userLogin = user.getLogin();
        userName = user.getUserName();
        userEmail = user.getMailAddress();
        userTel = user.getPhone();
        pi = user.getPi();
        piLogin = UserHelper.getUserByID(pi).getLogin();

    }

    /**
    * Saves experiment name and time step information for the current sample.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void saveExperimentInfo() {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // load the sample in this session
            Sample s = (Sample) session.load(Sample.class, sampleID);
            //update the sample          
            s.setExperimentName(experiment);
            Date d = null;
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-MM-SS");
            if (timestep != null) {
                try {
                    d = df.parse(timestep);
                } catch (ParseException ex) {
                    Logger.getLogger(SampleFormBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //s.setTimeSeriesStep(d);
            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "ExperimentSaveButton", "Sample update successful", sampleName + " saved correctly");

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "ExperimentSaveButton", "Saving Failed" + sampleName, fail);
        } finally {
            session.close();
        }

    }

    /**
    * Loads attribute value pairs for the current sample.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void loadAttributeValues() {
        attributevalues = new ListDataModel(AttributevaluesHelper.getAttributvalues(sampleID));

    }

    /**
    * Getter for attributevalues.
    *
    * @author Francesco Venco
    * @return DataModel
    * @since 1.0
    */
    public DataModel getAttributevalues() {
        return this.attributevalues;
    }

    /**
    * Getter for the value of an attribute.
    *
    * @author Francesco Venco
    * @param av
    * @return String
    * @since 1.0
    */
    public String getValue(AttributeValue av) {
        String toRet = "";
        if (av.getValue() != null && !av.getValue().equals("")) {
            toRet = av.getValue();
        } else {
            toRet = "" + av.getNumericValue();
        }
        return toRet;
    }

    /**
    * Return true if the current user has the permission to modify the sample.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean getHasModifyPermission() {
        FacesContext context = FacesContext.getCurrentInstance();        
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
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

        // if the user of the sample and the logged user are the same
        if (user.getId().equals(lu.getUserId())) {
            return true;
        }

        // if the logged user is the PI of the sample's user        
        if (lu.getLogin().equals(piLogin)) {
            return true;
        }

        // finally check for the projects and
        // eventually the permission to modify samples
        ArrayList<Collaboration> allCols = new ArrayList(lu.getCollaborations());
        for (Collaboration c : allCols) {
            if (c.getModifyPermission() > 0) {
                Project p = c.getProject();
                for (Sample s : p.getSamples()) {
                    if (s.getId().equals(sampleID)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    // load the data of an existing sample TODO to be moved ?
    /**
    * Loads data of a sample.
    *
    * @author Francesco Venco
    * @param silent
    * @return boolean
    * @since 1.0
    */
    public boolean load(boolean silent) {
        System.out.println("LOAD SAMPLE");
        boolean toRet = true;
        reset();
        //FacesContext context = FacesContext.getCurrentInstance();        
        //User loggeduser = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)).getLoggedUser(); 
        //loggeduser = new LoggedUser().getLoggedUser();
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        if (session == null) {
            System.out.println("Session null...");
        }
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            loadedSample = (Sample) session.load(Sample.class, sampleID);
            // check permission for loading
            System.out.println("Test for permission");
            if (!hasLoadPermission(loadedSample, loggeduser)) {
                fail = bundle.getObject("samples.permission.denied")
                        .toString();
                System.out.println("Fail message: " + fail);
                throw new RuntimeException();
            }
            System.out.println("Set data");
            setCurrentNgssampleData(loadedSample);
            // lazy init for projects
            for (Project p : loadedSample.getProjects()) {
                p.getId();
            }
            // init sample description ( used when loading information
            // for a new sample
            selectedIndexes = new ArrayList<SampleSpecDesc>();
            SampleSpecDesc ssd = new SampleSpecDesc(sequencingindex, this.comments, this.sampleDescription);
            selectedIndexes.add(ssd);
            tx.commit();
            System.out.println("Messaging");
            if (!silent) {
                NgsLimsUtility
                        .setSuccessMessage(
                                formId,
                                "NGSRequestsloadbutton",
                                "OK",
                                bundle.getString("samples.success.load.part1")
                                + " "
                                + sampleID
                                + " "
                                + bundle
                                .getString("samples.success.load.part2"));
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Rollback!! ");
            try {
                tx.rollback();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
            System.out.println("Rollback done");
            if (fail == null || fail.equals("")) {
                fail = bundle.getString("samples.success.load.part1") + " "
                        + sampleID + " "
                        + bundle.getString("samples.not.found");
            }
            if (!silent) {
                NgsLimsUtility.setFailMessage(formId, "NGSRequestsloadbutton",
                        bundle.getString("navigation.error") + ": " + fail,
                        fail);
            }
            toRet = false;

        } finally {
            session.close();
        }
        return toRet;

    }
    
    /**
    * Reset fields to default values.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void reset() {
        applicationName = "undefined";
        organism = "undefined";
        antibody = "";
        bulkFragmentSize = new Double(200);

    }
    
    /**
    * Tests if user has permission to load sample.
    *
    * @author Francesco Venco
    * @param s
    * @param u
    * @return boolean
    * @since 1.0
    */
    public boolean hasLoadPermission(Sample s, User u) {
        if (u.getUserRole().equals(Preferences.ROLE_ADMIN)
                || u.getUserRole().equals(Preferences.ROLE_TECHNICIAN)) {
            return true;
        }
        int spi = s.getUser().getPi().intValue();
        // sample group -> return true
        if (spi == u.getPi().intValue()) {
            return true;
        }

        // we need to check if the sample belong to a project
        // part of the user collaborations
        ArrayList<Project> projs = new ArrayList<Project>(s.getProjects());
        for (Project p : projs) {
            ArrayList<Collaboration> cols = new ArrayList<Collaboration>(
                    p.getCollaborations());
            for (Collaboration c : cols) {
                if (c.getUser().getId().equals(u.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
    * Setter for current sample data.
    *
    * @author Francesco Venco
    * @param ngssample
    * @since 1.0
    */
    private void setCurrentNgssampleData(Sample ngssample) {

        if (ngssample != null) {
            
            User u = ngssample.getUser();
            userName = u.getUserName();
            userLogin = u.getLogin();
            userEmail = u.getMailAddress();
            userTel = u.getPhone();
            piName = UserHelper.getPi(u).getUserName();

            // get sample info
            sampleID = ngssample.getId();
            sampleName = ngssample.getName();
            sampleType = ngssample.getType();
            organism = ngssample.getOrganism();
            antibody = ngssample.getAntibody();
            librarySynthesis = ngssample.isLibrarySynthesisNeeded();
            sampleConcentration = ngssample.getConcentration();
            totalAmount = ngssample.getTotalAmount();
            bulkFragmentSize = ngssample.getBulkFragmentSize();
            costCenter = ngssample.getCostCenter();
            processed = ngssample.getStatus();
            sampleDescription = ngssample.getDescription();
            comments = ngssample.getComment();
            status = ngssample.getStatus();
            try {
                sequencingindex = ngssample.getSequencingIndexes().getIndex();
            } catch (Exception e) {
                sequencingindex = "";
            }
            //if (ngssample.getTimeSeriesStep() != null) {
            //    SimpleDateFormat df = new SimpleDateFormat(
            //            "dd-MM-yyyy HH-MM-SS");
            //    timestep = df.format(ngssample.getTimeSeriesStep());
            //}
            // TODO select the one from input
			/*
             * if(ngssample.getSamplesequencingindexeses().iterator().hasNext()){
             * Samplesequencingindexes ss =
             * ngssample.getSamplesequencingindexeses().iterator().next();
             * sequencingindex = ss.getSequencingindexes().getIndex(); }
             */
            if (ngssample.getBioanalyzerDate() != null) {
                //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                //biodate = df.format(ngssample.getBioanalyzerDate());
                biodate = ngssample.getBioanalyzerDate();
            }
            biomolarity = ngssample.getBionalyzerBiomolarity();
            // get application info
            sampleApplication = ngssample.getApplication();
            applicationName = sampleApplication.getApplicationname();
            experiment = ngssample.getExperimentName();
            readMode = sampleApplication.getReadmode();
            depth = sampleApplication.getDepth();
            readLength = sampleApplication.getReadlength();
            instrument = sampleApplication.getInstrument();

        } else {
            System.out.println("ngssample not found");
            reset();
        }
    }
    
    /**
    * Finds projects associated to loaded sample.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public String getProjectsInvolvedList() {
        String toRet = "";
        for (Project p : loadedSample.getProjects()) {
            toRet = toRet + p.getId() + " ";
        }
        return toRet;
    }
    
    /**
    * Getter for sampleName.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSampleName() {
        return sampleName;
    }
    
    /**
    * Getter for sampleID.
    * 
    * @author Francesco Venco
    * @return Integer
    * @since 1.0
    */
    public Integer getSampleID() {
        return sampleID;
    }
    
    /**
    * Getter for experiment.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getExperiment() {
        return this.experiment;
    }

    public String getTimestep() {
        return timestep;
    }
    
    

}
