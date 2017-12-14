/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public abstract class AbstractValidator<T> {
    protected final T objectToValidate;
    private Boolean isValid=null;
    protected Set<ValidatorMessage> messages=new HashSet<>();
    
    public AbstractValidator (T objectToValidate) {
        this.objectToValidate = objectToValidate;
    }
    
    public T getValidatedObject() throws ValidatorException {
        if (isValid==null) isValid = objectIsValid();
        
        if (isValid) {
            return objectToValidate;
        }
        
        throw new ValidatorException("Could not validate object of Type "+objectToValidate.getClass().getSimpleName(),messages);
    }
    
    public T forceGetObect() {
        return objectToValidate;
    }
    
    public Set<ValidatorMessage> getMessages() {
        return messages;
    }
    
    protected abstract boolean objectIsValid();
    
    protected boolean stringNotEmpty(String toCheck,
            boolean allowUndefined, 
            ValidatorSeverity severity,
            String parameterName){
        
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
            String parameterName){
        
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
            String parameterName){
        
        if (toCheck==null || toCheck<0){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is undefined or negative"));
            return false;
        }
        
        return true;
    }
    
    protected boolean validPositiveNumber(Integer toCheck,
            ValidatorSeverity severity,
            String parameterName){
        
        if (toCheck==null || toCheck<0){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is undefined or negative"));
            return false;
        }
        
        return true;
    }
    
     protected boolean collectionNotEmpty(
            Collection toCheck, 
            ValidatorSeverity severity,
            String parameterName){
        
        if (toCheck==null || toCheck.isEmpty()){
            messages.add(new ValidatorMessage(severity,parameterName,parameterName+" is empty"));
            return false;
        }
        
        return true;
    }
            
}
