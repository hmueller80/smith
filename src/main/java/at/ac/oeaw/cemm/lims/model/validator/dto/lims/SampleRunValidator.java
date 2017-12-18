/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class SampleRunValidator extends AbstractValidator<SampleRunDTO> {

    @Override
    public boolean validateInternal(SampleRunDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;
        
        isValid = isValid && stringNotEmpty(objectToValidate.getFlowcell(),false,ValidatorSeverity.FAIL,"flow cell",messages);
        isValid = isValid && stringNotEmpty(objectToValidate.getRunFolder(),false,ValidatorSeverity.FAIL,"run folder",messages);
        isValid = isValid && collectionNotEmpty(objectToValidate.getLanes(),ValidatorSeverity.FAIL,"lanes",messages);
        
        return isValid;
              
    }
    
}
