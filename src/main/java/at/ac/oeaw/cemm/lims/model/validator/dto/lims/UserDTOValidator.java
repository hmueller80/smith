/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class UserDTOValidator extends AbstractValidator<UserDTO> {
    
    Boolean isNew = false;

    @Override
    public boolean validateInternal(UserDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;

        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "User", "User is null"));
            return false;
        }

        isValid = isValid && stringNotEmpty(objectToValidate.getLogin(), false, ValidatorSeverity.FAIL, "Login",messages);
        isValid = isValid && stringNotEmpty(objectToValidate.getPhone(), false, ValidatorSeverity.FAIL, "Phone",messages);
        isValid = isValid && validUserName(objectToValidate.getUserName(), ValidatorSeverity.FAIL,messages);
        isValid = isValid && validEmail(objectToValidate.getMailAddress(), ValidatorSeverity.FAIL,messages);

        
        return isValid;
    }

    private boolean validUserName(String userName, ValidatorSeverity severity, Set<ValidatorMessage> messages) {
        String fieldName = "User Name";
        if (!stringNotEmpty(userName, false, severity, fieldName, messages)) {
            return false;
        }
        if (!userName.contains(",")) {
            messages.add(new ValidatorMessage(severity, fieldName, "UserName not valid"));
            return false;
        }
        return true;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

}
