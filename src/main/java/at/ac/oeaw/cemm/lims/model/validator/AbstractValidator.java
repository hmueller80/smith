/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.validator.routines.EmailValidator;

/**
 *
 * @author dbarreca
 */
public abstract class AbstractValidator<T> {
         
    public ValidationStatus isValid(T objectToValidate) {
        Set<ValidatorMessage> messages = new HashSet<>();
        return new ValidationStatus(validateInternal(objectToValidate,messages),messages);       
    }
    
    protected abstract boolean validateInternal(T objectToValidate,Set<ValidatorMessage> messages);
    
    protected boolean stringNotEmpty(String toCheck,
            boolean allowUndefined, 
            ValidatorSeverity severity,
            String parameterName,
            Set<ValidatorMessage> messages){
        
        if (toCheck==null || toCheck.isEmpty()){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is empty"));
            return false;
        }else if (!allowUndefined && toCheck.trim().equalsIgnoreCase("undefined")){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is undefined"));
            return false;
        }
        
        return true;
    }
     
    protected boolean stringMatchesPattern(String toCheck,
            String pattern,
            ValidatorSeverity severity,
            String parameterName,
            Set<ValidatorMessage> messages){
        
        if (toCheck==null || toCheck.isEmpty()){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is empty"));
            return false;
        }else if (!toCheck.matches(pattern)){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" does not respect pattern "+pattern));
            return false;
        }
        
        return true;
    }
    
    protected boolean validPositiveNumber(Double toCheck,
            ValidatorSeverity severity,
            String parameterName,
            Set<ValidatorMessage> messages){
        
        if (toCheck==null || toCheck<0){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is undefined or negative"));
            return false;
        }
        
        return true;
    }
    
    protected boolean validPositiveNumber(Integer toCheck,
            ValidatorSeverity severity,
            String parameterName,
            Set<ValidatorMessage> messages){
        
        if (toCheck==null || toCheck<0){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is undefined or negative"));
            return false;
        }
        
        return true;
    }
    
     protected boolean collectionNotEmpty(
            Collection toCheck, 
            ValidatorSeverity severity,
            String parameterName,
            Set<ValidatorMessage> messages){
        
        if (toCheck==null || toCheck.isEmpty()){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is empty"));
            return false;
        }
        
        return true;
    }
     
     protected boolean validEmail(String email, ValidatorSeverity severity,Set<ValidatorMessage> messages) {
        return validEmail(email, severity, "User Email",messages);
    }
     
    protected boolean validEmail(String email, ValidatorSeverity severity, String fieldName,Set<ValidatorMessage> messages) {
        if (EmailValidator.getInstance().isValid(email)){
            return true;
        }else {
            messages.add(new ValidatorMessage(severity, fieldName, "Email "+email+" is not a valid address"));
            return false;
        }

    }
            
}
