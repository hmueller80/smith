/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class SingleSampleRunBean {
    private static final String FORM_ID = "SamplerundetailsForm";

    @Inject
    ServiceFactory services;

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    private SampleRunDTO sampleRun;
    
    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        String runId = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        String sampleId = (String) context.getExternalContext().getRequestParameterMap().get("sid");

        if (runId != null && sampleId!=null) {
            sampleRun = services.getRunService().getSampleRunById(Integer.parseInt(runId), Integer.parseInt(sampleId));
        }
        
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public SampleRunDTO getSampleRun() {
        return sampleRun;
    }
    
}
