/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.SampleDTOValidator;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.LibraryValidator;
import at.ac.oeaw.cemm.lims.util.Levenshtein;
import at.ac.oeaw.cemm.lims.util.SampleLock;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
    
    @ManagedProperty(value = "#{sampleLock}")
     private SampleLock sampleLock;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    

    private SampleDTO currentSample = null;
    private SampleDTO selectedSampleInLibrary = null;
    private LibraryDTO currentLibrary = null;
    
    private UserDTO principalInvestigator = null;
    
    private final List<String> possibleIndexesI5 = new LinkedList<>();
    private final List<String> possibleIndexesI7 = new LinkedList<>();
    
    //SETUP AND INITIALIZATION
    @PostConstruct
    public void init() {
        for (String index : services.getSampleService().getAllIndexes(IndexType.i7)) {
            possibleIndexesI7.add(index);
        }
        for (String index : services.getSampleService().getAllIndexes(IndexType.i5)) {
            possibleIndexesI5.add(index);
        }
        
        String sid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sid");

        if (sid != null) {
            Integer sampleId = Integer.parseInt(sid);
            refreshExisting(sampleId);

           if (!getModifyPermission()) {
                Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages("modifyPermission");
                while (messages.hasNext()) {
                    messages.next();
                    messages.remove();
                }
                NgsLimsUtility.setSuccessMessage("modifyPermission", null, "Sample not editable", "You do not have permissions to edit this sample");
            }

        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error404.xhtml");
        }
      
    }
    
    
    private void refreshExisting(Integer sampleId) {
        this.currentSample = services.getSampleService().getFullSampleById(sampleId);
        this.selectedSampleInLibrary = currentSample;
        this.currentLibrary = services.getRequestService().getLibraryByName(currentSample.getLibraryName());
        this.principalInvestigator = services.getUserService().getUserByID(currentSample.getUser().getPi());
        validateLibrary();
    }
    
    //PERMISSIONS
    public void hasViewPermission() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!roleManager.hasSampleViewPermission(currentSample)) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error401.xhtml");
        }
    }

    public boolean getModifyPermission() {
        boolean returnValue = roleManager.hasSampleModifyPermission(currentSample);
        return returnValue;
    }
    
    public boolean isDeleatable() {
        return currentSample.getStatus().equals(SampleDTO.status_requested);
    }

    //FOR WELDING...
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }  

    public SampleLock getSampleLock() {
        return sampleLock;
    }

    public void setSampleLock(SampleLock sampleLock) {
        this.sampleLock = sampleLock;
    }
    
    

    //GETTERS FOR MAIN DTOs
    public SampleDTO getCurrentSample() {
        return currentSample;
    }

    public LibraryDTO getCurrentLibrary() {  
        return currentLibrary;
    }

    public UserDTO getPrincipalInvestigator() {
        return principalInvestigator;
    }

           
    
    public List<String> getPossibleIndexesI7() {
        return possibleIndexesI7;
    }
    
    public List<String> getPossibleIndexesI5() {
        return possibleIndexesI5;
    }

    //LIBRARY TABLE BEHAVIOUR
    public SampleDTO getSelectedSampleInLibrary() {
        return selectedSampleInLibrary;
    }

    public void setSelectedSampleInLibrary(SampleDTO selectedSampleInLibrary) {
        this.selectedSampleInLibrary = selectedSampleInLibrary;
    }
    
    
    public String getLibraryRowClass(SampleDTO sample) {
        Integer editDistance;
        if (Objects.equals(sample.getId(), selectedSampleInLibrary.getId())) {
            return null;
        } else if (getEditDistance(sample.getCompoundIndex()) == 0) {            
            NgsLimsUtility.setFailMessage("indexCollisionMsg", null, "Index Collision", "The index is equal to another index in the library");
            return "index-collision-red";
        } else if ((editDistance=getEditDistance(sample.getCompoundIndex())) < minimumDistanceThreshold) {         
            NgsLimsUtility.setWarningMessage("indexCollisionMsg", null, "Index Collision", "The edit distance with another index in the library is " + editDistance);
            return "index-collision-yellow";
        }
        return null;
    }
    
    private Integer getEditDistance(String otherIndex) {
        Integer distance;
        String currentIndex = selectedSampleInLibrary.getCompoundIndex();
        if (otherIndex.equals(currentIndex)) {
            distance = 0;
        } else {
            distance = Levenshtein.computeLevenshteinDistance(currentIndex, otherIndex);
        }        
        return distance;
    }
    
    public void resetLibraryStatus(){
        for (SampleDTO sample: currentLibrary.getSamples()){
            if (SampleDTO.status_running.equals(sample.getStatus())){
                sample.setStatus(SampleDTO.status_rerun);
            }
        }
    }
    
    public boolean isLibraryRunning(){
        for (SampleDTO sample: currentLibrary.getSamples()){
            if (SampleDTO.status_running.equals(sample.getStatus())){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean validateLibrary() {
        SampleDTOValidator sampleValidator = new SampleDTOValidator();
        LibraryValidator libValidator = new LibraryValidator(sampleValidator, false);
        ValidationStatus validation = libValidator.isValid(currentLibrary);

        for (ValidatorMessage message : validation.getValidationMessages()) {
            if (message.getType().equals(ValidatorSeverity.FAIL)) {
                NgsLimsUtility.setFailMessage("libraryName", null, message.getSummary(), message.getDescription());
            } else if (message.getType().equals(ValidatorSeverity.WARNING)) {
                NgsLimsUtility.setWarningMessage("libraryName", null, message.getSummary(), message.getDescription());
            }
        }
        
        return validation.isValid();
    }

    
   //SAVE OR DELETE
    public String delete() {
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            return null;
        }

        if (isDeleatable()) {
            try {
                sampleLock.lock();
                services.getSampleService().deleteSample(currentSample);
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage(null, null,
                        "Error in DB while deleting " + currentSample.getId(), ex.getMessage());
                System.out.println("Error in DB " + ex.getMessage());

                return null;
            }finally{
                sampleLock.unlock();
            }
        } else {
            NgsLimsUtility.setFailMessage(null, null,
                    "Error deleting " + currentSample.getId(), "Only samples with status requested can be deleted.");
            System.out.println("Error due to sample status");

            return null;
        }

        System.out.println("Deletion success");

        return "sampleDeleted_1?faces-redirect=true&sid="+currentSample.getId();
    }
    
    public void persistLibrary() {
         
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(null, null, "User error", "You do not have permissions to modify this component");
            return;
        }
        
        if (validateLibrary()) {
            try {
                services.getSampleService().bulkUpdateSamples(currentLibrary.getSamples());
                refreshExisting(currentSample.getId());
                NgsLimsUtility.setSuccessMessage("libraryName", null, "Success", "Library Updated successfully");
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage("libraryName", null, "Server error", ex.getMessage());
            }
        }
    }

}
