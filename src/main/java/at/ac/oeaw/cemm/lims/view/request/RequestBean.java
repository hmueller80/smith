/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.model.requestform.Library;
import at.ac.oeaw.cemm.lims.model.requestform.RequestForm;
import at.ac.oeaw.cemm.lims.model.requestform.RequestedSample;
import at.ac.oeaw.cemm.lims.model.requestform.Requestor;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "requestBean")
@ViewScoped
public class RequestBean {
    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestForm request;
    private Library newLibrary;
    private boolean isFailed=false;
                
    @ManagedProperty(value = "#{newRoleManager}") private NewRoleManager roleManager;
    
    @PostConstruct
    public void init() {
        System.out.println("Initializing requestBean");
        Requestor requestor = new Requestor(roleManager.getCurrentUser(), roleManager.getPi());
        request = new RequestForm(requestor);
    }

    public RequestForm getRequest() {
        return request;
    }
 
    
    public String getSamples(){         
        try {
            List<RequestedSample> samples = new LinkedList<>();
            for (Library library: request.getLibraries()){
                samples.addAll(library.getSamples());
            }
            String output =   objectMapper.writeValueAsString(samples);            
            return output;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    public void setSamples(String json) {
        isFailed=false;
       
        try {
            System.out.println("SETTER CALLED WITH JSON "+json);
            List<RequestedSample> receivedSamples = objectMapper.readValue(json, new TypeReference<List<RequestedSample>>() {} );
                
            Set<ValidatorMessage> messages = request.addAllSamples(receivedSamples);
            Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("sampleMessage");
            while(messageIterator.hasNext()){
                messageIterator.remove();
            }
            
            for (ValidatorMessage message : messages) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    isFailed = true;
                    NgsLimsUtility.setFailMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                } else {
                    NgsLimsUtility.setWarningMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                }
            }
          
        } catch (IOException ex) {
            isFailed=true;
            NgsLimsUtility.setFailMessage("sampleMessages", null, "Error", ex.getMessage());
        }
    }
    
    public String onFlowProcess(FlowEvent event) {
        if(isFailed) {
            return event.getOldStep();
        }else {
            return event.getNewStep();
        }
    }
    
  
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public void createNewLibrary(){
        newLibrary = new Library();
    }

    public Library getNewLibrary() {
        return newLibrary;
    }
    
    public void saveLibrary(){
        request.addLibrary(newLibrary);
    }        
      
}
