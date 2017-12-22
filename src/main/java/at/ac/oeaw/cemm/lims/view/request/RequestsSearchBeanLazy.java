/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

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
public class RequestsSearchBeanLazy {
    @Inject private RequestDataModel requestDataModel;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    public RequestDataModel getRequestDataModel() {
        System.out.println("Getting dataModel....");
        requestDataModel.setRoleManager(roleManager);
        return requestDataModel;
    }

    public void setRunDataModel(RequestDataModel requestDataModel) {
        this.requestDataModel = requestDataModel;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    
}
