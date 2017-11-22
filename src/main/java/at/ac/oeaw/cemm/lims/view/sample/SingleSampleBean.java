/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.model.validator.ValidatorException;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.entity.ApplicationValidator;
import at.ac.oeaw.cemm.lims.model.validator.entity.SampleValidator;
import at.ac.oeaw.cemm.lims.service.LazySampleService;
import at.ac.oeaw.cemm.lims.service.PersistedSampleReceipt;
import at.ac.oeaw.cemm.lims.service.UserService;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.sampleBeans.SampleNameFilter;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

            
    @ManagedProperty(value = "#{lazySampleService}")
    private LazySampleService sampleService;
    
    @ManagedProperty(value = "#{userService}")
    private UserService userService;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    private Sample currentSample = null;
    private Application newApplication = null;

    private User principalInvestigator = null;
    private List<String> possibleIndexes = new LinkedList<>();
    
    private boolean isNewForm = false;
    
    @PostConstruct
    public void init() {
         for (String index: sampleService.getAllIndexes()){
            possibleIndexes.add(index);
        }
    }
    
    public String loadNew() {
        currentSample = new Sample();
        User currentUser = roleManager.getLoggedUser();
        principalInvestigator = userService.getUserByID(currentUser.getPi());
        currentSample.setUser(currentUser);
        currentSample.setOrganism("HUMAN");
        currentSample.setCostCenter(principalInvestigator.getUserName());
        currentSample.setStatus(Sample.status_queued);
        currentSample.setRequestDate(new Date(System.currentTimeMillis()));
        currentSample.setBioanalyzerDate(new Date(System.currentTimeMillis()));
        currentSample.setSequencingIndexes(new SequencingIndex("none"));
        currentSample.setApplication(new Application(Application.DNA_SEQ));
        isNewForm = true;

        return "/Sample/sampleDetails_1?faces-redirect=true";
    }
    
    public String loadId() {
        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        Integer sampleID = Integer.parseInt(sid);
        
        currentSample = sampleService.getFullSampleById(sampleID);
        principalInvestigator = userService.getUserByID(currentSample.getUser().getPi());
             
        return "/Sample/sampleDetails_1?faces-redirect=true";
    }
    
    //USED BY AUTO-WIRING
    public LazySampleService getSampleService() {
        return sampleService;
    }
    public void setSampleService(LazySampleService sampleService) {
        this.sampleService = sampleService;
    }
    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public NewRoleManager getRoleManager() {
        return roleManager;
    }
    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    //GETTERS
    public String getSampleName() { return currentSample.getName();}
    public Integer getSampleID() { return currentSample.getId();}
    public String getUserLogin() { return currentSample.getUser().getLogin();}
    public String getUserName() { return currentSample.getUser().getUserName();}
    public String getUserEmail() { return currentSample.getUser().getMailAddress();}
    public String getUserTel() { return currentSample.getUser().getPhone();}
    public String getPiLogin() { return principalInvestigator.getLogin();}
    public String getApplicationName() { 
        return newApplication!=null? newApplication.getApplicationname() : emptyIfNull(currentSample.getApplication().getApplicationname());
    }
    public String getReadMode() { 
        return newApplication!=null? newApplication.getReadmode() : currentSample.getApplication().getReadmode();
    }
    public Integer getReadLength() { 
        return newApplication!=null? newApplication.getReadlength() : currentSample.getApplication().getReadlength();
    }
    public Integer getDepth() { 
        return newApplication!=null? newApplication.getDepth() : currentSample.getApplication().getDepth();
    }
    public String getInstrument() { 
        return newApplication!=null? newApplication.getInstrument() :currentSample.getApplication().getInstrument();
    }

    public boolean getLibrarySynthesis() { return currentSample.getLibrarySynthesisNeeded();}
    public String getSequencingIndex() { return emptyIfNull(currentSample.getSequencingIndexes().getIndex());}
    public List<String> getPossibleIndexes() { return possibleIndexes;}
    public String getCostCenter() { return currentSample.getCostCenter();}
    public Date getBioDate() { return currentSample.getBioanalyzerDate();}
    public Double getBioMolarity() { return currentSample.getBionalyzerBiomolarity();}
    public String getSampleType() { return currentSample.getType();}
    public String getOrganism() { return currentSample.getOrganism();}
    public Double getSampleConcentration() { return currentSample.getConcentration();}
    public Double getTotalAmount() { return currentSample.getTotalAmount();}
    public Double getBulkFragmentSize() { return currentSample.getBulkFragmentSize();}
    public String getAntibody() { return currentSample.getAntibody();}
    public String getSampleDescription() { return currentSample.getDescription();}
    public String getComments() { return currentSample.getComment();}
    public String getStatus() { return currentSample.getStatus();}
    public String[] getPossibleDepths() { return "PE".equals(getReadMode()) ? Preferences.getDepth_PE(): Preferences.getDepth_SR();}
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
    public void setAntibody(String antibody) {  currentSample.setAntibody(antibody);}
    public void setBulkFragmentSize(Double bulkFragmentSize) { currentSample.setBulkFragmentSize(bulkFragmentSize);}
    public void setComments(String comment) { currentSample.setComment(comment);}
    public void setSampleConcentration(Double concentration) { currentSample.setConcentration(concentration);}
    public void setCostCenter(String costCenter) { currentSample.setCostCenter(costCenter);}
    public void setSampleDescription(String description) { currentSample.setDescription(SampleNameFilter.legalize(description));}
    public void setLibrarySynthesis(boolean syntesisNeeded) { 
        currentSample.setLibrarySynthesisNeeded(syntesisNeeded);
        if (syntesisNeeded){
            this.setSequencingIndex("none");
            this.setBioDate(null);
            this.setBioMolarity(null);
        }
    }
    public void setOrganism(String organism) { currentSample.setOrganism(organism);}
    public void setSampleName(String name) { currentSample.setName(SampleNameFilter.legalize(name));}
    public void setSampleType(String type) { currentSample.setType(type);}
    public void setTotalAmount(Double amount) {currentSample.setTotalAmount(amount);};
    public void setBioDate(Date date) { currentSample.setBioanalyzerDate(date);}
    public void setBioMolarity(Double molarity) { currentSample.setBionalyzerBiomolarity(molarity);}
    
    //Those setters refer to application
    public void setApplicationName(String appName) { 
        newApplication = new Application(appName,currentSample.getApplication().getInstrument());        
    }
    public void setReadMode(String readMode) { 
        if (newApplication == null) newApplication = currentSample.getApplication().createCopy();
        newApplication.setReadmode(readMode);
    }
    public void setReadLength(Integer readLength) { 
        if (newApplication == null) newApplication = currentSample.getApplication().createCopy();
        newApplication.setReadlength(readLength);
    }
    public void setDepth(Integer depth) { 
        if (newApplication == null) newApplication = currentSample.getApplication().createCopy();
        newApplication.setDepth(depth);
    }
    public void setInstrument(String instrument) {
        if (newApplication == null) newApplication = currentSample.getApplication().createCopy();
        newApplication.setInstrument(instrument);
    }
    
    public void setSequencingIndex(String index) { 
        currentSample.setSequencingIndexes(new SequencingIndex(index));
    }
   
    //CRUD OPs
    public void modify() {
        if (!getModifyPermission()){
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_MOD, "User error", "You do not have permissions to modify this component");
            return;
        }
        
        persist();
    }
    
     public String save() {
        if (!getAddPermission()){
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT_MOD, "User error", "You do not have permissions to save this component");
            return null;
        }
        
        if (persist()){
            return "sampleCreated_1?faces-redirect=true";
        }else{
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

        if (currentSample.getStatus().equals(Sample.status_requested)) {
            try {
                sampleService.deleteSample(currentSample);
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
                Application appToPersist = appValidator.getValidatedObject();
                currentSample.setApplication(appToPersist);
                currentSample.setExperimentName(appToPersist.getApplicationname());
            }

            SampleValidator sampValidator = new SampleValidator(currentSample);
            Sample sampleToPersist = sampValidator.getValidatedObject();
            PersistedSampleReceipt receipt = sampleService.saveOrUpdateSample(sampleToPersist,isNewForm);

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
                    "Updated sample with id " + receipt.getId() + " and name " + receipt.getSampleName());
            System.out.println("SUCCESS: " + "Updated sample with id " + receipt.getId() + " and name " + receipt.getSampleName());
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
    private String emptyIfNull(String toCheck){
        return toCheck == null ? "" : toCheck;
    }
}
