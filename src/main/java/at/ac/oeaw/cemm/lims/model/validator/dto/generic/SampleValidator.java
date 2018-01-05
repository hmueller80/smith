/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.generic;

import at.ac.oeaw.cemm.lims.api.dto.generic.Sample;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class SampleValidator<T extends Sample> extends AbstractValidator<T> {

    @Override
    protected boolean validateInternal(T objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;
        
        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Sample", "Sample is null"));
            return false;
        }
        
        System.out.println("Validating sample... "+objectToValidate.getName());
        //Check That Sample Name is Present and contains legal characters
        isValid = isValid && 
                stringMatchesPattern(objectToValidate.getName(),"[A-Za-z0-9_]+",ValidatorSeverity.FAIL,"Sample Name: "+objectToValidate.getName(),messages);
        
        isValid = isValid && stringNotEmpty(objectToValidate.getApplicationName(), false, ValidatorSeverity.FAIL, "Application name",messages);

        if (isValid){
            if (objectToValidate.getName().contains("__")){
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"Sample Name","Sample Name "+objectToValidate.getName()+" has a double underscore"));
            }
        }
        
        if (objectToValidate.getCompoundIndex() == null) {
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index error", "Sample "+objectToValidate.getName()+" has i5 index but not i7"));
        } else {

            isValid = isValid
                    && stringMatchesPattern(objectToValidate.getCompoundIndex(), RequestFormDTO.INDEX_REGEXP, ValidatorSeverity.FAIL, "Index in sample " + objectToValidate.getName(), messages);
        }
        
        return isValid;
    }
    
}
