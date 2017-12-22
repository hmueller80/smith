/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class ValidationStatus {
    private boolean isValid;
    private final Set<ValidatorMessage> validationMessages;
    
    public ValidationStatus(boolean isValid, Set<ValidatorMessage> messages) {
        this.isValid = isValid;
        this.validationMessages = messages;
    }

    public ValidationStatus() {
        this.isValid = true;
        this.validationMessages = new HashSet<>();
    }

    public boolean isValid() {
        return isValid;
    }

    public Set<ValidatorMessage> getValidationMessages() {
        return validationMessages;
    }
    
    public void merge(ValidationStatus other) {
        isValid = isValid && other.isValid;
        validationMessages.addAll(other.validationMessages);
    }
    
}
