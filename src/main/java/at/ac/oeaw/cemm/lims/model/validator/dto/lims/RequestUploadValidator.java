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
import at.ac.oeaw.cemm.lims.util.RequestIdBean;
import java.util.Set;

public class RequestUploadValidator extends RequestValidator<RequestDTO> {
    
    private RequestIdBean requestIdBean;
    
    public <T extends LibraryValidator> RequestUploadValidator(T libraryValidator, ServiceFactory services, RequestIdBean requestIdBean) {
        super(libraryValidator, services);
        this.requestIdBean = requestIdBean;
    }
    
    @Override
    protected boolean validateInternal(RequestDTO objectToValidate, Set<ValidatorMessage> messages) {      
        boolean isValid = super.validateInternal(objectToValidate, messages);
        
        try{
            if (objectToValidate.getRequestId() < requestIdBean.getNextId()){
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"Submission Id","Samples or requests with this ID already exists"));
            }
        }finally{
            requestIdBean.unlock();
        }
                
        return isValid;
    }

}
