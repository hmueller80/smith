package at.ac.oeaw.cemm.lims.view.upload;

import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import at.ac.oeaw.cemm.lims.model.parser.sampleCSV.RequestBuilder;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.parser.sampleCSV.SamplesCSVManager;
import at.ac.oeaw.cemm.lims.util.MailBean;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import at.ac.oeaw.cemm.lims.util.Preferences;
import at.ac.oeaw.cemm.lims.util.SampleLock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * @(#)UploadSampleRequestBean.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Backing bean for uploading of sample requests.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "uploadSampleRequestNewBean")
@SessionScoped
public class UploadSampleRequestNewBean implements Serializable {

    private String destination;
    private String filename = "test";
    private ValidatedCSV<RequestDTO> parsedCSV = null;
    
    @ManagedProperty("#{newRoleManager}")
    protected NewRoleManager roleManager;

    @ManagedProperty(value = "#{sampleLock}")
    private SampleLock sampleLock;
    
    @Inject private MailBean mailBean;
    @Inject private ServiceFactory services;
    @Inject private RequestBuilder sampleRequestBuilder;
        
   
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String applicationPath = context.getExternalContext().getRealPath("/");
        destination = applicationPath + "upload" + File.separator;
    }


    public void handleFileUpload(FileUploadEvent event) {
        String messageBoxComponent = "uploadDialogMsg";

        if (roleManager.hasSampleLoadPermission()) {
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

            parsedCSV = sampleRequestBuilder.buildRequestFromCSV(new File(destination + filename));
            //--------------LOG PARSING STATUS ------------------------------------
            System.out.println("---------Parsed file " + filename + "----------");
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
                NgsLimsUtility.setSuccessMessage(messageBoxComponent, null, "Parsing Success!", "");
                for (ParsingMessage message : parsedCSV.getValidationStatus().getWarningMessages()) {
                    NgsLimsUtility.setWarningMessage(messageBoxComponent, null, message.getSummary(), message.getMessage());
                }
            }
        } else {
            NgsLimsUtility.setFailMessage(messageBoxComponent, null, "User role error", "You do not have permission to submit samples");
        }
    }

   
    public void submitRequest() {
        if (roleManager.hasSampleLoadPermission()) {
            if (!parsedCSV.getValidationStatus().isFailed()) {
                RequestDTO requestObj = parsedCSV.getRequestObj();
                
                try {
                    sampleLock.lock();
                    if (requestObj.getRequestId() >= sampleLock.getRequestIdLock().getNextId()){
                        SamplesCSVManager.writeToFile(requestObj);
                        Set<PersistedEntityReceipt> receipts = services.getRequestService().uploadRequest(requestObj);
                        sendMailWithReceipts(requestObj.getRequestorUser(), receipts);
                        NgsLimsUtility.setSuccessMessage(null, null, "Success!", "Samples uploaded correctly");
                    }else{
                        NgsLimsUtility.setFailMessage(null, null, "Error while persisting request", "Samples or requests with the same submission Id already exists");
                    }
                } catch (Exception e) {
                    NgsLimsUtility.setFailMessage(null, null, "Error while persisting request", e.getMessage());
                    System.out.println("Failed upload to DB");
                    e.printStackTrace();
                }finally {                  
                    sampleLock.unlock();
                }
            } else {
                NgsLimsUtility.setFailMessage(null, null, "Error while parsing the request", "Malformed CSV");
            }
        } else {
            NgsLimsUtility.setFailMessage(null, null, "User role error", "You do not have permission to submit samples for this user.");
        }
    }

   
    private void transferFile(UploadedFile file) throws Exception {
        //String fileName = file.getFileName();
        String fileName = (new File(file.getFileName())).getName();
        this.filename = fileName;
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

    private void sendMailWithReceipts(UserDTO user, Set<PersistedEntityReceipt> receipts) {

        StringBuilder sb = new StringBuilder("");
        for (PersistedEntityReceipt receipt : receipts) {
            sb.append("<tr><td>" + receipt.getId() + "</td><td>" + receipt.getEntityName() + "</td></tr>");
        }

        String message = mailBean.composeRequestReceivedMessage(user.getFirstName(), sb.toString());
        String[] recipient = new String[2];
        recipient[0] = user.getMailAddress();
        recipient[1] = Preferences.getSentByMailAddress();

        try {
            mailBean.sendRequestIDMail(recipient, message);
        } catch (MessagingException me) {
            NgsLimsUtility.setWarningMessage(null, null, "Email error", "Sending email acknowledgement failed: MessagingException");
        } catch (UnsupportedEncodingException uee) {
            NgsLimsUtility.setWarningMessage(null, null, "Email error", "Sending email acknowledgement failed: UnsupportedEncodingException");
        }
    }

    public String getFilename() {
        return filename;
    }

   
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

 
    public MailBean getMailBean() {
        return mailBean;
    }

    public void setMailBean(MailBean mailBean) {
        this.mailBean = mailBean;
    }

    public SampleLock getSampleLock() {
        return sampleLock;
    }

    public void setSampleLock(SampleLock sampleLock) {
        this.sampleLock = sampleLock;
    }
    

    public ValidatedCSV<RequestDTO> getParsedCSV() {
        return parsedCSV;
    }

    public void setParsedCSV(ValidatedCSV<RequestDTO> parsedCSV) {
        this.parsedCSV = parsedCSV;
    }
}
