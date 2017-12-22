/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.SampleDTOValidator;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.LibraryValidator;
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
            String uid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uid");
            if (uid == null || uid.trim().isEmpty()) {
                throw new IllegalStateException("Cannot instantiate new sample without requestor");
            }

            refreshNew(Integer.parseInt(rid),uid);
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
                
        if (!isLibraryEditable()) {
            Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages("libraryName");
             while (messages.hasNext()) {
                messages.next();
                messages.remove();
            }
            NgsLimsUtility.setSuccessMessage("libraryName", null, "Library not editable", "The library has some running or run samples");
        }else{
            insertSampleInCurrentLibrary(currentSample);
        }
    }
    
    private void insertSampleInCurrentLibrary(SampleDTO sample){
        Iterator sampleIterator = currentLibrary.getSamples().iterator();

        while (sampleIterator.hasNext()) {
            SampleDTO existingSample = (SampleDTO) sampleIterator.next();
            if (sample.getId() != null) {
                if (existingSample.getId().equals(sample.getId())) {
                    sampleIterator.remove();
                    break;
                }
            } else {
                if (existingSample.getName().equals(sample.getName())) {
                    sampleIterator.remove();
                    break;
                }
            }
        }

        this.currentLibrary.addSample(sample);
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
        if (!isNewForm && !Pattern.matches(".*_S[0-9]+", name.toUpperCase())){
            name = name+"_S"+currentSample.getId();
        }
        currentSample.setName(NameFilter.legalizeSampleName(name));
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
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            return;
        }

        persist(false);
    }
    
    public void saveLibraryChanges() {

        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            return;
        }

        persist(true);        
    }

    public String delete() {
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
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
            ValidationStatus validation = new ValidationStatus();
            String oldLibraryName = null;
            SampleDTO sampleToPersist;
            
            SampleDTOValidator sampleValidator = new SampleDTOValidator();
            
            if (library ) {
                if (isNewForm){
                    return false;
                }
                sampleToPersist = services.getSampleService().getFullSampleById(currentSample.getId());
                oldLibraryName = sampleToPersist.getLibraryName();

                sampleToPersist.setApplication(currentApplication);
                sampleToPersist.setIndex(currentSample.getIndex());
                        
                if (!oldLibraryName.equals(currentLibrary.getName())) {
                    checkLibrary = true;
                    sampleToPersist.setLibraryName(this.currentLibrary.getName());
                }
                
                insertSampleInCurrentLibrary(sampleToPersist);
                
                LibraryValidator libValidator = new LibraryValidator(sampleValidator,false );                
                validation.merge(libValidator.isValid(currentLibrary));
            } else {
                sampleToPersist = currentSample;
            }
            
            validation.merge(sampleValidator.isValid(sampleToPersist));
            
            for (ValidatorMessage message : validation.getValidationMessages()) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage(null, null, message.getSummary(), message.getDescription());
                } else if (message.getType().equals(ValidatorSeverity.WARNING)) {
                    NgsLimsUtility.setWarningMessage(null, null, message.getSummary(), message.getDescription());
                }
            }
            
            if (validation.isValid()){
                PersistedEntityReceipt receipt = services.getSampleService().saveOrUpdateSample(sampleToPersist, isNewForm);
                if (checkLibrary && oldLibraryName != null) {
                    services.getRequestService().deleteLibraryIfEmpty(oldLibraryName);
                }

                //RESTORE STATUS
                refreshExisting(receipt.getId());

                NgsLimsUtility.setSuccessMessage(null, null,
                        "Sample with id " + receipt.getId() + " updated successfully",
                        "Updated sample with id " + receipt.getId() + " and name " + receipt.getEntityName());
                System.out.println("SUCCESS: " + "Updated sample with id " + receipt.getId() + " and name " + receipt.getEntityName());
                return true;
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
            refreshNew(currentSample.getSubmissionId(),currentSample.getUser().getLogin());
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

    private void refreshNew(Integer rid,String user) {
        RequestDTO existingRequest = services.getRequestService().getMinimalRequestByIdAndRequestor(rid,user);
        if (existingRequest==null){
            throw new IllegalStateException("Request with id "+rid+" not existing");
        }
        
        UserDTO requestor = existingRequest.getRequestorUser();
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
        currentSample.setExperimentName(currentApplication.getApplicationName());
        currentSample.setType("");
        isNewForm = true;
    }
    
}
