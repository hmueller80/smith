/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.generic;

import at.ac.oeaw.cemm.lims.api.dto.generic.Application;
import at.ac.oeaw.cemm.lims.api.dto.generic.User;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class ExistingUserValidator extends AbstractValidator<User> {
    ServiceFactory services;
    
    public ExistingUserValidator(ServiceFactory services) {
        this.services = services;
    }

    @Override
    protected boolean validateInternal(User objectToValidate, Set<ValidatorMessage> messages) {
        
        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "User", "User is null"));
            return false;
        }
         
        return userExists(objectToValidate,messages);
    }
    
    boolean userExists(User objectToValidate, Set<ValidatorMessage> messages) {
        UserDTO userFromPersistence;
        
        boolean hasLogin = stringNotEmpty(objectToValidate.getLogin(),false,ValidatorSeverity.WARNING,"User Login",messages);
        
        if (hasLogin){
            userFromPersistence = services.getUserService().getUserByLogin(objectToValidate.getLogin());
            if (userFromPersistence == null){
                messages.add(new ValidatorMessage(ValidatorSeverity.WARNING, "User", "User with login "+objectToValidate.getLogin()+" not found in LIMS DB"));
            }else{
                return true;
            }
        }
        
        if (validEmail(objectToValidate.getLogin(),ValidatorSeverity.FAIL,messages)){
            userFromPersistence = services.getUserService().getUserByMail(objectToValidate.getMailAddress());
            if (userFromPersistence == null){
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "User", "User with mail "+objectToValidate.getMailAddress()+" not found in LIMS DB"));
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
        
    }
    
}
