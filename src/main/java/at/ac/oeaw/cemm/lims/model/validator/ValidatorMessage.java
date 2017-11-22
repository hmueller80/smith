/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator;

/**
 *
 * @author dbarreca
 */
public class ValidatorMessage {
    private ValidatorSeverity type;
    private String summary;
    private String description;

    public ValidatorMessage(ValidatorSeverity type, String summary, String description) {
        this.type = type;
        this.summary = summary;
        this.description = description;
    }

    public ValidatorSeverity getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }
    
}
