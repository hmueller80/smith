/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.parser.CSVValidationStatus;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.LibrarySubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SampleSubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SequencingRequestSubmission;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.io.File;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
public class RequestFormBuilder {
    @Inject private RequestDTOFactory myDTOFactory;
    @Inject private DTOFactory limsDTOFactory;
    @Inject private ServiceFactory services;
    
     public ValidatedCSV<RequestFormDTO> buildRequestFromExcel(File excelFile) {
        CSVValidationStatus status = new CSVValidationStatus();
         
        try {
            SampleAnnotationParser parser = new SampleAnnotationParser(excelFile);
                   
            UserDTO requestorUser = services.getUserService().getUserByMail(parser.getSummary().getContactPersonEmail());
            UserDTO piUser = null;
            if (requestorUser!=null){
                piUser = services.getUserService().getUserByID(requestorUser.getPi());
            }else{
                requestorUser = limsDTOFactory.getUserDTO(null, 
                        parser.getSummary().getContactPersonName(), 
                        null, 
                        parser.getSummary().getContactPersonPhone(), 
                        parser.getSummary().getContactPersonEmail(), 
                        null, 
                        "user",
                        limsDTOFactory.getAffiliationDTO());
            }
            
            RequestorDTO requestor = myDTOFactory.getRequestorDTO(requestorUser, piUser);
            RequestFormDTO requestDTO = myDTOFactory.getRequestFormDTO(requestor);
            
            for (SequencingRequestSubmission sequencingRequest: parser.getSequencingRequestSubmission()){
                RequestLibraryDTO libraryDTO = getLibrary(sequencingRequest);
                requestDTO.addLibrary(libraryDTO);
            }
            
            for (LibrarySubmission libraryRequest: parser.getLibrarySubmission()){
                boolean found = false;
                
                RequestSampleDTO sampleDTO = getSample(libraryRequest);
                for (RequestLibraryDTO libraryDTO: requestDTO.getLibraries()){
                    if (libraryDTO.getName().equals(sampleDTO.getLibrary())){
                        found = true;
                        enrichLibrary(libraryDTO, libraryRequest);
                        libraryDTO.addSample(sampleDTO);
                    }
                }
                
                if(!found){
                    throw new ParsingException(
                            "Sequencing Request",
                            "Sequencing Request not found for library"
                                    +libraryRequest.getLibraryName()
                                    +" and sample "+libraryRequest.getSampleName());
                }
            }
            
            for (SampleSubmission sampleRequest: parser.getSampleSubmission()) {
                boolean found = false;
                 for (RequestLibraryDTO libraryDTO: requestDTO.getLibraries()){
                    for (RequestSampleDTO sampleDTO: libraryDTO.getSamples()){
                        if (sampleDTO.getName().equals(NameFilter.legalizeSampleName(sampleRequest.getSampleName()))){
                            found = true;
                            enrichSample(sampleDTO,sampleRequest);
                        }
                    }
                }
                
                if(!found){
                    throw new ParsingException(
                            "Library",
                            "Library (in sheet Libraries) not found for sample "
                                    +sampleRequest.getSampleName());
                }                
            }
            
            return new ValidatedCSV<>(requestDTO,status);
            
            
        } catch (ParsingException ex) {
            status.addFailMessage(ex.getSummary(), ex.getMessage());
            return new ValidatedCSV<>(null,status);
        }
     }
     
    private RequestLibraryDTO getLibrary(SequencingRequestSubmission seqRequest) throws ParsingException {
        RequestLibraryDTO libraryDTO = myDTOFactory.getEmptyRequestLibraryDTO(false);
        libraryDTO.setName(seqRequest.getLibraryName());
        libraryDTO.setReadMode(seqRequest.getSequencingType());
        libraryDTO.setReadLength(
                getInteger(seqRequest.getReadLength(), ExcelParserConstants.readLength + " for library " + libraryDTO.getName(),null)
        );
        libraryDTO.setLanes(
                getInteger(seqRequest.getNumberofLanes(), ExcelParserConstants.numberofLanes + " for library " + libraryDTO.getName(),null)
        );
        return libraryDTO;
    }

