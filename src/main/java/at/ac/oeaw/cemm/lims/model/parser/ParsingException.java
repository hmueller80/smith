/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser;

/**
 *
 * @author dbarreca
 */
public class ParsingException extends Exception {
    private String summary;

    public ParsingException(String summary, String message) {
        super(message);
        this.summary = summary;
    }
    
    public ParsingException(String summary, String message, Throwable cause) {
        super(message,cause);
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }
    
    
}
