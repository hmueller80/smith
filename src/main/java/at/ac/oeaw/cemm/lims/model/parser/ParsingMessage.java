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
public class ParsingMessage {
    String summary;
    String message;

    public ParsingMessage(String summary, String message) {
        this.summary = summary;
        this.message = message;
    }

    public String getSummary() {
        return summary;
    }

    public String getMessage() {
        return message;
    }
    
}
