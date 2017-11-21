/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.model.dto.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsingException;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.sampleBeans.SampleNameFilter;
import it.iit.genomics.cru.smith.utils.DateParser;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class SampleCSVParser extends DTOCSVParser<SampleDTO> {
    
    public SampleCSVParser(CSVRecord record) {
        super(record);
    }

    @Override
    public ParsedObject<SampleDTO> parse() throws ParsingException {
        SampleDTO sample = new SampleDTO();
        ParsedObject<SampleDTO> returnValue = new ParsedObject<>(sample);
        
        String sampleType = record.get(SampleRequestCSVHeader.SampleType);
        if (sampleType != null && !sampleType.trim().isEmpty()) {
            if (!Arrays.asList(prefs.getSampletype()).contains(sampleType)) {
                returnValue.addMessage("Sample type", "Unknown sample Type ("+sampleType + ") in line " + record.getRecordNumber());
            }
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
            if (!Arrays.asList(prefs.getOrganisms()).contains(organism)) {
                returnValue.addMessage("Organism", "Unknown organism ("+organism + ") in line " + record.getRecordNumber());
            }
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

        sample.setStatus(Sample.status_requested);

        String sampleName = record.get(SampleRequestCSVHeader.SampleName);
        if (sampleName != null && !sampleName.trim().isEmpty()) {
            sample.setName(SampleNameFilter.legalize(sampleName));
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
            Date date = DateParser.parseDateAustria(bioAnalyzerDate, "dd.MM.YYYY");
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
            requestDate = DateParser.parseDateAustria(requestDateString, "dd.MM.YYYY");
        }
        if (requestDate != null) {
            sample.setRequestDate(requestDate);
        } else {
            throw new ParsingException("Request Date","Error in parsing request Date ("+requestDateString+") in line "+record.getRecordNumber());
        }

        String submissionIdString = record.get(SampleRequestCSVHeader.submissionId);
        Integer submissionId = null;
        if (submissionIdString != null && !submissionIdString.trim().isEmpty()) {
            try {
                submissionId = Integer.parseInt(submissionIdString);
            } catch (NumberFormatException e) {
            }
        }
        if (submissionId != null) {
            sample.setSubmissionId(submissionId);
        } else {
            throw new ParsingException("Submission ID","Error in parsing submission id ("+submissionIdString+") in line "+record.getRecordNumber());
        }

        String costCenter = record.get(SampleRequestCSVHeader.PI);
        if (costCenter != null && !costCenter.trim().isEmpty()) {
            sample.setCostcenter(costCenter);
        } else {
            sample.setCostcenter("");
        }
        
        return returnValue;
    }
}
   
