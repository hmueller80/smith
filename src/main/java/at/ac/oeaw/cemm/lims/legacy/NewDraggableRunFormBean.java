package at.ac.oeaw.cemm.lims.legacy;


import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.legacy.mindex.Mindex;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
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

    @Inject private ServiceFactory services;
    @Inject private DTOFactory dtoFactory;

    private List<SampleDTO> samples;
    private List<SampleDTO> droppedSamples;
    private List<String> currentindices;
    private SampleDTO selectedSample;
    private String laserbalance1 = "";
    private String mismatchesL1; 
    private Mindex mindex;
    private String recipe = "Single read : 30 mio reads : one fiths of a lane : 50 bases";
    private List<String> recipes;
    private String flowcell = "flowcell barcode";
    private int lane = 1;
    private SampleDataModel sampleList;
    private SampleDataModel droppedSampleList;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;

 

    @PostConstruct
    public void init() {
        
        samples = services.getSampleService().getSamplesByStatus(SampleDTO.status_requested);
        sampleList = new SampleDataModel(samples);
        droppedSamples = new ArrayList<>();
        droppedSampleList = new SampleDataModel(droppedSamples);
        mindex = new Mindex();
        recipes = services.getSampleService().getAllLibraries();
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
      
        hash = 23 * hash + this.samples.hashCode();
        hash = 23 * hash + this.droppedSamples.hashCode();
        return hash;
    }

   
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

  
    public void onSampleDrop(DragDropEvent ddEvent) {

        SampleDTO sample = ((SampleDTO) ddEvent.getData());
        droppedSamples = services.getSampleService().getAllPooledSamples(sample);
        
        if (mindex.getProposal() != null && mindex.getProposal().length() == 6) {
            samples = mindex.bringProposalsToTopOfList(samples);
        }
    }
        
    public void removeSelectedSample() {
        System.out.println("remove selected sample called");
        if (selectedSample != null) {
            for (SampleDTO toRemove: services.getSampleService().getAllPooledSamples(selectedSample)){                
                for (Iterator<SampleDTO> iterator=droppedSamples.iterator();iterator.hasNext();){
                    SampleDTO dropped = iterator.next();
                    if (toRemove.getId().equals(dropped.getId())){
                        iterator.remove();
                    }
                }
            }
             if (mindex.getProposal() != null && mindex.getProposal().length() == 6) {
                samples = mindex.bringProposalsToTopOfList(samples);
            }
        }
    }
    

    public void clearLane(){
        samples = new ArrayList<>();               
        for (SampleDTO candidate: services.getSampleService().getSamplesByStatus(SampleDTO.status_requested)){
            if (candidate.getApplication().getApplicationName().equals(recipe)){
                samples.add(candidate);
            }
        }
        droppedSamples = new ArrayList<>();
    }
    
    /**
     * Clear lane action listener.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void submitLane(){   
        System.out.println("submitting samples. Flowcell "  + flowcell + " lane " + lane);
        
        Set<SampleRunDTO> toAdd = new HashSet<>();
        
        List<SampleRunDTO> existingRuns = services.getRunService().getRunsByFlowCell(flowcell);
        
        for (SampleDTO sampleToRun: droppedSamples){
            boolean runExists = false;
            for (SampleRunDTO candidate:existingRuns){
                if (candidate.getSample().getId().equals(sampleToRun.getId())){
                    candidate.addLane(String.valueOf(lane));
                    toAdd.add(candidate);
                    runExists=true;
                }
            }
            if (!runExists){
                SampleRunDTO sampleRun = dtoFactory.getSampleRunDTO(null, 
                        sampleToRun, 
                        roleManager.getCurrentUser(),
                        flowcell, null, flowcell, false);
                sampleRun.addLane(String.valueOf(lane));
                toAdd.add(sampleRun);                
            }
        }
        
        try {
            Set<PersistedEntityReceipt> persistedSampleRuns = services.getRunService().bulkUploadRuns(toAdd,false);
        } catch (Exception ex) {
           ex.printStackTrace();
           System.out.println("Error while persisting sample runs"+ex.getMessage());
        }
    }
       
    public List<SampleDTO> getSamples() {
        return samples;
    }

 
    public List<SampleDTO> getDroppedSamples() {
        return droppedSamples;
    }

  
    public SampleDTO getSelectedSample() {
        return selectedSample;
    }

    public  void setSelectedSample(SampleDTO selectedSample) {
        this.selectedSample = selectedSample;
    }

   
    public boolean getDroppedSamplesListIsEmpty() {
        return (droppedSamples == null || droppedSamples.isEmpty());
    }

    
      
    private void findLaserBalance() {
        if (droppedSamples != null && droppedSamples.size() > 0) {
            currentindices = findSampleListIndices(droppedSamples);
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
            currentindices = findSampleListIndices(droppedSamples);
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
    
    private List<String> findSampleListIndices(List<SampleDTO> samples){
        List<String> result = new ArrayList<>();
        for(SampleDTO sample: samples){
            result.add(sample.getIndex().getIndex());              
        }        
        return result;
    }
    /**
     * On recipe change action listener.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void onRecipeChange(){
        System.out.println("onRecipeChange() called");
        droppedSamples = new ArrayList<>();
        
        samples = new ArrayList<>();               
        for (SampleDTO candidate: services.getSampleService().getSamplesByStatus(SampleDTO.status_requested)){
            if (candidate.getApplication().getApplicationName().equals(recipe)){
                samples.add(candidate);
            }
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
    private boolean hasDuplicates(List<String> indices) {
        boolean result = false;
        Collections.sort(indices);
        for (int i = 0; i < indices.size() - 1; i++) {
            String id = indices.get(i);
            if(id != null){
                String si = id.trim().toUpperCase();
                for (int j = i + 1; j < indices.size(); j++) {
                    String idx = indices.get(j);
                    if(idx != null){
                        String sj = idx.trim().toUpperCase();
                        if (si.equals(sj)) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }


    public String getLaserbalance1() {
        findLaserBalance();
        return laserbalance1;
    }


    public void setLaserbalance1(String laserbalance1) {
        this.laserbalance1 = laserbalance1;
    }


    public String getMismatchesL1() {
        findAllowedMismatches();
        return mismatchesL1;
    }


    public void setMismatchesL1(String mismatchesL1) {
        this.mismatchesL1 = mismatchesL1;
    }


    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
        
    }

    public List<String> getRecipes() {
        return recipes;
    }

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
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    
}
