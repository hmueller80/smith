/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.parser;

import at.ac.oeaw.cemm.lims.service.RequestUploadService;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public abstract class DTOCSVParser<T> {
            
    protected CSVRecord record;
    protected Preferences prefs;
    protected RequestUploadService uploadService;
    
    
    public DTOCSVParser(CSVRecord record) {
        this.record=record;
        FacesContext context = FacesContext.getCurrentInstance();
        this.prefs = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);  
        this.uploadService = (RequestUploadService) context.getApplication().evaluateExpressionGet(context, "#{requestUploadService}", RequestUploadService.class);  

    }
    
    public abstract ParsedObject<T> parse() throws ParsingException;
}
