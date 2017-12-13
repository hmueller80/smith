/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class AddSampleBean {
    @Inject private ServiceFactory services;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private RequestDTO selectedRequest;
    
    @PostConstruct
    public void init() {
        selectedRequest = services.getRequestService().getAllRequests().get(0);
    }
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public RequestDTO getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(RequestDTO selectedRequest) {
        this.selectedRequest = selectedRequest;
    }        
    
    public List<RequestDTO> getAllRequests() {
        return services.getRequestService().getAllRequests();
    }
    
}
