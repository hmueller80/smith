package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SampleRunId;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.reagentsBeans.ReagentHelper;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * @(#)UploadRunFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for uploading sample runs.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "uploadRunFormBean")
@SessionScoped
public class UploadRunFormBean implements Serializable {

    private ResourceBundle bundle;
    private String destination;
    private String applicationPath;
    private UploadedFile file1;
    private String filename = "undefined";
    private String filecontent;
    private String userLogin = "";
    private User operator;
    private String formId = "";
    private int runID = -1;
    RunsSearchBean rsb;
    DataBean dataBean;
    int progress;
    
    //AssembleSamplesheetBean assembleSamplesheetBean;
    //SubmitNGSRequestBean submitNGSRequestBean;

    /**
    * Bean constructor
    *
    * @author Heiko Muller
    * @since 1.0
    */
    public UploadRunFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init UploadRunFormBean...");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        //loggedUserBean = new LoggedUser();
        rsb = (RunsSearchBean) context.getApplication().evaluateExpressionGet(context, "#{runsSearchBean}", RunsSearchBean.class);    
        dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
        //assembleSamplesheetBean = (AssembleSamplesheetBean) context.getApplication().evaluateExpressionGet(context, "#{assembleSamplesheetBean}", AssembleSamplesheetBean.class);  
        //submitNGSRequestBean = (SubmitNGSRequestBean) context.getApplication().evaluateExpressionGet(context, "#{submitNGSRequestBean}", SubmitNGSRequestBean.class);  
        userLogin = context.getExternalContext().getRemoteUser();
        operator = UserHelper.getUserByLoginName(userLogin);
        formId = "runTableUploadForm";
        //List<SampleRun> runs = RunHelper.getRunsList();
        //if (runs != null && runs.size() > 0) {
        //runID = rsb.getNewRunId();
        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        //} else {
        //    runID = -1;
        //}
        try {
            applicationPath = context.getExternalContext().getRealPath("/");
            destination = applicationPath + "upload" + File.separator;
            if(Preferences.getVerbose()){
                System.out.println(destination);
            }            
        } catch (UnsupportedOperationException uoe) {
            uoe.printStackTrace();
        }  
        
        if(Preferences.getVerbose()){
            System.out.println("init UploadRunFormBean...done");
        }
        
    }

    /**
    * Action listener for file upload event.
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
    * Copies remote file to upload directory.
    *
    * @author Heiko Muller
    * @param file - a remote file
    * @since 1.0
    */
    public void transferFile(UploadedFile file) {
        //writes uploaded file to upload directory
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
    * Parses a comma separated sample sheet.
    *
    * @author Heiko Muller
    * @return String[][]
    * @since 1.0
    */
    private String[][] parseRequestCSV() {
        File f = new File(destination + filename);
        return readFile(readFile(f));
    }

    /**
    * Reads a file.
    *
    * @author Heiko Muller
    * @param f
    * @return Vector<String> - the file content with each line of text in a Vector<String>
    * @since 1.0
    */
    private Vector<String> readFile(File f) {
        Vector<String> v = new Vector<String>();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                if(line.length() > 0){
                    //System.out.println(line);
                    v.add(line);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return v;
    }

    /**
    * Reads file content and returns it as comma separated values.
    *
    * @author Heiko Muller
    * @param v - file content
    * @return String[][] - the file content with each line of text in a Vector<String>
    * @since 1.0
    */
    private String[][] readFile(Vector<String> v) {
        String[][] result = new String[v.size()][21];
        //System.out.println("vector size " + v.size());
        for (int i = 0; i < v.size(); i++) {
            result[i] = v.get(i).split(",");
        }
        return result;
    }

    /**
    * Submits a sample run.
    *
    * @author Heiko Muller
    * @since 1.0
    */
    public void submitSampleRun() {
        //0 Lane	
        //1 ID	
        //2 UserName	
        //3 Flowcell	
        //4 Cluster gen	
        //5 Sequencing	
        //6 Run Date	
        //7 Barcode	
        //8 SampleName
        formId = "runsTableForm";
        ArrayList<SampleRun> toBeSubmitted = new ArrayList<SampleRun>();
        ArrayList<Lane> lanesToBeSubmitted = new ArrayList<Lane>();
        boolean allSampleRunsCorrect = true;
        StringBuilder error = new StringBuilder();
        String[][] data = null;
        ArrayList<Integer> ids = null;
        progress = 0;
        if (operator != null && (operator.getUserRole().equals(Preferences.ROLE_TECHNICIAN) || operator.getUserRole().equals(Preferences.ROLE_ADMIN))) {
            
            data = parseRequestCSV();
            System.out.println(data.length);
            ids = findNonredundantListOfSampleIds(data);
            runID = Integer.parseInt(data[0][2]);
            for (int i = 0; i < ids.size(); i++) {
                double d = (i + 1) * 1.0/ids.size();
                progress = (int)d * 100;
                //if (!data[i][0].startsWith("Lane")) {
                    ArrayList<Integer> lanes = findLanesForSample(data, ids.get(i).toString());
                    String[] entry = findFirstSampleOccurrence(data, ids.get(i).toString());
                    int sampleid = ids.get(i);
                    Sample sample = SampleHelper.getSampleByID(sampleid);
                    User user = sample.getUser();
                    if (user != null) {
                        SampleRun sr = setSampleRun(entry, error, lanes);
                        //sr.
                        if(sr != null){
                            //System.out.println(sr.getsamId());
                            //SampleRunId sid = sr.getId();
                            
                            toBeSubmitted.add(sr);
                        }else{
                            System.out.println("error in sample " + user.getUserName() + " " + sample.getName());
                            
                            allSampleRunsCorrect = false;
                        }

                    } else {
                        NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", bundle.getString("user.not.found1"), bundle.getString("user.not.found2"));
                    }
                //}
            }
            
        }
        
        if(allSampleRunsCorrect){
            for(SampleRun sr : toBeSubmitted){
                submitSampleRun(sr);
            }
            
            
            
            
            //**********************************************************************//
            //submit also to LIMS1
            
            /*
            FacesContext context = FacesContext.getCurrentInstance();
   
            assembleSamplesheetBean = (AssembleSamplesheetBean) context.getApplication().evaluateExpressionGet(context, "#{assembleSamplesheetBean}", AssembleSamplesheetBean.class);  
            submitNGSRequestBean = (SubmitNGSRequestBean) context.getApplication().evaluateExpressionGet(context, "#{submitNGSRequestBean}", SubmitNGSRequestBean.class); 
            String operator = context.getExternalContext().getRemoteUser();
            
            for(int i = 0; i < data.length; i++){
                int id = Integer.parseInt(data[i][1]);

                Reagentuse temp = new Reagentuse();
                temp.setClusterGeneration(data[i][4]);
                //temp.setCustomIndex(ru.getCustomIndex());
                temp.setFcid(data[i][3]);
                temp.setLane(Integer.parseInt(data[i][0]));            
                temp.setOperator(operator);
                String date = data[i][6];


                //System.out.println(date);
                Date dat = new Date(System.currentTimeMillis());





                try{
                    temp.setRunDate(dat);
                }catch(Exception e){
                    temp.setRunDate(new Date(System.currentTimeMillis()));
                }
                temp.setSampleId(Integer.parseInt(data[i][1]));
                temp.setSampleName(data[i][8]);
                //temp.setSamplePrep((String)tm.getValueAt(i, 4));
                temp.setSeqIndex(data[i][7]);
                temp.setSequencing(data[i][5]);
                temp.setUserName(data[i][2]);

                //System.out.println(sb.updateReagentuse(temp));
                //now do the update
                updateReagentuse(temp);


                boolean exists = SampleSheetExists(id);

                if(exists){
                    //System.out.println("Samplesheet exists");
                    //Samplesheet s = as.assemble(id);
                    updateExistingSamplesheet(id, temp);
                }else{
                    //System.out.println("Samplesheet doesn't exist");
                    Samplesheet s = assemble(id, temp);
                    saveNewSamplesheet(s);
                    //as.updateExistingSamplesheet(id, temp);
                }
            
            }
            */
            
            
            
            
            
            
            
        }else{
            NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "Error uploading a run", error.toString());
        }
        rsb.init();
        progress = 0;
    }
    
    /*
    public String updateReagentuse(Reagentuse run){
        Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();
            //Session session = entity.HibernateUtil.getSessionFactory().getCurrentSession(); 
        StringBuilder sb = new StringBuilder();
                Transaction tx = null;
                //String[] run = null;
                
                
                try {
                    tx = session.beginTransaction();
                    //Reagentuse temp = (Reagentuse)session.load(Reagentuse.class, id);
                    Reagentuse temp = (Reagentuse)session.load(Reagentuse.class, run.getSampleId());
                    copy(temp, run);
                    session.saveOrUpdate(temp);
                    Chipseq cs = (Chipseq)session.load(Chipseq.class, run.getSampleId());
                    cs.setProcessed(true);
                    session.saveOrUpdate(cs);
                    //run = runToStringArray(temp);
                    
                    tx.commit();  
                  sb.append("Update succeded.");
                    
                }
                catch (RuntimeException e) {
                    tx.rollback();
                    //throw e; // or display error message
                    sb.append("Update failed.");
                }
                finally {
                    session.close();
                }  
                return sb.toString();
    }
    
    private void copy(Reagentuse temp, Reagentuse ru){        
        temp.setClusterGeneration(ru.getClusterGeneration());
        temp.setCustomIndex(ru.getCustomIndex());
        temp.setFcid(ru.getFcid());
        temp.setLane(ru.getLane());
        temp.setOperator(ru.getOperator());
        temp.setRunDate(ru.getRunDate());
        temp.setSampleId(ru.getSampleId());
        temp.setSampleName(ru.getSampleName());
        temp.setSamplePrep(ru.getSamplePrep());
        temp.setSeqIndex(ru.getSeqIndex());
        temp.setBarcodename(ru.getBarcodename());
        temp.setSequencing(ru.getSequencing());
        temp.setUserName(ru.getUserName());
        
    }
    
    public boolean SampleSheetExists(int id){
            
            org.hibernate.classic.Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();
            boolean result = false;
                //Session session = entity.HibernateUtil.getSessionFactory().getCurrentSession();                
                Transaction tx = null;
                try {
                    
                    tx = session.beginTransaction();
                    //Samplesheet a = (Samplesheet)session.load(Samplesheet.class, id);
                    List<Samplesheet> lc = session.createQuery("SELECT a FROM Samplesheet a WHERE a.counter = " + id).list(); 
                    if(lc != null && lc.size() > 0){
                        //System.out.println("Samplesheet counter " + a.getCounter());
                        result = true;
                    }
     
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
    
    public String updateExistingSamplesheet(int id, Reagentuse r){
            org.hibernate.classic.Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();     
            StringBuilder sb = new StringBuilder();
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    //Samplesheet s = (Samplesheet)session.load(Samplesheet.class, ss.getCounter());
                    System.out.println("selcting samplesheet");
                    List<Samplesheet> lc = session.createQuery("SELECT a FROM Samplesheet a WHERE a.counter = " + id).list(); 
                    System.out.println("selcting request");
                    List<Chipseq> cs = session.createQuery("SELECT a FROM Chipseq a WHERE a.sampleId = " + id).list(); 
                    System.out.println("selcting samplesheet 0 ");
                    //List<Reagentuse> lr = session.createQuery("SELECT a FROM Reagentuse a WHERE a.sampleId = " + ss.getCounter()).list(); 
                    Samplesheet s = lc.get(0);
                    //ss.setIndexed(r.getSeqIndex());    
                    System.out.println("updating samplesheet ");
                    s = updateSamplesheet(s, r, cs.get(0));
                    System.out.println("save or update ");
                    session.saveOrUpdate(s);
                    System.out.println("commit");
                    tx.commit();
                    System.out.println("New Samplesheet saved successfully");
                    sb.append("New Samplesheet saved successfully");
                    
                }
                catch (RuntimeException e) {
                    tx.rollback();
                   
                    sb.append("Samplesheet modification failed.");
                    System.out.println("Samplesheet modification failed.");
                    //throw e; // or display error message
                    //System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                finally {
                    session.close(); 
                } 
                return sb.toString();
        }
    
    private Samplesheet updateSamplesheet(Samplesheet a, Reagentuse r, Chipseq cs){
            a.setControl("N");
            //a.setCounter(updates.getCounter());
            a.setDate(r.getRunDate());
            a.setDescription(cs.getSampleDescription());
            a.setFcid(r.getFcid());
            a.setIndexed(r.getSeqIndex());
            a.setLane(r.getLane());
            a.setInstrument("HiSeq2000");
            a.setOperator("lrotta"); 
            if(r.getSeqIndex().length() > 0){
                a.setRecipe(cs.getReadLength() + cs.getReadMode() + "_indexed");
            }else{
                a.setRecipe(cs.getReadLength() + cs.getReadMode());
            }
            //a.setSampleId(r.getSampleName());
            a.setSampleProject(cs.getUserLogin());
            a.setSampleRef(cs.getOrganism());
            return a;
           
            
        }
    
    public Samplesheet assemble(int sampleId, Reagentuse r){
            
            
            //**********************************
            //called when clicking submit run data
            //**********************************
            
            
            
            
            //FacesContext context = FacesContext.getCurrentInstance();
            //String paramValue = (String)context.getExternalContext().getRequestParameterMap().get("paramName");

            //if(requestid.length() > 0){
                int id = sampleId;
            //int id = Integer.parseInt(paramValue);
                org.hibernate.classic.Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();
                Samplesheet s = null;
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    List<Chipseq> lc = session.createQuery("SELECT a FROM Chipseq a WHERE a.sampleId = " + id).list(); 
                    //List<Reagentuse> lr = session.createQuery("SELECT a FROM Reagentuse a WHERE a.sampleId = " + id).list(); 
                    
                    if(lc != null && r != null){
                        if(lc.size() == 1){
                            Chipseq chipseq = lc.get(0);
                            Reagentuse reagentuse = r;
                            s = assembleSamplesheetData2(chipseq, reagentuse);
                            tx.commit();
                            //setLoadSuccessMessage();
                        }else{
                            tx.commit();
                            //currentSamplesheet = null;
                            //setLoadFailMessage();
                        }
                    }else{
                        tx.commit();
                            //currentSamplesheet = null;
                            //setLoadFailMessage();
                        }
                    
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    tx.rollback();
                    //setLoadFailMessage();
                    //throw e; // or display error message
                    //System.out.println(e.getMessage());
                }
                finally {
                    session.close(); 
                } 
            //}else{
            //    currentAffy = null;
            //} 
                return s;
        }
    
    private Samplesheet assembleSamplesheetData2(Chipseq chipseq, Reagentuse reagentuse){
            Samplesheet currentSamplesheet = new Samplesheet();          
            currentSamplesheet.setCounter(reagentuse.getSampleId());
            currentSamplesheet.setFcid(reagentuse.getFcid());
            currentSamplesheet.setLane(reagentuse.getLane());
            currentSamplesheet.setSampleId(chipseq.getSampleName());
            //String g = 
            currentSamplesheet.setSampleRef(chipseq.getOrganism());
            String s = reagentuse.getSeqIndex();
            String indexed = "";
            if(s != null && s.length() >= 6){
                s = s.substring(0,6);
                currentSamplesheet.setIndexed(s);
                indexed = "indexed";
            }else{
                currentSamplesheet.setIndexed("");
            }
            currentSamplesheet.setDescription(chipseq.getSampleType() + "_" + chipseq.getSampleDescription());
            currentSamplesheet.setControl("N");
            currentSamplesheet.setRecipe(chipseq.getReadLength() + chipseq.getReadMode() + "_" + indexed);
            currentSamplesheet.setOperator(reagentuse.getOperator());
            currentSamplesheet.setSampleProject(chipseq.getUserLogin());
            currentSamplesheet.setDate(reagentuse.getRunDate());
            currentSamplesheet.setInstrument(chipseq.getInstrument());
            
            //setCurrentSamplesheetData();
            return currentSamplesheet;
            
        }
    
    public String saveNewSamplesheet(Samplesheet ss){
            org.hibernate.classic.Session session = it.iit.genomics.cru.crudb.HibernateUtil.getSessionFactory().openSession();     
            StringBuilder sb = new StringBuilder();
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.save(ss);
                    tx.commit();
                    sb.append("New Samplesheet saved successfully");
                    sb.append("New Samplesheet saved successfully");
                    
                }
                catch (RuntimeException e) {
                    tx.rollback();
                   
                    sb.append("New Samplesheet submission failed.");
                    System.out.println("New Samplesheet submission failed.");
                    //throw e; // or display error message
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                finally {
                    session.close(); 
                } 
                return sb.toString();
        }
    //end submit to LIMS1
    */
    //**********************************************************************//
    
   
    
    /**
    * Finds the non-redundant list of samples to be processed.
    *
    * @author Heiko Muller
    * @param String[][] data - the uploaded run form
    * @return ArrayList<Integer> - th list of sample ids to be processed
    * @since 1.0
    */
    private ArrayList<Integer> findNonredundantListOfSampleIds(String[][] data){
        ArrayList<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < data.length; i++) {
                if (!data[i][0].startsWith("Lane")) {
                    Integer in = Integer.parseInt(data[i][1]);
                    if(!l.contains(in)){
                        l.add(in);
                    }
                }        
        }
        return l;    
    }
    
    /**
    * Finds the the lanes where this sample is run.
    *
    * @author Heiko Muller
    * @param String[][] data - the uploaded run form
    * @param String id - the sample id of the sample
    * @return ArrayList<Integer> - the list of lanes where this sample is run
    * @since 1.0
    */
    private ArrayList<Integer> findLanesForSample(String[][] data, String id){
        ArrayList<Integer> l = new ArrayList<Integer>();
        for (int i = 0; i < data.length; i++) {
                if (!data[i][0].startsWith("Lane") && id.equals(data[i][1])) {
                    Integer in = Integer.parseInt(data[i][0]);
                    if(!l.contains(in)){
                        l.add(in);
                    }
                }        
        }
        return l;    
    }
    
    /**
    * Finds the first occurrence of this sample in the uploaded run form.
    *
    * @author Heiko Muller
    * @param String[][] data - the uploaded run form
    * @param String id - the sample id of the sample
    * @return String[] - the row where this sample first occurs
    * @since 1.0
    */
    private String[] findFirstSampleOccurrence(String[][] data, String id){        
        for (int i = 0; i < data.length; i++) {
                if (!data[i][0].startsWith("Lane") && id.equals(data[i][1])) {
                    return data[i];
                }        
        }
        return null;    
    }

    /*
    * Sets current sample run data.
    *
    * @author Heiko Muller
    * @param s - a row of the uploaded request csv file
    * @return SampleRun
    * @since 1.0
    
    private SampleRun setSampleRun(String[] s, StringBuilder error) {
        //0 Lane	
        //1 ID	
        //2 UserName	
        //3 Flowcell	
        //4 Cluster gen	
        //5 Sequencing	
        //6 Run Date	
        //7 Barcode	
        //8 SampleName
        SampleRun sr = new SampleRun();
        try {  
            Integer id = Integer.parseInt(s[1]);
            Sample samp = SampleHelper.getSampleByID(id);
            if(samp == null){
                //sample not found in database
                error.append(id + ": " + "sample not found in database");
                return null;
            }
            samp = testIndex(samp, s[7]);
            if(samp == null){
                //this happens when a conflict between database record and uploaded index is found;
                error.append(id + ": " + "a conflict between database record and uploaded index is found");
                return null;
            }
            
            Lane l = new Lane();
            l.setLaneName(s[0]);
            HashSet<Lane> hsl = new HashSet<Lane>();
            hsl.add(l);
            sr.setLanes(hsl);

            
            sr.setUser(operator);
            sr.setsample(samp);
            SampleRunId sid = new SampleRunId();
            sid.setRunId(runID);
            sid.setSamId(samp.getId());
            sr.setId(sid);

            sr.setFlowcell(s[3]);
            sr.setControl(false);

            Reagent cg = testReagent(s[4], "Cluster generation");
            sr.setReagentByClustergenerationReagentCode(cg);
            //Reagent sp = testReagent(s[4], "Sequencing");
            //sr.setReagentBySamplePrepReagentCode(sp);
            Reagent sq = testReagent(s[5], "Sequencing");
            sr.setReagentBySequencingReagentCode(sq);
            String runfoldername = filename.replace(".csv", "");
            sr.setRunFolder(runfoldername);          
            
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            error.append("Unhandled error");
            return null;
        }

        return sr;
    }
    */
    /**
    * Sets current sample run data.
    *
    * @author Heiko Muller
    * @param s - a row of the uploaded request csv file
    * @return SampleRun
    * @since 1.0
    */
    private SampleRun setSampleRun(String[] s, StringBuilder error, ArrayList<Integer> lanes) {
        //0 Lane	
        //1 ID	
        //2 UserName	
        //3 Flowcell	
        //4 Cluster gen	
        //5 Sequencing	
        //6 Run Date	
        //7 Barcode	
        //8 SampleName
        SampleRun sr = new SampleRun();
        try {  
            Integer id = Integer.parseInt(s[1]);
            Sample samp = SampleHelper.getSampleByID(id);
            if(samp == null){
                //sample not found in database
                error.append(id + ": " + "sample not found in database");
                return null;
            }
            
            //index testing inactivated
            //samp = testIndex(samp, s[7]);
            //if(samp == null){
            //    //this happens when a conflict between database record and uploaded index is found;
            //    error.append(id + ": " + "a conflict between database record and uploaded index is found");
            //    return null;
            //}
            
            
            
            HashSet<Lane> hsl = new HashSet<Lane>();
            for(Integer lane : lanes){
                Lane l = new Lane();
                l.setLaneName(lane.toString());
                hsl.add(l);
            }            
            sr.setLanes(hsl);

            
            sr.setUser(operator);
            sr.setsample(samp);
            SampleRunId sid = new SampleRunId();
            //sid.setRunId(runID);
            //sid.setRunId(Integer.parseInt(s[9]));
            sid.setRunId(runID);
            sid.setSamId(samp.getId());
            sr.setId(sid);

            sr.setFlowcell(s[3]);
            sr.setControl(false);

            Reagent cg = testReagent(s[4], "Cluster generation");
            sr.setReagentByClustergenerationReagentCode(cg);
            //Reagent sp = testReagent(s[4], "Sequencing");
            //sr.setReagentBySamplePrepReagentCode(sp);
            Reagent sq = testReagent(s[5], "Sequencing");
            sr.setReagentBySequencingReagentCode(sq);
            String runfoldername = filename.replace(".csv", "");
            sr.setRunFolder(runfoldername);          
            
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            error.append("Unhandled error");
            return null;
        }

        return sr;
    }
    
    /**
    * Tests if reagent is found in database. If not, it is added with limited information.
    *
    * @author Heiko Muller
    * @param reagent
    * @param application - the uploaded sequencing index
    * @return Reagent
    * @since 1.0
    */
    private Reagent testReagent(String reagent, String application){
        Reagent result = null;
        
        if(reagent == null || reagent.length() == 0){
            //uploaded field for reagent is empty
            result = new Reagent();
            result.setApplication(application);
            result.setReagentBarCode("unknown");
            result.setCatalogueNumber("unknown");
            result.setComments("Reagent added automatically by LIMS. Please add missing information.");
            result.setCostCenter("unknown");
            result.setReceptionDate(new Date(System.currentTimeMillis()));
            result.setExpirationDate(new Date(System.currentTimeMillis()));
            result.setInstitute("unknown");
            result.setPrice(0);
            result.setSupportedReactions(1000);
            result.setUserByOperatorUserId(operator);
            result.setUserByOwnerId(operator);
            ReagentHelper.saveOrUpdateReagent(result);
        }
        result = ReagentHelper.getReagentsByBarcode(reagent);
        if(result != null){
            //reagent was found
            return result;
        }else{
            //indicated reagent is missing from database, add it
            result = new Reagent();
            result.setApplication(application);
            result.setReagentBarCode(reagent);
            result.setCatalogueNumber("unknown");
            result.setComments("Reagent added automatically by LIMS. Please add missing information.");
            result.setCostCenter("unknown");
            result.setReceptionDate(new Date(System.currentTimeMillis()));
            result.setExpirationDate(new Date(System.currentTimeMillis()));
            result.setInstitute("unknown");
            result.setPrice(0);
            result.setSupportedReactions(1000);
            result.setUserByOperatorUserId(operator);
            result.setUserByOwnerId(operator);
            ReagentHelper.saveOrUpdateReagent(result);
            
        }
        return result;
        
    
    }
    
    /**
    * Tests correctness of sequencing index information.
    *
    * @author Heiko Muller
    * @param sample
    * @param index - the uploaded sequencing index
    * @return Sample
    * @since 1.0
    */
    private Sample testIndex(Sample sample, String index){
        if(index == null){
            index = new String("none");
        }
        if(index != null && index.length() == 0){
            index = new String("none");
        }
        
        //first, remove any whitespace from the uploaded index
        index = index.trim();
        if(index.length() > 17){
            //the index field in the uploaded form does not contain a sequencing index
            NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "index too long " + index, "index too long " + index);
            return null;
        }
        
        //second, make sure uploaded index seqquences are in upper case letters, except for "none"
        if(!index.equals("none")){
            index = index.toUpperCase();
        }
        
        //Retrieve the index sequence of the sample
        SequencingIndex idx = sample.getSequencingIndexes();
        
        //cases to handle:
        
        //case 1:
        //uploaded index and sample index may be the same.
        //In that case just return.
        if(idx != null && idx.getIndex().equals(index)){
            return sample;
        }else if(idx == null && index.equals("none")){
            //this should never happen because all samples have at least the null index "none"
            SequencingIndex idex = SampleHelper.getSequencingIndexIdBySequence(index); 
            if(idex == null){
                //the index sequence wasn't found in the database.
                NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "unknown index " + index, "unknown index " + index);
                return null;
            }
            sample.setSequencingIndexes(idex);
            (new SampleHelper()).updateSample(sample);
            return sample;
        }
           
        //case 2: 
        //idx may be null/none because the facility added the index to the sample without updating the database record.
        //In that case update the database record for sample using the index sequence in the uploaded run form.
        if(idx != null && idx.getIndex().equals("none") && index.length() <= 17 ){
            //System.out.println("updating sample index using uploaded sample run form information. Using " + index);
            SequencingIndex idex = SampleHelper.getSequencingIndexIdBySequence(index);  
            if(idex == null){
                //the index sequence wasn't found in the database.
                NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "unknown index " + index, "unknown index " + index);
                return null;
            }
            sample.setSequencingIndexes(idex);
            (new SampleHelper()).updateSample(sample);
            return sample;
        }else if(idx == null && index.length() <= 17){
            //this should never happen because all samples have at least the null index "none"
            SequencingIndex idex = SampleHelper.getSequencingIndexIdBySequence(index);   
            if(idex == null){
                //the index sequence wasn't found in the database.
                NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "unknown index " + index, "unknown index " + index);
                return null;
            }
            sample.setSequencingIndexes(idex);
            (new SampleHelper()).updateSample(sample);
            return sample;
        }
        
        //case 3: 
        //idx may differ from the uploaded index. In that case return null and alert the staff to resolve the conflict.
        //Either they need to update the database record for the sample because a user reported a mistake or the staff made a mistake in the run form.        
        else if(idx != null && index.length() <= 17 && !idx.getIndex().equals(index)){
            NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "conflict for index " + index, "conflict for index " + index);
            return null;
        }
        NgsLimsUtility.setFailMessage(formId, "RunUploadProcessButton", "unknown index error " + index, "error for index " + index);
        return null;//this handles unforeseen cases. Alert the staff about the problem.
    }

    /**
    * Submits sample run.
    *
    * @author Heiko Muller
    * @param sr - a sample run
    * @return String
    * @since 1.0
    */
    public String submit(SampleRun sr) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            boolean firstSample = true;

            // set the id object
            SampleRunId sid = new SampleRunId();
            sid.setRunId(runID);
            sid.setSamId(sr.getsamId());




            // set the sample to queued and (re)set sequencing index
            Sample sample = (Sample) session.load(Sample.class, sr.getsamId());
            sample.setStatus("running");
            session.update(sample);
            session.save(sr);
            
            //lane management
            Set<Lane> ls = sr.getLanes();
            Iterator it = ls.iterator();
            while(it.hasNext()){
                Lane l = (Lane)it.next();
                l.setSamplerun(sr);
                session.save(l);
            }

            // if good, try to save and commit
            session.update(sr);


            //}
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
    * Submits sample run.
    *
    * @author Heiko Muller
    * @param sr - a sample run
    * @return String
    * @since 1.0
    */
    public void submitSampleRun(SampleRun sr) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            boolean firstSample = true;

            // set the id object
            SampleRunId sid = new SampleRunId();
            sid.setRunId(runID);
            sid.setSamId(sr.getsamId());




            // set the sample to queued and (re)set sequencing index
            Sample sample = (Sample) session.load(Sample.class, sr.getsamId());
            sample.setStatus("running");
            session.update(sample);
            session.save(sr);
            
            //lane management
            Set<Lane> ls = sr.getLanes();
            Iterator it = ls.iterator();
            while(it.hasNext()){
                Lane l = (Lane)it.next();
                l.setSamplerun(sr);
                session.save(l);
            }

            // if good, try to save and commit
            session.update(sr);
            dataBean.updateRun(sr);


            //}
            tx.commit();

            NgsLimsUtility.setSuccessMessage(formId, "submission",
                    "Sample run added, close browser before submitting new sample run", " inserted correctly");
            

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            fail = fail + e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "submission", fail,
                    "Run insertion failed ");
            
        } finally {
            session.close();
        }

    }
    
    /**
    * Submits sample run.
    *
    * @author Heiko Muller
    * @param sr - a sample run
    * @return String
    * @since 1.0
    */
    public void submitSampleRun(SampleRun sr, int runid, int samid) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            boolean firstSample = true;

            // set the id object
            SampleRunId sid = new SampleRunId(runid, samid);
            //sid.setRunId(sid.getRunId());
            //sid.setSamId(sr.getsamId());
            sr.setId(sid);




            // set the sample to queued and (re)set sequencing index
            Sample sample = (Sample) session.load(Sample.class, sr.getsamId());
            sample.setStatus("running");
            session.update(sample);
            session.save(sr);
            
            //lane management
            Set<Lane> ls = sr.getLanes();
            Iterator it = ls.iterator();
            while(it.hasNext()){
                Lane l = (Lane)it.next();
                l.setSamplerun(sr);
                session.save(l);
            }

            // if good, try to save and commit
            session.update(sr);
            dataBean.updateRun(sr);

            //}
            tx.commit();

            NgsLimsUtility.setSuccessMessage(formId, "submission",
                    "Sample run added, close browser before submitting new sample run", " inserted correctly");
            

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            fail = fail + e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "submission", fail,
                    "Run insertion failed ");
            
        } finally {
            session.close();
        }

    }

    /**
    * Getter for destination.
    *
    * @author Heiko Muller
    * @return String
    * @since 1.0
    */
    public String getDestination() {
        return destination;
    }

    /**
    * Setter for destination.
    *
    * @author Heiko Muller
    * @param destination
    * @since 1.0
    */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
    * Getter for destination.
    *
    * @author Heiko Muller
    * @return String
    * @since 1.0
    */
    public String getApplicationPath() {
        return applicationPath;
    }

    /**
    * Setter for applicationPath.
    *
    * @author Heiko Muller
    * @param applicationPath
    * @since 1.0
    */
    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
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
    * @param file1
    * @since 1.0
    */
    public void setFile1(UploadedFile file1) {
        this.file1 = file1;
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
    * Setter for filename.
    *
    * @author Heiko Muller
    * @param filename
    * @since 1.0
    */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
    * Getter for filecontent.
    *
    * @author Heiko Muller
    * @return String
    * @since 1.0
    */
    public String getFilecontent() {
        return filecontent;
    }

    /**
    * Setter for filecontent.
    *
    * @author Heiko Muller
    * @param filecontent
    * @since 1.0
    */
    public void setFilecontent(String filecontent) {
        this.filecontent = filecontent;
    }
    
    public int getProgress(){
        return progress;
    }
}
