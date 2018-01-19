/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
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
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User PI","PI with name "+pi.getFirstName()+" "+pi.getLastName()+" has role not allowed for PI: "+pi.getUserRole()));
                }
            }  
        }
                
        isValid = isValid && stringNotEmpty(objectToValidate.getLogin(), false, ValidatorSeverity.FAIL, "Login",messages);
        isValid = isValid && stringNotEmpty(objectToValidate.getPhone(), false, ValidatorSeverity.FAIL, "Phone",messages);
        isValid = isValid && validUserName(objectToValidate.getFirstName(),objectToValidate.getLastName(), ValidatorSeverity.FAIL,messages);
        isValid = isValid && validEmail(objectToValidate.getMailAddress(), ValidatorSeverity.FAIL,messages);
        
        AffiliationDTO affiliation = objectToValidate.getAffiliation();
        if (affiliation == null || affiliation.getDepartment()==null || affiliation.getOrganization() == null){
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User AFfiliation","Users must have a valid Affiliation"));
        }else {
            OrganizationDTO orgaFromDB = services.getUserService().getOrganizationByName(affiliation.getOrganizationName());
            if (orgaFromDB == null){
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User AFfiliation","Organization "+affiliation.getOrganizationName()+" not found in DB"));
            }else{
                boolean deptFound = false;
                for (DepartmentDTO dbDept: orgaFromDB.getDepartments()){
                    if (dbDept.getName().equals(affiliation.getDepartmentName())){
                        deptFound = true;
                        break;
                    }
                }
                if (!deptFound){
                    isValid = false;
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"User AFfiliation","Department "+affiliation.getDepartmentName()+" not found in DB for orga "+affiliation.getOrganizationName()));
                }
            }
        }
        
        return isValid;
    }

    private boolean validUserName(String firstName, String lastName, ValidatorSeverity severity, Set<ValidatorMessage> messages) {
        
        if (!stringNotEmpty(firstName, false, severity, "First Name", messages)) {
            return false;
        }
        
        if (!stringNotEmpty(lastName, false, severity, "Last Name", messages)) {
            return false;
        }
        
        return true;
    }

}
