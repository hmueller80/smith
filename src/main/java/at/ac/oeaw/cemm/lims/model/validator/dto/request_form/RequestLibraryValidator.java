/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.LibraryValidator;
import java.util.Set;
/**
 *
 * @author dbarreca
 */
public class RequestLibraryValidator extends LibraryValidator<RequestLibraryDTO> {

    public RequestLibraryValidator() {
        super(new RequestSampleValidator());
    }

    public RequestLibraryValidator(boolean validateSamples) {
        super(new RequestSampleValidator(),validateSamples);
    }

   
    @Override
    protected boolean validateInternal(RequestLibraryDTO objectToValidate, Set<ValidatorMessage> messages) {

        boolean isValid = super.validateInternal(objectToValidate, messages);
        
        if (stringNotEmpty(objectToValidate.getReadMode(), false, ValidatorSeverity.FAIL, "Read Mode",messages)) {
            String readMode = objectToValidate.getReadMode();
            if (!"PE".equals(readMode) && !"SR".equals(readMode)) {
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Read Mode", "Read Mode must be PE or SR"));
            }else{
                objectToValidate.setReadMode(readMode);
            }
        } else {
            isValid = false;
        }    
        isValid = isValid && stringNotEmpty(objectToValidate.getApplicationName(), false, ValidatorSeverity.FAIL, "Application", messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getReadLength(), ValidatorSeverity.FAIL, "Read Length",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getLanes(), ValidatorSeverity.FAIL, "Lanes",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getVolume(), ValidatorSeverity.FAIL, "Volume",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getDnaConcentration(), ValidatorSeverity.FAIL, "DNA Concentration",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getTotalSize(), ValidatorSeverity.FAIL, "Total Size", messages);
        
        return isValid;
    }      
}
