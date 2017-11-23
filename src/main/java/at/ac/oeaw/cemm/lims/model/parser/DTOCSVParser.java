/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser;

import it.iit.genomics.cru.smith.defaults.Preferences;
import javax.faces.context.FacesContext;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public abstract class DTOCSVParser<T> {
            
    protected CSVRecord record;
    protected Preferences prefs;
    
    
    public DTOCSVParser(CSVRecord record) {
        this.record=record;
        FacesContext context = FacesContext.getCurrentInstance();
        this.prefs = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);  
    }
    
    public abstract ParsedObject<T> parse() throws ParsingException;
}
