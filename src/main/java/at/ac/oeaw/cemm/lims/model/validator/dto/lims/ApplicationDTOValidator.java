/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.ApplicationValidator;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class ApplicationDTOValidator extends ApplicationValidator<ApplicationDTO> {

    @Override
    public boolean validateInternal(ApplicationDTO objectToValidate, Set<ValidatorMessage> messages) {
        
        boolean isValid = super.validateInternal(objectToValidate, messages);
                
        return isValid && validPositiveNumber(objectToValidate.getDepth(), ValidatorSeverity.FAIL, "Depth",messages);
    } 
    
}