    private void enrichLibrary(RequestLibraryDTO library, LibrarySubmission libRequest) throws ParsingException {
        String reqAppName = libRequest.getLibraryType();
        Double reqVolume = getDouble(libRequest.getLibraryVolume(), ExcelParserConstants.LibraryVolume + " for sample " + libRequest.getSampleName(),0.0);
        Double reqDnaConcentration = getDouble(libRequest.getLibraryDNAConcentration(), ExcelParserConstants.LibraryDNAConcentration + " for sample " + libRequest.getSampleName(),0.0);
        Double reqSize = getDouble(libRequest.getLibraryTotalSize(), ExcelParserConstants.LibraryTotalSize + " for sample " + libRequest.getSampleName(),0.0);

        if (library.getApplicationName() == null) {
            library.setApplicationName(reqAppName);
        } else if (!library.getApplicationName().equals(reqAppName)) {
            throw new ParsingException(
                    "Library Type",
                    "Inconsistent Library Type for library " + library.getName()
                    + ". At least two values found: "
                    + library.getApplicationName()
                    + " and " + reqAppName);
        }

        if (reqVolume != null) {
            if (library.getVolume() == null) {
                library.setVolume(reqVolume);
            } else if (!library.getVolume().equals(reqVolume)) {
                throw new ParsingException(
                        "Library Volume",
                        "Inconsistent Library Volume for library " + library.getName()
                        + ". At least two values found: "
                        + library.getVolume()
                        + " and " + reqVolume);
            }
        }

        if (reqDnaConcentration != null) {
            if (library.getDnaConcentration() == null) {
                library.setDnaConcentration(reqDnaConcentration);
            } else if (!library.getDnaConcentration().equals(reqDnaConcentration)) {
                throw new ParsingException(
                        "DNA Concentration",
                        "Inconsistent DNA Concentration for library " + library.getName()
                        + ". At least two values found: "
                        + library.getDnaConcentration()
                        + " and " + reqDnaConcentration);
            }
        }

        if (reqSize != null) {
            if (library.getTotalSize() == null) {
                library.setTotalSize(reqSize);
            } else if (!library.getTotalSize().equals(reqSize)) {
                throw new ParsingException(
                        "Total Size",
                        "Inconsistent Total Size for library " + library.getName()
                        + ". At least two values found: "
                        + library.getTotalSize()
                        + " and " + reqSize);
            }
        }
    }
    
    private RequestSampleDTO getSample(LibrarySubmission libSubmission){
        RequestSampleDTO sampleDTO = myDTOFactory.getRequestSampleDTO(false);
        sampleDTO.setName(libSubmission.getSampleName());
        
        if ((libSubmission.getLibraryLabel()== null || libSubmission.getLibraryLabel().trim().isEmpty())
            && (libSubmission.getLibraryName()!= null && !libSubmission.getLibraryName().trim().isEmpty())){
            sampleDTO.setLibrary(libSubmission.getLibraryName());
        }else{
            sampleDTO.setLibrary(libSubmission.getLibraryLabel());
        }
        sampleDTO.setI7Index(libSubmission.getBarcodeSequencei7());
        sampleDTO.setI7Adapter(libSubmission.getLibraryAdapteri7());
        sampleDTO.setI5Index(libSubmission.getBarcodeSequencei5());
        sampleDTO.setI5Adapter(libSubmission.getLibraryAdapteri5());
        sampleDTO.setPrimerType(libSubmission.getSequencingPrimerType());
        sampleDTO.setPrimerSequence(libSubmission.getCustomSequencingPrimerSequence());
        sampleDTO.setPrimerName(libSubmission.getCustomSequencingPrimerName());
        
        return sampleDTO;
    }
    
    private void enrichSample(RequestSampleDTO sampleDTO, SampleSubmission sampleReq) throws ParsingException{
        sampleDTO.setSampleDescription(sampleReq.getSampleDescription());
        sampleDTO.setOrganism(sampleReq.getOrganism());
    }

    private Integer getInteger(String string, String fieldName, Integer defaultValue) throws ParsingException {
        Integer theInteger = defaultValue;
        if (string!=null && !string.isEmpty() && !string.equals("null") && !string.equals("NA")){
            try {
                Double parsedDouble = Double.parseDouble(string);
                theInteger = parsedDouble.intValue();
            }catch(NumberFormatException e){
                throw new ParsingException("Number format error","Error while parsing integer "+string+" for field "+fieldName);
            }
        }
        
        return theInteger;
    }
    
     private Double getDouble(String string, String fieldName, Double defaultValue) throws ParsingException {
        Double theDouble = defaultValue;
        
        if (string!=null && !string.isEmpty() && !string.equals("null") && !string.equals("NA")){
            try {
                theDouble = Double.parseDouble(string);
            }catch(NumberFormatException e){
                throw new ParsingException("Number format error","Error while parsing double "+string+" for field "+fieldName);
            }
        }
        
        return theDouble;
    }
    
}
