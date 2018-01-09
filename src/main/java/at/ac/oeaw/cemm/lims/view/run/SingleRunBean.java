/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.analysis.AnalysisManager;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.util.RunIdBean;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class SingleRunBean {

    @Inject
    ServiceFactory services;
    
    @Inject
    AnalysisManager analysisManager;
    
    @ManagedProperty(value = "#{runIdBean}")
    private RunIdBean runIdBean;

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    
    @ManagedProperty(value = "#{runFileManagerBean}")
    private RunFileManagerBean fileManager;

    private Integer runId;
    private List<SingleRunTableRow> allSamples = new LinkedList<>();
    private String flowcell;
    private String runFolder;
    private UserDTO operator;

    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        String rid = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        if (rid != null) {
            runId = Integer.parseInt(rid);
            boolean firstResult = true;
            Set<SampleRunDTO> sampleRunsDTO = services.getRunService().getSampleRunByRunId(runId);
            for (SampleRunDTO sampleRun : sampleRunsDTO) {
                if (firstResult) {
                    this.flowcell = sampleRun.getFlowcell();
                    this.runFolder = sampleRun.getRunFolder();
                    this.operator = sampleRun.getOperator();
                    firstResult = false;
                }
                for (String lane : sampleRun.getLanes()) {
                    allSamples.add(new SingleRunTableRow(lane, sampleRun.getSample().getLibraryName(), sampleRun.getSample()));
                }
            }
            Collections.sort(allSamples, new Comparator<SingleRunTableRow>() {
                @Override
                public int compare(SingleRunTableRow o1, SingleRunTableRow o2) {
                    Integer laneComparison = o1.getLane().compareTo(o2.getLane());
                    if (laneComparison==0){
                        Integer libraryComparison = o1.getSample().getLibraryName().compareTo(o2.getSample().getLibraryName());
                        if (libraryComparison == 0){
                            return o1.getSample().getId().compareTo(o2.getSample().getId());
                        }else{
                            return libraryComparison;
                        }
                    }else{
                        return laneComparison;
                    }
                }
            });
            fileManager.init(runId, runFolder);
        }
    }
    
    public void hasViewPermission() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!roleManager.getHasRunAddPermission()) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, "/error401.xhtml");
        }
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public RunFileManagerBean getFileManager() {
        return fileManager;
    }

    public void setFileManager(RunFileManagerBean fileManager) {
        this.fileManager = fileManager;
    }
    
    

    public RunIdBean getRunIdBean() {
        return runIdBean;
    }

    public void setRunIdBean(RunIdBean runIdBean) {
        this.runIdBean = runIdBean;
    }

    public Integer getRunId() {
        return runId;
    }

    public List<SingleRunTableRow> getAllSamples() {
        return allSamples;
    }

    public String getFlowcell() {
        return flowcell;
    }

    public String getRunFolder() {
        return runFolder;
    }

    public UserDTO getOperator() {
        return operator;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public String deleteRun() {
        try {
            runIdBean.getLock();
            services.getRunService().bulkDeleteRun(runId);
            return "runDeleted_1?rid=" + runId + "&faces-redirect=true";
        } catch (Exception e) {
            NgsLimsUtility.setFailMessage("delete", null, "Error in deleting ", e.getMessage());
            return null;
        }finally{
            runIdBean.unlock();
        }
    }
    
    public void resetDemux(){
         try {
            analysisManager.resetDemux(runFolder);
        } catch (Exception e) {
            NgsLimsUtility.setFailMessage("delete", null, "Error in resetting Demux ", e.getMessage());
        }
    }
    
    public boolean demuxStarted(){
        String newsBody = "Fastq analysis for " + runFolder + " has started.";
        return services.getNewsService().newsExists(newsBody);
    }
}
