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
import at.ac.oeaw.cemm.lims.model.validator.ValidatorException;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.ApplicationValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.SampleValidator;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@SessionScoped
public class SingleSampleBean implements Serializable {

    private static final String FORM_ID = null;
    private static final String COMPONENT_MOD = "SampleModbutton";
    private static final String COMPONENT_DEL = "SampleDeletionButton";

    @Inject private ServiceFactory services;
    @Inject private DTOFactory myDTOFactory;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    private SampleDTO currentSample = null;
    private ApplicationDTO newApplication = null;

    private UserDTO principalInvestigator = null;
    private List<String> possibleIndexes = new LinkedList<>();

    private boolean isNewForm = false;
    
    public SingleSampleBean() {
        System.out.println("Initializing SingleSampleBean");
    }

    @PostConstruct
    public void init() {
        System.out.println("SingleSampleBean post construct");
        for (String index : services.getSampleService().getAllIndexes()) {
            possibleIndexes.add(index);
        }
    }

    public String loadNew() {
        currentSample = myDTOFactory.getSampleDTO(null);
        UserDTO currentUser = roleManager.getCurrentUser();
        principalInvestigator = roleManager.getPi();
        currentSample.setUser(currentUser);
        currentSample.setOrganism("HUMAN");
        currentSample.setCostcenter(principalInvestigator.getUserName());
        currentSample.setStatus(SampleDTO.status_queued);
        currentSample.setRequestDate(new Date(System.currentTimeMillis()));
        currentSample.setBioanalyzerDate(new Date(System.currentTimeMillis()));
        currentSample.setIndex(myDTOFactory.getIndexDTO("none"));
        currentSample.setApplication(myDTOFactory.getApplicationDTO(ApplicationDTO.DNA_SEQ));
        isNewForm = true;

        return "/Sample/sampleDetails_1?faces-redirect=true";
    }

    public String loadId() {
        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        Integer sampleID = Integer.parseInt(sid);

        currentSample = services.getSampleService().getFullSampleById(sampleID);
        
        principalInvestigator = services.getUserService().getUserByID(currentSample.getUser().getPi());
        isNewForm = false;
        
        return "/Sample/sampleDetails_1?faces-redirect=true";
    }

    //FOR WELDING...
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public ServiceFactory getServices() {
        return services;
    }

    public void setServices(ServiceFactory services) {
        this.services = services;
    }

    //GETTERS
    public String getSampleName() {
        return currentSample.getName();
    }

    public Integer getSampleID() {
        return currentSample.getId();
    }

    public String getUserLogin() {
        return currentSample.getUser().getLogin();
    }

    public String getUserName() {
        return currentSample.getUser().getUserName();
    }

    public String getUserEmail() {
        return currentSample.getUser().getMailAddress();
    }

    public String getUserTel() {
        return currentSample.getUser().getPhone();
    }

    public String getPiLogin() {
        return principalInvestigator.getLogin();
    }

    public String getApplicationName() {
        return newApplication != null ? newApplication.getApplicationName() : emptyIfNull(currentSample.getApplication().getApplicationName());
    }

    public String getReadMode() {
        return newApplication != null ? newApplication.getReadMode() : currentSample.getApplication().getReadMode();
    }

    public Integer getReadLength() {
        return newApplication != null ? newApplication.getReadLength() : currentSample.getApplication().getReadLength();
    }

    public Integer getDepth() {
        return newApplication != null ? newApplication.getDepth() : currentSample.getApplication().getDepth();
    }

    public String getInstrument() {
        return newApplication != null ? newApplication.getInstrument() : currentSample.getApplication().getInstrument();
    }

    public boolean getLibrarySynthesis() {
        return currentSample.isSyntehsisNeeded();
    }

    public String getSequencingIndex() {
        return emptyIfNull(currentSample.getIndex().getIndex());
    }

