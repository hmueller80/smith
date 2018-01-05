/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.LibraryValidator;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.SampleValidator;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class LibraryDTOValidator extends LibraryValidator<LibraryDTO> {
    
    public <T extends SampleValidator> LibraryDTOValidator(boolean checkSamples) {
        super(new SampleDTOValidator(),checkSamples);
    }
    
    @Override
    protected boolean validateInternal(LibraryDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = super.validateInternal(objectToValidate, messages);
        
        isValid = isValid && validateApplicationConsistency(objectToValidate,messages);
        
        return isValid;
    }

    private boolean validateApplicationConsistency(LibraryDTO library, Set<ValidatorMessage> messages) {
        ApplicationDTO application = null;
        
        for (SampleDTO sample : library.getSamples()) {
            if (application == null) {
                application = sample.getApplication();
            } else {
                if (!application.getReadLength().equals(sample.getApplication().getReadLength())) {
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,
                            "Application name",
                            "Inconsistent read length accross samples in library " + library.getName()));
                    return false;
                }
                if (!application.getReadLength().equals(sample.getApplication().getReadLength())) {
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,
                            "Application name",
                            "Inconsistent read mode accross samples in library " + library.getName()));
                    return false;
                }
            }
        }

        return true;
    }
    
}
