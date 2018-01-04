/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.upload;

import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import at.ac.oeaw.cemm.lims.api.dto.lims.RunDTO;
import at.ac.oeaw.cemm.lims.util.RunIdBean;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import javax.annotation.PostConstruct;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="deleteRunBean")
@ViewScoped
public class DeleteRunBean {
    @Inject ServiceFactory services;
    
    @ManagedProperty(value = "#{runIdBean}")
    private RunIdBean runIdBean;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private RunDTO selectedRun = null;
    
    @PostConstruct
    public void init() {
        selectedRun = services.getRunService().getAllRunsMinimalInfo().get(0);
    }
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public RunIdBean getRunIdBean() {
        return runIdBean;
    }

    public void setRunIdBean(RunIdBean runIdBean) {
        this.runIdBean = runIdBean;
    }

    public List<RunDTO> getRunsAvailable() {
        return services.getRunService().getAllRunsMinimalInfo();
    }

    public RunDTO getSelectedRun() {
        return selectedRun;
    }

    public void setSelectedRun(RunDTO selectedRun) {
        this.selectedRun = selectedRun;
        System.out.println("Selected run is "+selectedRun.getId());
    }
    
    public void deleteRun() {
        try{
            runIdBean.getLock();
            services.getRunService().bulkDeleteRun(selectedRun.getId());
            NgsLimsUtility.setSuccessMessage(null, null, "Success!", "Deleted run for flowcell "+selectedRun.getFlowCell()+"("+selectedRun.getId()+")");
            init();
        }catch(Exception e){
            NgsLimsUtility.setFailMessage(null, null, "Error in deleting ", e.getMessage());
        }finally{
            runIdBean.unlock();
        }
    }
            
}
