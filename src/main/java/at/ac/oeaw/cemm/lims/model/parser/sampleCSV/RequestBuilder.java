/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.parser.CSVValidationStatus;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.LibraryValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.RequestValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.RequestUploadValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.lims.SampleDTOValidator;
import at.ac.oeaw.cemm.lims.util.RequestIdBean;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import javax.inject.Inject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class RequestBuilder {
    
    @Inject private DTOFactory myDTOFactory;
    @Inject private ServiceFactory services;
    
    public ValidatedCSV<RequestDTO> buildRequestFromCSV(File csvFile, RequestIdBean requestIdBean) {
        RequestDTO requestObj = null;
        CSVValidationStatus validationStatus = new CSVValidationStatus();
        
        try {
            Reader reader = new FileReader(csvFile);

            CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withHeader(SampleRequestCSVHeader.class).withRecordSeparator(',').withSkipHeaderRecord().withIgnoreEmptyLines());
            List<CSVRecord> records = parser.getRecords();
            UserDTO requestor = getUserFromCSVRecords(records);
            Integer requestId = getSubmissionIdFromCSVRecords(records);
            
            requestObj = myDTOFactory.getRequestDTO(requestor,requestId);

            for (CSVRecord record : records) {
                SampleDTO sample = getObject(new SampleCSVParser(record, myDTOFactory,requestId), validationStatus);

                IndexDTO index = getObject(new IndexCSVParser(record, services, myDTOFactory), validationStatus);
                sample.setIndex(index);

                ApplicationDTO application = getObject(new ApplicationCSVParser(record, myDTOFactory), validationStatus);
                sample.setApplication(application);
                sample.setExperimentName(application.getApplicationName());

                LibraryDTO library = requestObj.addOrGetLibrary(getObject(new LibraryCSVParser(record, myDTOFactory), validationStatus));
                library.addSample(sample);

            }
            
            //Validate Business Logic!
            RequestValidator requestValidator = new RequestUploadValidator(new LibraryValidator(new SampleDTOValidator()),services, requestIdBean);
            ValidationStatus requestValidation = requestValidator.isValid(requestObj);
            if (!requestValidation.isValid()){
                validationStatus.addFailMessage("Submission Validation", "");
            }
            
            for(ValidatorMessage message: requestValidation.getValidationMessages()){
                if (!ValidatorSeverity.FAIL.equals(message.getType())){
                    validationStatus.addWarning(message.getSummary(), message.getDescription());
                }else{
                    validationStatus.addFailMessage(message.getSummary(), message.getDescription());
                }
            }
           
        } catch (ParsingException e) {
            validationStatus.addFailMessage(e.getSummary(), e.getMessage());
        } catch (IOException e){
            validationStatus.addFailMessage("CSV Parsing", e.getMessage());
        } catch (Exception e){
            validationStatus.addFailMessage("CSV Parsing", e.getMessage());
        }
        
        return new ValidatedCSV(requestObj,validationStatus);
    }
    
    private UserDTO getUserFromCSVRecords(List<CSVRecord> records) throws ParsingException{
        String previousUser = null;
        
        for (CSVRecord record: records){
            String sampleUserLogin = record.get(SampleRequestCSVHeader.UserLogin);
            System.out.println(sampleUserLogin);
            if (sampleUserLogin==null || sampleUserLogin.trim().isEmpty()){
                throw new ParsingException("User error ","Missing user in line "+record.getRecordNumber());
            }
            if (previousUser==null){
                previousUser = sampleUserLogin;
            }else if (!previousUser.equals(sampleUserLogin)){
                throw new ParsingException("User error ","Multiple users found in file");
            }
        }
        
        UserDTO requestor =  services.getUserService().getUserByLogin(previousUser);
        if (requestor == null){
            throw new ParsingException("User error ","Requestor is not in the user DB");
        }
        
        return requestor;       
    }
    
    private Integer getSubmissionIdFromCSVRecords(List<CSVRecord> records) throws ParsingException {
        Integer previousId = null;

        for (CSVRecord record : records) {
            String submissionIdString = record.get(SampleRequestCSVHeader.submissionId);
            if (submissionIdString == null || submissionIdString.trim().isEmpty()) {
                throw new ParsingException("Submission ID ", "Missing submission ID in line " + record.getRecordNumber());
            } else {
                try {
                    Integer submissionId = Integer.parseInt(submissionIdString);
                    if (previousId==null){
                        previousId = submissionId;
                    }else if (!previousId.equals(submissionId)){
                        throw new ParsingException("Submission ID ","Multiple submission IDs found in file");
                    }                
                } catch (NumberFormatException e) {
                    throw new ParsingException("Submission ID ", "Error in parsing submission id (" + submissionIdString + ") in line " + record.getRecordNumber());
                }
            }

        }
        
        if(services.getRequestService().checkRequestExistence(previousId)){
            throw new ParsingException("Submission ID ", "A request with id "+previousId+" already exists in the DB");
        }
        
        return previousId;
    }
    
    private static <T> T getObject(DTOCSVParser<T> parser,  CSVValidationStatus validationStatus) throws ParsingException {
        ParsedObject<T> parsedObj = parser.parse();
        validationStatus.addWarnings(parsedObj.getWarnings());
        return parsedObj.getObject();
    }
}
