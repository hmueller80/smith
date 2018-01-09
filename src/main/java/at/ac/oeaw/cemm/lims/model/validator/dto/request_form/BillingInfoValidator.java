/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.BillingInfoDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class BillingInfoValidator extends AbstractValidator<BillingInfoDTO> {

    @Override
    protected boolean validateInternal(BillingInfoDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;
        
        isValid = isValid && stringNotEmpty(objectToValidate.getContact(),false,ValidatorSeverity.FAIL,"Billing Contact",messages);
        isValid = isValid && stringNotEmpty(objectToValidate.getAddress(),false,ValidatorSeverity.FAIL,"Billing Address",messages);
        stringNotEmpty(objectToValidate.getBillingCode(),false,ValidatorSeverity.WARNING,"Billing Code",messages);

        return isValid;
    }

    
}