    public List<String> getPossibleIndexes() {
        return possibleIndexes;
    }

    public String getCostCenter() {
        return currentSample.getCostcenter();
    }

    public Date getBioDate() {
        return currentSample.getBioanalyzerDate();
    }

    public Double getBioMolarity() {
        return currentSample.getBioAnalyzerMolarity();
    }

    public String getSampleType() {
        return currentSample.getType();
    }

    public String getOrganism() {
        return currentSample.getOrganism();
    }

    public Double getSampleConcentration() {
        return currentSample.getConcentration();
    }

    public Double getTotalAmount() {
        return currentSample.getTotalAmount();
    }

    public Double getBulkFragmentSize() {
        return currentSample.getBulkFragmentSize();
    }

    public String getAntibody() {
        return currentSample.getAntibody();
    }

    public String getSampleDescription() {
        return currentSample.getDescription();
    }

    public String getComments() {
        return currentSample.getComment();
    }

    public String getStatus() {
        return currentSample.getStatus();
    }

    public String[] getPossibleDepths() {
        return "PE".equals(getReadMode()) ? Preferences.getDepthPE() : Preferences.getDepthSR();
    }

    public boolean getModifyPermission() {
        boolean returnValue = roleManager.hasModifyPermission(currentSample);
        return returnValue;
    }

    public boolean getAddPermission() {
        return roleManager.hasSampleLoadPermission();
    }

    public boolean isIsNewForm() {
        return isNewForm;
    }

    //SETTERS
    public void setAntibody(String antibody) {
        currentSample.setAntibody(antibody);
    }

    public void setBulkFragmentSize(Double bulkFragmentSize) {
        currentSample.setBulkFragmentSize(bulkFragmentSize);
    }

    public void setComments(String comment) {
        currentSample.setComment(comment);
    }

    public void setSampleConcentration(Double concentration) {
        currentSample.setConcentration(concentration);
    }

    public void setCostCenter(String costCenter) {
        currentSample.setCostcenter(costCenter);
    }

    public void setSampleDescription(String description) {
        currentSample.setDescription(NameFilter.legalize(description));
    }

    public void setLibrarySynthesis(boolean syntesisNeeded) {
        currentSample.setSyntehsisNeeded(syntesisNeeded);
        if (syntesisNeeded) {
            this.setSequencingIndex("none");
            this.setBioDate(null);
            this.setBioMolarity(null);
        }
    }

    public void setOrganism(String organism) {
        currentSample.setOrganism(organism);
    }

    public void setSampleName(String name) {
        currentSample.setName(NameFilter.legalize(name));
    }

    public void setSampleType(String type) {
        currentSample.setType(type);
    }

    public void setTotalAmount(Double amount) {
        currentSample.setTotalAmount(amount);
    }

    ;
    public void setBioDate(Date date) {
        currentSample.setBioanalyzerDate(date);
    }

    public void setBioMolarity(Double molarity) {
        currentSample.setBioAnalyzerMolarity(molarity);
    }

    //Those setters refer to application
    public void setApplicationName(String appName) {
        newApplication = myDTOFactory.getApplicationDTO(appName, currentSample.getApplication().getInstrument());
    }

    public void setReadMode(String readMode) {
        if (newApplication == null) {
            newApplication = currentSample.getApplication().getCopy();
        }
        newApplication.setReadMode(readMode);
    }

    public void setReadLength(Integer readLength) {
        if (newApplication == null) {
            newApplication = currentSample.getApplication().getCopy();
        }
        newApplication.setReadLength(readLength);
    }

    public void setDepth(Integer depth) {
        if (newApplication == null) {
            newApplication = currentSample.getApplication().getCopy();
        }
        newApplication.setDepth(depth);
    }

    public void setInstrument(String instrument) {
        if (newApplication == null) {
            newApplication = currentSample.getApplication().getCopy();
        }
        newApplication.setInstrument(instrument);
    }

