/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class ValidatorException extends Exception{
    Set<ValidatorMessage> payload = new HashSet<>();
    
    public ValidatorException() {
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }
    
     public ValidatorException(String message,Set<ValidatorMessage> payload) {
        super(message);
        this.payload=payload;
    }

    public Set<ValidatorMessage> getPayload() {
        return payload;
    }
     
    
    
}
