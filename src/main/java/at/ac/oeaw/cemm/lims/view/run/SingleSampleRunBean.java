/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.util.List;
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
    
    @Inject
    ServiceFactory services;

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;

    private SampleRunDTO sampleRun;
    private List<String> possibleIndexes;
    
    @PostConstruct
    public void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        String runId = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        String sampleId = (String) context.getExternalContext().getRequestParameterMap().get("sid");

        if (runId != null && sampleId!=null) {
            sampleRun = services.getRunService().getSampleRunById(Integer.parseInt(runId), Integer.parseInt(sampleId));
        }
        
        possibleIndexes=services.getSampleService().getAllIndexes();
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

    public List<String> getPossibleIndexes() {
        return possibleIndexes;
    }
    
    public String getLanesString() {
        StringBuilder sb = null;
        for (String lane: sampleRun.getLanes()) {
            if (sb==null){
                sb=new StringBuilder();
            }else{
                sb.append('\n');
            }
            sb.append(lane);
        }
        if (sb==null){
            return "";
        }else{
            return sb.toString();
        }
    }
    
    public void setLanesString(String lanesString) {
        //TODO
    }
    
    public Integer getLanesRows() {
        return sampleRun.getLanes().size();
    }
    
    public void setFlowCell(String flowCell) {
        //TODO
    }
    
    public String getFlowCell() {
        return sampleRun.getFlowcell();
    }
    
     
    public void setSequencingIndex(String sequencingIndex) {
        //TODO
    }
    
    public String getSequencingIndex() {
        return sampleRun.getSample().getIndex().getIndex();
    }
    
    public void setRunFolder(String runFolder) {
        //TODO
    }
    
    public String getRunFolder() {
        return sampleRun.getRunFolder();
    }
    
     public void setIsControl(String control) {
        //TODO
    }
    
    public String getIsControl() {
        return String.valueOf(sampleRun.getIsControl());
    }
    
}
