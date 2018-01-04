/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.dto.request_form.RequestLibraryDTOImpl;
import at.ac.oeaw.cemm.lims.model.dto.request_form.RequestSampleDTOImpl;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.RequestFormBuilder;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.SampleAnnotationWriter;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.RequestValidator;
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
    private final static String SAMPLE_ANNOTATION_FILENAME = "SampleAnnotationSheet";
    private final static String SEQUENCING_AUTHORIZATION_FORM = "SequencingAuthorizationForm";

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private RequestFormDTO request;

    private boolean areSamplesFailed = false;
    private boolean areLibrariesFailed = false;
    private String wizardStep = "personal";
    private boolean newRequest = false;
    
    private File sampleAnnotationPath;
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
    private RequestFormBuilder requestFormBuilder;
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
            sampleAnnotationPath = new File(Preferences.getAnnotationSheetFolder(),"TEMP_"+UUID.randomUUID().toString());
        } else {
            newRequest = false;
            request = services.getRequestFormService().getFullRequestById(requestId);
            if (request == null) {
                throw new IllegalStateException("Request with rid "+requestId+" is null");
            }
            sampleAnnotationPath =  buildSampleAnnotationPath(request);
            excelFile = new File(sampleAnnotationPath,request.getSampleAnnotationFileName());
            if(!excelFile.exists()){
                throw new IllegalStateException("Excel not found for request with rid "+requestId);
            }
            
            if (!isRequestorCemm()){
                authFile = new File(sampleAnnotationPath,request.getAuthorizationFileName());
                if (!authFile.exists()) {
                    throw new IllegalStateException("Sequence Authorization not found for request with rid " + requestId);
                }
            }
            
            if (!isEditable()){
                NgsLimsUtility.setSuccessMessage("informationMessages", null, "This request form is not editable", "You might not have permission to edit this request or it has already been accepted");
            }         
            
            fileManager.init(sampleAnnotationPath);
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
                NgsLimsUtility.setWarningMessage("legalMessage", "", message.getSummary(), message.getDescription());
            }
        }
        
        return true;
    }
    
    public boolean isRequestorCemm(){
        return checkCemmUser(request.getRequestor().getUser());
    }
    
    private boolean checkCemmUser(UserDTO user){
        boolean hasCemmMail;
        
        try{
            hasCemmMail = user.getMailAddress().split("@")[1].equalsIgnoreCase("cemm.oeaw.ac.at");
        }catch(Exception e){
            hasCemmMail = false;
        }
        
        boolean hasCemmAffiliation = user.getAffiliation().getOrganizationName().equalsIgnoreCase("cemm");
        
        return hasCemmMail || hasCemmAffiliation;
    }
    
    public void uploadAuthorizationForm(FileUploadEvent event){
        if (isEditable()){    
            File newAuthFile = fileManager.handleFileUpload(event,"legalMessage",sampleAnnotationPath,SEQUENCING_AUTHORIZATION_FORM,true);
            if (newAuthFile!=null && newAuthFile.exists()){
                if (authFile!=null && authFile.exists()){
                    authFile.delete();
                }
                authFile = newAuthFile;
                request.setAuthorizationFileName(authFile.getName());
            }
        }
    }

    public void uploadSampleSheet(FileUploadEvent event) {
        String messageBoxComponent = "uploadDialogMsg";

        if (isEditable() && newRequest) {
            excelFile = fileManager.handleFileUpload(event, messageBoxComponent, sampleAnnotationPath, SAMPLE_ANNOTATION_FILENAME, true);
            if (excelFile != null && excelFile.exists()) {
                ValidatedCSV<RequestFormDTO> parsedCSV = requestFormBuilder.buildRequestFromExcel(excelFile);

                if (parsedCSV.getValidationStatus().isFailed()) {
                    this.excelFile = null;
                    for (ParsingMessage message : parsedCSV.getValidationStatus().getFailMessages()) {
                        NgsLimsUtility.setFailMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                    }
                } else {
                    RequestFormDTO requestForm = parsedCSV.getRequestObj();
                    RequestValidator validator = new RequestValidator(new RequestLibraryValidator(), services);
                    ValidationStatus validation = validator.isValid(requestForm);
                    if (!roleManager.hasAnnotationSheetModifyPermission(requestForm)) {
                        NgsLimsUtility.setFailMessage(messageBoxComponent, null, "User Error", "You don't have permission to upload this excel");
                    } else if (validation.isValid()) {
                        requestForm.setSampleAnnotationFileName(excelFile.getName());
                         
                        if (checkCemmUser(requestForm.getRequestor().getUser())) {
                            if (requestForm.getBillingInfo().getAddress() == null || requestForm.getBillingInfo().getAddress().isEmpty()) {
                                OrganizationDTO cemm = services.getUserService().getOrganizationByName("CeMM");
                                requestForm.getBillingInfo().setAddress(cemm.getAddress());
                            }

                            if (requestForm.getBillingInfo().getContact() == null || requestForm.getBillingInfo().getContact().isEmpty()) {
                                requestForm.getBillingInfo().setContact(requestForm.getRequestor().getPi().getUserName());
                            }
                        }
                                              
                        this.request = requestForm;

                        NgsLimsUtility.setSuccessMessage(messageBoxComponent, null, "Parsing Success!", "");
                        for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
                            NgsLimsUtility.setWarningMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                        }
                        for (ValidatorMessage message : validation.getValidationMessages()) {
                            NgsLimsUtility.setWarningMessage(messageBoxComponent, null, message.getSummary(), message.getDescription());
                        }
                    } else {
                        this.excelFile = null;
                        for (ValidatorMessage message : validation.getValidationMessages()) {
                            if (ValidatorSeverity.FAIL.equals(message.getType())) {
                                NgsLimsUtility.setFailMessage(messageBoxComponent, null, message.getSummary(), message.getDescription());
                            }
                        }
                    }
                }

            }
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
            if (!isLegalValid()){
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
                    requestIdBean.getLock();

                    File targetPath = sampleAnnotationPath;
                    
                    if (newRequest) {
                        request.setRequestId(requestIdBean.getNextId());
                        targetPath = buildSampleAnnotationPath(request);
                        if (!targetPath.exists()){
                            targetPath.mkdir();
                        }
                    }
                    
                    SampleAnnotationWriter excelWriter = new SampleAnnotationWriter(excelFile,request);
                    excelWriter.writeToFile();
                    
                    if (!excelFile.getParentFile().getAbsolutePath().equals(targetPath.getAbsolutePath())){
                         Files.move(excelFile.toPath(), (new File(targetPath,excelFile.getName())).toPath(), REPLACE_EXISTING);
                    }
                    
                    if (!isRequestorCemm() && (!authFile.getParentFile().getAbsolutePath().equals(targetPath.getAbsolutePath()))){
                        Files.move(authFile.toPath(), (new File(targetPath,authFile.getName())).toPath(), REPLACE_EXISTING);
                        
                    }
                    
                    if (newRequest){
                        FileUtils.deleteDirectory(sampleAnnotationPath);
                        sampleAnnotationPath = targetPath;
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
            try{
                requestIdBean.getLock();
                return DeleteRequestFormBean.deleteRequestInternal(request, roleManager, services, "validationMessages", "requestDeleted.jsf?faces-redirect=true&rid="+request.getRequestId());
            }finally{
                requestIdBean.unlock();
            }
        }
        
        return null;
    }

    public boolean isNewRequest() {
        return newRequest;
    }
    
    protected static File buildSampleAnnotationPath(RequestFormDTO request){
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
