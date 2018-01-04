/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

/**
 *
 * @author dbarreca
 */
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.LibraryValidator;

import at.ac.oeaw.cemm.lims.model.validator.dto.generic.RequestValidator;
import java.util.Set;

public class RequestCSVUploadValidator extends RequestValidator<RequestDTO> {
    
    
    public <T extends LibraryValidator> RequestCSVUploadValidator(T libraryValidator, ServiceFactory services) {
        super(libraryValidator, services);
    }
    
    @Override
    protected boolean validateInternal(RequestDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = super.validateInternal(objectToValidate, messages);

        if (objectToValidate.getRequestId() < services.getRequestFormService().getMaxRequestId()) {
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Submission Id", "Samples or requests with this ID already exists"));
        }

        return isValid;
    }

}
