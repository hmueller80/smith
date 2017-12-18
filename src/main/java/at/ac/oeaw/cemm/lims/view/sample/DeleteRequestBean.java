/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.util.LinkedList;
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
    private final static String DEFAULT_LIBRARY_NAME = "All libraries in request";
    
    private LibraryDTO defaultLibrary;
    
    @Inject ServiceFactory services;
    @Inject DTOFactory myDTOFactory;

    @ManagedProperty(value="#{newRoleManager}")
    private NewRoleManager roleManager;
    
    private RequestDTO selectedRequest = null;
    private LibraryDTO selectedLibrary = null;    
    
    @PostConstruct
    public void init() {
        selectedRequest = services.getRequestService().getDeleatableLibrariesInRequests().get(0);
        defaultLibrary = myDTOFactory.getLibraryDTO(DEFAULT_LIBRARY_NAME, null);
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public List<RequestDTO> getRequestsAvailable() {
        return services.getRequestService().getDeleatableLibrariesInRequests();
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
            if (selectedLibrary == null){
                services.getRequestService().deleteAllLibrariesInRequest(selectedRequest);
            }else{
                services.getRequestService().deleteAllSamplesForLibraryAndRequest(selectedLibrary.getId(),selectedRequest.getRequestId());
            }
            NgsLimsUtility.setSuccessMessage(null, null, "Success!", "Deleted request with id "+selectedRequest.getRequestId());
            init();
        }catch(Exception e){
            e.printStackTrace();
            NgsLimsUtility.setFailMessage(null, null, "Error in deleting ", e.getMessage());
        }
    }
    
    public List<LibraryDTO> getDeleatableLibraries() {
        List<LibraryDTO> returnValue = new LinkedList<>();
        returnValue.add(defaultLibrary);
        returnValue.addAll(selectedRequest.getLibrariesMap().values());
        return returnValue;
    }

    public LibraryDTO getSelectedLibrary() {
        if (selectedLibrary == null) {
            return defaultLibrary;
        }
        return selectedLibrary;
    }

    public void setSelectedLibrary(LibraryDTO selectedLibrary) {
         if (selectedLibrary.equals(defaultLibrary)) {
            this.selectedLibrary = null;
        }else {
            this.selectedLibrary = selectedLibrary;
         }
    }  
  
}
