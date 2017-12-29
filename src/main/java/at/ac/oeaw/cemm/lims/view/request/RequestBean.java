/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.dto.request_form.RequestLibraryDTOImpl;
import at.ac.oeaw.cemm.lims.model.dto.request_form.RequestSampleDTOImpl;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.SampleAnnotationWriter;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.request_form.BillingInfoValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.request_form.RequestLibraryValidator;
import at.ac.oeaw.cemm.lims.util.Preferences;
import at.ac.oeaw.cemm.lims.util.RequestIdBean;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "requestBean")
@ViewScoped
public class RequestBean {
    private final static String SAMPLE_ANNOTATION_FILENAME = "SampleAnnotationSheet.xlsx";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private RequestFormDTO request;

    private boolean isRequestFailed = false;    
    private boolean areSamplesFailed = false;
    private boolean areLibrariesFailed = false;
    private String wizardStep = "personal";
    private boolean newRequest = false;
    private File excelFile = null;
    private File authFile = null;
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    
    @ManagedProperty(value = "#{requestIdBean}")
    private RequestIdBean requestIdBean;
    
    @ManagedProperty(value = "#{requestFileManagerBean}")
    private RequestFileManagerBean fileManager;
    
    @Inject
    private RequestDTOFactory dtoFactory;
    @Inject
    private ServiceFactory services;
    
