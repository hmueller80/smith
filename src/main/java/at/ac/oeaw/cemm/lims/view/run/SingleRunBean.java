/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import it.iit.genomics.cru.smith.news.NewsHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public boolean isRunIsClosed() {
        for (SampleRunDTO sampleRun : sampleRuns) {
            String status = sampleRun.getSample().getStatus();
            if (SampleDTO.status_queued.equals(status)
                    || SampleDTO.status_confirmed.equals(status)
                    || SampleDTO.status_requested.equals(status)) {
                return false;
            }
        }

        return true;
    }

    public boolean isAllConfirmed() {
        for (SampleRunDTO sampleRun : sampleRuns) {
            String status = sampleRun.getSample().getStatus();
            if (!SampleDTO.status_confirmed.equals(status)) {
                return false;
            }
        }

        return true;
    }

    public String closeRun() {
        List<SampleDTO> samplesToUpdate = new ArrayList<>();

        for (SampleRunDTO sampleRun : sampleRuns) {
            SampleDTO sample = sampleRun.getSample();
            sample.setStatus(SampleDTO.status_running);
            samplesToUpdate.add(sample);
        }

        services.getSampleService().bulkUpdateSamples(samplesToUpdate);

        return "runDetails.jsf?rid=" + runId + " faces-redirect=true";
    }

    public void resetRun() {
        //TODONewsHelper.resetRun(runID);
        //System.out.println("delete news for run " + runID);
    }
}
