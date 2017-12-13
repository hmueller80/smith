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
import at.ac.oeaw.cemm.lims.util.NameFilter;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashSet;
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
    private boolean areSamplesFailed=false;
    private boolean areLibrariesFailed=false;
    private String wizardStep = "personal";
                
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
        areSamplesFailed=false;
       
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
                    areSamplesFailed = true;
                    NgsLimsUtility.setFailMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                } else {
                    NgsLimsUtility.setWarningMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                }
            }
          
        } catch (IOException ex) {
            areSamplesFailed=true;
            NgsLimsUtility.setFailMessage("sampleMessages", null, "Error", ex.getMessage());
        }
    }
    
      public String getLibraries(){         
        try {
            List<Library> libraries = new LinkedList<>();
            for (Library library: request.getLibraries()){
                libraries.add(library.getCopyWithoutSamples());
            }
            String output =   objectMapper.writeValueAsString(libraries);            
            return output;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
      
    public void setLibraries(String json) {
        areLibrariesFailed= false;
        try {
            System.out.println("SETTER CALLED WITH JSON " + json);
            List<Library> receivedLibraries = objectMapper.readValue(json, new TypeReference<List<Library>>() {
            });
            Set<String> libraryNames = new HashSet<>();
            for (Library library : receivedLibraries) {

                String libraryName = library.getName();
                if (isStringValid(libraryName, "Library Name")) {
                    libraryName = NameFilter.legalize(libraryName.trim());
                    library.setName(libraryName);
                    if (libraryNames.contains(libraryName)) {
                        areLibrariesFailed = true;
                        NgsLimsUtility.setFailMessage("libraryMessage", null, "Library Name", "There are duplicate libraries");
                    } else {
                        libraryNames.add(libraryName);
                    }
                }

                isStringValid(library.getType(), "Application");
                
                if (isStringValid(library.getReadMode(), "Read Mode")) {
                    String readMode = library.getReadMode().trim().toUpperCase();
                    if (!"PE".equals(readMode) && !"SR".equals(readMode)) {
                        areLibrariesFailed = true;
                        NgsLimsUtility.setFailMessage("libraryMessage", null, "Read Mode", "Read Mode must be PE or SR");
                    }
                }
                
                isIntegerValid(library.getReadLength(),"Read Length");
                isIntegerValid(library.getLanes(),"Lanes");
                isDoubleValid(library.getVolume(),"Volume");
                isDoubleValid(library.getDnaConcentration(),"DNA Concentration");
                isDoubleValid(library.getTotalSize(),"Total Size");
                
                if (!areLibrariesFailed) {        
                    System.out.println("Received library "+library.getName()+" UUID "+library.getUuid());

                    for (Library existingLibrary : request.getLibraries()) {
                        if (library.getUuid().equals(existingLibrary.getUuid())){
                            System.out.println("Existing library "+existingLibrary.getName()+" UUID "+existingLibrary.getUuid());
                            existingLibrary.setParametersFromCopy(library);
                        }
                    }
                }
            }            
        } catch (IOException ex) {
            areLibrariesFailed = true;
            NgsLimsUtility.setFailMessage("libraries", null, "Error", ex.getMessage());
        }
    }
    
    private boolean isStringValid(String theString, String theField){
          if (theString== null || theString.trim().isEmpty()){
            NgsLimsUtility.setFailMessage("libraryMessage", null, theField,theField+" cannot be empty");
            areLibrariesFailed=true;
            return false;
          }         
          return true;
    }
    
    private boolean isIntegerValid(Integer theValue, String theField){
          if (theValue== null || theValue<=0){
            NgsLimsUtility.setFailMessage("libraryMessage", null, theField,theField+" must be a positive integer");
            areLibrariesFailed=true;
            return false;
          }         
          return true;
    }
    
     private boolean isDoubleValid(Double theValue, String theField){
          if (theValue== null || theValue<0){
            NgsLimsUtility.setFailMessage("libraryMessage", null, theField,theField+" must be a positive double");
            areLibrariesFailed=true;
            return false;
          }         
          return true;
    }
    
    
    public String onFlowProcess(FlowEvent event) {
        if(areLibrariesFailed) {
            wizardStep = "libraries";
        }else if (areSamplesFailed){
            wizardStep = "samples";
        }else {
            wizardStep = event.getNewStep();
        }
        return wizardStep;
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
    
    public String getWizStep() {
        return wizardStep;
    }
      
}
