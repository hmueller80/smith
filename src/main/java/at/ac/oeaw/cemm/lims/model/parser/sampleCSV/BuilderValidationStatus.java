/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.parser.ParsingMessage;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class BuilderValidationStatus {
    private Set<ParsingMessage> warningMessages=new HashSet<>();
    private Set<ParsingMessage> failMessages=new HashSet<>();
    boolean isFailed = false;
    
    public void addWarning(String summary,String description) {
        warningMessages.add(new ParsingMessage(summary,description));       
    }
    
     public void addWarnings(Set<ParsingMessage> warnings) {
        warningMessages.addAll(warnings);       
    }
    
    public void addFailMessage(String summary,String description) {
        isFailed = true;
        failMessages.add(new ParsingMessage(summary,description));       
    }

    public Set<ParsingMessage> getWarningMessages() {
        return warningMessages;
    }

    public Set<ParsingMessage> getFailMessages() {
        return failMessages;
    }

    public boolean isIsFailed() {
        return isFailed;
    }
     
}
