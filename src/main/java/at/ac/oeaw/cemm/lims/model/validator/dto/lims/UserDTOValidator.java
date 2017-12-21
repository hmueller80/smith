/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class UserDTOValidator extends AbstractValidator<UserDTO> {
    
    ServiceFactory services;

    public UserDTOValidator(ServiceFactory services) {
        this.services = services;
    }
    
    @Override
    public boolean validateInternal(UserDTO objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;

        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "User", "User is null"));
            return false;
        }
        
        if (Preferences.ROLE_GUEST.equals(objectToValidate.getUserRole())){
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"Guest User","Guest User is not editable"));
        }else if (Preferences.ROLE_GROUPLEADER.equalsIgnoreCase(objectToValidate.getUserRole())){
            Integer userPi = objectToValidate.getPi();
            if (userPi!=null && userPi!=objectToValidate.getId()){
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User PI","Group leaders must be PIs of themselves"));
            }
        }else {
            Integer userPi = objectToValidate.getPi();
            if (userPi==null){
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User PI","Users, Admins and technician must have a vald PI"));
            }else{
                UserDTO pi = services.getUserService().getUserByID(userPi);
                if (pi == null) {
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User PI","PI with ID "+pi.getId()+" not found in the user DB"));
                }else if (!Preferences.ROLE_ADMIN.equals(pi.getUserRole()) && !Preferences.ROLE_GROUPLEADER.equals(pi.getUserRole())){
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User PI","PI with name "+pi.getUserName()+" has role not allowed for PI: "+pi.getUserRole()));
                }
            }  
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

}
