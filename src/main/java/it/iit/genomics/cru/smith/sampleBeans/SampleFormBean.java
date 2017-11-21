package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
//import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
//import it.iit.genomics.cru.smith.userBeans.LoginMonitor;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)SampleFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for sample form.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "sampleFormBean")
@SessionScoped
public class SampleFormBean implements Serializable {

    //Sample fields
    protected Integer sampleID;
    protected String applicationName = "ChIP_Seq";
    protected User user;
    protected String sequencingindex = "none";
    protected String organism = "HUMAN";
    protected String sampleType = "IP";
    protected String antibody = "undefined";
    protected Boolean librarySynthesis = false;
    protected Double sampleConcentration = new Double(100.0);
    protected Double totalAmount = new Double(100.0);
    protected Double bulkFragmentSize = new Double(200.0);
    protected String costCenter;
    protected String status = "requested";
    protected String sampleName = "undefined";
    protected String comments = "";
    protected String sampleDescription;
    protected Date requestDate = new Date(System.currentTimeMillis());
    protected Date biodate = new Date(System.currentTimeMillis());
    protected Double biomolarity = new Double(100.0);
    protected Integer submissionId;
    protected String experiment = "ChIP-Seq";
    
    //form fields
    protected ResourceBundle bundle;
    protected String userName;
    protected String userLogin;
    protected String userEmail;
    protected String userTel;
    protected String institute;
    protected Integer pi;
    protected String piLogin;
    protected String piName;    
    protected Integer loadedID;
    protected Integer readLength;
    protected String readMode;
    protected Integer depth;
    protected String instrument = "HiSeq2000";    
    protected List<SampleSpecDesc> selectedIndexes;  
    protected String recipe = Preferences.getDefault_application();
    protected String formId;    
    
    //bean fields   
    protected Application sampleApplication;
    protected SequencingIndex sequencingIndexes;
    protected User loggeduser;      
    protected Sample loadedSample;
    protected static List<Application> applications;
    protected static List<SampleSpecDesc> possibleIndexes;
    protected static List<SequencingIndex> indexesObjectsList;    
    protected static Recipes recipes;
    
    protected RoleManager roleManager;
    DataBean dataBean;
    private boolean permissionDenied;
    private boolean hasModifyPermission;

