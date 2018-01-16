/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.model.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class IndexCSVParser extends DTOCSVParser<IndexDTO> {
    
    private final SampleService sampleService;
    private final IndexType indexType;
    
    public IndexCSVParser(CSVRecord record, ServiceFactory services, DTOFactory myDTOFactory, IndexType type) {
        super(record,myDTOFactory);
        this.sampleService = services.getSampleService();
        this.indexType = type;
    }
  
    @Override
    public ParsedObject<IndexDTO> parse() throws ParsingException {
        
        IndexDTO index =  myDTOFactory.getIndexDTO("NONE",indexType);
        
        ParsedObject<IndexDTO> returnObj = new ParsedObject<>(index);
        
        boolean hasSeparateIndices = record.isSet(SampleRequestCSVHeader.indexI7.toString());
        
        String indexInCSV = null;
        if (hasSeparateIndices) {
            switch(indexType){
                case i5:
                    indexInCSV = record.get(SampleRequestCSVHeader.indexI5);
                    break;
                case i7:
                    indexInCSV = record.get(SampleRequestCSVHeader.indexI7);
                    break;
            }
        }else{
            String compoundIndex = record.get(SampleRequestCSVHeader.BarcodeByName);
            switch(indexType){
                case i7:
                    if (compoundIndex.length() <= 8) {
                        indexInCSV = compoundIndex;
                    }else {
                        indexInCSV = compoundIndex.substring(0, 8);
                    }
                    break;
                case i5:
                    if (compoundIndex.length() > 8) {
                        indexInCSV = compoundIndex.substring(8);
                    }
                    break;
            }
        }
        
        if (indexInCSV != null && !indexInCSV.trim().isEmpty()) {
            //Index is specified in CSV
            String parsedIndex = parseIndex(indexInCSV);
            if (parsedIndex.trim().isEmpty() || !sampleService.checkIdxExistence(parsedIndex,indexType)){
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
