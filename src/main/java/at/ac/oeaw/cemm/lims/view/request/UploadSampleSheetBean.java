/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.RequestFormBuilder;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.RequestValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.request_form.RequestLibraryValidator;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "uploadSampleSheetBean")
@ViewScoped
public class UploadSampleSheetBean {

    private String destination;
    private String fileName;

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    @ManagedProperty(value = "#{requestBean}")
    private RequestBean requestBean;

    @Inject
    private RequestFormBuilder requestFormBuilder;
    
     @Inject
    private ServiceFactory services;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            String applicationPath = context.getExternalContext().getRealPath("/");
            destination = applicationPath + "upload" + File.separator;
        } catch (UnsupportedOperationException uoe) {
            uoe.printStackTrace();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        String messageBoxComponent = "uploadDialogMsg";

        System.out.println("uploading");
        UploadedFile file = event.getFile();
        if (file == null) {
            NgsLimsUtility.setFailMessage(messageBoxComponent, null, "File upload", "the File is null");
            return;
        }
        System.out.println("getting data");
        try {
            transferFile(file);
        } catch (Exception ex) {
            NgsLimsUtility.setFailMessage(messageBoxComponent, null, "File transfer", "Error while transfering the file: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }
        System.out.println("Parsing data");
        ValidatedCSV<RequestFormDTO> parsedCSV = requestFormBuilder.buildRequestFromExcel(new File(destination + fileName));
        //--------------LOG PARSING STATUS ------------------------------------
        System.out.println("---------Parsed file " + fileName + "----------");
        System.out.println("Is Valid: " + !parsedCSV.getValidationStatus().isFailed());
        System.out.println("Errors");
        for (ParsingMessage message : parsedCSV.getValidationStatus().getFailMessages()) {
            System.out.println(message.getSummary() + ":" + message.getMessage());
        }
        System.out.println("Warnings");
        for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
            System.out.println(message.getSummary() + ":" + message.getMessage());
        }
        //----------------------------------------------------------------------

        
        if (parsedCSV.getValidationStatus().isFailed()) {
            for (ParsingMessage message : parsedCSV.getValidationStatus().getFailMessages()) {
                NgsLimsUtility.setFailMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                System.out.println("Failed validation");
            }
        } else {
            RequestFormDTO requestForm = parsedCSV.getRequestObj();
            RequestValidator validator = new RequestValidator(new RequestLibraryValidator(),services);
            ValidationStatus validation = validator.isValid(requestForm);
            if (!roleManager.hasAnnotationSheetModifyPermission(requestForm)){
                NgsLimsUtility.setFailMessage(messageBoxComponent, null, "User Error", "You don't have permission to upload this excel");
            }else if (validation.isValid()) {
                requestBean.setRequest(requestForm);
                NgsLimsUtility.setSuccessMessage(messageBoxComponent, null, "Parsing Success!", "");
                for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
                    NgsLimsUtility.setWarningMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                }
                for (ValidatorMessage message: validation.getValidationMessages()){
                    NgsLimsUtility.setWarningMessage(messageBoxComponent, null, message.getSummary(), message.getDescription());
                }
            }else{
                for (ValidatorMessage message: validation.getValidationMessages()){
                    NgsLimsUtility.setFailMessage(messageBoxComponent, null, message.getSummary(), message.getDescription());
                }
            }
        }
    }

    private void transferFile(UploadedFile file) throws Exception {
        //writes uploaded file to upload directory
        //String fileName = file.getFileName();
        String fileName = (new File(file.getFileName())).getName();
        this.fileName = fileName;

        InputStream in = file.getInputstream();
        OutputStream out = new FileOutputStream(new File(destination + fileName));

        int reader = 0;
        byte[] bytes = new byte[(int) file.getSize()];
        while ((reader = in.read(bytes)) != -1) {
            out.write(bytes, 0, reader);
        }
        in.close();
        out.flush();
        out.close();

    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean requestBean) {
        this.requestBean = requestBean;
    }

    public RequestFormBuilder getRequestFormBuilder() {
        return requestFormBuilder;
    }

    public void setRequestFormBuilder(RequestFormBuilder requestFormBuilder) {
        this.requestFormBuilder = requestFormBuilder;
    }

    public String getFileName() {
        return fileName;
    }
    

}
