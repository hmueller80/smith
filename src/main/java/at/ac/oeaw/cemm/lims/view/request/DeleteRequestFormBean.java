/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.io.File;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author dbarreca
 */

@ManagedBean(name="deleteRequestFormBean")
@ViewScoped
public class DeleteRequestFormBean {
    
    @Inject ServiceFactory services;
    
    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private RequestFormDTO selectedRequest = null;
    
    @PostConstruct
    public void init() {
        List<RequestFormDTO> deleatableRequests = services.getRequestFormService().getDeleatableRequests();
        if (!deleatableRequests.isEmpty()){
            selectedRequest = services.getRequestFormService().getDeleatableRequests().get(0);
        }
    }
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public List<RequestFormDTO> getDeleatableRequests() {
        return services.getRequestFormService().getDeleatableRequests();
    }

    public RequestFormDTO getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(RequestFormDTO selectedRequest) {
        System.out.println("Setting request with id "+selectedRequest.getRequestId());
        this.selectedRequest = selectedRequest;
    }
    
     public void deleteRequest(){
        deleteRequestInternal(selectedRequest,roleManager,services,null,null);
        init();
     }
    
    protected static String deleteRequestInternal(
            RequestFormDTO request, 
            NewRoleManager roleManager, 
            ServiceFactory services, 
            String messageComponent,
            String redirectURL) {
        try{
            if(RequestFormDTO.STATUS_NEW.equals(request.getStatus()) && roleManager.hasAnnotationSheetDeletePermission()){
                services.getRequestFormService().bulkDeleteRequest(request.getRequestId());
                File path = RequestBean.buildSampleAnnotationPath(request);

                if (path.exists()) {
                    FileUtils.deleteDirectory(path);
                }
                
                NgsLimsUtility.setSuccessMessage(messageComponent, null, "Success!", "Deleted request with id "+request.getRequestId()
                        +" requested by "+request.getRequestor().getUser().getLogin());
                
                return "requestDeleted.jsf?faces-redirect=true&rid="+request.getRequestId();
            }else{
                NgsLimsUtility.setFailMessage("validationMessages", null, "Error in deleting ", "This request cannot be deleted due to status or user role");
            }
        }catch(Exception e){
            NgsLimsUtility.setFailMessage("validationMessages", null, "Error in deleting ", e.getMessage());
        }
        
        return null;
    }
            
}
