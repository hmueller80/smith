/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.SampleValidator;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class RequestSampleValidator  extends SampleValidator<RequestSampleDTO> {

  
    @Override
    protected boolean validateInternal(RequestSampleDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = super.validateInternal(objectToValidate, messages);
        
        isValid = isValid
                && stringMatchesPattern(objectToValidate.getI5Index(), RequestFormDTO.INDEX_REGEXP, ValidatorSeverity.FAIL, "I5 Index in sample " + objectToValidate.getName(), messages);

        isValid = isValid
                && stringMatchesPattern(objectToValidate.getI7Index(), RequestFormDTO.INDEX_REGEXP, ValidatorSeverity.FAIL, "I7 Index in sample " + objectToValidate.getName(), messages);

        isValid = isValid
                && stringMatchesPattern(objectToValidate.getPrimerSequence(), RequestFormDTO.INDEX_REGEXP, ValidatorSeverity.FAIL, "Primer Index in sample " + objectToValidate.getName(), messages);
   
        
        return isValid;
    }
    
}
