/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorException;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.ApplicationValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.SampleValidator;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.util.Levenshtein;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class SingleSampleBean implements Serializable {
    public static final Integer minimumDistanceThreshold = 3;
    
    @Inject private ServiceFactory services;
    @Inject private DTOFactory myDTOFactory;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    

    private SampleDTO currentSample = null;
    private ApplicationDTO currentApplication = null;
    private LibraryDTO currentLibrary = null;
    
    private UserDTO principalInvestigator = null;
    
    private List<String> possibleIndexes = new LinkedList<>();
    
    private boolean isNewForm = false;
    
    @PostConstruct
    public void init() {
        System.out.println("SingleSampleBean post construct");
        for (String index : services.getSampleService().getAllIndexes()) {
            possibleIndexes.add(index);
        }
        
        String sid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sid");
        
        if (sid != null) {
           Integer sampleId = Integer.parseInt(sid);
           refreshExisting(sampleId);
        }else{
            String rid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rid");
            if (rid==null || rid.trim().isEmpty()){              
                    throw new IllegalStateException("Cannot instantiate new sample without request");
            }
            refreshNew(Integer.parseInt(rid));
        }
        
        if (!isEditable()) {
            Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages("modifyPermission");
            while (messages.hasNext()) {
                messages.next();
                messages.remove();
            }
            NgsLimsUtility.setSuccessMessage("modifyPermission", null, "Sample not editable", "The sample is in status " + currentSample.getStatus());
        } else if (!getModifyPermission()) {
            Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages("modifyPermission");
            while (messages.hasNext()) {
                messages.next();
                messages.remove();
            }
            NgsLimsUtility.setSuccessMessage("modifyPermission", null, "Sample not editable", "You do not have permissions to edit this sample");
        }
      
    }

    //FOR WELDING...
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }  

    //GETTERS FOR MAIN DTOs
    public SampleDTO getCurrentSample() {
        return currentSample;
    }

    public ApplicationDTO getCurrentApplication() {
        return currentApplication;
    }

    public LibraryDTO getCurrentLibrary() {
        if (!isLibraryEditable()) {
            Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages("libraryName");
            while (messages.hasNext()) {
                messages.next();
                messages.remove();
            }
            NgsLimsUtility.setSuccessMessage("libraryName", null, "Library not editable", "The library has some running or run samples");
        }
        
        return currentLibrary;
    }

    public void setCurrentLibrary(LibraryDTO currentLibrary) {
        this.currentLibrary = currentLibrary;
        boolean containsCurrentSample = false;
        for (SampleDTO sample:  this.currentLibrary.getSamples()){
            if (currentSample.getId()!=null){
                if (currentSample.getId().equals(sample.getId())){
                    containsCurrentSample = true;
                    break;
                }
            }else{
                if (currentSample.getName().equals(sample.getName())){
                    containsCurrentSample = true;
                    break;
                }
            }
        }
        if (!containsCurrentSample){
             this.currentLibrary.addSample(currentSample);
        }
        if (!isLibraryEditable()) {
            Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages("libraryName");
             while (messages.hasNext()) {
                messages.next();
                messages.remove();
            }
            NgsLimsUtility.setSuccessMessage("libraryName", null, "Library not editable", "The library has some running or run samples");
        }
    }

    public UserDTO getPrincipalInvestigator() {
        return principalInvestigator;
    }
        
    
    //GETTERS FOR OTHER INFO
    public List<String> getPossibleIndexes() {
        return possibleIndexes;
    }

    public boolean getModifyPermission() {
        boolean returnValue = roleManager.hasSampleModifyPermission(currentSample);
        return returnValue;
    }
       
    public boolean isIsNewForm() {
        return isNewForm;
    }
    
    public boolean isLibraryEditable() {
        for (SampleDTO sample : currentLibrary.getSamples()) {
            if ((!SampleDTO.status_requested.equals(sample.getStatus())) && (!SampleDTO.status_queued.equals(sample.getStatus()))) {
                return false;
            }
        }
        return true;
    }
    
    public List<LibraryDTO> getEditableLibraries() {
        List<LibraryDTO> editableLibraries = services.getRequestService().getEditableLibrariesInRequest(currentSample.getSubmissionId());
        if (!editableLibraries.contains(currentLibrary)){
            editableLibraries.add(currentLibrary);
        }
        return editableLibraries;
    }
    
    public String getNewLibraryName() {                
       return "";
    }

    public void setNewLibraryName(String newLibraryName) {
                
         if (newLibraryName!=null && !newLibraryName.trim().isEmpty()){
            currentLibrary = myDTOFactory.getLibraryDTO(newLibraryName, null);
            currentLibrary.addSample(currentSample);
        }
    }
     

    //SPECIAL SETTERS AND GETTERS
    public String getSampleName() {          
        return currentSample.getName();
    }

    public void setSampleName(String name) {
        System.out.println("Setting sample Name");
        if (!isNewForm && !Pattern.matches(".*_S[0-9]+", name.toUpperCase())){
            name = name+"_S"+currentSample.getId();
        }
        currentSample.setName(NameFilter.legalize(name));
    }
    
    public String getSequencingIndex() {
        return currentSample.getIndex().getIndex();
    }
  
    public void setSequencingIndex(String index) {
        currentSample.setIndex(myDTOFactory.getIndexDTO(index));      
    }
    
    public String getLibraryRowClass(SampleDTO sample) {
        Integer editDistance;
        if (Objects.equals(sample.getId(), currentSample.getId())) {
            return "highlighted-sample";
        } else if (getEditDistance(sample.getIndex().getIndex()) == 0) {            
            NgsLimsUtility.setFailMessage("indexCollisionMsg", null, "Index Collision", "The index is equal to another index in the library");
            return "index-collision-red";
        } else if ((editDistance=getEditDistance(sample.getIndex().getIndex())) < minimumDistanceThreshold) {         
            NgsLimsUtility.setWarningMessage("indexCollisionMsg", null, "Index Collision", "The edit distance with another index in the library is " + editDistance);
            return "index-collision-yellow";
        }
        return null;
    }
    
    public Integer getEditDistance(String otherIndex) {
        Integer distance;
        String currentIndex = currentSample.getIndex().getIndex();
        if (otherIndex.equals(currentIndex)) {
            distance = 0;
        } else {
            distance = Levenshtein.computeLevenshteinDistance(currentIndex, otherIndex);
        }        
        return distance;
    }

    //CRUD OPs  
    public void saveSampleChanges() {
        System.out.println("Saving sample Changes...");
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            return;
        }

        persist(false);
    }
    
    public void saveLibraryChanges() {
	System.out.println("Saving Library Changes...");

        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            return;
        }

        persist(true);        
    }

    public String delete() {
        System.out.println("Deleting Sample with id " + currentSample.getId());
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            System.out.println("User has no permissions to delete this sample");
            return null;
        }

        if (isEditable()) {
            try {
                services.getSampleService().deleteSample(currentSample);
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage(null, null,
                        "Error in DB while deleting " + currentSample.getId(), ex.getMessage());
                System.out.println("Error in DB " + ex.getMessage());

                return null;
            }
        } else {
            NgsLimsUtility.setFailMessage(null, null,
                    "Error deleting " + currentSample.getId(), "Only samples with status requested can be deleted.");
            System.out.println("Error due to sample status");

            return null;
        }

        System.out.println("Deletion success");

        return "sampleDeleted_1?sid="+currentSample.getId()+"&activeMenu=1&faces-redirect=true";
    }
    
    public boolean isEditable() {
        return currentSample.getStatus().equals(SampleDTO.status_requested) || currentSample.getStatus().equals(SampleDTO.status_queued);
    }

    private boolean persist(boolean library) {
        try {
            boolean checkLibrary = false;
            String oldLibraryName = null;
            SampleDTO sampleToPersist;
            
            if (library ) {
                if (isNewForm){
                    return false;
                }
                sampleToPersist = services.getSampleService().getFullSampleById(currentSample.getId());
                oldLibraryName = sampleToPersist.getLibraryName();

                ApplicationValidator appValidator = new ApplicationValidator(currentApplication);
                ApplicationDTO appToPersist = appValidator.getValidatedObject();
                sampleToPersist.setApplication(appToPersist);
                sampleToPersist.setExperimentName(appToPersist.getApplicationName());
                sampleToPersist.setIndex(currentSample.getIndex());
                        
                if (!oldLibraryName.equals(currentLibrary.getName())) {
                    checkLibrary = true;
                    sampleToPersist.setLibraryName(this.currentLibrary.getName());
                }

                for (ValidatorMessage message : appValidator.getMessages()) {
                    if (message.getType().equals(ValidatorSeverity.WARNING)) {
                        NgsLimsUtility.setWarningMessage(null, null, message.getSummary(), message.getDescription());
                        System.out.println("WARNING: " + message.getSummary() + " Description: " + message.getDescription());
                    }
                }

            } else {
                SampleValidator sampValidator = new SampleValidator(currentSample, myDTOFactory);
                sampleToPersist = sampValidator.getValidatedObject();

                for (ValidatorMessage message : sampValidator.getMessages()) {
                    if (message.getType().equals(ValidatorSeverity.WARNING)) {
                        NgsLimsUtility.setWarningMessage(null, null, message.getSummary(), message.getDescription());
                        System.out.println("WARNING: " + message.getSummary() + " Description: " + message.getDescription());
                    }
                }
            }
      
            PersistedEntityReceipt receipt = services.getSampleService().saveOrUpdateSample(sampleToPersist, isNewForm);
            if (checkLibrary && oldLibraryName!=null){
                services.getRequestService().deleteLibraryIfEmpty(oldLibraryName);
            }
            
            //RESTORE STATUS
            refreshExisting(receipt.getId());

            NgsLimsUtility.setSuccessMessage(null, null,
                    "Sample with id " + receipt.getId() + " updated successfully",
                    "Updated sample with id " + receipt.getId() + " and name " + receipt.getEntityName());
            System.out.println("SUCCESS: " + "Updated sample with id " + receipt.getId() + " and name " + receipt.getEntityName());
            return true;

        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            for (ValidatorMessage message : e.getPayload()) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage(null, null, message.getSummary(), message.getDescription());
                    System.out.println("FATAL: " + message.getSummary() + " Description: " + message.getDescription());
                }
            }
        } catch (Exception e) {
            NgsLimsUtility.setFailMessage(null, null, "Error while persisting sample", e.getMessage());
            System.out.println("FATAL: exception " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    public void refresh() {
        if (isNewForm) {
            refreshNew(currentSample.getSubmissionId());
        } else {
            refreshExisting(currentSample.getId());
        }
        
    }
    
    private void refreshExisting(Integer sampleId) {
        this.currentSample = services.getSampleService().getFullSampleById(sampleId);
        this.currentLibrary = services.getRequestService().getLibraryByName(currentSample.getLibraryName());
        this.currentApplication = currentSample.getApplication();
        this.principalInvestigator = services.getUserService().getUserByID(currentSample.getUser().getPi());
        isNewForm = false;

    }

    private void refreshNew(Integer rid) {
        RequestDTO existingRequest = services.getRequestService().getMinimalRequestById(rid);
        if (existingRequest==null){
            throw new IllegalStateException("Request with id "+rid+" not existing");
        }
        UserDTO requestor = existingRequest.getRequestor();
        principalInvestigator = services.getUserService().getUserByID(requestor.getPi());

        currentSample = myDTOFactory.getSampleDTO(null);
        currentSample.setUser(requestor);
        currentSample.setCostcenter(principalInvestigator.getUserName());
        currentSample.setSubmissionId(rid);               
        currentSample.setStatus(SampleDTO.status_requested);
        currentSample.setRequestDate(new Date(System.currentTimeMillis()));

        currentLibrary = myDTOFactory.getLibraryDTO("UNDEFINED", null);
        currentLibrary.addSample(currentSample);
        currentSample.setLibraryName(currentLibrary.getName());
        currentSample.setIndex(myDTOFactory.getIndexDTO("none"));
        currentApplication = myDTOFactory.getApplicationDTO(ApplicationDTO.DNA_SEQ);
        currentSample.setApplication(currentApplication);
        currentSample.setType("");
        isNewForm = true;
    }
    
}
