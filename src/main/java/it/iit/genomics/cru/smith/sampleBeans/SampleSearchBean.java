package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
//import it.iit.genomics.cru.smith.userBeans.LoginMonitor;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)SampleSearchBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for sample search page.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "sampleSearchBean")
//@RequestScoped
//@SessionScoped
@ViewScoped
public class SampleSearchBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // data model containing the loaded samples
    private SampleDataModel sampleList;
    private Sample[] selectedSamples;
    // variable storing the information of the selected
    // run. Used only when searching for a sample to add in
    // an existing run
    private int selectedRun = -1;
    private int selectedProject = -1;
    //RunsSearchBean runsSearchBean;

    LoggedUser loggedUserBean;
    
    List<Sample> allSamples = null;
    List<Sample> loadedSamples = null;
    RoleManager roleManager;
    //LoginMonitor loginMonitor;
    DataBean dataBean;
    List<String> libraries;// = {"TS_06_L1139", "TS_05_L1138", "AK10_pool_8_L1137"};
    List<String> submissions;// = {"TS_06_L1139", "TS_05_L1138", "AK10_pool_8_L1137"};

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SampleSearchBean() {
        if(Preferences.getVerbose()){
            System.out.println("init SampleSearchBean");
        }        
        FacesContext context = FacesContext.getCurrentInstance();        
        loggedUserBean = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //loginMonitor  = ((LoginMonitor) context.getApplication().evaluateExpressionGet(context, "#{loginMonitor}", LoginMonitor.class)); 
        roleManager = ((RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class)); 
        //dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
        //loggedUserBean = new LoggedUser();
        dataBean = roleManager.getDataBean();
        if(dataBean == null){
            System.out.println("SampleSearchBean dataBean == null");
        }else{
            System.out.println("SampleSearchBean dataBean not null");
        }
        
        //FacesContext context = FacesContext.getCurrentInstance();
        //runsSearchBean = (RunsSearchBean) context.getApplication().evaluateExpressionGet(context, "#{runsSearchBean}", RunsSearchBean.class);
        System.out.println("initSampleList start");
        if (loggedUserBean != null) {
            //System.out.println("logged as guest");
            //System.out.println("init Sample list");
            this.initSampleList();
            //System.out.println("init Sample list...done");
        } else {
            //System.out.println("logged as null");
        }
        //System.out.println("init Sample list...done");
        if(Preferences.getVerbose()){
            System.out.println("init SampleSearchBean...done");
        }
        libraries = dataBean.getLibraries();
        submissions = dataBean.getSubmissions();
    }

    //public void udDate(){
    //    this.initSampleList();
    //}
    
    /**
    * Setter for loggedUserBean.
    *
    * @author Francesco Venco
    * @param loggedUserBean
    * @since 1.0
    */
    public void setLoggedUserBean(LoggedUser loggedUserBean) {
        this.loggedUserBean = loggedUserBean;
    }

    //public void updateLoggedUserBean(){
    //    FacesContext context = FacesContext.getCurrentInstance(); 
    //    loggedUserBean = (LoggedUserBean) context.getApplication().evaluateExpressionGet(context, "#{loggedUserBean}", LoggedUserBean.class);  
    //}
    
    /**
    * Getter for sampleList.
    *
    * @author Francesco Venco
    * @return SampleDataModel
    * @since 1.0
    */
    public SampleDataModel getSampleList() {
        //updateLoggedUserBean();
        return this.sampleList;
    }
    
    /**
    * Inits sampleList.
    *
    * @author Heiko Muller
    * @since 1.0
    */
    public void initSampleList() {
        // find if coming from the sample search for adding sample tu run
        if(Preferences.getVerbose()){
            System.out.println("init initSampleList");
        }
        //System.out.println("SampleHelper.getSampleList()");
        
        
        allSamples = dataBean.getAllSamples();
        System.out.println("number of all samples " + allSamples.size());
        //allSamples = SampleHelper.getSampleList();
        //allSamples = SampleHelper.getLazySampleList();
        //if(allSamples.size() == dataBean.getAllSamples().size()){
        //    allSamples = dataBean.getAllSamples();
        //}else{
        //    dataBean.updateSamples();
        //    allSamples = dataBean.getAllSamples();
        //}
        //System.out.println("SampleHelper.getSampleList() done");
        //System.out.println(allSamples.get(0).getId());
        System.out.println("initSampleList: " + allSamples.size() + " samples loaded.");
        loadedSamples = new ArrayList<Sample>();
        if(!(roleManager.isAdmin() || roleManager.isTechnician())){
            System.out.println("loading role dependent samples ");
            for(Sample s : allSamples){
                if(roleManager.hasLoadPermission(s)){
                    //System.out.println("loading sample " + s.getName());
                    loadedSamples.add(s);
                }
            }
        }else{
            System.out.println("loading all samples for Admin ");
            for(Sample s : allSamples){
                //if(roleManager.hasLoadPermission(s)){
                    
                    loadedSamples.add(s);
                //}
            }
        }
        //System.out.println("setting SampleDataModel ");
        this.sampleList = new SampleDataModel(loadedSamples);
        
        if(Preferences.getVerbose()){
            System.out.println("initSampleList...done");
        }
        //System.out.println("initSampleList...done");
        
        

    }

    /**
    * Inits sampleList.
    *
    * @author Heiko Muller
    * @since 1.0
    */
    public void initSampleList1() {
        // find if coming from the sample search for adding sample tu run
        //System.out.println("initSampleList");
        User u = loggedUserBean.getLoggedUser();
        if(u != null){
            if(u.getUserRole().equals(Preferences.ROLE_USER)){
                this.sampleList = new SampleDataModel(SampleHelper.getSampleList(u.getId()));
            }else if(u.getUserRole().equals(Preferences.ROLE_GROUPLEADER)){
                this.sampleList = new SampleDataModel(SampleHelper.getSampleListByGroupId(u.getId()));
            }else if( u.getUserRole().equals(Preferences.ROLE_TECHNICIAN) || u.getUserRole().equals(Preferences.ROLE_ADMIN)){
                this.sampleList = new SampleDataModel(SampleHelper.getSampleList());   
            }
        }else{
            this.sampleList = new SampleDataModel(new ArrayList<Sample>());
        }
        //System.out.println("initSampleList...done");
        

    }
    
    /**
    * Inits sampleList.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void initSampleList0() {
        // find if coming from the sample search for adding sample tu run
        System.out.println("initSampleList");
        //updateLoggedUserBean();
        FacesContext context = FacesContext.getCurrentInstance();
        String specifySamples = (String) context.getExternalContext().getRequestParameterMap().get("specifySamples");
        System.out.println("specifySamples value " + specifySamples);
        String rid = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        if (rid != null) {
            selectedRun = Integer.parseInt(rid);
        }

        String pid = (String) context.getExternalContext().getRequestParameterMap().get("pid");
        if (pid != null) {
            selectedProject = Integer.parseInt(pid);
        }

        //TODO risolvere 'sto casino...
        if (specifySamples != null && specifySamples.equals(Sample.status_queued)) {
            this.sampleList = new SampleDataModel(SampleHelper.getQueuedSampleList());
            System.out.println("returning queued list of samples");
            return;
        } else if (specifySamples != null && specifySamples.equals(Sample.status_requested)) {
            //List<Sample> ls = SampleHelper.getRequestedSampleList();
            //System.out.println("number of requested samples " + ls.size());
            this.sampleList = new SampleDataModel(SampleHelper.getRequestedSampleList());
            System.out.println("returning requested list of samples");
            System.out.println("sampleList number of samples " + sampleList.getRowCount());
            return;
        }

        // retrieve all the samples if user is admin or a technician
        if (loggedUserBean.getIsTech() || loggedUserBean.getIsAdmin()) {
            this.sampleList = new SampleDataModel(SampleHelper.getSampleList());
        } // retrieve all the samples of the user's group and
        // for all collaborations
        else {
            List<Collaboration> cols = new ArrayList(loggedUserBean.getCollaborations());
            List<Sample> temp = new ArrayList<Sample>();
            for (Collaboration c : cols) {
                List<Sample> projsam = new ArrayList(c.getProject().getSamples());
                for (Sample s : projsam) {
                    if (s.getUser().getPi() != loggedUserBean.getPi()) {
                        temp.add(s);
                    }
                }
            }

            temp.addAll(SampleHelper.getSampleListByGroupId(loggedUserBean.getPi()));
            this.sampleList = new SampleDataModel(temp);
        }

    }

    /**
    * Tests if logged user has permission to add new samples.
    * Used for conditional rendering of the "New Sample" button.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean getNewSamplePermission() {
        //updateLoggedUserBean();
        if (loggedUserBean.getIsTech() || loggedUserBean.getIsGuest()) {
            return false;
        } else {
            return true;
        }

    }

    /**
    * Sets state of selected samples from requested to queued.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void setSelectedToQueued() {

        System.out.println("setting requested to queued");
        System.out.println("selected samples " + selectedSamples.length);
        //updateLoggedUserBean();
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        //int runid = runsSearchBean.getNewRunId();
        int runid = RunHelper.getNextRunId();
        try {
            System.out.println("begin transaction ");
            tx = session.beginTransaction();

            for (Sample selected : selectedSamples) {
                System.out.println("processing sample " + selected.getId());
                Sample s = (Sample) session.load(Sample.class, selected.getId());
                //Reagent cg = (Reagent) session.load(Reagent.class, "aaccsdd");
                //Reagent sp = (Reagent) session.load(Reagent.class, "aasdff");
                //Reagent seq = (Reagent) session.load(Reagent.class, "aaxe");

                Reagent cg = (Reagent) session.load(Reagent.class, "RGT2665056");
                Reagent sp = (Reagent) session.load(Reagent.class, "1234567890");
                Reagent seq = (Reagent) session.load(Reagent.class, "RGT2672923");
                if (s.getStatus().equals(Sample.status_requested)) {
                    //SampleRunId rid = new SampleRunId(runid, s.getId());
                    SampleRun sr = new SampleRun(runid, s.getId());
                    s.setStatus(Sample.status_queued);
                    sr.setFlowcell("undefined");
                    sr.setReagentByClustergenerationReagentCode(cg);
                    sr.setReagentBySamplePrepReagentCode(sp);
                    sr.setReagentBySequencingReagentCode(seq);
                    sr.setRunFolder("undefined");
                    sr.setUser(loggedUserBean.getLoggedUser());

                    session.save(s);
                    session.save(sr);
                    dataBean.updateSample(s);
                    dataBean.updateRun(sr);
                    System.out.println("saving queued sample " + s.getId());
                }
            }

            tx.commit();
            this.initSampleList();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();

        } finally {
            session.close();
        }

    }

    /**
    * Tests if bean method calling works correctly.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void test() {

        System.out.println("SampleSearchBean called test()");

    }

    /**
    * Tests if logged user has permission to add flow cells.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean hasAddFlowcellPermission() {
        System.out.println("hasAddFlowcellPermission");
        //updateLoggedUserBean();
        boolean val = loggedUserBean.getIsTech() || loggedUserBean.getIsAdmin();
        System.out.println("hasAddFlowcellPermission " + val);
        return val;
    }

    /**
    * Getter for selectedRun.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getSelectedRun() {
        return this.selectedRun;
    }

    /**
    * Getter for selectedRun as String.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSelectedRunString() {
        if (selectedRun == -1) {
            return "";
        }
        return "" + selectedRun;
    }

    /**
    * Getter for selectedProject.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getSelectedProject() {
        return this.selectedProject;
    }

    /**
    * Getter for selectedSamples.
    *
    * @author Francesco Venco
    * @return Sample[]
    * @since 1.0
    */
    public Sample[] getSelectedSamples() {
        return this.selectedSamples;
    }

    /**
    * Setter for selectedSamples.
    *
    * @author Francesco Venco
    * @param selectedSamples
    * @since 1.0
    */
    public void setSelectedSamples(Sample[] selectedSamples) {
        //updateLoggedUserBean();
        this.selectedSamples = selectedSamples;
    }
    
    public List<String> getLibraries(){
        return libraries;
    }
    
    public List<String> getSubmissions(){ 
        return submissions;   
    }

}
