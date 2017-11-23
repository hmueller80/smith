/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.model.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.model.parser.DTOCSVParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsedObject;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class ApplicationCSVParser extends DTOCSVParser<ApplicationDTO> {
    private static final String RECIPE_DELIMITER = ":";
    private static final String RECIPE_PAIR_END = "Paired end";

    
    private Set<ParsingMessage> warningMessages = new HashSet<>();

    private String appName = null;
    private String instrument = "HiSeq2000";
    private String readMode = null;
    private Integer readDepth = null;
    private Integer readLength = null;
       
    public ApplicationCSVParser(CSVRecord record) {
        super(record);
    }

    @Override
    public ParsedObject<ApplicationDTO> parse() throws ParsingException {
        
        appName = record.get(SampleRequestCSVHeader.Application);
        
        if (appName == null || appName.trim().isEmpty()) {
            warningMessages.add(new ParsingMessage("Application Name Warning", "Application is empty in line" + record.getRecordNumber()));
            appName = "";
        } else if (!Arrays.asList(prefs.getNgsapplication()).contains(appName)) {
                warningMessages.add(new ParsingMessage("Application Name Warning", "Unknown application name ("+appName+") in line" + record.getRecordNumber()));
        }
            
        String recipeString = record.get(SampleRequestCSVHeader.Receipe);
        parseRecipeString(recipeString);
            
        
        String readLengthString = record.get(SampleRequestCSVHeader.ReadLength);
        if (readLengthString == null || readLengthString.trim().isEmpty()) {
            throw new ParsingException("Read Length Error", "Empty Read Length in line " + record.getRecordNumber());
        }
        try {
            readLength = Integer.parseInt(readLengthString);
        } catch (NumberFormatException e) {
            throw new ParsingException("Read Length Error", "Wrong Read Length ("+readLengthString+") in line " + record.getRecordNumber());
        }

        ApplicationDTO returnObj = DTOFactory.getApplicationDTO(readLength,readMode,instrument,appName,readDepth);
        
        return new ParsedObject<>(returnObj,warningMessages);
    }
    
    private void parseRecipeString(String recipe){
        if (recipe==null || recipe.trim().isEmpty()){
            warningMessages.add(new ParsingMessage("Recipe Warning", "Empty Recipe in line" + record.getRecordNumber()));
            return;
        }
          
        StringTokenizer st = new StringTokenizer(recipe, RECIPE_DELIMITER);
        
        if (st.hasMoreTokens()) {
            String mode = st.nextToken().trim();
            if (RECIPE_PAIR_END.equals(mode)) {
                readMode = "PE";
            } else {
                readMode = "SR";
            }
        }
        
        if(st.hasMoreTokens()){
            String mode = st.nextToken().trim();
            StringTokenizer dt = new StringTokenizer(mode);
            if(dt.hasMoreTokens()){
                try {
                    readDepth = Integer.parseInt(dt.nextToken().trim());
                }catch(NumberFormatException e){
                    warningMessages.add(new ParsingMessage("Recipe Warning", "Error while parsing readDepth for Recipe in line" + record.getRecordNumber()));
                }
            }
        }
        
        if(st.hasMoreTokens()){
            String lanes = st.nextToken().trim();
        }
        
        if(st.hasMoreTokens()){
            String length = st.nextToken().trim();
            StringTokenizer dt = new StringTokenizer(length);
            if(dt.hasMoreTokens()){
                try {
                    readLength = Integer.parseInt(dt.nextToken().trim());
                }catch(NumberFormatException e){
                    warningMessages.add(new ParsingMessage("Recipe Warning", "Error while parsing readLength for Recipe in line" + record.getRecordNumber()));
                }
            }
        }
    }
    
}
