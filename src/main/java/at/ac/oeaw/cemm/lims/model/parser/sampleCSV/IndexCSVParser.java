/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.model.dto.DTOFactory;
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
    
    public IndexCSVParser(CSVRecord record) {
        super(record);
        FacesContext context = FacesContext.getCurrentInstance();
        sampleService = 
                ((ServiceFactory) context.getApplication().evaluateExpressionGet(context, "#{hibernateServiceFactory}", ServiceFactory.class))
                .getSampleService(); 
    }
  
    @Override
    public ParsedObject<IndexDTO> parse() throws ParsingException {
        IndexDTO index =  DTOFactory.getIndexDTO("none");
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
        if(!input.contains("_")){
            return input;
        }else{
            String[] temp = input.split("_");
            return temp[temp.length - 1];
        }
    }

}
