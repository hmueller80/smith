/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

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
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    

    private SampleDTO currentSample = null;
    private SampleDTO selectedSampleInLibrary = null;
    private LibraryDTO currentLibrary = null;
    
    private UserDTO principalInvestigator = null;
    
    private final List<String> possibleIndexes = new LinkedList<>();
    
    
    //SETUP AND INITIALIZATION
    @PostConstruct
    public void init() {
        for (String index : services.getSampleService().getAllIndexes()) {
            possibleIndexes.add(index);
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
    
    public boolean isEditable() {
        return currentSample.getStatus().equals(SampleDTO.status_requested) || currentSample.getStatus().equals(SampleDTO.status_queued);
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

    public LibraryDTO getCurrentLibrary() {  
        return currentLibrary;
    }

    public UserDTO getPrincipalInvestigator() {
        return principalInvestigator;
    }

           
    
    public List<String> getPossibleIndexes() {
        return possibleIndexes;
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
        } else if (getEditDistance(sample.getIndex().getIndex()) == 0) {            
            NgsLimsUtility.setFailMessage("indexCollisionMsg", null, "Index Collision", "The index is equal to another index in the library");
            return "index-collision-red";
        } else if ((editDistance=getEditDistance(sample.getIndex().getIndex())) < minimumDistanceThreshold) {         
            NgsLimsUtility.setWarningMessage("indexCollisionMsg", null, "Index Collision", "The edit distance with another index in the library is " + editDistance);
            return "index-collision-yellow";
        }
        return null;
    }
    
    private Integer getEditDistance(String otherIndex) {
        Integer distance;
        String currentIndex = selectedSampleInLibrary.getIndex().getIndex();
        if (otherIndex.equals(currentIndex)) {
            distance = 0;
        } else {
            distance = Levenshtein.computeLevenshteinDistance(currentIndex, otherIndex);
        }        
        return distance;
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
        
        System.out.println("HERE!");
        return validation.isValid();
    }

    
   //SAVE OR DELETE
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
