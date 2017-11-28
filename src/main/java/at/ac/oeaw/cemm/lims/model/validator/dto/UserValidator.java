/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
public class UserValidator extends AbstractValidator<UserDTO> {
    
    Boolean isNew = false;

    public UserValidator(UserDTO objectToValidate) {
        super(objectToValidate);
    }

    @Override
    public boolean objectIsValid() {
        boolean isValid = true;

        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "User", "User is null"));
            return false;
        }

        isValid = isValid && stringNotEmpty(objectToValidate.getLogin(), false, ValidatorSeverity.FAIL, "Login");
        isValid = isValid && stringNotEmpty(objectToValidate.getPhone(), false, ValidatorSeverity.FAIL, "Phone");
        isValid = isValid && validUserName(objectToValidate.getUserName(), ValidatorSeverity.FAIL);
        isValid = isValid && validEmail(objectToValidate.getMailAddress(), ValidatorSeverity.FAIL);

        
        return isValid;
    }

    private boolean validUserName(String userName, ValidatorSeverity severity) {
        String fieldName = "User Name";
        if (!stringNotEmpty(userName, false, severity, fieldName)) {
            return false;
        }
        if (!userName.contains(",")) {
            messages.add(new ValidatorMessage(severity, fieldName, "UserName not valid"));
            return false;
        }
        return true;
    }

    private boolean validEmail(String userName, ValidatorSeverity severity) {
        String fieldName = "Email";

        if (!stringNotEmpty(userName, false, severity, fieldName)) {
            return false;
        }
        if (!userName.contains("@")) {
            messages.add(new ValidatorMessage(severity, fieldName, "Mail not valid"));
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
