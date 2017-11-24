/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class RunsSearchBeanLazy {
    @Inject private RunLazyDataModel runDataModel;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    public RunLazyDataModel getRunDataModel() {
        System.out.println("Getting dataModel....");
        runDataModel.setRoleManager(roleManager);
        return runDataModel;
    }

    public void setRunDataModel(RunLazyDataModel runDataModel) {
        this.runDataModel = runDataModel;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    
}