    public void setSequencingIndex(String index) {
        currentSample.setIndex(myDTOFactory.getIndexDTO(index));
    }

    //CRUD OPs
    public void modify() {
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_MOD, "User error", "You do not have permissions to modify this component");
            return;
        }

        persist();
    }

    public String save() {
        if (!getAddPermission()) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_MOD, "User error", "You do not have permissions to save this component");
            return null;
        }

        if (persist()) {
            return "sampleCreated_1?faces-redirect=true";
        } else {
            return null;
        }
    }

    public String delete() {
        System.out.println("Deleting Sample with id " + currentSample.getId());
        if (!getModifyPermission()) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_DEL, "User error", "You do not have permissions to modify this component");
            System.out.println("User has no permissions to delete this sample");
            return null;
        }

        if (currentSample.getStatus().equals(SampleDTO.status_requested) || currentSample.getStatus().equals(SampleDTO.status_queued)) {
            try {
                services.getSampleService().deleteSample(currentSample);
            } catch (Exception ex) {
                NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_DEL,
                        "Error in DB while deleting " + currentSample.getId(), ex.getMessage());
                System.out.println("Error in DB " + ex.getMessage());

                return null;
            }
        } else {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_DEL,
                    "Error deleting " + currentSample.getId(), "Only samples with status requested can be deleted.");
            System.out.println("Error due to sample status");

            return null;
        }

        System.out.println("Deletion success");

        return "sampleDeleted_1?faces-redirect=true";
    }

    private boolean persist() {
        try {

            ApplicationValidator appValidator = null;
            if (newApplication != null) {
                appValidator = new ApplicationValidator(newApplication);
                ApplicationDTO appToPersist = appValidator.getValidatedObject();
                currentSample.setApplication(appToPersist);
                currentSample.setExperimentName(appToPersist.getApplicationName());
            }

            SampleValidator sampValidator = new SampleValidator(currentSample,myDTOFactory);
            SampleDTO sampleToPersist = sampValidator.getValidatedObject();
            PersistedEntityReceipt receipt = services.getSampleService().saveOrUpdateSample(sampleToPersist, isNewForm);
            this.currentSample = services.getSampleService().getFullSampleById(receipt.getId());

            if (appValidator != null) {
                for (ValidatorMessage message : appValidator.getMessages()) {
                    if (message.getType().equals(ValidatorSeverity.WARNING)) {
                        NgsLimsUtility.setWarningMessage(FORM_ID, COMPONENT_MOD, message.getSummary(), message.getDescription());
                        System.out.println("WARNING: " + message.getSummary() + " Description: " + message.getDescription());
                    }
                }
            }

            for (ValidatorMessage message : sampValidator.getMessages()) {
                if (message.getType().equals(ValidatorSeverity.WARNING)) {
                    NgsLimsUtility.setWarningMessage(FORM_ID, COMPONENT_MOD, message.getSummary(), message.getDescription());
                    System.out.println("WARNING: " + message.getSummary() + " Description: " + message.getDescription());
                }
            }

            NgsLimsUtility.setSuccessMessage(FORM_ID, COMPONENT_MOD,
                    "Sample with id " + receipt.getId() + " updated successfully",
                    "Updated sample with id " + receipt.getId() + " and name " + receipt.getEntityName());
            System.out.println("SUCCESS: " + "Updated sample with id " + receipt.getId() + " and name " + receipt.getEntityName());
            return true;

        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            for (ValidatorMessage message : e.getPayload()) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_MOD, message.getSummary(), message.getDescription());
                    System.out.println("FATAL: " + message.getSummary() + " Description: " + message.getDescription());
                }
            }
        } catch (Exception e) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_MOD, "Error while persisting sample", e.getMessage());
            System.out.println("FATAL: exception " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    //UTILITY
    private String emptyIfNull(String toCheck) {
        return toCheck == null ? "" : toCheck;
    }
}
