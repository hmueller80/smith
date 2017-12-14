/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;

/**
 *
 * @author dbarreca
 */
public class RequestLibrariesValidator extends AbstractValidator<List<RequestLibraryDTO>>{

    public RequestLibrariesValidator(List<RequestLibraryDTO> objectToValidate) {
        super(new LinkedList<RequestLibraryDTO>(objectToValidate));
    }

    @Override
    protected boolean objectIsValid() {
        return validateObjectInternal();
    }
    
    private boolean validateObjectInternal() {
        boolean isValid = true;

        Set<String> libraryNames = new HashSet<>();
        
        ListIterator iter = (ListIterator) objectToValidate.iterator();
        while (iter.hasNext()) {
            RequestLibraryValidator libraryValidator = new RequestLibraryValidator((RequestLibraryDTO) iter.next());
            
            RequestLibraryDTO validatedLibrary;
            try {
                validatedLibrary = libraryValidator.getValidatedObject();
            } catch (ValidatorException ex) {
                isValid = false;
                validatedLibrary = libraryValidator.forceGetObect();
            }
            messages.addAll(libraryValidator.getMessages());
            iter.set(validatedLibrary);
           
            String libraryName = validatedLibrary.getName();
            if (libraryNames.contains(libraryName)) {
                Integer sampleNonce = 1;
                Matcher matcher = RequestFormDTO.NAME_PATTERN.matcher(libraryName);
                String prefix = matcher.matches() ? matcher.group(1) : libraryName;

                while (libraryNames.contains(libraryName)) {
                    libraryName = prefix + RequestFormDTO.DEFAULT_SUFFIX + sampleNonce;
                    sampleNonce += 1;
                }
            }

            libraryNames.add(libraryName);
            validatedLibrary.setName(libraryName);
        }
                
        return isValid;
    }
    
}
