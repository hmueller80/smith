package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.mindex.Mindex;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.MultipleRequest;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SampleRunId;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.reagentsBeans.ReagentHelper;
import it.iit.genomics.cru.smith.sampleBeans.SampleDataModel;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;
//import it.iit.genomics.cru.smith.userBeans.LoginMonitor;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext; 
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.DragDropEvent;


/**
 * @(#)NewDraggableRunFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for drag and drop composition of a lane with Mindex support.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newDraggableRunFormBean")
@SessionScoped
public class NewDraggableRunFormBean implements Serializable {

    //http://www.primefaces.org/showcase/ui/dnd/dataTable.xhtml
    //@ManagedProperty("#{sampleService}")
    private SampleService service;
    private List<Sample> samples;
    private List<Sample> droppedSamples;
    private Sample selectedSample;
    private boolean droppedSampleListIsEmpty = true;
    private String laserbalance1 = "";
    private String mismatchesL1; 
    private Mindex mindex;
    private ArrayList<SequencingIndex> currentindices;
    private String recipe = "Single read : 30 mio reads : one fiths of a lane : 50 bases";
    private List<String> recipes;
    private String flowcell = "flowcell barcode";
    private String clustergeneration = "cluster barcode";
    private String sequencing = "sequencing barcode";
    private int lane = 1;
    private RoleManager roleManager;
    private SampleDataModel sampleList;
    private SampleDataModel droppedSampleList;

    /**
     * init
     *
     * @author Heiko Muller
     * @since 1.0
     */
    @PostConstruct
    public void init() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        service = (SampleService) context.getApplication().evaluateExpressionGet(context, "#{sampleService}", SampleService.class);
        //LoginMonitor lm = (LoginMonitor) context.getApplication().evaluateExpressionGet(context, "#{loginMonitor}", LoginMonitor.class);
        roleManager = (RoleManager) context.getApplication().evaluateExpressionGet(context, "#{roleManager}", RoleManager.class);
        //samples = service.loadRequestedSamples();
        //samples = service.loadRequestedAndQueuedSamplesByMatchingApplication(recipe);
        //if(samples == null){
        //    samples = service.loadRequestedSamples();
        //}
        samples = service.loadRequestedSamples();
        sampleList = new SampleDataModel(samples);
        if(Preferences.getVerbose()){
            System.out.println("samples loaded " + samples.size());
        }
        
        droppedSamples = new ArrayList<Sample>();
        droppedSampleList = new SampleDataModel(droppedSamples);
        mindex = new Mindex();
        recipes = service.loadRecipeNames();
        //droppedSamples.add(samples.get(0));
        //selectedSample = samples.get(0);
    }

    /**
     * Bean constructor
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public NewDraggableRunFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init NewDraggableRunFormBean");
        }
        //init();
    }

    /**
     * Getter for hashCode
     *
     * @author Heiko Muller
     * @return int
     * @since 1.0
     */
    @Override
    public int hashCode() {
        int hash = 3;
        //hash = 23 * hash + Objects.hashCode(this.samples);
        //hash = 23 * hash + Objects.hashCode(this.droppedSamples);
        
        hash = 23 * hash + this.samples.hashCode();
        hash = 23 * hash + this.droppedSamples.hashCode();
        return hash;
    }

    /**
     * equals
     *
     * @author Heiko Muller
     * @return boolean
     * @since 1.0
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NewDraggableRunFormBean other = (NewDraggableRunFormBean) obj;
        if (!this.samples.equals(other.samples)) {
            return false;
        }
        if (!this.droppedSamples.equals(other.droppedSamples)) {
            return false;
        }
        return true;
    }

    /**
     * On sample drop action listener.
     *
     * @author Heiko Muller
     * @param ddEvent
     * @since 1.0
     */
    public void onSampleDrop(DragDropEvent ddEvent) {

        Sample sample = ((Sample) ddEvent.getData());
        //Set<MultipleRequest> mrs = SampleHelper.getMultipleRequests(sample);
        Set<Library> mrl = SampleHelper.getLibrary(sample);
        //System.out.println("onSampleDrop called");
        if (mrl.isEmpty()) {
        //if (true) {
            //if(!contains(droppedSamples, sample)){
            droppedSamples.add(sample);
            //}
            //samples.remove(sample);
        } else {
            List<Sample> pool = SampleHelper.getPoolMembers(sample);
            for (Sample s : pool) {
                //if(!contains(droppedSamples, s)){
                droppedSamples.add(s);
                //}
                //samples.remove(s);
            }
        }

        //System.out.println("Sample dropped" + sam.getId());
        //if sample is part of a sample pool, they must be managed together

        //System.out.println(mindex.getProposal());
        if (mindex.getProposal() != null && mindex.getProposal().length() == 6) {
            samples = mindex.bringProposalsToTopOfList(samples);
        }
    }
    
    /**
     * Remove selected sample action listener.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void removeSelectedSample() {
        //System.out.println("remove selected sample called");
        if (selectedSample != null) {
            Set<Library> mrs = SampleHelper.getLibrary(selectedSample);
            if (mrs.isEmpty()) {
            //if (true) {
                droppedSamples.remove(selectedSample);
                //if(!contains(samples, selectedSample)){
                //samples.add(selectedSample);
                //}
            } else {
                List<Sample> pool = SampleHelper.getPoolMembers(selectedSample);
                for (Sample s : pool) {
                    droppedSamples.remove(s);
                    //if(!contains(samples,s)){
                    //samples.add(s);
                    //}
                }
            }
            //System.out.println(droppedSamples.size() + " " + samples.size());
            
            if (mindex.getProposal() != null && mindex.getProposal().length() == 6) {
                samples = mindex.bringProposalsToTopOfList(samples);
            }
        }
    }
    
    /**
     * Clear lane action listener.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void clearLane(){
        samples = service.loadRequestedSamplesByMatchingApplication(recipe);        
        droppedSamples = new ArrayList<Sample>();
    }
    
    /**
     * Clear lane action listener.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void submitLane(){   
        System.out.println("submitting samples. Flowcell "  + flowcell + " lane " + lane);
        
        List<SampleRun> runs = RunHelper.getRunsByFCIDList(flowcell);
        int runid = RunHelper.getNextRunId();
        if(!(runs == null || runs.isEmpty())){
            runid--;
        }
        //System.out.println("runid " + runid);
        //User u = roleManager.getLoggedUser();
        //if(u == null){
        //    System.out.println("u = " + u);
        //}

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Reagent cg = ReagentHelper.getReagentsByBarcode(clustergeneration);
            if(cg == null){
                cg = ReagentHelper.getDefaultReagent();
                cg.setReagentBarCode(clustergeneration); 
                cg.setApplication("Cluster generation");   
                cg.setUserByOperatorUserId(roleManager.getLoggedUser());
                cg.setUserByOwnerId(roleManager.getLoggedUser());
                session.save(cg);
            }
            Reagent se = ReagentHelper.getReagentsByBarcode(sequencing);
            if(se == null){
                se = ReagentHelper.getDefaultReagent();
                se.setReagentBarCode(sequencing);
                se.setApplication("Sequencing");
                se.setUserByOperatorUserId(roleManager.getLoggedUser());
                se.setUserByOwnerId(roleManager.getLoggedUser());
                session.save(se);
            }
            
            
            for(Sample s : droppedSamples){
                //case 1: SampleRun does not exist
                //this is the default situation when the sample has not yet been run
                SampleRun sr = RunHelper.getSampleRun(runid, s.getId());
                if(sr == null){     
                    //System.out.println("new sample run");
                    SampleRunId sid = new SampleRunId();
                    sr = new SampleRun();
                    sid.setRunId(runid);
                    sid.setSamId(s.getId());
                    sr.setId(sid);
                    
                    sr.setFlowcell(flowcell);
                    sr.setReagentByClustergenerationReagentCode(cg);
                    sr.setReagentBySequencingReagentCode(se);

                    sr.setUser(roleManager.getLoggedUser());             
                    sr.setsample(s);

                    sr.setRunFolder(flowcell);
                    session.save(sr);
          
                    Lane newlane = new Lane();
                    newlane.setLaneName("" + lane);
                    newlane.setSamplerun(sr);
                    session.save(newlane);
                    s.setStatus("queued");
                    session.update(s);
                }
                //case 2: SampleRun exists
                //this happens when the sample has already been assigned to another lane
                else{
                    //System.out.println("adding lane to existing sample run");
                    Lane newlane = RunHelper.getLane(sr.getRunId(), sr.getsamId(), lane); 
                    //if lane is not already present, add it
                    if(newlane == null){
                        newlane = new Lane();
                        newlane.setLaneName("" + lane);
                        newlane.setSamplerun(sr);                        
                        session.save(newlane);
                        sr.setFlowcell(flowcell);
                        sr.setReagentByClustergenerationReagentCode(cg);
                        sr.setReagentBySequencingReagentCode(se);
                        sr.setUser(roleManager.getLoggedUser());             
                        sr.setsample(s);
                        sr.setRunFolder(flowcell);
                        session.saveOrUpdate(sr);
                        s.setStatus("queued");
                        session.update(s);
                    }
                }  
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            session.close();
        }
    }
    
    
    
    /**
     * Setter for service.
     *
     * @author Heiko Muller
     * @param service
     * @since 1.0
     */
    public void setService(SampleService service) {
        this.service = service;
    }

    /**
     * Getter for samples.
     *
     * @author Heiko Muller
     * @return List<Sample>
     * @since 1.0
     */
    public List<Sample> getSamples() {
        return samples;
    }

    /**
     * Getter for droppedSamples.
     *
     * @author Heiko Muller
     * @return List<Sample>
     * @since 1.0
     */
    public List<Sample> getDroppedSamples() {
        return droppedSamples;
    }

    /**
     * Getter for selectedSample.
     *
     * @author Heiko Muller
     * @return Sample
     * @since 1.0
     */
    public Sample getSelectedSample() {
        return selectedSample;
    }

    /**
     * Setter for selectedSample.
     *
     * @author Heiko Muller
     * @param selectedSample
     * @since 1.0
     */
    public void setSelectedSample(Sample selectedSample) {
        this.selectedSample = selectedSample;
    }

    /**
     * Getter for droppedSampleListIsEmpty.
     *
     * @author Heiko Muller
     * @return boolean
     * @since 1.0
     */
    public boolean getDroppedSamplesListIsEmpty() {
        if (droppedSamples == null || droppedSamples.size() == 0) {
            droppedSampleListIsEmpty = true;
        } else {
            droppedSampleListIsEmpty = false;
        }
        return droppedSampleListIsEmpty;
    }

    
    /**
     * Tests if a sample is present in a list of samples.
     *
     * @author Heiko Muller
     * @param list
     * @param s - a sample
     * @return boolean
     * @since 1.0
     */
    private boolean contains(List<Sample> list, Sample s){
        boolean result = false;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId() == s.getId()){
                result = true;
                System.out.println("duplicate found");
                break;
            }
        }
        return result;
    }

    /**
     * Finds laser balance for a set of indices run on the same lane.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    private void findLaserBalance() {
        if (droppedSamples != null && droppedSamples.size() > 0) {
            currentindices = service.findSampleListIndices(droppedSamples);
            laserbalance1 = mindex.getLaserBalance(currentindices);
        } else {
            laserbalance1 = "";
        }

    }

    /**
     * Finds the number of allowed mismatches for a set of indices.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    private void findAllowedMismatches() {
        if (droppedSamples != null && droppedSamples.size() > 0) {
            currentindices = service.findSampleListIndices(droppedSamples);
            if (hasDuplicates(currentindices)) {
                mismatchesL1 = "duplicates found";
            } else {
                int one = mindex.calculateHashSet1(currentindices);
                int two = mindex.calculateHashSet2(currentindices);
                mismatchesL1 = "0";
                if (one == 1) {
                    mismatchesL1 = "1";
                }
                if (two == 2) {
                    mismatchesL1 = "2";
                }
            }
        } else {
            mismatchesL1 = "";
        }
    }
    
    /**
     * On recipe change action listener.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void onRecipeChange(){
        System.out.println("onRecipeChange() called");
        droppedSamples = new ArrayList<Sample>();
        //samples = service.loadRequestedSamples();
        
        //samples = service.loadRequestedSamplesByApplicationID(recipe);
        samples = service.loadRequestedSamplesByMatchingApplication(recipe);
        if(samples == null){
            System.out.println("Sample loaded 0 for recipe " + recipe);
            samples = new ArrayList<Sample>();
        }
    }

    /**
     * Tests if in a list of indices there are duplicates.
     *
     * @author Heiko Muller
     * @param indices
     * @return boolean
     * @since 1.0
     */
    private boolean hasDuplicates(ArrayList<SequencingIndex> indices) {
        boolean result = false;
        for (int i = 0; i < indices.size() - 1; i++) {
            SequencingIndex id = indices.get(i);
            if(id != null){
                String si = id.getIndex().trim().toUpperCase();
                for (int j = i + 1; j < indices.size(); j++) {
                    SequencingIndex idx = indices.get(j);
                    if(idx != null){
                        String sj = idx.getIndex().trim().toUpperCase();
                        if (si.equals(sj)) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Getter for laser balance.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getLaserbalance1() {
        findLaserBalance();
        return laserbalance1;
    }

    /**
     * Setter for laserbalance1.
     *
     * @author Heiko Muller
     * @param laserbalance1
     * @since 1.0
     */
    public void setLaserbalance1(String laserbalance1) {
        this.laserbalance1 = laserbalance1;
    }

    /**
     * Getter for mismatches.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getMismatchesL1() {
        findAllowedMismatches();
        return mismatchesL1;
    }

    /**
     * Setter for mismatchesL1.
     *
     * @author Heiko Muller
     * @param mismatchesL1
     * @since 1.0
     */
    public void setMismatchesL1(String mismatchesL1) {
        this.mismatchesL1 = mismatchesL1;
    }

    /**
     * Getter for recipe.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getRecipe() {
        return recipe;
    }

    /**
     * Setter for recipe.
     *
     * @author Heiko Muller
     * @param recipe
     * @since 1.0
     */
    public void setRecipe(String recipe) {
        this.recipe = recipe;
        
    }

    /**
     * Getter for recipes.
     *
     * @author Heiko Muller
     * @return List<String>
     * @since 1.0
     */
    public List<String> getRecipes() {
        return recipes;
    }

    /**
     * Setter for recipes.
     *
     * @author Heiko Muller
     * @param recipes
     * @since 1.0
     */
    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }

    public String getFlowcell() {
        return flowcell;
    }

    public void setFlowcell(String flowcell) {
        this.flowcell = flowcell;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public String getClustergeneration() {
        return clustergeneration;
    }

    public void setClustergeneration(String clustergeneration) {
        this.clustergeneration = clustergeneration;
    }

    public String getSequencing() {
        return sequencing;
    }

    public void setSequencing(String sequencing) {
        this.sequencing = sequencing;
    }

    public SampleDataModel getSampleList() {
        return sampleList;
    }

    public void setSampleList(SampleDataModel sampleList) {
        this.sampleList = sampleList;
    }

    public SampleDataModel getDroppedSampleList() {
        return droppedSampleList;
    }

    public void setDroppedSampleList(SampleDataModel droppedSampleList) {
        this.droppedSampleList = droppedSampleList;
    }
    
    
    
    
}
