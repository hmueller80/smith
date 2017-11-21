/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.model.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.model.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.model.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.model.dto.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsedObject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class RequestBuilder {
    
    public static ValidatedRequest buildRequestFromCSV(File csvFile) {
        RequestDTO requestObj = null;
        BuilderValidationStatus validationStatus = new BuilderValidationStatus();
        
        try {
            Reader reader = new FileReader(csvFile);

            CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withHeader(SampleRequestCSVHeader.class).withRecordSeparator(',').withSkipHeaderRecord());
            List<CSVRecord> records = parser.getRecords();
            String user = getUserFromCSVRecords(records);
            requestObj= new RequestDTO(user);
            
            for (CSVRecord record: records){
                SampleDTO sample= getObject(new SampleCSVParser(record),validationStatus);

                IndexDTO index = getObject(new IndexCSVParser(record),validationStatus);
                sample.setIndex(index);

                ApplicationDTO application= getObject(new ApplicationCSVParser(record),validationStatus);
                sample.setApplication(application);
                sample.setExperimentName(application.getApplicationName());
                
                LibraryDTO library = requestObj.addOrGetLibrary(getObject(new LibraryCSVParser(record),validationStatus));
                library.addSample(sample);
                
            }
            
        } catch (ParsingException e) {
            validationStatus.addFailMessage(e.getSummary(), e.getMessage());
        } catch (IOException e){
            validationStatus.addFailMessage("CSV Parsing", e.getMessage());
        }
        
        return new ValidatedRequest(requestObj,validationStatus);
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
    
    private static <T> T getObject(DTOCSVParser<T> parser,  BuilderValidationStatus validationStatus) throws ParsingException {
        ParsedObject<T> parsedObj = parser.parse();
        validationStatus.addWarnings(parsedObj.getWarnings());
        return parsedObj.getObject();
    }
}
