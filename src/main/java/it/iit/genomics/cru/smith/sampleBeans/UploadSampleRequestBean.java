package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.*;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.mail.MailBean;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import it.iit.genomics.cru.smith.utils.DateParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * @(#)UploadSampleRequestBean.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Backing bean for uploading of sample requests.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "uploadSampleRequestBean")
@SessionScoped
public class UploadSampleRequestBean implements Serializable {
    
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

    private String destination;
    private String applicationPath;
    private UploadedFile file1;
    private String filename = "test";
    private String filecontent;
    private SampleSearchBean ssb;
    private MailBean mailBean;
    
    protected RoleManager roleManager;
    DataBean dataBean;
    int progress;
    
    //private RequestSubmissionBean cruUploadBean;

    /**
     * Bean constructor.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public UploadSampleRequestBean() {
        if(Preferences.getVerbose()){
            System.out.println("init uploadSampleRequestBean...");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class);          
        loggeduser = roleManager.getLoggedUser();
        dataBean = roleManager.getDataBean();
        //this.loggeduser = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)).getLoggedUser();
        ssb = ((SampleSearchBean) context.getApplication().evaluateExpressionGet(context, "#{sampleSearchBean}", SampleSearchBean.class));
        mailBean = ((MailBean) context.getApplication().evaluateExpressionGet(context, "#{mailBean}", MailBean.class));
        if(mailBean == null){
            System.out.println("mailBean not found");
        }
        //cruUploadBean = ((RequestSubmissionBean) context.getApplication().evaluateExpressionGet(context, "#{requestSubmissionBean}", RequestSubmissionBean.class));
        userLogin = this.loggeduser.getLogin();
        formId = "sampleTableUploadProcessForm";
        try {
            applicationPath = context.getExternalContext().getRealPath("/");
            //destination = context.getExternalContext().getInitParameter("uploadDirectory");
            //destination = context.getExternalContext().getInitParameter("uploadDirectory");
            destination = applicationPath + "upload" + File.separator;
            if(Preferences.getVerbose()){
                System.out.println("upload directory " + destination);
            }
            
        } catch (UnsupportedOperationException uoe) {
            System.out.println("upload directory exception " + destination);
            uoe.printStackTrace();
        }
        if(Preferences.getVerbose()){
            System.out.println("init uploadSampleRequestBean...done");
        }

    }

    /**
     * Action listener for FileUploadEvent.
     *
     * @author Heiko Muller
     * @param event - a file upload event
     * @since 1.0
     */
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("uploading");
        file1 = event.getFile();
        if (file1 != null) {
            FacesMessage msg = new FacesMessage("Successful", file1.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        System.out.println("getting data");
        transferFile(file1);
        //annotationname1 = file1.getFileName();
        filename = (new File(file1.getFileName())).getName();
        System.out.println(filename);
        System.out.println("upload completed");

    }

    /**
     * Submits uploaded sample requests.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void submitRequest() {
        String[][] data = parseRequestCSV();
        boolean singleuser = isSingleUserSubmission(data);
        boolean roletest = roleTest(data);
        boolean submitsuccess = true;
        StringBuilder sb = new StringBuilder();  
        User u = null;
        String previousLibrary = "";
        //List<User> userlist = UserHelper.getUsersList();
        progress = 0;
        if(roletest){
            if(singleuser){   
                u = UserHelper.getUserByLoginName(data[1][1]);
                int libid = SampleHelper.getNextLibraryId();
                for (int i = 0; i < data.length; i++) {
                    double d = (i + 1) * 1.0 / data.length;
                    progress = (int)d * 100;
                    if (!data[i][0].startsWith("UserName:")) {
                        //if (UserHelper.getUserExists(data[i][1])) {
                        //if ((u = userExists(userlist, data[i][1])) != null) {
                            if (u != null) {
                            //userExists
                            //u = UserHelper.getUserByLoginName(data[i][1]);
                            if (!u.getUserRole().equals(Preferences.ROLE_GUEST)) {
                                Sample s = setSample(data[i], u);
                                //Sample s = setSample2(data[i], u);//new form
                                
                                //handle library
                                if(data[i][21] != null && data[i][21].length() > 0 && !data[i][21].equals(previousLibrary)){
                                    previousLibrary = data[i][21];
                                    libid = SampleHelper.getNextLibraryId();
                                }else if(data[i][21] != null && data[i][21].length() == 0){
                                    //if no library is defined, set library name equal to sample name
                                    previousLibrary = data[i][5];
                                    data[i][21] = data[i][5];
                                    libid = SampleHelper.getNextLibraryId();
                                }
                                String lib = SampleNameFilter.legalizeLibrary(data[i][21]);
                                lib = lib + "_L" + libid;
                                //end handle library
                                
                                if (s != null) {
                                    //boolean result = submit(s);
                                    boolean result = submit(s, libid, lib);//legalizes library name
                                    //boolean result = true;
                                    if(result){
                                        sb.append("<tr><td>" + s.getId() + "</td><td>" + s.getName() + "</td></tr>");
                                        Sample sample = SampleHelper.getSampleByID(s.getId().intValue());
                                        dataBean.addSample(sample);
                                    }else{
                                        submitsuccess = false;
                                    }
                                }
                            } else {
                                NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Submission error", "Guest cannot submit samples");
                            }
                        } else {
                            NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "User error", "User not found");
                        }
                    }
                }
            }else{
                NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "User error", "Possible reason: Only one user is allowed per request form and login cannot be empty.");
            }
        }else{
            NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "User role error", "You do not have permission to submit samples for this user.");
        }
        ssb.initSampleList();
        
        //upload to LIMS1 as well
        //if(submitsuccess){
        //    submitRequest(data);
        //}
        
        if(submitsuccess){
        //if(false){
            String message = mailBean.composeRequestReceivedMessage(u.getFirstName(), sb.toString());
            String[] recipient = new String[2];
            recipient[0] = u.getMailAddress();
            recipient[1] = Preferences.getSentByMailAddress();
            
            //for testing
            //recipient[0] = "heiko.muller@ieo.eu";
            //recipient[1] = "heiko.muller@ieo.eu";
            try{
                mailBean.sendRequestIDMail(recipient, message);
            }catch(MessagingException me){
                NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Email error", "Sending email acknowledgement failed: MessagingException");
            }catch(UnsupportedEncodingException uee){
                NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Email error", "Sending email acknowledgement failed: UnsupportedEncodingException");        
            }
        }
        progress = 0;
    }
    
    private User userExists(List<User> userlist, String login){
        for(User u : userlist){
            if(u.getLogin().equals(login)){
                return u;
            }
        }
        return null;
    }
    
    //LIMS1 upload
    /*
    public void submitRequest(String[][] data) {
        //filename = "submitted";
        //filename = (new File(file1.getFileName())).getName();
        //System.out.println("file upload called"); 
        
        //SubmitNGSRequestBean sb = new SubmitNGSRequestBean();
        
        //String[][] data = parseRequestCSV();
        for(int i = 0; i < data.length; i++){
            if(!data[i][0].startsWith("UserName:")){
                UserBean ub = new UserBean();
                it.iit.genomics.cru.crudb.User u = ub.getUser(data[i][1]);
                if(u != null){
                    Chipseq cs = setChipseq(data[i], u.getUserlogin() , u.getUseremail());
                    submit(cs);
                }else{
                    boolean newuser = ub.createUser(data[i][1], data[i][4], data[i][3]);                    
                    if(newuser){
                        u = ub.getUser(data[i][1]);
                        Chipseq cs = setChipseq(data[i], u.getUserlogin() , u.getUseremail());
                        submit(cs);
                    }else{
                        System.out.println("couldn't submit request");
                    }
                }
            }
        }
        System.out.println("upload finished"); 
    }
    
    public void submit(Chipseq a){
            //findSampleID();
            
            Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();
                //Session session = entity.HibernateUtil.getSessionFactory().getCurrentSession();                
                Transaction tx = null;                
                try {   
                    tx = session.beginTransaction();
                    //Chipseq a = setChipseq();    
                    Reagentuse r = setReagentUse(a);
                    session.save(a);
                    session.save(r);
                    tx.commit();  
                    System.out.println("Submission successful.");  
                }
                catch (RuntimeException e) {
                    System.out.println("Submission failed.");
                    tx.rollback();
                }
                finally {
                    if(session.isOpen()){
                        session.close();   
                    }
                }
                
                
        }
    
    public Chipseq setChipseq(String[] s, String login, String email){
            Chipseq a = new Chipseq();
            String[] recipe = parseRecipe(s[9]);
            String index = parseIndex(s[6]);
            String indexname = parseIndexName(s[6]);
            try{
                a.setUserName(s[0]);
                a.setUserTel(s[2]);
                a.setInstitute(s[3]);
                a.setPi(s[4]);
                //a.setCostCenter(s[4]);
                a.setApplication(s[7]);
                a.setReadLength(Integer.parseInt(s[8]));
                a.setInstrument("HiSeq2000");
                a.setReadMode(recipe[0]);
                a.setDepth(recipe[1]);
                a.setSampleType(s[10]);
                a.setLibrarySynthesis(s[11]);
                a.setOrganism(s[12]);
                a.setAntibody(s[13]);
                s[14] = it.iit.genomics.cru.crudb.bean.SampleNameFilter.legalize(s[14]);
                a.setSampleDescription(s[14]);
                
                if(s[17] == null || s[17].length() == 0){
                    a.setSampleConcentration(Double.parseDouble("0"));
                }else{
                    a.setSampleConcentration(Double.parseDouble(s[17]));
                }
                
                if(s[18] == null || s[18].length() == 0){
                    a.setTotalAmount(Double.parseDouble("0")); 
                }else{
                    a.setTotalAmount(Double.parseDouble(s[18])); 
                }
                
                if(s[19] == null || s[19].length() == 0){
                    a.setBulkFragmentSize(Integer.parseInt("0"));
                }else{
                    a.setBulkFragmentSize(Integer.parseInt(s[19]));
                }
                if(s.length > 20 && s[20] != null){
                    a.setComments(s[20]);
                }
                //if(requestDate == null){
                //    requestDate = new Date(System.currentTimeMillis());
                //}
                a.setRequestDate(new Date(System.currentTimeMillis()));
                a.setUserLogin(login);
                a.setUserEmail(email);
                int id = findSampleID();
                a.setSampleId(id);
                s[5] = it.iit.genomics.cru.crudb.bean.SampleNameFilter.legalize(s[5]);
                a.setSampleName(s[5] + "_S" + id);                
                a.setProcessed(false);
                //a.setIndex(index);
                a.setBarcode(index);
                a.setBarcodename(indexname);
                if(s[15] != null && s[15].length() > 0){
                    a.setBioanalyzerDate(new Date(Date.parse(s[15])));
                }
                if(s[16] != null && s[16].length() > 0){
                    a.setBioanalyzerNanomolarity(Double.parseDouble(s[16]));
                }
                
                
                
            }catch(Exception e){
                e.printStackTrace();
                
                return null;
            }
            return a;
            
        }
    
    private Reagentuse setReagentUse(Chipseq cs){
            Reagentuse a = new Reagentuse();
            a.setSampleId(cs.getSampleId());
            a.setUserName(cs.getUserName());
            a.setSampleName(cs.getSampleName());  
            //a.setSeqIndex(cs.getIndex());
            a.setSeqIndex(cs.getBarcode());
            return a;
            
        }
    
    private int findSampleID(){
        Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();
        int result = -1;
            //Session session = entity.HibernateUtil.getSessionFactory().getCurrentSession();                
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    List<Integer> l = session.createQuery("select sampleId from Chipseq order by sampleId").list();
                    result = (l.get(l.size()-1).intValue() + 1);
                    tx.commit();
                    
                }
                catch (RuntimeException e) {
                    tx.rollback();
                    //throw e; // or display error message
                }
                finally {
                    session.close();
                } 
                return result;
    }
    */
    
    private String[] parseRecipe(String input){
            String[] temp = input.split(":");
            temp[0] = temp[0].trim();
            temp[1] = temp[1].trim();
            if(temp[0].equals("Single read")){
                temp[0] = "SR";
            }else if(temp[0].equals("Paired end")){
                temp[0] = "PE";
            }
            
            temp[1] = temp[1].replace("mio reads", "");
            temp[1] = temp[1].trim();
            return temp;
        }
    
    /**
     * Tests if the uploaded form contains only data for a single user.
     *
     * @author Heiko Muller
     * @param data - the uploaded parsed form
     * @return - true if single user, false otherwise
     * @since 1.0
     */
    private boolean isSingleUserSubmission(String[][] data){
        HashSet<String> hs = getUserLogins(data);
        
        if(hs.size() == 1){
            return true;
        }
        
        return false;
    }
    
    /**
     * Tests user has permission to submit these data.
     * Admins, technicians and groupleaders can submit for other users.
     * Users can only submit their own samples.
     *
     * @author Heiko Muller
     * @param data - the uploaded parsed form
     * @return boolean - true if user can submit, false otherwise
     * @since 1.0
     */
    private boolean roleTest(String[][] data){
        HashSet<String> hs = getUserLogins(data);
        
        if(hs.size() == 1){
            User u = UserHelper.getUserByLoginName((String)hs.toArray()[0]);
            if(u.getLogin().equals(userLogin)){
                return true;
            }else if(this.loggeduser.getUserRole().equals(Preferences.ROLE_ADMIN) || this.loggeduser.getUserRole().equals(Preferences.ROLE_TECHNICIAN) || this.loggeduser.getUserRole().equals(Preferences.ROLE_GROUPLEADER)){
                return true;
            }else{
                return false;
            }
        }
        return false;    
    }
    
    /**
     * get the set of the user logins
     *
     * @author Heiko Muller
     * @param data - the uploaded parsed form
     * @return - user logins
     * @since 1.0
     */
    private HashSet<String> getUserLogins(String[][] data){
        HashSet<String> hs = new HashSet<String>();
        if(data == null){
            return hs;
        }
        for(int i = 1; i < data.length; i++){
            if(data[i][1] == null || data[i][1].length() == 0){
                continue;
            }else{
                if(!hs.contains(data[i][1])){
                    hs.add(data[i][1]);
                }
            }
        }
        return hs;        
    }

    /**
     * Persists a new sample.
     *
     * @author Heiko Muller
     * @param sample
     * @since 1.0
     */
    public boolean submit(Sample sample) {
        boolean result = true;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(sample);
            sample = addSampleIdToSampleName(sample);
            session.saveOrUpdate(sample);
            tx.commit();
            System.out.println("Submission successful.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Submission failed.");
            tx.rollback();
            result = false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return result;
    }
     
    /**
     * Persists a new sample.
     *
     * @author Heiko Muller
     * @param sample
     * @since 1.0
     */
    public boolean submit(Sample sample, int libid, String libraryname) {
        boolean result = true;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            System.out.println("Persisting sample with id " + sample.getId());
            tx = session.beginTransaction();
            session.save(sample);
            sample = addSampleIdToSampleName(sample);
            session.saveOrUpdate(sample);            
            LibraryId lid = new LibraryId(libid, sample.getId());
            Library l = new Library(lid, sample, libraryname);
            session.save(l);
            //session.save(sample);   
            
            tx.commit();
            //int id = sample.getId();
            //Sample s = (Sample)session.load(Sample.class, id);
            //s.getLibrary();
            //System.out.println("library name " + s.getLibraryName());
            //dataBean.addSample(s);
            System.out.println("Submission successful.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Submission failed.");
            tx.rollback();
            dataBean.removeSample(sample);
            result = false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return result;
    }
    
    /**
     * Persists a new sample.
     *
     * @author Heiko Muller
     * @param sample - the sample to be submitted
     * @param sb - a StringBuilder that tracks requestIds and sample names of submitted samples to be used in email alert
     * @return boolean - true if sample was submitted successfully, false otherwise.
     * @since 1.0
     
    public boolean submitWithSQL(Sample sample, StringBuilder sb) {
        //sample.setId(SampleHelper.getNextSampleId());
        sample = addSampleIdToSampleName(sample);
        //System.out.println(SampleHelper.getSQLSaveSampleString(sample));
        boolean outcome = SampleHelper.saveSampleSQL(sample);
        if(outcome){
            sb.append("<tr><td>" + sample.getId() + "</td><td>" + sample.getName() + "</td></tr>");
        }      
        return outcome;
    }
*/
    /**
     * Tests if String val is present in a String array arr.
     *
     * @author Heiko Muller
     * @param arr
     * @param val
     * @return boolean
     * @since 1.0
     */
    private boolean containsValue(String[] arr, String val) {
        boolean contains = false;
        for (String s : arr) {
            if (val.equals(val)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    /**
     * Creates new sample using the data of a single row of the uploaded csv
     * file.
     *
     * @author Heiko Muller
     * @param s - a row of the csv file as a String[]
     * @param u - the user submitting this sample
     * @return Sample
     * @since 1.0
     */
    public Sample setSample(String[] s, User u) {
        //Preferences prefs = new Preferences();
        FacesContext context = FacesContext.getCurrentInstance();
        Preferences prefs  = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);   
        Sample a = new Sample();
        int id = SampleHelper.getNextSampleId();
        a.setId(id);
        try {
            a.setUser(u);

            SequencingIndex idx = SampleHelper.getSequencingIndexIdBySequence("none");
            a.setSequencingIndexes(idx);
            if (s.length > 6 && s[6].length() > 0) {
                String ind = parseIndex(s[6]);
                if (ind != null && ind.length() > 0) {
                    //System.out.println(ind);
                    idx = SampleHelper.getSequencingIndexIdBySequence(ind);
                    if (idx == null) {
                        NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Unknown index", s[6]);
                        return null;
                    }
                    a.setSequencingIndexes(idx);
                }
            }
    
            Application app = parseApplication(s);
            if (app == null) {
                NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Unknown application", s[9]);
                return null;
            }
            app = SampleHelper.saveOrUpdate(app);
            a.setApplication(app);


            a.setType("");
            if (s.length > 10 && s[10].length() > 0) {
               String sampleType = s[10];
                if (!containsValue(prefs.getSampletype(), sampleType)) {
                    NgsLimsUtility.setWarningMessage(formId, "RequestUploadProcessButton", "Unknown sample type", sampleType);
                }
                a.setType(sampleType);
            }
            
            a.setLibrarySynthesisNeeded(false);
            if (s.length > 11 && s[11] == null) {
                if (s[11].toUpperCase().trim().equals("TRUE") || s[11].toUpperCase().trim().equals("NEEDED")) {
                    a.setLibrarySynthesisNeeded(true);
                }
            }

            a.setOrganism("undefined");
            if (s.length > 12 && s[12].length() > 0) {
                String organism = s[12];
                if (!containsValue(prefs.getOrganisms(), organism)) {
                    NgsLimsUtility.setWarningMessage(formId, "RequestUploadProcessButton", "Unknown organism", organism);
                }
                a.setOrganism(organism);
            }          
            
            a.setAntibody("");
            if (s.length > 13 && s[13].length() > 0) {
                a.setAntibody(s[13]);
            }
            

            a.setStatus(Sample.status_requested);
            
            a.setName("undefined");            
            if (s.length > 5 && s[5].length() > 0) {
                a.setName(SampleNameFilter.legalize(s[5]));
            }
            
            a.setDescription("");
            if (s.length > 14 && s[14].length() > 0) {
                a.setDescription(SampleNameFilter.legalize(s[14]));
            }
            
            //a.setBioanalyzerDate("1-Jan-70", "d-MMM-YY");
            //a.setBioanalyzerDate(DateParser.parseDateAustria(s[23], "dd.MM.YYYY"));
            if (s.length > 15 && s[15].length() > 0) {
                Date d = DateParser.parseDateAustria(s[15], "dd.MM.YYYY");
                if(d != null){
                    a.setBioanalyzerDate(d);
                }
            }

            a.setBionalyzerBiomolarity(0.0);
            if (s.length > 16 && s[16] != null && s[16].trim().length() > 0) {
                a.setBionalyzerBiomolarity(Double.parseDouble(s[16]));
            }

            a.setConcentration(0.0);
            if (s.length > 17 && s[17] != null && s[17].trim().length() > 0) {
                a.setConcentration(Double.parseDouble(s[17]));
            }

            a.setTotalAmount(0.0);
            if (s.length > 18 && s[18] != null && s[18].trim().length() > 0) {
                a.setTotalAmount(Double.parseDouble(s[18]));
            }

            a.setBulkFragmentSize(0.0);
            if (s.length > 19 && s[19] != null && s[19].trim().length() > 0) {
                a.setBulkFragmentSize(Double.parseDouble(s[19]));
            }

            a.setComment("");
            if (s.length > 20) {
                a.setComment(s[20]);
            }
            
            //a.setBioanalyzerDate(DateParser.parseDateItaly("1-Jan-70", "d-MMM-YY"));
            a.setRequestDate(DateParser.parseDateAustria(s[23], "dd.MM.YYYY"));
            a.setSubmissionId(Integer.parseInt(s[22]));

            //a.setTimeSeriesStep(null);
            
            a.setExperimentName(bundle.getString("undefined"));
            if (s.length > 7 && s[7] != null && s[7].trim().length() > 0) {
                a.setExperimentName(app.getApplicationname());
            }
            
            a.setCostCenter("");
            if (s.length > 4 && s[4] != null && s[4].trim().length() > 0) {
                a.setCostCenter(s[4]);
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return a;

    }
    
    /**
     * Creates new sample using the data of a single row of the uploaded csv
     * file.
     *
     * @author Heiko Muller
     * @param s - a row of the csv file as a String[]
     * @param u - the user submitting this sample
     * @return Sample
     * @since 1.0
     */
    public Sample setSample2(String[] s, User u) {
        //to be used for request form from February 2015
           /*      
        0 user name 
        1 user login
        2 user tel
        3 Institute
        4 PI
        5 Sample name
        6 Barcode
        7 Application
        8 Instrument
        9 Read length
        10 Recipe
        11 Sample type
        12 Library synthesis
        13 Organism
        14 Antibody
        15 Sample description
        16 Bioanalyzer date
        17 Bioanalyzer nanomolarity
        18 Sample concentration
        19 Total amount
        20 Bulk frag size
        21 Volume
        22 Comments
*/
        //Preferences prefs = new Preferences();
        FacesContext context = FacesContext.getCurrentInstance();
        Preferences prefs  = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);   
        Sample a = new Sample();
        int id = SampleHelper.getNextSampleId();
        a.setId(id);
        try {
            a.setUser(u);

            SequencingIndex idx = SampleHelper.getSequencingIndexIdBySequence("none");
            a.setSequencingIndexes(idx);
            if (s.length > 6 && s[6].length() > 0) {
                String ind = parseIndex(s[6]);
                if (ind != null && ind.length() > 0) {
                    idx = SampleHelper.getSequencingIndexIdBySequence(ind);
                    if (idx == null) {
                        NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Unknown index", s[6]);
                        return null;
                    }
                    a.setSequencingIndexes(idx);
                }
            }
    
            Application app = parseApplication2(s);
            if (app == null) {
                NgsLimsUtility.setFailMessage(formId, "RequestUploadProcessButton", "Unknown application", s[9]);
                return null;
            }
            app = SampleHelper.saveOrUpdate(app);
            a.setApplication(app);


            a.setType("");
            if (s.length > 11 && s[11].length() > 0) {
               String sampleType = s[11];
                if (!containsValue(prefs.getSampletype(), sampleType)) {
                    NgsLimsUtility.setWarningMessage(formId, "RequestUploadProcessButton", "Unknown sample type", sampleType);
                }
                a.setType(sampleType);
            }
            
            a.setLibrarySynthesisNeeded(false);
            if (s.length > 12 && s[12] == null) {
                if (s[12].toUpperCase().trim().startsWith("NEEDED")) {
                    a.setLibrarySynthesisNeeded(true);
                }
            }

            a.setOrganism("undefined");
            if (s.length > 13 && s[13].length() > 0) {
                String organism = s[13];
                if (!containsValue(prefs.getOrganisms(), organism)) {
                    NgsLimsUtility.setWarningMessage(formId, "RequestUploadProcessButton", "Unknown organism", organism);
                }
                a.setOrganism(organism);
            }          
            
            a.setAntibody("");
            if (s.length > 14 && s[14].length() > 0) {
                a.setAntibody(s[14]);
            }
            

            a.setStatus(Sample.status_requested);
            
            a.setName("undefined");            
            if (s.length > 5 && s[5].length() > 0) {
                a.setName(SampleNameFilter.legalize(s[5]));
            }
            
            a.setDescription("");
            if (s.length > 15 && s[15].length() > 0) {
                a.setDescription(SampleNameFilter.legalize(s[15]));
            }
            
            //a.setBioanalyzerDate("1-Jan-70", "d-MMM-YY");
            a.setBioanalyzerDate(DateParser.parseDateItaly("1-Jan-70", "d-MMM-YY"));
            if (s.length > 16 && s[16].length() > 0) {
                Date d = DateParser.parseDateItaly(s[16], "d-MMM-YY");
                if(d != null){
                    a.setBioanalyzerDate(d);
                }
            }

            a.setBionalyzerBiomolarity(0.0);
            if (s.length > 17 && s[17] != null && s[17].trim().length() > 0) {
                a.setBionalyzerBiomolarity(Double.parseDouble(s[17]));
            }

            a.setConcentration(0.0);
            if (s.length > 18 && s[18] != null && s[18].trim().length() > 0) {
                a.setConcentration(Double.parseDouble(s[18]));
            }

            a.setTotalAmount(0.0);
            if (s.length > 19 && s[19] != null && s[19].trim().length() > 0) {
                a.setTotalAmount(Double.parseDouble(s[19]));
            }

            a.setBulkFragmentSize(0.0);
            if (s.length > 20 && s[20] != null && s[20].trim().length() > 0) {
                a.setBulkFragmentSize(Double.parseDouble(s[20]));
            }

            a.setComment("");
            if (s.length > 22) {
                if(s[21] != null && s[21].length() > 0){
                    a.setComment(s[22] + ", volume " + s[21] + ", library synthesis " + s[12]);
                }else{
                    a.setComment(s[22] + ", volume " + "0" + ", library synthesis " + s[12]);
                }
                
            }
            a.setRequestDate(new Date(System.currentTimeMillis()));

            //a.setTimeSeriesStep(null);
            
            a.setExperimentName(bundle.getString("undefined"));
            if (s.length > 7 && s[7] != null && s[7].trim().length() > 0) {
                a.setExperimentName(app.getApplicationname());
            }
            
            a.setCostCenter("");
            if (s.length > 4 && s[4] != null && s[4].trim().length() > 0) {
                a.setCostCenter(s[4]);
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        return a;

    }

    /**
     * Parses uploaded csv file.
     *
     * @author Heiko Muller
     * @return String[][]
     * @since 1.0
     */
    private String[][] parseRequestCSV() {
        File f = new File(destination + filename);
        return convertFileContent(readFile(f));
    }

    /**
     * Parses index sequence.
     *
     * @author Heiko Muller
     * @param input
     * @return String
     * @since 1.0
     */
    private String parseIndex(String input) {
        if(input.indexOf("_") == -1){
            return input;
        }
        if (input != null && input.length() > 0) {
            String[] temp = input.split("_");
            return temp[temp.length - 1];
        } else {
            return "";
        }
    }

    /**
     * Parses application.
     *
     * @author Heiko Muller
     * @param input
     * @return Application
     * @since 1.0
     */
    private Application parseApplication(String[] input) {
        //app name idx = 7
        //app readlength idx = 8
        //app recipe idx = 9
        Application a = new Application();
        String appName = input[7];
        if (isNewApplicationName(appName)) {
            NgsLimsUtility.setWarningMessage(formId, "RequestUploadProcessButton", "Unknown application name", appName);
        }
        Recipes r = new Recipes();
        r.updateApplicationByRecipe(input[9], a);
        a.setApplicationname(appName);
        a.setReadlength(Integer.parseInt(input[8]));
        a.setInstrument("HiSeq2000");

        return a;
    }
    
    /**
     * Parses application.
     *
     * @author Heiko Muller
     * @param input
     * @return Application
     * @since 1.0
     */
    private Application parseApplication2(String[] input) {
        //app name idx = 7
        //app readlength idx = 9
        //app recipe idx = 10
        Application a = new Application();
        String appName = input[7];
        if (isNewApplicationName(appName)) {
            NgsLimsUtility.setWarningMessage(formId, "RequestUploadProcessButton", "Unknown application name", appName);
        }
        Recipes r = new Recipes();
        r.updateApplicationByRecipe(input[10], a);
        a.setApplicationname(appName);
        a.setReadlength(Integer.parseInt(input[9]));
        a.setInstrument(input[8]);

        return a;
    }
    
    /**
     * Tests if application is new.
     *
     * @author Heiko Muller
     * @param appName
     * @return boolean
     * @since 1.0
     */
    private boolean isNewApplicationName(String appName) {
        //Preferences prefs = new Preferences();
        FacesContext context = FacesContext.getCurrentInstance();
        Preferences prefs  = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);   
        boolean isNewApp = true;
        for (String ngsapplication : prefs.getNgsapplication()) {
            if (ngsapplication.equals(appName)) {
                isNewApp = false;
            }
        }
        return isNewApp;
    }

    /**
     * Reads file f and returns content as a Vector<String> with each line of text as a separate String.
     *
     * @author Heiko Muller
     * @param f
     * @return Vector<String>
     * @since 1.0
     */
    private Vector<String> readFile(File f) {
        Vector<String> v = new Vector<String>();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                v.add(line);
            }
        } catch (IOException ioe) {
        }
        return v;
    }

    /**
     * Converts file content into a String[][] object.
     *
     * @author Heiko Muller
     * @param v - the file content
     * @return String[][]
     * @since 1.0
     */
    private String[][] convertFileContent(Vector<String> v) {
        //remove empty lanes if present
        for (int i = 0; i < v.size(); i++) {
            if(v.get(i).length() < 24){
                v.remove(i);
            }
        }
        
        //String[][] result = new String[v.size()][21];
        String[][] result = getInitializedStringArray(v.size(), 24);
        
        for (int i = 0; i < v.size(); i++) {
            //System.out.println("parsing csv file line " + (i + 1) + " " + v.get(i).length());
            String[] temp = v.get(i).split(",");
            for(int j = 0; j < temp.length && j < 24; j++){
                result[i][j] = temp[j];
            }
        }
        return result;
    }
    
    private String[][] getInitializedStringArray(int rows, int columns){
        String[][] result = new String[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                result[i][j] = "";
            }
        }
        return result;
    }

    /**
     * Copies uploaded file to local disk.
     *
     * @author Heiko Muller
     * @param file - UploadedFile
     * @since 1.0
     */
    public void transferFile(UploadedFile file) {
        //String fileName = file.getFileName();
        String fileName = (new File(file.getFileName())).getName();
        filename = fileName;
        try {
            InputStream in = file.getInputstream();
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int reader = 0;
            byte[] bytes = new byte[(int) file.getSize()];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Parses index name.
     *
     * @author Heiko Muller
     * @param input
     * @return String
     * @since 1.0
     */
    private String parseIndexName(String input) {
        if (input != null && input.length() > 0) {
            return input.substring(0, input.length() - 7);
        } else {
            return "";
        }
    }

    /**
     * Setter for filename.
     *
     * @author Heiko Muller
     * @param name - the file name
     * @since 1.0
     */
    public void setFilename(String name) {
        filename = name;
    }

    /**
     * Getter for filename.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Getter for file1.
     *
     * @author Heiko Muller
     * @return UploadedFile
     * @since 1.0
     */
    public UploadedFile getFile1() {
        return file1;
    }

    /**
     * Setter for file1.
     *
     * @author Heiko Muller
     * @param file1 - the uploaded file
     * @since 1.0
     */
    public void setFile1(UploadedFile file1) {
        this.file1 = file1;
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
    
    public int getProgress(){
        return progress;
    }

}