    @PostConstruct
    public void init() {
        System.out.println("Initializing requestBean");
        
        String rid =  (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rid");
        Integer requestId = null;
        if (rid!=null){
            requestId = Integer.parseInt(rid);
        }
        initInternal(requestId);
       
    }

    protected void initInternal(Integer requestId){
         if(requestId==null) {              
            RequestorDTO requestor = dtoFactory.getRequestorDTO(roleManager.getCurrentUser(), roleManager.getPi());
            request = dtoFactory.getRequestFormDTO(requestor,dtoFactory.getEmptyBillingInfoDTO());
            newRequest = true;
        } else {
            newRequest = false;
            request = services.getRequestFormService().getFullRequestById(requestId);
            if (request == null) {
                throw new IllegalStateException("Request with rid "+requestId+" is null");
            }
            excelFile = new File(getSampleAnnotationPath(request),SAMPLE_ANNOTATION_FILENAME);
            if(!excelFile.exists()){
                throw new IllegalStateException("Excel not found for request with rid "+requestId);
            }
            if (!isEditable()){
                NgsLimsUtility.setSuccessMessage("informationMessages", null, "This request form is not editable", "You might not have permission to edit this request or it has already been accepted");
            }
            if (!isRequestorCemm() && request.getAuthorizationFileName()!=null && !request.getAuthorizationFileName().trim().isEmpty()){
                authFile = new File(getSampleAnnotationPath(request),request.getAuthorizationFileName());
            }
            
            fileManager.init(getSampleAnnotationPath(request),authFile == null ? null : authFile.getName());
        }
    }
    
    public void hasViewPermission() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!canView()) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error401.xhtml");
        }
    }

    public boolean canView(){
        return newRequest || roleManager.hasAnnotationSheetModifyPermission(request);
    }
    
    public boolean isEditable() {
        boolean isFormEditable = (newRequest)|| (!newRequest && RequestFormDTO.STATUS_NEW.equals(request.getStatus()));
        return isFormEditable && roleManager.hasAnnotationSheetModifyPermission(request);
    }

    public void setEditable(boolean value) {
    }
    

    public RequestFormDTO getRequest() {
        return request;
    }

    protected void setRequest(RequestFormDTO request, File excel) {
        
        //checks on request ID
        if (this.request.getRequestId()!=null && request.getRequestId()==null){
            request.setRequestId(this.request.getRequestId());
        }else if (!Objects.equals(this.request.getRequestId(), request.getRequestId())){
            NgsLimsUtility.setFailMessage("uploadDialogMsg", null, "Excel uplad", "The uploaded and loaded requests have different ID");
            return;
        }
        
        //checks on user
        if (!newRequest && !this.request.getRequestorUser().getLogin().equals(request.getRequestorUser().getLogin())){
            NgsLimsUtility.setFailMessage("uploadDialogMsg", null, "Excel uplad", "The uploaded and loaded requests have different requestors");
            return;
        }
        
        this.request = request;
        excelFile = excel;
    }
    

    public String getSamples() {
        try {
            List<RequestSampleDTO> samples = new LinkedList<>();
            for (RequestLibraryDTO library : request.getLibraries()) {
                samples.addAll(library.getSamples());
            }
            String output = objectMapper.writeValueAsString(samples);
            return output;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public void setSamples(String json) {
        Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("sampleMessage");
        while (messageIterator.hasNext()) {
            messageIterator.remove();
        }
        request.resetLibraries();
        
        try {
            List<RequestSampleDTO> receivedSamples =
                    objectMapper.readValue(json, new TypeReference<List<RequestSampleDTOImpl>>() {});
             
            RequestNormalizer.normalizeSamples(receivedSamples);
         
            Map<String, RequestLibraryDTO> libraries = new LinkedHashMap<>();
            
            for (RequestLibraryDTO existingLibrary: request.getLibraries()){
                libraries.put(existingLibrary.getName(), existingLibrary);
            }
            
            for (RequestSampleDTO sample : receivedSamples) {
                RequestLibraryDTO library = libraries.get(sample.getLibrary());
                if (library == null) {
                    library = dtoFactory.getRequestLibraryDTO(true);
                    library.setName(sample.getLibrary());
                    libraries.put(library.getName(), library);
                }
                library.addSample(sample);
            }
               
            RequestLibraryValidator libraryValidator = new RequestLibraryValidator();
            ValidationStatus validationStatus = new ValidationStatus();
             
            for (RequestLibraryDTO library: libraries.values()) {
                validationStatus.merge(libraryValidator.isValid(library));          
                request.addLibrary(library);
            }
            
            areSamplesFailed = !validationStatus.isValid();
            
            request.removeEmptyLibraries();
            
            for (ValidatorMessage message : validationStatus.getValidationMessages()) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                } else {
                    NgsLimsUtility.setWarningMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                }
            }

        } catch (IOException ex) {
            areSamplesFailed = true;
            NgsLimsUtility.setFailMessage("sampleMessages", null, "Error", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getLibraries() {
        try {
            List<RequestLibraryDTO> libraries = new LinkedList<>();
            for (RequestLibraryDTO library : request.getLibraries()) {
                libraries.add(library.getCopyWithoutSamples());
            }
            String output = objectMapper.writeValueAsString(libraries);
            return output;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public void setLibraries(String json) {
        Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("libraryMessage");
        while (messageIterator.hasNext()) {
            messageIterator.remove();
        }

        try {
            List<RequestLibraryDTO> receivedLibraries = objectMapper.readValue(json, new TypeReference<List<RequestLibraryDTOImpl>>() {
            });
            
            RequestNormalizer.normalizeLibraries(receivedLibraries);

            RequestLibraryValidator libraryValidator = new RequestLibraryValidator(false);
            ValidationStatus librariesValidation = new ValidationStatus();
            
            for (RequestLibraryDTO library: receivedLibraries){
                librariesValidation.merge(libraryValidator.isValid(library));
            }
            
            areLibrariesFailed = !librariesValidation.isValid();
            
         
            for (ValidatorMessage message : librariesValidation.getValidationMessages()) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage("libraryMessage", null, message.getSummary(), message.getDescription());
                } else {
                    NgsLimsUtility.setWarningMessage("libraryMessage", null, message.getSummary(), message.getDescription());
                }
            }

            for (RequestLibraryDTO library : receivedLibraries) {
                RequestLibraryDTO existingLibrary = request.getLibraryByUUID(library.getUuid());
                if (existingLibrary != null) {
                    existingLibrary.setParametersFromCopy(library);
                } else {
                    areLibrariesFailed = true;
                    NgsLimsUtility.setFailMessage("libraryMessage", null, "Server Error", "Inconsistency between libraries");
                }
            }

        } catch (IOException ex) {
            areLibrariesFailed = true;
            NgsLimsUtility.setFailMessage("libraryMessage", null, "Server Error", ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private boolean isLegalValid(){
        if (!isRequestorCemm()){
            if (authFile==null || !authFile.exists()){
                NgsLimsUtility.setFailMessage("legalMessage", "", "Authorization Form", "Please upload the sequencing authorization form");
                return false;
            }
        }
        
        BillingInfoValidator billingValidator = new BillingInfoValidator();
        ValidationStatus billingValidation = billingValidator.isValid(request.getBillingInfo());
        
        if (!billingValidation.isValid()){
            for (ValidatorMessage message: billingValidation.getValidationMessages()){
                if (ValidatorSeverity.FAIL.equals(message.getType())){
                    NgsLimsUtility.setFailMessage("legalMessage", "", message.getSummary(), message.getDescription());
                }
            }
            return false;
        }
        
        for (ValidatorMessage message : billingValidation.getValidationMessages()) {
            if (ValidatorSeverity.WARNING.equals(message.getType())) {
                NgsLimsUtility.setFailMessage("legalMessage", "", message.getSummary(), message.getDescription());
            }
        }
        
        return true;
    }
    
    public boolean isRequestorCemm(){
        boolean hasCemmMail;
        
        try{
            hasCemmMail = request.getRequestorUser().getMailAddress().split("@")[1].equalsIgnoreCase("cemm.oeaw.ac.at");
        }catch(Exception e){
            hasCemmMail = false;
        }
        
        boolean hasCemmAffiliation = request.getRequestor().getUser().getAffiliation().getOrganizationName().equalsIgnoreCase("cemm");
        
        return hasCemmMail || hasCemmAffiliation;
 
    }
    
    public void uploadAuthorizationForm(FileUploadEvent event){
        File folder = new File(Preferences.getAnnotationSheetFolder(),"TEMP_"+UUID.randomUUID().toString());
        if (!newRequest){
            folder = getSampleAnnotationPath(request);
        }        
        authFile = fileManager.handleFileUpload(event,"legalMessage",folder,true);
        if (authFile!=null && authFile.exists()){
            request.setAuthorizationFileName(authFile.getName());
        }
    }
 
    public String onFlowProcess(FlowEvent event) {
        if (event.getOldStep().equals("personal") && (newRequest && excelFile==null)) {
            NgsLimsUtility.setFailMessage("requestMessages", null, "Excel error", "Please upload a sample sheet");
            wizardStep = event.getOldStep();
        }else if (event.getOldStep().equals("libraries") && areLibrariesFailed) {
            wizardStep = event.getOldStep();
        } else if (event.getOldStep().equals("samples")) {
            if (areSamplesFailed) {
                wizardStep = event.getOldStep();
            } else if (event.getNewStep().equals("libraries") && request.getLibraries().isEmpty()) {
                wizardStep = event.getOldStep();
            } else {
                wizardStep = event.getNewStep();
            }
        } else if (event.getOldStep().equals("legal")){
            isRequestFailed = !isLegalValid();
            if (isRequestFailed){
                wizardStep = event.getOldStep();
            }else{
                wizardStep = event.getNewStep();
                
            }
        }else{
            wizardStep = event.getNewStep();
        }

        return wizardStep;
    }

    public String getWizStep() {
        return wizardStep;
    }
    
    protected boolean submit() {
        Boolean success = false;

        if (!areSamplesFailed && !areLibrariesFailed && isLegalValid()) {
            if (isEditable()) {
                try {
                    if (newRequest) {
                        request.setRequestId(requestIdBean.getNextId());
                    }
                    SampleAnnotationWriter excelWriter = new SampleAnnotationWriter(excelFile,request);
                    File path = getSampleAnnotationPath(request);
                    if (!path.exists()){
                        path.mkdir();
                    }
                    excelWriter.writeToFile(SAMPLE_ANNOTATION_FILENAME,path);
                    
                    if (!isRequestorCemm() && (!authFile.getParentFile().getAbsolutePath().equals(path.getAbsolutePath()))){
                        Files.move(authFile.toPath(), (new File(path,authFile.getName())).toPath(), REPLACE_EXISTING);
                        FileUtils.deleteDirectory(authFile.getParentFile());
                    }
                    
                    services.getRequestFormService().saveRequestForm(request, newRequest);
                    success = true;
                    NgsLimsUtility.setSuccessMessage("validationMessages", null, "Request Updated", "");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NgsLimsUtility.setFailMessage("validationMessages", null, "Server error", ex.getMessage());
                } finally {
                    
                    requestIdBean.unlock();
                }
            } else {
                NgsLimsUtility.setFailMessage("validationMessages", null, "User error", "You don't have permission to upload this request");
            }
        } else {
            NgsLimsUtility.setFailMessage("validationMessages", null, "Validation error", "Samples, Libraries or legal informations (and sequencing auth. form) have not passed validation");
        }

        return success;
    }

    public String submitAndRedirect() {
        if (submit()){
            if (newRequest){ 
                return "requestCreated.jsf?faces-redirect=true&rid="+request.getRequestId();
            }else {
                return "sampleRequest.jsf?faces-redirect=true&rid="+request.getRequestId();
            }
        }
        
        return null;
    }
    
    
    public String deleteRequest() {
        if (!newRequest){
            return DeleteRequestFormBean.deleteRequestInternal(request, roleManager, services, "validationMessages", "requestDeleted.jsf?faces-redirect=true&rid="+request.getRequestId());
        }
        
        return null;
    }

    public boolean isNewRequest() {
        return newRequest;
    }
    
  
    protected static File getSampleAnnotationPath(RequestFormDTO request){
        return new File(Preferences.getAnnotationSheetFolder(),request.getRequestId() + "_" + request.getRequestorUser().getLogin());
    }
    
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public RequestIdBean getRequestIdBean() {
        return requestIdBean;
    }

    public void setRequestIdBean(RequestIdBean requestIdBean) {
        this.requestIdBean = requestIdBean;
    }

    public RequestFileManagerBean getFileManager() {
        return fileManager;
    }

    public void setFileManager(RequestFileManagerBean fileManager) {
        this.fileManager = fileManager;
    }
    
}
