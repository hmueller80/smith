/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;

/**
 *
 * @author dbarreca
 */
public class ApplicationValidator extends AbstractValidator<ApplicationDTO> {

    public ApplicationValidator(ApplicationDTO objectToValidate) {
        super(objectToValidate);
    }

    @Override
    public boolean objectIsValid() {
        return stringNotEmpty(objectToValidate.getApplicationName(),false,ValidatorSeverity.FAIL,"Application name");
    }
    
}