    /**
    * Constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SampleFormBean() { 
        
        if(Preferences.getVerbose()){
            System.out.println("init SampleFormBean");
        }
        init();
    }

    /**
    * init.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void init() {
        
        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        formId = "";
        FacesContext context = FacesContext.getCurrentInstance();        
        roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class);  
        recipes = ((Recipes) context.getApplication().evaluateExpressionGet(context, "#{recipes}", Recipes.class));  
        loggeduser = roleManager.getLoggedUser();
        dataBean = roleManager.getDataBean();
        if(dataBean == null){
            System.out.println("initSampleFormBean: dataBean is null" );
        }else{
            System.out.println("initSampleFormBean: dataBean is not null" );
        } 
        
        loadApplications();
        loadIndexes(); 
        
        
   }
    
    /**
    * Tests if user has permission to modify sample.
    *
    * @author Heiko Muller
    * @param s
    * @return boolean
    * @since 1.0
    */
    public boolean hasModifyPermission() {
        if(roleManager == null){
            FacesContext context = FacesContext.getCurrentInstance();  
            roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class);
        }
        return roleManager.hasModifyPermission(loadedSample);
    }
    
    /**
    * Tests if user has permission to modify sample.
    *
    * @author Heiko Muller
    * @param s
    * @return boolean
    * @since 1.0
    */
    public boolean hasDeletePermission() {
        if(roleManager == null){
            FacesContext context = FacesContext.getCurrentInstance();  
            roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class);
        }
        return roleManager.hasDeletePermission(loadedSample);
    }
    
    // load all the applications TODO to be moved ?
    /**
    * Loads applications from database.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    @SuppressWarnings("unchecked")
    private static void loadApplications() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            applications = session.createQuery("from Application").list();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        //recipes = new Recipes();
        
    }
    
    /**
    * Getter for current application.
    *
    * @author Francesco Venco
    * @return Application
    * @since 1.0
    */
    protected Application getCurrentApplication() {
        Application a = new Application();
        if (applicationName != null) {
            a.setApplicationname(applicationName);
        } else {
            a.setApplicationname("undefined");
        }

        a.setDepth(depth);
        a.setReadlength(readLength);

        if (instrument != null) {
            a.setInstrument(instrument);
        } else {
            a.setInstrument("HiSeq2000");
        }

        if (readMode != null) {
            a.setReadmode(readMode);
        } else {
            a.setReadmode("SR");
        }

        return a;
    }
    
    /**
    * Getter for recipe.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getRecipe() {
        return recipe;
    }

    /**
    * Setter for recipe.
    * 
    * @author Francesco Venco
    * @param recipe
    * @since 1.0
    */
    public void setRecipe(String recipe) {
        this.recipe = recipe;
        Application a = getCurrentApplication();
        recipes.updateApplicationByRecipe(recipe, a);
        updateRecipe(a);        
    }
    
    /**
    * Loads sequencing indexes from database.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    @SuppressWarnings("unchecked")
    private static void loadIndexes() {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        Transaction tx = null;
        indexesObjectsList = new ArrayList<SequencingIndex>();
        try {
            tx = session.beginTransaction();
            indexesObjectsList = session.createQuery("from SequencingIndex").list();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        possibleIndexes = new ArrayList<SampleSpecDesc>();
        for (SequencingIndex s : indexesObjectsList) {
            //if (!s.getIndex().equals("none")) {
                possibleIndexes.add(new SampleSpecDesc(s.getIndex(), "none", "none"));
            //}
        }
    }
    
    /**
    * Finds the id for a given index sequence.
    *
    * @author Francesco Venco
    * @param index - a sequencing index
    * @return int - the id of the index
    * @since 1.0
    */
    protected int findIndexID(String index) {
        for (SequencingIndex i : indexesObjectsList) {
            if (i.getIndex().equals(index)) {
                return i.getId();
            }
        }
        return -1;
    }
    
    /**
    * Loads data of a sample.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean load() {
        return this.load(false);
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
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            loadedSample = (Sample) session.load(Sample.class, sampleID);
            // check permission for loading
            System.out.println("Test for permission");
            if (!hasLoadPermission(loadedSample, loggeduser)) {
                fail = bundle.getObject("samples.permission.denied").toString();
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
    
    // load the data of an existing sample TODO to be moved ?
    /**
    * Loads data of a sample.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String loadid() {
        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        sampleID = Integer.parseInt(sid);
        //System.out.println("loading sample " + sampleID);
         return loadid(sampleID);

        /*
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            loadedSample = (Sample) session.load(Sample.class, sampleID);
            setCurrentNgssampleData(loadedSample);
            // check permission for loading
            //System.out.println("Test for permission");
            //if (hasLoadPermission(loadedSample, loggeduser)) {
            //    setCurrentNgssampleData(loadedSample);
                // lazy init for projects
            //    for (Project p : loadedSample.getProjects()) {
            //        p.getId();
            //    }
            //}
            
            
            
            // init sample description ( used when loading information
            // for a new sample
            selectedIndexes = new ArrayList<SampleSpecDesc>();
            SampleSpecDesc ssd = new SampleSpecDesc(sequencingindex, this.comments, this.sampleDescription);
            selectedIndexes.add(ssd);
            tx.commit();
            System.out.println("Messaging");
           
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
            

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Rollback!! ");
            try {
                tx.rollback();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
            
        } finally {
            session.close();
        }
        return "sampleDetails?faces-redirect=true";
        */
    }
    
    // load the data of an existing sample TODO to be moved ?
    /**
    * Loads data of a sample.
    *
    * @author Francesco Venco
    * @param sampleid
    * @return String
    * @since 1.0
    */
    public String loadid(int sampleid) {
        //FacesContext context = FacesContext.getCurrentInstance();
        //String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        sampleID = sampleid;
        //System.out.println("loading sample " + sampleID);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            loadedSample = (Sample) session.load(Sample.class, sampleID);
            setCurrentNgssampleData(loadedSample);
            // check permission for loading
            //System.out.println("Test for permission");
            //if (hasLoadPermission(loadedSample, loggeduser)) {
            //    setCurrentNgssampleData(loadedSample);
                // lazy init for projects
            //    for (Project p : loadedSample.getProjects()) {
            //        p.getId();
            //    }
            //}
            
            
            
            // init sample description ( used when loading information
            // for a new sample
            selectedIndexes = new ArrayList<SampleSpecDesc>();
            SampleSpecDesc ssd = new SampleSpecDesc(sequencingindex, this.comments, this.sampleDescription);
            selectedIndexes.add(ssd);
            tx.commit();
            System.out.println("Messaging");
           
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
            

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Rollback!! ");
            try {
                tx.rollback();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }
            
        } finally {
            session.close();
        }
        return "/Sample/sampleDetails?faces-redirect=true";
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
            //processed = ngssample.getStatus();
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
    * Tests if application is new.
    *
    * @author Francesco Venco
    * @param a
    * @return boolean
    * @since 1.0
    */
    protected boolean isNewApplication(Application a) {
        boolean result = true;
        for (int i = 0; i < applications.size(); i++) {

            if (NgsLimsUtility.dump(applications.get(i)).equals(
                    NgsLimsUtility.dump(a))) {

                result = false;
                break;
            }
        }
        return result;
    }

    /**
    * Finds id for a given application.
    *
    * @author Francesco Venco
    * @param a
    * @return Integer
    * @since 1.0
    */
    protected Integer findApplicationID(Application a) {
        Integer result = new Integer(-1);
        for (int i = 0; i < applications.size(); i++) {
            if (NgsLimsUtility.dump(applications.get(i)).equals(
                    NgsLimsUtility.dump(a))) {
                return applications.get(i).getApplicationId();
            }
        }
        return result;
    }
    
    /**
    * Finds application by application name.
    *
    * @author Francesco Venco
    * @param applicationName
    * @return Application
    * @since 1.0
    */
    protected Application findApplicationByApplicationName(String applicationName) {
        Application result = null;
        for (int i = 0; i < applications.size(); i++) {
            if(applications.get(i).getApplicationname().equals(applicationName)){
                result = applications.get(i);
                break;
            }
        }
        return result;
    }

    /**
    * Form validator.
    * The method returns true only if each part of the validation form is
    * correct. It produces also all the warnings and errors of the case.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    protected boolean validateMultipleForms() {
        boolean formIsValid = true;

        // cost center
        if (costCenter == null || costCenter.equals("")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "costCenter",
                    bundle.getString("sample.error.cost.center.1"),
                    bundle.getString("sample.error.cost.center.2"));
        }
        // application
        if (applicationName.equals("undefined")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "application",
                    bundle.getString("sample.error.application.1"),
                    bundle.getString("sample.error.application.2"));
        }

        if (false == librarySynthesis.booleanValue()) {
            // biodate
            //if (biodate == null || biodate.equals("")) {
            //    formIsValid = false;
             //   NgsLimsUtility.setFailMessage(formId, "biodate",
            //            bundle.getString("sample.error.biodate.1"),
            //            bundle.getString("sample.error.biodate.2"));
            //} else {
            //    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            //    try {
            //        df.parse(biodate);
            //    } catch (ParseException ex) {
            //        formIsValid = false;
            //        NgsLimsUtility.setFailMessage(formId, "biodate",
            //                bundle.getString("sample.error.biodate.1"),
            //                bundle.getString("sample.error.biodate.3"));
            //    }
            //}
            // biomolarity
            if (biomolarity == null || biomolarity.doubleValue() < 0) {
                formIsValid = false;
                NgsLimsUtility.setFailMessage(formId, "biomolarity",
                        bundle.getString("sample.error.biomolarity.1"),
                        bundle.getString("sample.error.biomolarity.2"));
            }
            // sequencing index
            //if (selectedIndexes.isEmpty()|| selectedIndexes.get(0).getIndex().equals("none")) {
            if (selectedIndexes.isEmpty()) {                
                NgsLimsUtility.setFailMessage(formId, "sequencingindex",
                        bundle.getString("sample.error.sequencingindex.1"),
                        bundle.getString("sample.error.sequencingindex.2"));
                formIsValid = false;
            }

        } else {
            try {
                // biodate
                if (biodate != null && !biodate.equals("")) {
                    // NgsLimsUtility.setWarningMessage(formId, "biodate",
                    // "Warning: bionalyzer date",
                    // "date should not be defined - resetted");
                    biodate = null;
                }
            } catch (Exception e) {
            }
            // biomolarity
            try {
                if (biomolarity != null) {
                    // NgsLimsUtility.setWarningMessage(formId, "biomolarity",
                    // "Warning: bionalyzer biomolarity",
                    // "Biomolarity should not be defined - resetted");
                    biomolarity = null;
                }
            } catch (Exception e) {
            }

        }
        // sample name
        if (sampleName == null || sampleName.equals("")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "sampleName",
                    bundle.getString("sample.error.sampleName.1"),
                    bundle.getString("sample.error.sampleName.2"));
        }
        // sample concentration
        if (sampleConcentration == null
                || sampleConcentration.doubleValue() < 0) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "sampleConcentration",
                    bundle.getString("sample.error.sampleConcentration.1"),
                    bundle.getString("sample.error.sampleConcentration.2"));
        }
        // organism
        if (organism.equals("undefined")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "organism",
                    bundle.getString("sample.error.organism.1"),
                    bundle.getString("sample.error.organism.2"));
        }
        // sample type
        if (sampleType.equals("undefined")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "sampleType",
                    bundle.getString("sample.error.sampleType.1"),
                    bundle.getString("sample.error.sampleType.2"));
        }
        // total amount
        if (totalAmount == null || totalAmount.doubleValue() < 0) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "totalAmount",
                    bundle.getString("sample.error.totalAmount.1"),
                    bundle.getString("sample.error.totalAmount.2"));
        }
        // bulk fragment size
        if (bulkFragmentSize == null || bulkFragmentSize.doubleValue() < 0) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "bulkFragmentSize",
                    bundle.getString("sample.error.totalAmount.1"),
                    bundle.getString("sample.error.bulkFragmentSize.2"));
        }
        // antibody - necessary only for ChIP-Seq, reset otherwise
        if (applicationName.equals("ChIP-Seq")
                && (antibody == null || antibody.equals(""))) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "antibody",
                    bundle.getString("sample.error.antibody.1"),
                    bundle.getString("sample.error.antibody.2"));
        } else if (!applicationName.equals("ChIP-Seq") && antibody != null
                && !antibody.equals("")) {
            NgsLimsUtility.setWarningMessage(formId, "antibody",
                    bundle.getString("sample.warning.antibody.1"),
                    bundle.getString("sample.warning.antibody.2") + " "
                    + applicationName);
            antibody = "";
        }

        for (SampleSpecDesc ssd : selectedIndexes) {
            // sample description
            // TODO: controllare se è una espressione regolare. Da fare sul
            // faces?
            if (ssd.getDescription() == null || ssd.getDescription().equals("")) {
                NgsLimsUtility
                        .setWarningMessage(
                                formId,
                                "sampleDescription",
                                bundle.getString("sample.warning.sampleDescription.1"),
                                bundle.getString("sample.warning.sampleDescription.2"));
            } else {
                //check regular expression: Description must not contain spaces or "strange" characters
                String pattern = "[a-zA-Z_0-9£$&%?'.]*";
                if (!ssd.getDescription().matches(pattern)) {
                    formIsValid = false;
                    NgsLimsUtility.setFailMessage(formId, "sampleDescription",
                            "Error: description", "Description must not contain accents, spaces or strange characters");
                }
            }

            // comment
            if (ssd.getComment() == null || ssd.getComment().equals("")) {
                NgsLimsUtility.setWarningMessage(formId, "comments",
                        bundle.getString("sample.warning.comments.1"),
                        bundle.getString("sample.warning.comments.2"));
            }
        }
        // status
		/*
         * if(status == null || !status.equals("queued")){ formIsValid = false;
         * Utility.setFailMessage(formId, "status", "Error: status",
         * "Status in not queued"); }
         */
        // TODO others checks

        return formIsValid;
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
    * Form validator.
    *
    * @author Francesco Venco
    * @param isNew
    * @return boolean
    * @since 1.0
    */
    protected boolean validateForm(boolean isNew) {

        boolean formIsValid = true;

        // cost center
        if (costCenter == null || costCenter.equals("")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "costCenter",
                    "Error: Cost Center", "Missin cost center");
        }
        // application
        if (applicationName.equals("undefined")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "application",
                    "Error: Application Name", "Application is undefined");
        }

        if (false == librarySynthesis.booleanValue()) {
            // biodate
            //if (biodate == null || biodate.equals("")) {
            //    formIsValid = false;
            //    NgsLimsUtility.setFailMessage(formId, "biodate",
            //            "Error: bionalyzer date", "Date is missing");
            //} else {
            //    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            //    try {
            //        df.parse(biodate);
            //    } catch (ParseException ex) {
            //        formIsValid = false;
            //        NgsLimsUtility.setFailMessage(formId, "biodate",
            //                "Error: bionalyzer date",
            //                "Date format not correct ( dd-mm-year)");
            //    }
            //}
            // biomolarity
            if (biomolarity == null || biomolarity.doubleValue() < 0) {
                formIsValid = false;
                NgsLimsUtility.setFailMessage(formId, "biomolarity",
                        "Error: bionalyzer biomolarity",
                        "Biomolarity not correct");
            }
            // sequencing index
            if (isNew && selectedIndexes.isEmpty()) {
                NgsLimsUtility.setFailMessage(formId, "sequencingindex",
                        "Error: sequencing index",
                        "Sequencing Index not defined");
                formIsValid = false;
            }

        } else {
            try {
                // biodate
                if (biodate != null && !biodate.equals("")) {
                    // NgsLimsUtility.setWarningMessage(formId, "biodate",
                    // "Warning: bionalyzer date",
                    // "date should not be defined - resetted");
                    biodate = null;
                }
            } catch (Exception e) {
            }
            // biomolarity
            try {
                if (biomolarity != null) {
                    // NgsLimsUtility.setWarningMessage(formId, "biomolarity",
                    // "Warning: bionalyzer biomolarity",
                    // "Biomolarity should not be defined - resetted");
                    biomolarity = null;
                }
            } catch (Exception e) {
            }
            // sequencing Index
            try {
                /*
                 * if(!sequencingindex.equals("none")){
                 * //NgsLimsUtility.setWarningMessage(formId, "sequencingindex",
                 * "Warning: sequencing index",
                 * "Sequencing Index should not be defined - resetted");
                 * sequencingindex = "none"; }
                 */
                if (!selectedIndexes.isEmpty()) {
                    selectedIndexes = new ArrayList<SampleSpecDesc>();
                }
            } catch (Exception e) {
            }

        }
        // sample name
        if (sampleName == null || sampleName.equals("")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "sampleName",
                    "Error: Sample Name", "Missing sample name");
        }
        // sample concentration
        if (sampleConcentration == null
                || sampleConcentration.doubleValue() < 0) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "sampleConcentration",
                    "Error: Sample Concentration", "Must be positive");
        }
        // organism
        if (organism.equals("undefined")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "organism",
                    "Error: Organism", "Organism is undefined");
        }
        // sample type
        if (sampleType.equals("undefined")) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "sampleType",
                    "Error: Sample Type", "Type is undefined");
        }
        // total amount
        if (totalAmount == null || totalAmount.doubleValue() < 0) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "totalAmount",
                    "Error: Total Amount", "Must be positive");
        }
        // bulk fragment size
        if (bulkFragmentSize == null || bulkFragmentSize.doubleValue() < 0) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "bulkFragmentSize",
                    "Error: bulk fragment size", "Must be positive");
        }
        // antibody - necessary only for ChIP-Seq, reset otherwise
        if (applicationName.equals("ChIP-Seq")
                && (antibody == null || antibody.equals(""))) {
            formIsValid = false;
            NgsLimsUtility.setFailMessage(formId, "antibody",
                    "Error: Antibody", "Antibody is undefined");
        } else if (!applicationName.equals("ChIP-Seq") && antibody != null
                && !antibody.equals("")) {
            NgsLimsUtility.setWarningMessage(formId, "antibody",
                    "Warning: Antibody", "Antibody should be undefined for "
                    + applicationName);
            antibody = "";
        }
        // sample description		
        if (sampleDescription == null || sampleDescription.equals("")) {
            NgsLimsUtility.setWarningMessage(formId, "sampleDescription",
                    "Warning: description", "There is no description");
        } else {
            //check regular expression: Description must not contain spaces or "strange" characters
            String pattern = "[a-zA-Z_0-9£$&%?'.]*";
            if (!sampleDescription.matches(pattern)) {
                formIsValid = false;
                NgsLimsUtility.setFailMessage(formId, "sampleDescription",
                        "Error: description", "Description must not contain accents, spaces or strange characters");
            }
        }
        // comment
        if (comments == null || comments.equals("")) {
            NgsLimsUtility.setWarningMessage(formId, "comments",
                    "Warning: comments", "There are no comments");
        }
        // status
		/*
         * if(status == null || !status.equals("queued")){ formIsValid = false;
         * Utility.setFailMessage(formId, "status", "Error: status",
         * "Status in not queued"); }
         */
        // TODO others checks

        return formIsValid;
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
    * Setter for sample data.
    * 
    * @author Francesco Venco
    * @param a
    * @param ssd
    * @since 1.0
    */
    protected void setSampleData(Sample a, SampleSpecDesc ssd) {
        a.setUser(user);
        requestDate = new Date(System.currentTimeMillis());
        a.setRequestDate(requestDate);
        a.setStatus(Sample.status_requested);
        updateSampleData(a, ssd);

    }

    /**
    * Updates sample data.
    * 
    * @author Francesco Venco
    * @param a
    * @param ssd
    * @since 1.0
    */
    protected void updateSampleData(Sample a, SampleSpecDesc ssd) {

        a.setAntibody(antibody);
        a.setBulkFragmentSize(bulkFragmentSize);
        a.setComment(ssd.getComment());
        a.setConcentration(sampleConcentration);
        a.setCostCenter(costCenter);
        a.setDescription(SampleNameFilter.legalize(ssd.getDescription()));
        a.setLibrarySynthesisNeeded(librarySynthesis);
        a.setOrganism(organism);
        a.setName(SampleNameFilter.legalize(sampleName));
        a.setType(sampleType);
        a.setExperimentName(applicationName);
        a.setTotalAmount(totalAmount);
        // sequencing index is more complex and must be done in a
        // separate part?
        a.setSequencingIndexes(new SequencingIndex(ssd.getIndex()));
        /*
        Date d = null;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        if (biodate != null) {
            try {
                d = df.parse(biodate);
            } catch (ParseException ex) {
                Logger.getLogger(SampleFormBean.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        */
        //a.setBioanalyzerDate(d);
        a.setBioanalyzerDate(biodate);
        a.setBionalyzerBiomolarity(biomolarity);

    }

    /**
    * Getter for possible depths.
    * 
    * @author Francesco Venco
    * @return String[] - supported sequencing depths
    * @since 1.0
    */
    public String[] getPossibleDepths() {
        System.out.println("Print udpdate for depth with read mode" + readMode);
        if (readMode == null || readMode.equals("SR")) {
            return Preferences.getDepth_SR();
        } else {
            return Preferences.getDepth_PE();
        }
    }

    /**
    * Getter for bulkFragmentSize.
    * 
    * @author Francesco Venco
    * @return Double
    * @since 1.0
    */
    public Double getBulkFragmentSize() {
        return bulkFragmentSize;
    }

    /**
    * Getter for comments.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getComments() {
        return comments;
    }

    /**
    * Getter for costCenter.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getCostCenter() {
        return costCenter;
    }

    /**
    * Getter for fragmentationImage.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    //public String getFragmentationImage() {
    //    return fragmentationImage;
    //}

    /**
    * Getter for institute.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getInstitute() {
        return institute;
    }

    /**
    * Getter for instrument.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getInstrument() {
        return instrument;
    }

    /**
    * Getter for librarySynthesis.
    * 
    * @author Francesco Venco
    * @return Boolean
    * @since 1.0
    */
    public Boolean getLibrarySynthesis() {
        return librarySynthesis;
    }

    /**
    * Getter for organism.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getOrganism() {
        return organism;
    }

    /**
    * Getter for pi.
    * 
    * @author Francesco Venco
    * @return Integer
    * @since 1.0
    */
    public Integer getPi() {
        return pi;
    }

    /**
    * Getter for picoGreenImage.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    //public String getPicoGreenImage() {
    //    return picoGreenImage;
    //}

    /**
    * Getter for picoGreenQuantification.
    * 
    * @author Francesco Venco
    * @return Double
    * @since 1.0
    */
    //public Double getPicoGreenQuantification() {
    //    return picoGreenQuantification;
    //}

    /**
    * Getter for readLength.
    * 
    * @author Francesco Venco
    * @return Integer
    * @since 1.0
    */
    public Integer getReadLength() {
        return readLength;
    }

    /**
    * Getter for readMode.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getReadMode() {
        return readMode;
    }

    /**
    * Getter for requestDate.
    * 
    * @author Francesco Venco
    * @return Date
    * @since 1.0
    */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
    * Getter for sampleConcentration.
    * 
    * @author Francesco Venco
    * @return Double
    * @since 1.0
    */
    public Double getSampleConcentration() {
        return sampleConcentration;
    }

    /**
    * Getter for sampleDescription.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSampleDescription() {
        return sampleDescription;
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
    * Getter for sampleType.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSampleType() {
        return sampleType;
    }

    /**
    * Getter for totalAmount.
    * 
    * @author Francesco Venco
    * @return Double
    * @since 1.0
    */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
    * Getter for userName.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getUserName() {
        return userName;
    }

    /**
    * Setter for bulkFragmentSize.
    * 
    * @author Francesco Venco
    * @param bulkFragmentSize
    * @since 1.0
    */
    public void setBulkFragmentSize(Double bulkFragmentSize) {
        this.bulkFragmentSize = bulkFragmentSize;
    }

    /**
    * Setter for comments.
    * 
    * @author Francesco Venco
    * @param comments
    * @since 1.0
    */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
    * Setter for costCenter.
    * 
    * @author Francesco Venco
    * @param costCenter
    * @since 1.0
    */
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    /**
    * Setter for fragmentationImage.
    * 
    * @author Francesco Venco
    * @param fragmentationImage
    * @since 1.0
    */
    //public void setFragmentationImage(String fragmentationImage) {
    //    this.fragmentationImage = fragmentationImage;
    //}

    /**
    * Setter for institute.
    * 
    * @author Francesco Venco
    * @param institute
    * @since 1.0
    */
    public void setInstitute(String institute) {
        this.institute = institute;
    }

    /**
    * Setter for instrument.
    * 
    * @author Francesco Venco
    * @param instrument
    * @since 1.0
    */
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    /**
    * Setter for librarySynthesis.
    * 
    * @author Francesco Venco
    * @param librarySynthesis
    * @since 1.0
    */
    public void setLibrarySynthesis(Boolean librarySynthesis) {
        this.librarySynthesis = librarySynthesis;

        if (this.librarySynthesis.booleanValue()) {
            this.selectedIndexes = new ArrayList<SampleSpecDesc>();
            this.selectedIndexes.add(new SampleSpecDesc("none", "none", "none"));
        }
        //System.out.println("set lib syntehisis to " + this.librarySynthesis);
    }

    /**
    * Setter for organism.
    * 
    * @author Francesco Venco
    * @param organism
    * @since 1.0
    */
    public void setOrganism(String organism) {
        this.organism = organism;
    }

    /**
    * Setter for pi.
    * 
    * @author Francesco Venco
    * @param pi
    * @since 1.0
    */
    public void setPi(Integer pi) {
        this.pi = pi;
    }

    /**
    * Setter for picoGreenImage.
    * 
    * @author Francesco Venco
    * @param picoGreenImage
    * @since 1.0
    */
    //public void setPicoGreenImage(String picoGreenImage) {
    //    this.picoGreenImage = picoGreenImage;
    //}

    /**
    * Setter for picoGreenQuantification.
    * 
    * @author Francesco Venco
    * @param picoGreenQuantification
    * @since 1.0
    */
    //public void setPicoGreenQuantification(Double picoGreenQuantification) {
    //    this.picoGreenQuantification = picoGreenQuantification;
    //}

    /**
    * Setter for readLength.
    * 
    * @author Francesco Venco
    * @param readLength
    * @since 1.0
    */
    public void setReadLength(Integer readLength) {
        this.readLength = readLength;
    }

    /**
    * Setter for readMode.
    * 
    * @author Francesco Venco
    * @param readMode
    * @since 1.0
    */
    public void setReadMode(String readMode) {
        //System.out.println("Read mode setted");
        this.readMode = readMode;
    }

    /**
    * Setter for requestDate.
    * 
    * @author Francesco Venco
    * @param requestDate
    * @since 1.0
    */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
    * Setter for sampleConcentration.
    * 
    * @author Francesco Venco
    * @param sampleConcentration
    * @since 1.0
    */
    public void setSampleConcentration(Double sampleConcentration) {
        this.sampleConcentration = sampleConcentration;
    }

    /**
    * Setter for sampleDescription.
    * 
    * @author Francesco Venco
    * @param sampleDescription
    * @since 1.0
    */
    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    /**
    * Setter for sampleID.
    * 
    * @author Francesco Venco
    * @param sampleID
    * @since 1.0
    */
    public void setSampleID(Integer sampleID) {
        this.sampleID = sampleID;
    }

    /**
    * Setter for sampleName.
    * 
    * @author Francesco Venco
    * @param sampleName
    * @since 1.0
    */
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    /**
    * Setter for sampleType.
    * 
    * @author Francesco Venco
    * @param sampleType
    * @since 1.0
    */
    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    /**
    * Setter for totalAmount.
    * 
    * @author Francesco Venco
    * @param totalAmount
    * @since 1.0
    */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
    * Setter for userName.
    * 
    * @author Francesco Venco
    * @param userName
    * @since 1.0
    */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
    * Getter for userEmail.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getUserEmail() {
        return userEmail;
    }

    /**
    * Getter for userLogin.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getUserLogin() {
        return userLogin;
    }

    /**
    * Getter for userTel.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getUserTel() {
        return userTel;
    }

    /**
    * Setter for userEmail.
    * 
    * @author Francesco Venco
    * @param userEmail
    * @since 1.0
    */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
    * Setter for userLogin.
    * 
    * @author Francesco Venco
    * @param userLogin
    * @since 1.0
    */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
    * Setter for userTel.
    * 
    * @author Francesco Venco
    * @param userTel
    * @since 1.0
    */
    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    /**
    * Setter for fragmentationImageFileName.
    * 
    * @author Francesco Venco
    * @param fragmentationImageFileName
    * @since 1.0
    */
    //public void setFragmentationImageFileName(String fragmentationImageFileName) {
    //    this.fragmentationImageFileName = fragmentationImageFileName;
    //}

    /**
    * Setter for picoGreenImageFileName.
    * 
    * @author Francesco Venco
    * @param picoGreenImageFileName
    * @since 1.0
    */
    //public void setPicoGreenImageFileName(String picoGreenImageFileName) {
    //    this.picoGreenImageFileName = picoGreenImageFileName;
    //}

    /**
    * Getter for fragmentationImageFileName.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    //public String getFragmentationImageFileName() {
    //    return fragmentationImageFileName;
    //}

    /**
    * Getter for picoGreenImageFileName.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    //public String getPicoGreenImageFileName() {
    //    return picoGreenImageFileName;
    //}

    /**
    * Getter for antibody.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getAntibody() {
        return antibody;
    }

    /**
    * Setter for antibody.
    * 
    * @author Francesco Venco
    * @param antibody
    * @since 1.0
    */
    public void setAntibody(String antibody) {
        this.antibody = antibody;
    }

    /**
    * Getter for depth.
    * 
    * @author Francesco Venco
    * @return Integer
    * @since 1.0
    */
    public Integer getDepth() {
        return depth;
    }

    /**
    * Setter for depth.
    * 
    * @author Francesco Venco
    * @param depth
    * @since 1.0
    */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    /**
    * Setter for processed.
    * 
    * @author Francesco Venco
    * @param processed
    * @since 1.0
    */
    //public void setProcessed(String processed) {
    //    this.processed = processed;
    //}

    /**
    * Getter for processed.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    //public String getProcessed() {
    //    return processed;
    //}

    /**
    * Getter for applications.
    * 
    * @author Francesco Venco
    * @return List<Application>
    * @since 1.0
    */
    public List<Application> getApplications() {
        return applications;
    }

    /**
    * Setter for applications.
    * 
    * @author Francesco Venco
    * @param applications
    * @since 1.0
    */
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    /**
    * Setter for piLogin.
    * 
    * @author Francesco Venco
    * @param piLogin
    * @since 1.0
    */
    public void setPiLogin(String piLogin) {
        this.piLogin = piLogin;
    }

    /**
    * Getter for piLogin.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getPiLogin() {
        return this.piLogin;
    }
    
    /**
    * Getter for piName.
    * 
    * @author Heiko Muller
    * @return String
    * @since 1.0
    */
    public String getPiName() {
        return piName;
    }

    /**
    * Setter for status.
    * 
    * @author Francesco Venco
    * @param status - the sample state
    * @since 1.0
    */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
    * Getter for status.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getStatus() {
        return this.status;
    }

    /**
    * Setter for biodate. The date when Bioanalyzer analysis of the sample was performed.
    * 
    * @author Francesco Venco
    * @param biodate
    * @since 1.0
    */
    public void setBiodate(Date biodate) {
        this.biodate = biodate;
    }

    /**
    * Getter for biodate. The date when Bioanalyzer analysis of the sample was performed.
    * 
    * @author Francesco Venco
    * @return Date
    * @since 1.0
    */
    public Date getBiodate() {
        return this.biodate;
    }

    /**
    * Setter for biomolarity. The sample molarity.
    * 
    * @author Francesco Venco
    * @param biomolarity
    * @since 1.0
    */
    public void setBiomolarity(Double biomolarity) {
        this.biomolarity = biomolarity;
    }

    /**
    * Getter for biomolarity.
    * 
    * @author Francesco Venco
    * @return Double
    * @since 1.0
    */
    public Double getBiomolarity() {
        return this.biomolarity;
    }

    /**
    * Setter for sequencingindex.
    * 
    * @author Francesco Venco
    * @param sequencingindex
    * @since 1.0
    */
    public void setSequencingindex(String sequencingindex) {
        this.sequencingindex = sequencingindex;
    }

    /**
    * Getter for sequencingindex.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSequencingindex() {
        return this.sequencingindex;
    }

    /**
    * Setter for experiment.
    * 
    * @author Francesco Venco
    * @param experiment
    * @since 1.0
    */
    public void setExperiment(String experiment) {
        this.experiment = experiment;
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

    /**
    * Setter for timestep.
    * 
    * @author Francesco Venco
    * @param timestep
    * @since 1.0
    */
    //public void setTimestep(String timestep) {
    //    this.timestep = timestep;
    //}

    /**
    * Getter for timestep.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    //public String getTimestep() {
    //    return this.timestep;
    //}

    /**
    * Getter for possibleIndexes.
    * 
    * @author Francesco Venco
    * @return List<SampleSpecDesc>
    * @since 1.0
    */
    public List<SampleSpecDesc> getPossibleIndexes() {
        return this.possibleIndexes;
    }

    /**
    * Getter for selectedIndexes.
    * 
    * @author Francesco Venco
    * @return List<SampleSpecDesc>
    * @since 1.0
    */
    public List<SampleSpecDesc> getSelectedIndexes() {
        System.out.println("get " + selectedIndexes
                + selectedIndexes.getClass());
        if (!selectedIndexes.isEmpty()) {
            System.out.println(selectedIndexes.get(0).getClass());
        }
        return this.selectedIndexes;
    }

    /**
    * Setter for selectedIndexes.
    * 
    * @author Francesco Venco
    * @param selectedIndexes
    * @since 1.0
    */
    public void setSelectedIndexes(List<String> selectedIndexes) {
        this.selectedIndexes = new ArrayList<SampleSpecDesc>();
        for (String i : selectedIndexes) {
            this.selectedIndexes.add(new SampleSpecDesc(i, "none", "none"));
        }
        if (this.selectedIndexes.isEmpty()) {
            this.selectedIndexes.add(new SampleSpecDesc("none", "none", "none"));
        }
    }
    
    /**
    * Getter for applicationName.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getApplicationName() {
        if (applicationName == null) {
            applicationName = "other";
        }
        return applicationName;
    }

    /**
    * Setter for applicationName.
    * 
    * @author Francesco Venco
    * @param applicationName
    * @since 1.0
    */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        this.experiment  = applicationName;
        Application a = getCurrentApplication();
        String defaultRecipe = recipes.findDefaultRecipe(a);
        setRecipe(defaultRecipe);
    }

    
    
    /**
    * Updates recipe.
    * 
    * @author Francesco Venco
    * @param a
    * @since 1.0
    */
    protected void updateRecipe(Application a){
        readLength = a.getReadlength();
        readMode = a.getReadmode();
        depth = a.getDepth();
    }
    
    /**
    * Getter for recipes.
    * 
    * @author Francesco Venco
    * @return List<String>
    * @since 1.0
    */
    public List<String> getRecipeNames() {
        return recipes.getRecipes();
    }
    
    /**
    * Tests if logged user has add permission.
    * 
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean getUserHasAddPermission() {
        if (user.getUserRole().equals(Preferences.ROLE_TECHNICIAN)) {
            return false;
        }
        if (user.getUserRole().equals(Preferences.ROLE_GUEST)) {
            return false;
        }
        return true;
    }
    
    /**
    * Appends sample id to sample name.
    * 
    * @author Heiko Muller
    * @param s - a sample
    * @return Sample
    * @since 1.0
    */
    protected Sample addSampleIdToSampleName(Sample s){
        String st = s.getName();
        st = SampleNameFilter.legalize(st);
        st = st + "_S" + s.getId();
        s.setName(st);
        return s;
    }
    
    
    
    /*
    private void updateLims1Entities(Sample s, Chipseq cs, Reagentuse ru){
        cs.setCostCenter(s.getCostCenter());
        cs.setApplication(s.getApplication().getApplicationname());
        cs.setReadMode(s.getApplication().getReadmode());
        cs.setReadLength(s.getApplication().getReadlength());
        cs.setDepth(s.getApplication().getDepth().toString());
        cs.setLibrarySynthesis(s.getLibrarySynthesisNeeded());
        cs.setBarcode(s.getSequencingIndexes().getIndex());
        cs.setBioanalyzerDate(s.getBioanalyzerDate());
        cs.setBioanalyzerNanomolarity(s.getBionalyzerBiomolarity());
        cs.setSampleName(s.getName());
        cs.setSampleType(s.getType());
        cs.setOrganism(s.getOrganism());
        cs.setSampleConcentration(s.getConcentration());
        cs.setTotalAmount(s.getTotalAmount());
        cs.setBulkFragmentSize(s.getBulkFragmentSize().intValue());
        cs.setAntibody(s.getAntibody());
        cs.setSampleDescription(s.getDescription());
        cs.setComments(s.getComment());
        
        ru.setSeqIndex(s.getSequencingIndexes().getIndex());
        ru.setSampleName(s.getName());       
    
    }
    */

    
    
    /*
    //delete in LIMS1
    public String delete(int id){
        StringBuilder sb = new StringBuilder();
        
            //findSampleID();
            
            Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();
                //Session session = entity.HibernateUtil.getSessionFactory().getCurrentSession();                
                Transaction tx = null;                
                try {
                    
                    tx = session.beginTransaction();
                    Chipseq cs = (Chipseq)session.load(Chipseq.class, id);
                    Reagentuse r = (Reagentuse)session.load(Reagentuse.class, id);
                    session.delete(cs);
                    session.delete(r);
                    //session.save(cs);
                    //session.save(r);
                    tx.commit();  
                    System.out.println("Deletion successful.");
                    sb.append("Deletion successful.");
                }
                catch (RuntimeException e) {
                    System.out.println("Deletion failed for sample " + id);
                    sb.append("Deletion failed for sample " + id);
                    tx.rollback();
                    //throw e; // or display error message
                }
                finally {
                    if(session.isOpen()){
                        session.close();   
                    }
                }
                return sb.toString();
        } 
    */
    
    
    
    /**
    * Test NgsLimsUtility.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public String test() {
        System.out.println("sampleFormBean.test() called");
        //NgsLimsUtility.setSuccessMessage("SampledetailsForm", "SampleDeletionButton", "Test ", "Test");
        return "sampleDeleted?faces-redirect=true";
    }
    
    /**
    * Tests if sample status is "requested".
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean getIsRequested() {
        return status.equals(Sample.status_requested);
    }
    
    // TODO merge the code in new method, modify here and submit if possible
    /**
    * Persists sample modifications.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    
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
            dataBean.updateSample(s);

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
    
    /**
    * Deletes a sample.
    *
    * @author Francesco Venco
    * @return String - a redirect to sample deleted page
    * @since 1.0
     */
    public String delete() {
        System.out.println("deleting sample " + sampleID);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
       
        try {
            tx = session.beginTransaction();
            // load the sample in this session
            Sample s = (Sample) session.load(Sample.class, sampleID);
            if(s.getStatus().equals(Sample.status_requested)){
                session.delete(s);
                dataBean.removeSample(sampleID);
                // load the multiple request, if any, 
                // and delete all of them
                //Set<MultipleRequest> mrs = s.getMultipleRequests();
                //for (MultipleRequest mr : mrs) {
                //    session.delete((MultipleRequest) session.load(MultipleRequest.class, mr.getId()));
                //}
            }else{
                NgsLimsUtility.setFailMessage("SampledetailsForm", "SampleDeletionButton",
                    "Error deleting " + sampleID, ". Only samples with status requested can be deleted.");
                return "sampleDetails?faces-redirect=true";
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage("SampledetailsForm", "SampleModbutton",
                    "Error deleting " + sampleID, fail);
            return "sampleDetails?faces-redirect=true";
        } finally {
            session.close();
        }
        
        //if(sampleSearchBean == null){
        //    FacesContext context = FacesContext.getCurrentInstance();    
        //    sampleSearchBean = (SampleSearchBean) context.getApplication().evaluateExpressionGet(context, "#{sampleSearchBean}", SampleSearchBean.class);
        //}
        //sampleSearchBean.initSampleList();
        
        
        return "sampleDeleted?faces-redirect=true";

    }
   
}
