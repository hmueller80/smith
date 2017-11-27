/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto;

import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;

/**
 *
 * @author dbarreca
 */
public class SampleRunValidator extends AbstractValidator<SampleRunDTO> {

    public SampleRunValidator(SampleRunDTO objectToValidate) {
        super(objectToValidate);
    }

    @Override
    public boolean objectIsValid() {
        boolean isValid = true;
        
        isValid = isValid && stringNotEmpty(objectToValidate.getFlowcell(),false,ValidatorSeverity.FAIL,"flow cell");
        isValid = isValid && stringNotEmpty(objectToValidate.getRunFolder(),false,ValidatorSeverity.FAIL,"run folder");
        isValid = isValid && collectionNotEmpty(objectToValidate.getLanes(),ValidatorSeverity.FAIL,"flowcell");
        
        return isValid;
              
    }
    
}
