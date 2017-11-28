/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.parser.CSVValidationStatus;
import at.ac.oeaw.cemm.lims.model.parser.ValidatedCSV;
import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsedObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    
    public ValidatedCSV<RequestDTO> buildRequestFromCSV(File csvFile, ServiceFactory services) {
        RequestDTO requestObj = null;
        CSVValidationStatus validationStatus = new CSVValidationStatus();
        
        try {
            Reader reader = new FileReader(csvFile);

            CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withHeader(SampleRequestCSVHeader.class).withRecordSeparator(',').withSkipHeaderRecord());
            List<CSVRecord> records = parser.getRecords();
            String user = getUserFromCSVRecords(records);
            requestObj= myDTOFactory.getRequestDTO(user);
            
            for (CSVRecord record: records){
                SampleDTO sample= getObject(new SampleCSVParser(record,myDTOFactory),validationStatus);

                IndexDTO index = getObject(new IndexCSVParser(record,services,myDTOFactory),validationStatus);
                sample.setIndex(index);

                ApplicationDTO application= getObject(new ApplicationCSVParser(record,myDTOFactory),validationStatus);
                sample.setApplication(application);
                sample.setExperimentName(application.getApplicationName());
                
                LibraryDTO library = requestObj.addOrGetLibrary(getObject(new LibraryCSVParser(record,myDTOFactory),validationStatus));
                library.addSample(sample);
                
            }
            
        } catch (ParsingException e) {
            validationStatus.addFailMessage(e.getSummary(), e.getMessage());
        } catch (IOException e){
            validationStatus.addFailMessage("CSV Parsing", e.getMessage());
        }
        
        return new ValidatedCSV(requestObj,validationStatus);
    }
    
    private static String getUserFromCSVRecords(List<CSVRecord> records) throws ParsingException{
        Set<String> usersFound = new HashSet<> ();
        
        for (CSVRecord record: records){
            String sampleUserLogin = record.get(SampleRequestCSVHeader.UserLogin);
            System.out.println(sampleUserLogin);
            if (sampleUserLogin==null || sampleUserLogin.trim().isEmpty()){
                throw new ParsingException("User error","Missing user in line "+record.getRecordNumber());
            }
            usersFound.add(sampleUserLogin);
        }
        
        if (usersFound.size()!=1){
            throw new ParsingException("User error","Multiple users found in file");
        }else{
            return (String) usersFound.toArray()[0];
        }
    }
    
    private static <T> T getObject(DTOCSVParser<T> parser,  CSVValidationStatus validationStatus) throws ParsingException {
        ParsedObject<T> parsedObj = parser.parse();
        validationStatus.addWarnings(parsedObj.getWarnings());
        return parsedObj.getObject();
    }
}
