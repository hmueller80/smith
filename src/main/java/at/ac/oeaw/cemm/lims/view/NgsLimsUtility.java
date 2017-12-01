package at.ac.oeaw.cemm.lims.view;

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
            context.addMessage(formId, message);
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
            context.addMessage(formId, message);
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
            context.addMessage(formId, message);
        } else {
            context.addMessage(null, message);
        }
    }


}
