package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.MultipleRequest;
import it.iit.genomics.cru.smith.entity.MultipleRequestId;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)NewSampleFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for new sample form.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newSampleFormBean")
//@RequestScoped
//@SessionScoped
@SessionScoped
public class NewSampleFormBean implements Serializable {
    
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

    private int tab1Index;
    private int tab2Index;
    //SampleSearchBean sampleSearchBean;
    DataBean dataBean;
    RoleManager roleManager;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public NewSampleFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init NewSampleFormBean");
        }
        init();
    }

    /**
    * init.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    //@Override
    public void init() {
        //System.out.println("init NewSampleFormBean");
        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        formId = "RequestForm";

        // retrieve informations on the logged user
        FacesContext context = FacesContext.getCurrentInstance();
        //sampleSearchBean = (SampleSearchBean) context.getApplication().evaluateExpressionGet(context, "#{sampleSearchBean}", SampleSearchBean.class);    
        //LoggedUser loggedUserBean = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class); 
        recipes = ((Recipes) context.getApplication().evaluateExpressionGet(context, "#{recipes}", Recipes.class));  
        //dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
        //LoggedUser loggedUserBean = new LoggedUser();
        dataBean = roleManager.getDataBean();

        userLogin = roleManager.getLoggedUser().getLogin();
        userName = roleManager.getLoggedUser().getUserName();
        userEmail = roleManager.getLoggedUser().getMailAddress();
        userTel = roleManager.getLoggedUser().getPhone();
        pi = roleManager.getLoggedUser().getPi();
        piLogin = (UserHelper.getPi(roleManager.getLoggedUser())).getUserName();
        user = roleManager.getLoggedUser();

        status = "queued";

        selectedIndexes = new ArrayList<SampleSpecDesc>();
        selectedIndexes.add(new SampleSpecDesc("none", "none", "none"));

        //load all the applications and indexes
        loadApplications();
        loadIndexes();      
        costCenter = UserHelper.getPi(UserHelper.getUserByLoginName(userLogin)).getUserName();
        
        biodate = Calendar.getInstance().getTime();

    }

    //persists a new request entry TODO to be moved?
    /**
    * Persists a new sample to the database.
    *
    * @author Francesco Venco
    * @return String - a redirect to the samplesCreated page
    * @since 1.0
    */
    public String submit() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String fail = "";
        String newSamplesIds = "";
        try {
            tx = session.beginTransaction();
            //Sample s = new Sample();
            ArrayList<Sample> newSamples = new ArrayList<Sample>();
            for (int i = 0; i < selectedIndexes.size(); i++) {
                newSamples.add(new Sample());
            }

            //validate the form
            if (!validateMultipleForms()) {
                RuntimeException e = new RuntimeException("Errors in the form");
                throw e;
            }

            //application management
            Application a = getCurrentApplication();
            if (isNewApplication(a)) {
                session.save(a);
            } else {
                //load the existing application
                a = (Application) session.load(Application.class, findApplicationID(a));
                session.saveOrUpdate(a);
            }
            //update sample(s) for indexes and application
            for (int i = 0; i < selectedIndexes.size(); i++) {
                Sample s = newSamples.get(i);
                SampleSpecDesc ssd = selectedIndexes.get(i);
                System.out.println("USING " + ssd);
                s.setApplication(a);
                setSampleData(s, ssd);
                if (selectedIndexes.size() > 1) {
                    s.setName(s.getName() + "_" + ssd.getIndex());
                }
                SequencingIndex si = (SequencingIndex) session.load(SequencingIndex.class, findIndexID(ssd.getIndex()));
                s.setSequencingIndexes(si); 
                s.setId(SampleHelper.getNextSampleId());
                session.save(s);
                newSamplesIds = newSamplesIds + " " + s.getId();
                s = addSampleIdToSampleName(s);
                session.saveOrUpdate(s);
                dataBean.addSample(s);
                
            }

            //if there is more than one selected index, a multiple request is needed to be
            //inserted in the database
            if (selectedIndexes.size() > 1) {
                int newRequestId = 1;
                List<MultipleRequest> totalRequests = session.createQuery("from MultipleRequest").list();
                //find the last used ID
                /*for(Multiplerequest mr : totalRequests){
                 if(mr.getId().getRequestId() >= newRequestId)
                 newRequestId = mr.getId().getRequestId() + 1;
                 }*/
                if (!totalRequests.isEmpty()) {
                    MultipleRequest last = totalRequests.get(totalRequests.size() - 1);
                    newRequestId = last.getId().getRequestId() + 1;
                }
                //make the new multiple requests 
                for (int i = 0; i < selectedIndexes.size(); i++) {
                    System.out.println("adding request " + newRequestId);
                    Sample s = newSamples.get(i);
                    int sampleid = s.getId();
                    MultipleRequest mr = new MultipleRequest();
                    MultipleRequestId mrId = new MultipleRequestId();
                    mrId.setRequestId(newRequestId);
                    mrId.setSampleId(sampleid);
                    mr.setId(mrId);
                    mr.setSample(s);
                    session.save(mr);
                }

            }

            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "NGSRequestssubmitbutton", "Sample request successful",
                    sampleName + " inserted correctly with id(s) " + newSamplesIds);
            NgsLimsUtility.setSuccessMessage("sampleTableForm", "NGSRequestssubmitbutton", "Sample request successful",
                    sampleName + " inserted correctly with id(s) " + newSamplesIds);

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            fail = fail + " " + e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "NGSRequestssubmitbutton", fail, sampleName + " not inserted");
            loadApplications();
            return null;
        } finally {
            session.close();
        }

        loadApplications();
        //sampleSearchBean.initSampleList();

        //return "samplesSearch?faces-redirect=true";
        //return "sampleDetails";
        //return "samplesCreated?faces-redirect=true?sids=" + newSamplesIds;
        return "samplesCreated?sids=" + newSamplesIds + " faces-redirect=true";

    }

    

    /**
    * Getter for tab1Index.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getTab1Index() {
        return tab1Index;
    }

    /**
    * Setter for tab1Index.
    *
    * @author Francesco Venco
    * @param tab1Index
    * @since 1.0
    */
    public void setTab1Index(int tab1Index) {
        this.tab1Index = tab1Index;
    }

    /**
    * Getter for tab2Index.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getTab2Index() {
        return tab2Index;
    }

    /**
    * Setter for tab2Index.
    *
    * @author Francesco Venco
    * @param tab2Index
    * @since 1.0
    */
    public void setTab2Index(int tab2Index) {
        this.tab2Index = tab2Index;
    }
    
     protected void loadApplications() {
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
        Application a = getCurrentApplication();
        String defaultRecipe = recipes.findDefaultRecipe(a);
        setRecipe(defaultRecipe);
    }

    /**
    * Loads sequencing indexes from database.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    @SuppressWarnings("unchecked")
    protected void loadIndexes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
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
    * Form validator.
    * The method returns true only if each part of the validation form is
    * correct. It produces also all the warnings and errors of the case.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    private boolean validateMultipleForms() {
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

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public Integer getSampleID() {
        return sampleID;
    }

    public void setSampleID(Integer sampleID) {
        this.sampleID = sampleID;
    }

    public Double getBulkFragmentSize() {
        return bulkFragmentSize;
    }

    public void setBulkFragmentSize(Double bulkFragmentSize) {
        this.bulkFragmentSize = bulkFragmentSize;
    }

    public String getFragmentationImage() {
        return fragmentationImage;
    }

    public void setFragmentationImage(String fragmentationImage) {
        this.fragmentationImage = fragmentationImage;
    }

    public Double getPicoGreenQuantification() {
        return picoGreenQuantification;
    }

    public void setPicoGreenQuantification(Double picoGreenQuantification) {
        this.picoGreenQuantification = picoGreenQuantification;
    }

    public String getPicoGreenImage() {
        return picoGreenImage;
    }

    public void setPicoGreenImage(String picoGreenImage) {
        this.picoGreenImage = picoGreenImage;
    }

    public Integer getReadLength() {
        return readLength;
    }

    public void setReadLength(Integer readLength) {
        this.readLength = readLength;
    }

    public String getReadMode() {
        return readMode;
    }

    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getFragmentationImageFileName() {
        return fragmentationImageFileName;
    }

    public void setFragmentationImageFileName(String fragmentationImageFileName) {
        this.fragmentationImageFileName = fragmentationImageFileName;
    }

    public String getPicoGreenImageFileName() {
        return picoGreenImageFileName;
    }

    public void setPicoGreenImageFileName(String picoGreenImageFileName) {
        this.picoGreenImageFileName = picoGreenImageFileName;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getAntibody() {
        return antibody;
    }

    public void setAntibody(String antibody) {
        this.antibody = antibody;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Application getSampleApplication() {
        return sampleApplication;
    }

    public void setSampleApplication(Application sampleApplication) {
        this.sampleApplication = sampleApplication;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getSampleConcentration() {
        return sampleConcentration;
    }

    public void setSampleConcentration(Double sampleConcentration) {
        this.sampleConcentration = sampleConcentration;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    public Boolean getLibrarySynthesis() {
        return librarySynthesis;
    }

    public void setLibrarySynthesis(Boolean librarySynthesis) {
        this.librarySynthesis = librarySynthesis;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSequencingindex() {
        return sequencingindex;
    }

    public void setSequencingindex(String sequencingindex) {
        this.sequencingindex = sequencingindex;
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

    public Date getBiodate() {
        return biodate;
    }

    public void setBiodate(Date biodate) {
        this.biodate = biodate;
    }

    public Double getBiomolarity() {
        return biomolarity;
    }

    public void setBiomolarity(Double biomolarity) {
        this.biomolarity = biomolarity;
    }

    public String getExperiment() {
        return experiment;
    }

    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    public String getTimestep() {
        return timestep;
    }

    public void setTimestep(String timestep) {
        this.timestep = timestep;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPiLogin() {
        return piLogin;
    }

    public void setPiLogin(String piLogin) {
        this.piLogin = piLogin;
    }

    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) {
        this.piName = piName;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipes getRecipes() {
        return recipes;
    }

    public void setRecipes(Recipes recipes) {
        this.recipes = recipes;
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
    
    
}
