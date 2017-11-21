/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.model.dto.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.dto.parser.ParsingMessage;
import it.iit.genomics.cru.smith.sampleBeans.SampleNameFilter;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class LibraryCSVParser extends DTOCSVParser<LibraryDTO> {

    public LibraryCSVParser(CSVRecord record) {
        super(record);
    }

    @Override
    public ParsedObject parse() throws ParsingException {
        Set<ParsingMessage> messages = new HashSet<>();
        
        String libraryName = record.get(SampleRequestCSVHeader.library);
        if (libraryName==null || libraryName.trim().isEmpty()){
            libraryName = record.get(SampleRequestCSVHeader.SampleName);
            messages.add(new ParsingMessage("Library Name","Empty Library Name in line "+record.getRecordNumber()));
        }
        if (libraryName==null || libraryName.trim().isEmpty()){
            throw new ParsingException("Library Name","Both library name and sample name are empty in line "+record.getRecordNumber());
        }
        libraryName = SampleNameFilter.legalizeLibrary(libraryName);
        
        return new ParsedObject<>(new LibraryDTO(libraryName),messages);
    }
    
}
