/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.entity;

import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import it.iit.genomics.cru.smith.entity.Application;

/**
 *
 * @author dbarreca
 */
public class ApplicationValidator extends AbstractValidator<Application> {

    public ApplicationValidator(Application objectToValidate) {
        super(objectToValidate);
    }

    @Override
    public boolean objectIsValid() {
        return stringNotEmpty(objectToValidate.getApplicationname(),false,ValidatorSeverity.FAIL,"Application name");
    }
    
}
