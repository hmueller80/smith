/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.generic;

import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.Set;
import at.ac.oeaw.cemm.lims.api.dto.generic.RunRequest;

/**
 *
 * @author dbarreca
 * @param <T>
 */
public class RunRequestValidator<T extends RunRequest> extends AbstractValidator<T> {


    @Override
    public boolean validateInternal(T objectToValidate, Set<ValidatorMessage> messages) {
       
        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Application", "Application is null"));
            return false;
        }
        
        boolean isValid = true;
        
        isValid = isValid && validPositiveNumber(objectToValidate.getReadLength(), ValidatorSeverity.FAIL, "Read Length",messages);
        
         if (stringNotEmpty(objectToValidate.getReadMode(), false, ValidatorSeverity.FAIL, "Read Mode",messages)) {
            String readMode = objectToValidate.getReadMode();
            if (!"PE".equals(readMode) && !"SR".equals(readMode)) {
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Read Mode", "Read Mode must be PE or SR"));
            }
        } else {
            isValid = false;
        }    
          
         return isValid;
    }

    
}
