/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
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
@ManagedBean(name="deleteRequestBean")
@ViewScoped
public class DeleteRequestBean {
    @Inject ServiceFactory services;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private RequestDTO selectedRequest = null;
    
    @PostConstruct
    public void init() {
        selectedRequest = services.getSampleService().getDeleatableRequests().get(0);
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
      public List<RequestDTO> getRequestsAvailable() {
        return services.getSampleService().getDeleatableRequests();
    }

    public RequestDTO getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(RequestDTO selectedRequest) {
        this.selectedRequest = selectedRequest;
        System.out.println("Selected request is "+selectedRequest.getRequestId());
    }
    
    public void deleteRequest() {
        try{
            services.getSampleService().deleteAllSamplesForRequest(selectedRequest.getRequestId());
            NgsLimsUtility.setSuccessMessage(null, null, "Success!", "Deleted request with id "+selectedRequest.getRequestId());
            init();
        }catch(Exception e){
            NgsLimsUtility.setFailMessage(null, null, "Error in deleting ", e.getMessage());
        }
    }
}
