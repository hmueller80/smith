/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.model.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class SampleCSVParser extends DTOCSVParser<SampleDTO> {
    Integer submissionId;
    
     public SampleCSVParser(CSVRecord record, DTOFactory myDTOFactory,Integer submissionId) {
        super(record,myDTOFactory);
        this.submissionId = submissionId;
    }

    @Override
    public ParsedObject<SampleDTO> parse() throws ParsingException {
        SampleDTO sample = myDTOFactory.getSampleDTO(null);
        ParsedObject<SampleDTO> returnValue = new ParsedObject<>(sample);
        
        String sampleType = record.get(SampleRequestCSVHeader.SampleType);
        if (sampleType != null && !sampleType.trim().isEmpty()) {
            /*if (!Arrays.asList(prefs.getSampletype()).contains(sampleType)) {
                returnValue.addMessage("Sample type", "Unknown sample Type ("+sampleType + ") in line " + record.getRecordNumber());
            }*/
            sample.setType(sampleType);
        } else {
            sample.setType("");
        }

        String librarySynthesis = record.get(SampleRequestCSVHeader.LibrarySynthesis);
        if (librarySynthesis != null && !librarySynthesis.trim().isEmpty()) {
            if (librarySynthesis.toUpperCase().trim().equals("TRUE") || librarySynthesis.toUpperCase().trim().equals("NEEDED")) {
                sample.setSyntehsisNeeded(true);
            }
        } else {
            sample.setSyntehsisNeeded(false);
        }

        String organism = record.get(SampleRequestCSVHeader.Organism);
        if (organism != null && !organism.trim().isEmpty()) {
            /*if (!Arrays.asList(prefs.getOrganisms()).contains(organism)) {
                returnValue.addMessage("Organism", "Unknown organism ("+organism + ") in line " + record.getRecordNumber());
            }*/
            sample.setOrganism(organism);
        } else {
            sample.setOrganism("undefined");
        }

        String antibody = record.get(SampleRequestCSVHeader.Antibody);
        if (antibody != null && !antibody.trim().isEmpty()) {
            sample.setAntibody(antibody);
        } else {
            sample.setAntibody("");
        }

        sample.setStatus(SampleDTO.status_requested);

        String sampleName = record.get(SampleRequestCSVHeader.SampleName);
        if (sampleName != null && !sampleName.trim().isEmpty()) {
            sample.setName(NameFilter.legalize(sampleName));
        } else {
            sample.setName("undefined");
        }

        String sampleDescription = record.get(SampleRequestCSVHeader.SampleDescription);
        if (sampleDescription != null && !sampleDescription.trim().isEmpty()) {
            sample.setDescription(sampleDescription);
        } else {
            sample.setDescription("");
        }

        //TODO: double check default and errors from parser
        String bioAnalyzerDate = record.get(SampleRequestCSVHeader.BioAnalyzerDate);
        if (bioAnalyzerDate != null && !bioAnalyzerDate.trim().isEmpty()) {
            Date date = parseDate(bioAnalyzerDate, "dd.MM.YYYY");
            if (date != null) {
                sample.setBioanalyzerDate(date);
            }
        }

        String bioAnalyzerMolarity = record.get(SampleRequestCSVHeader.BioAnalyzerNanomolarity);
        if (bioAnalyzerMolarity != null && !bioAnalyzerMolarity.trim().isEmpty()) {
            try {
                sample.setBioAnalyzerMolarity(Double.parseDouble(bioAnalyzerMolarity));
            } catch (NumberFormatException e) {
                returnValue.addMessage("Bio Analyzer Nanomolarity", "Error in parsing Bio Analyzer Nanomolarity ("+bioAnalyzerMolarity + ") in line " + record.getRecordNumber());
                sample.setBioAnalyzerMolarity(0.0);
            }
        } else {
            sample.setBioAnalyzerMolarity(0.0);
        }

        String concentration = record.get(SampleRequestCSVHeader.SampleConcentration);
        if (concentration != null && !concentration.trim().isEmpty()) {
            try {
                sample.setConcentration(Double.parseDouble(concentration));
            } catch (NumberFormatException e) {
                returnValue.addMessage("Sample concentration", "Error in parsing Sample concentration ("+concentration + ") in line " + record.getRecordNumber());
                sample.setConcentration(0.0);
            }
        } else {
            sample.setConcentration(0.0);
        }

        String totAmount = record.get(SampleRequestCSVHeader.TotalAmount);
        if (totAmount != null && !totAmount.trim().isEmpty()) {
            try {
                sample.setTotalAmount(Double.parseDouble(totAmount));
            } catch (NumberFormatException e) {
                returnValue.addMessage("Total Amount", "Error in parsing Total Amount ("+totAmount + ") in line " + record.getRecordNumber());
                sample.setTotalAmount(0.0);
            }
        } else {
            sample.setTotalAmount(0.0);
        }

        String fragmentSize = record.get(SampleRequestCSVHeader.BulkFragmentSize);
        if (fragmentSize != null && !fragmentSize.trim().isEmpty()) {
            try {
                sample.setBulkFragmentSize(Double.parseDouble(fragmentSize));
            } catch (NumberFormatException e) {
                returnValue.addMessage("Bulk Fragment Size", "Error in parsing Bulk Fragment Size ("+fragmentSize + ") in line " + record.getRecordNumber());
                sample.setBulkFragmentSize(0.0);
            }
        } else {
            sample.setBulkFragmentSize(0.0);
        }

        String comment = record.get(SampleRequestCSVHeader.Comments);
        if (comment != null && !comment.trim().isEmpty()) {
            sample.setComment(comment);
        } else {
            sample.setComment("");
        }

        //TODO: double check default and errors from parser
        String requestDateString = record.get(SampleRequestCSVHeader.date);
        Date requestDate = null;
        if (requestDateString != null && !requestDateString.trim().isEmpty()) {
            requestDate = parseDate(requestDateString, "dd.MM.YYYY");
        }
        if (requestDate != null) {
            sample.setRequestDate(requestDate);
        } else {
            throw new ParsingException("Request Date","Error in parsing request Date ("+requestDateString+") in line "+record.getRecordNumber());
        }

        
        sample.setSubmissionId(submissionId);
       
        String costCenter = record.get(SampleRequestCSVHeader.PI);
        if (costCenter != null && !costCenter.trim().isEmpty()) {
            sample.setCostcenter(costCenter);
        } else {
            sample.setCostcenter("");
        }
        
        return returnValue;
    }
    
    private static Date parseDate(String date, String format){
        if(date == null){
            date = new String("1.1.1970");
        }
        if(date.length() == 0){
            date = new String("1.1.1970");
        }
        
        DateFormat df = new SimpleDateFormat(format, Locale.GERMANY);
        df.setLenient(false); 
        try{
            return df.parse(date);
        }
        catch(ParseException ex){ 
            ex.printStackTrace();  
        }
        return new Date(System.currentTimeMillis());
    }
}
   
