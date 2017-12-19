/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.generic;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import at.ac.oeaw.cemm.lims.api.dto.generic.Request;
import at.ac.oeaw.cemm.lims.api.dto.generic.Sample;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class RequestValidator<T extends Request> extends AbstractValidator<T> {
    private final ExistingUserValidator userValidator; 
    private final LibraryValidator libraryValidator;
   

    public <T extends LibraryValidator> RequestValidator(T libraryValidator,ServiceFactory services){
        this.libraryValidator = libraryValidator;
        this.userValidator = new ExistingUserValidator(services);
    }

    @Override
    protected boolean validateInternal(T objectToValidate, Set<ValidatorMessage> messages) {
        
        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Request", "Request is null"));
            return false;
        }
        
        boolean isValid = validateLibraries(objectToValidate.getLibraries(), messages);
        
        ValidationStatus userValidation = userValidator.isValid(objectToValidate.getRequestorUser());
        messages.addAll(userValidation.getValidationMessages());
        
        isValid = isValid && userValidation.isValid();

        return isValid;
    }

    private boolean validateLibraries(List<? extends Library> libraries,Set<ValidatorMessage> messages  ) {
        boolean isValid = true;
        
        Set<String> sampleNames = new HashSet<>();
        Set<String> librariesNames = new HashSet<>();
        
        isValid = isValid && collectionNotEmpty(libraries,ValidatorSeverity.FAIL,"Libraries in request",messages);
        
        for (Library library: libraries){
            
            ValidationStatus libraryValidation = libraryValidator.isValid(library);
            isValid = isValid && libraryValidation.isValid();
            messages.addAll(libraryValidation.getValidationMessages());
          
            if (librariesNames.contains(library.getName())){
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL,"Library Name","Duplicate name for library "+library.getName()));
                isValid = false;
            }else{
                librariesNames.add(library.getName());
            }
            
            for (Sample sample : library.getSamples()) {
                if (sampleNames.contains(sample.getName())) {
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Sample Name", "Duplicate name for sample " + sample.getName()));
                    isValid = false;
                } else {
                    sampleNames.add(sample.getName());
                }
            }            
            
        }
        
        return isValid;
        
    }

}
