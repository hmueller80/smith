/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.request;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.model.dto.request_form.RequestLibraryDTOImpl;
import at.ac.oeaw.cemm.lims.model.dto.request_form.RequestSampleDTOImpl;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorException;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.request_form.RequestLibrariesValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.request_form.RequestLibraryValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.request_form.RequestSamplesValidator;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "requestBean")
@ViewScoped
public class RequestBean {

    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestFormDTO request;
    private boolean areSamplesFailed = false;
    private boolean areLibrariesFailed = false;
    private String wizardStep = "personal";

    @ManagedProperty(value = "#{newRoleManager}")
    private NewRoleManager roleManager;
    @Inject
    private RequestDTOFactory dtoFactory;

    @PostConstruct
    public void init() {
        System.out.println("Initializing requestBean");
        RequestorDTO requestor = dtoFactory.getRequestorDTO(roleManager.getCurrentUser(), roleManager.getPi());
        request = dtoFactory.getRequestFormDTO(requestor);
    }

    public RequestFormDTO getRequest() {
        return request;
    }

    public String getSamples() {
        try {
            List<RequestSampleDTO> samples = new LinkedList<>();
            for (RequestLibraryDTO library : request.getLibraries()) {
                samples.addAll(library.getSamples());
            }
            String output = objectMapper.writeValueAsString(samples);
            return output;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public void setSamples(String json) {
        areSamplesFailed = false;
        Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("sampleMessage");
        while (messageIterator.hasNext()) {
            messageIterator.remove();
        }
        request.resetLibraries();
        
        try {
            List<RequestSampleDTO> receivedSamples = objectMapper.readValue(json, new TypeReference<List<RequestSampleDTOImpl>>() {
            });
            
            RequestSamplesValidator samplesValidator = new RequestSamplesValidator(receivedSamples);
            
            try {
                receivedSamples = samplesValidator.getValidatedObject();
            } catch (ValidatorException ex) {
                areSamplesFailed = true;
                receivedSamples = samplesValidator.forceGetObect();
            }            
            Set<ValidatorMessage> messages = samplesValidator.getMessages();
                  
            Map<String, RequestLibraryDTO> libraries = new LinkedHashMap<>();
            for (RequestLibraryDTO existingLibrary: request.getLibraries()){
                libraries.put(existingLibrary.getName(), existingLibrary);
            }
            
            for (RequestSampleDTO sample : receivedSamples) {
                RequestLibraryDTO library = libraries.get(sample.getLibrary());
                if (library == null) {
                    library = dtoFactory.getRequestLibraryDTO();
                    library.setName(sample.getLibrary());
                    libraries.put(library.getName(), library);
                }
                library.addSample(sample);
            }
            
            for (RequestLibraryDTO library: libraries.values()) {
                RequestLibraryValidator libraryValidator = new RequestLibraryValidator(library);
                RequestLibraryDTO validatedLibrary;
                try {
                    validatedLibrary = libraryValidator.getValidatedObject();
                } catch (ValidatorException ex) {
                    areSamplesFailed = true;
                    validatedLibrary = libraryValidator.forceGetObect();
                }
                request.addLibrary(validatedLibrary);
                messages.addAll(libraryValidator.getMessages());
            }
            
            request.removeEmptyLibraries();
            
            for (ValidatorMessage message : messages) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                } else {
                    NgsLimsUtility.setWarningMessage("sampleMessage", null, message.getSummary(), message.getDescription());
                }
            }

        } catch (IOException ex) {
            areSamplesFailed = true;
            NgsLimsUtility.setFailMessage("sampleMessages", null, "Error", ex.getMessage());
        }
    }

    public String getLibraries() {
        try {
            List<RequestLibraryDTO> libraries = new LinkedList<>();
            for (RequestLibraryDTO library : request.getLibraries()) {
                libraries.add(library.getCopyWithoutSamples());
            }
            String output = objectMapper.writeValueAsString(libraries);
            return output;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RequestBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public void setLibraries(String json) {
        areLibrariesFailed = false;
        Iterator messageIterator = FacesContext.getCurrentInstance().getMessages("libraryMessage");
        while (messageIterator.hasNext()) {
            messageIterator.remove();
        }

        try {
            List<RequestLibraryDTO> receivedLibraries = objectMapper.readValue(json, new TypeReference<List<RequestLibraryDTOImpl>>() {
            });

            RequestLibrariesValidator librariesValidator = new RequestLibrariesValidator(receivedLibraries);

            try {
                receivedLibraries = librariesValidator.getValidatedObject();
            } catch (ValidatorException ex) {
                receivedLibraries = librariesValidator.forceGetObect();
                areLibrariesFailed = true;
            }

            for (ValidatorMessage message : librariesValidator.getMessages()) {
                if (message.getType().equals(ValidatorSeverity.FAIL)) {
                    NgsLimsUtility.setFailMessage("libraryMessage", null, message.getSummary(), message.getDescription());
                } else {
                    NgsLimsUtility.setWarningMessage("libraryMessage", null, message.getSummary(), message.getDescription());
                }
            }

            for (RequestLibraryDTO library : receivedLibraries) {
                RequestLibraryDTO existingLibrary = request.getLibraryByUUID(library.getUuid());
                if (existingLibrary != null) {
                    existingLibrary.setParametersFromCopy(library);
                } else {
                    areLibrariesFailed = true;
                    NgsLimsUtility.setFailMessage("libraryMessage", null, "Server Error", "Inconsistency between libraries");
                }
            }

        } catch (IOException ex) {
            areLibrariesFailed = true;
            NgsLimsUtility.setFailMessage("libraryMessage", null, "Server Error", ex.getMessage());
        }
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getOldStep().equals("libraries") && areLibrariesFailed) {
            wizardStep = event.getOldStep();
        } else if (event.getOldStep().equals("samples")) {
            if (areSamplesFailed) {
                wizardStep = event.getOldStep();
            } else if (event.getNewStep().equals("libraries") && request.getLibraries().isEmpty()) {
                wizardStep = event.getOldStep();
            } else {
                wizardStep = event.getNewStep();
            }
        } else {
            wizardStep = event.getNewStep();
        }

        return wizardStep;
    }

    public String getWizStep() {
        return wizardStep;
    }

}