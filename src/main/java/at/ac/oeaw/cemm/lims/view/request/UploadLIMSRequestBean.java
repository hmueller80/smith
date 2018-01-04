/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.LibraryDTOValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.RequestFormUploadValidator;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import at.ac.oeaw.cemm.lims.util.MailBean;
import at.ac.oeaw.cemm.lims.util.Preferences;
import at.ac.oeaw.cemm.lims.util.RequestIdBean;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "uploadLIMSRequestBean")
@ViewScoped
public class UploadLIMSRequestBean {
    
    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    @ManagedProperty(value = "#{requestBean}")
    private RequestBean requestBean;
    
    @ManagedProperty(value = "#{requestIdBean}")
    private RequestIdBean requestIdBean;
    
    @Inject
    private ServiceFactory services;
    
    @Inject
    private DTOFactory myDTOFactory;
    
    @Inject private MailBean mailBean;

    
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

    public RequestIdBean getRequestIdBean() {
        return requestIdBean;
    }

    public void setRequestIdBean(RequestIdBean requestIdBean) {
        this.requestIdBean = requestIdBean;
    }
    
    public void submitToLims() {
        if (this.requestBean.submit()) {
            if (roleManager.hasSampleLoadPermission()) {
                RequestFormDTO requestForm = requestBean.getRequest();
                RequestDTO requestToLims = myDTOFactory.getRequestDTO(requestForm);
                RequestFormUploadValidator validator = new RequestFormUploadValidator(new LibraryDTOValidator(true), services);
                ValidationStatus validation = validator.isValid(requestToLims);

                if (validation.isValid()) {
                    try {
                        Set<PersistedEntityReceipt> receipts = services.getRequestService().uploadRequest(requestToLims);
                        sendMailWithReceipts(requestToLims.getRequestorUser(), receipts);
                        NgsLimsUtility.setSuccessMessage("validationMessages", null, "Success!", "Samples uploaded correctly");
                        
                        requestBean.initInternal(requestToLims.getRequestId());

                    } catch (Exception e) {
                        NgsLimsUtility.setFailMessage("validationMessages", null, "Error while persisting request", e.getMessage());
                        System.out.println("Failed upload to DB");
                        e.printStackTrace();

                    }
                }
                
                for (ValidatorMessage message : validation.getValidationMessages()) {
                    if (ValidatorSeverity.FAIL.equals(message.getType())) {
                        NgsLimsUtility.setFailMessage("validationMessages", null, message.getSummary(), message.getDescription());
                    } else {
                        NgsLimsUtility.setWarningMessage("validationMessages", null, message.getSummary(), message.getDescription());
                    }
                }
            } else {
                NgsLimsUtility.setFailMessage("validationMessages", null, "User role error", "You do not have permission to submit samples to LIMS.");
            }
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

        try {
            mailBean.sendRequestIDMail(recipient, message);
        } catch (MessagingException me) {
            NgsLimsUtility.setWarningMessage("validationMessages", null, "Email error", "Sending email acknowledgement failed: MessagingException");
        } catch (UnsupportedEncodingException uee) {
            NgsLimsUtility.setWarningMessage("validationMessages", null, "Email error", "Sending email acknowledgement failed: UnsupportedEncodingException");
        }
    }
    
}
