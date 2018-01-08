/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.model.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import javax.faces.context.FacesContext;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class IndexCSVParser extends DTOCSVParser<IndexDTO> {
    
    private final SampleService sampleService;
    
    public IndexCSVParser(CSVRecord record, ServiceFactory services, DTOFactory myDTOFactory) {
        super(record,myDTOFactory);
        FacesContext context = FacesContext.getCurrentInstance();
        this.sampleService = services.getSampleService();
    }
  
    @Override
    public ParsedObject<IndexDTO> parse() throws ParsingException {
        IndexDTO index =  myDTOFactory.getIndexDTO("none");
        ParsedObject<IndexDTO> returnObj = new ParsedObject<>(index);
        
        String indexInCSV = record.get(SampleRequestCSVHeader.BarcodeByName);

        if (indexInCSV != null && !indexInCSV.trim().isEmpty()) {
            //Index is specified in CSV
            String parsedIndex = parseIndex(indexInCSV);
            if (parsedIndex.trim().isEmpty() || !sampleService.checkIdxExistence(parsedIndex)){
                throw new ParsingException("Unknown index","Index "+indexInCSV+" in line "+record.getRecordNumber());
            }else{
                index.setIndex(parsedIndex);
            }
        }
        
        return returnObj;
    }
    
      
    private String parseIndex(String input) {
        if (RequestFormDTO.DEFAULT_INDEX.equals(input) || RequestFormDTO.NO_DEMUX_INDEX.equals(input)){
            return input;
        } else if(!input.contains("_")){
            return input;
        }else{
            String[] temp = input.split("_");
            return temp[temp.length - 1];
        }
    }

}
