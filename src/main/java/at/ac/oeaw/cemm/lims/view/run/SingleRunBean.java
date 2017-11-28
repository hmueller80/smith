/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import java.util.HashSet;
import java.util.Set;
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
public class SingleRunBean {
    private static final String FORM_ID="rundetailsForm";

    @Inject
    ServiceFactory services;

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    private Integer runId;
    private Set<SampleRunDTO> sampleRuns = new HashSet<>();

    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        String rid = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        if (rid != null) {
            runId = Integer.parseInt(rid);
            sampleRuns = services.getRunService().getSampleRunByRunId(runId);
        }
    }

    public Integer getRunId() {
        return runId;
    }

    public Set<SampleRunDTO> getSampleRuns() {
        return sampleRuns;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public String deleteRun() {
         try{
            services.getRunService().bulkDeleteRun(runId);
            return "runDeleted_1?rid="+runId+"&faces-redirect=true";
        }catch(Exception e){
            NgsLimsUtility.setFailMessage(FORM_ID, "delete", "Error in deleting ", e.getMessage());
            return null;
        }
        
    }
}
