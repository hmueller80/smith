package at.ac.oeaw.cemm.lims.view.upload;

import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import at.ac.oeaw.cemm.lims.model.parser.sampleCSV.RequestBuilder;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.util.MailBean;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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

    private final static String FORM_ID = "sampleTableUploadProcessForm";
    private final static String COMPONENT = "RequestUploadProcessButton";

    private final static Set<String> ALLOWED_ROLES = new HashSet<String>() {
        {
            add(Preferences.ROLE_ADMIN);
            add(Preferences.ROLE_TECHNICIAN);
            add(Preferences.ROLE_GROUPLEADER);
        }
    };

    protected UserDTO loggeduser;
    protected String userLogin;
    private String destination;

    private String filename = "test";

    @ManagedProperty("#{newRoleManager}")
    protected NewRoleManager roleManager;

    @Inject private MailBean mailBean;
    @Inject private ServiceFactory services;
    @Inject private RequestBuilder sampleRequestBuilder;
    
    int progress;
    
    public UploadSampleRequestNewBean() {
        System.out.println("Initializing UploadSampleRequestNewBean");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("UploadSampleRequestNewBean post construct");

        FacesContext context = FacesContext.getCurrentInstance();
        loggeduser = roleManager.getCurrentUser();
        userLogin = this.loggeduser.getLogin();
        try {
            String applicationPath = context.getExternalContext().getRealPath("/");
            destination = applicationPath + "upload" + File.separator;
            if (Preferences.getVerbose()) {
                System.out.println(destination);
            }

        } catch (UnsupportedOperationException uoe) {
            uoe.printStackTrace();
        }
    }

    /**
     * Action listener for FileUploadEvent.
     *
     * @author Heiko Muller
     * @param event - a file upload event
     * @since 1.0
     */
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("uploading");
        UploadedFile uploadedFile = event.getFile();
        if (uploadedFile != null) {
            FacesMessage msg = new FacesMessage("Successful", uploadedFile.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        System.out.println("getting data");
        transferFile(uploadedFile);
        System.out.println(filename);
        System.out.println("upload completed");
    }

    /**
     * Submits uploaded sample requests.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void submitRequest() {

        ValidatedCSV<RequestDTO> request = sampleRequestBuilder.buildRequestFromCSV(new File(destination + filename),services);

        System.out.println("---------Parsed file " + filename + "----------");
        System.out.println("Is Valid: " + !request.getValidationStatus().isFailed());
        System.out.println("Errors");
        for (ParsingMessage message : request.getValidationStatus().getFailMessages()) {
            System.out.println(message.getSummary() + ":" + message.getMessage());
        }
        System.out.println("Warnings");
        for (ParsingMessage message : request.getValidationStatus().getWarningMessages()) {
            System.out.println(message.getSummary() + ":" + message.getMessage());
        }

        if (request.getValidationStatus().isFailed()) {
            for (ParsingMessage message : request.getValidationStatus().getFailMessages()) {
                NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, message.getSummary(), message.getMessage());
                System.out.println("Failed validation");
            }
        } else {
            RequestDTO requestObj = request.getRequestObj();
            UserDTO requestor = checkPermission(requestObj.getRequestor());
            if (requestor != null) {
                try {
                    for (ParsingMessage message : request.getValidationStatus().getWarningMessages()) {
                        NgsLimsUtility.setWarningMessage(FORM_ID, COMPONENT, message.getSummary(), message.getMessage());
                    }
                    Set<PersistedEntityReceipt> receipts = services.getRequestUploadService().uploadRequest(requestObj);
                    sendMailWithReceipts(requestor, receipts);
                } catch (Exception e) {
                    NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "Error while persisting request", e.getMessage());
                    System.out.println("Failed upload to DB");

                }
            } else {
                System.out.println("Failed user verification");
            }
        }

    }

    private UserDTO checkPermission(String userInCSV) {

        UserDTO u = services.getUserService().getUserByLogin(userInCSV);
        if (u == null) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "User error", "User " + userInCSV + " not found in DB");
            return null;
        }

        String userRole = u.getUserRole();
        if (userRole.equals(Preferences.ROLE_GUEST)) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "User error", "Guest cannot submit samples");
            return null;
        }

        if (u.getLogin().equals(userLogin)) {
            //TODO: double check if this is valid: This means that the user is able to upload its own data!!!!
            return u;
        } else if (ALLOWED_ROLES.contains(this.loggeduser.getUserRole())) {
            return u;
        } else {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "User role error", "You do not have permission to submit samples for this user.");
            return null;
        }
    }

    /**
     * Copies uploaded file to local disk.
     *
     * @author Heiko Muller
     * @param file - UploadedFile
     * @since 1.0
     */
    private void transferFile(UploadedFile file) {
        //String fileName = file.getFileName();
        String fileName = (new File(file.getFileName())).getName();
        this.filename = fileName;
        try {
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

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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

        //for testing
        //recipient[0] = "heiko.muller@ieo.eu";
        //recipient[1] = "heiko.muller@ieo.eu";
        try {
            mailBean.sendRequestIDMail(recipient, message);
        } catch (MessagingException me) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "Email error", "Sending email acknowledgement failed: MessagingException");
        } catch (UnsupportedEncodingException uee) {
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "Email error", "Sending email acknowledgement failed: UnsupportedEncodingException");
        }
    }

    /**
     * Getter for filename.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getFilename() {
        return filename;
    }

    public int getProgress() {
        return progress;
    }

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


    public MailBean getMailBean() {
        return mailBean;
    }

    public void setMailBean(MailBean mailBean) {
        this.mailBean = mailBean;
    }

}
