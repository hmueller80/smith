package it.iit.genomics.cru.smith.defaults;

import it.iit.genomics.cru.smith.entity.Application;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @(#)NgsLimsUtility.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 *
 * Class handles display of Faces messages..
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class NgsLimsUtility {

    static{
        if(Preferences.getVerbose()){
            System.out.println("init NgsLimsUtility");
        }
    }

    /**
     * Displays success message.
     *
     * @author Francesco Venco
     * @param formId
     * @param component
     * @param summary
     * @param details
     * @since 1.0
     */
    public static void setSuccessMessage(String formId, String component, String summary, String details) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary(summary);
        message.setDetail(details);
        if (formId != null) {
            context.addMessage(formId + ":" + component, message);
        } else {
            context.addMessage(null, message);
        }
    }

    /**
     * Displays fail message.
     *
     * @author Francesco Venco
     * @param formId
     * @param component
     * @param summary
     * @param details
     * @since 1.0
     */
    public static void setFailMessage(String formId, String component, String summary, String details) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        message.setSummary(summary);
        message.setDetail(details);
        if (formId != null) {
            context.addMessage(formId + ":" + component, message);
        } else {
            context.addMessage(null, message);
        }
    }

    /**
     * Displays warning message.
     *
     * @author Francesco Venco
     * @param formId
     * @param component
     * @param summary
     * @param details
     * @since 1.0
     */
    public static void setWarningMessage(String formId, String component, String summary, String details) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_WARN);
        message.setSummary(summary);
        message.setDetail(details);
        if (formId != null) {
            context.addMessage(formId + ":" + component, message);
        } else {
            context.addMessage(null, message);
        }
    }

    /**
     * Dumps details of an application.
     *
     * @author Francesco Venco
     * @param application
     * @return String
     * @since 1.0
     */
    public static String dump(Application application) {
        StringBuilder sb = new StringBuilder();
        sb.append(application.getApplicationname() + "\r\n");
        sb.append(application.getInstrument() + "\r\n");
        sb.append(application.getReadmode() + "\r\n");
        sb.append(application.getDepth() + "\r\n");
        sb.append(application.getReadlength() + "\r\n");
        return sb.toString();
    }

}
