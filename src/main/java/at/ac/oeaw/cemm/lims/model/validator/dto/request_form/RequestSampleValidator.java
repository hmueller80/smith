/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.util.NameFilter;

/**
 *
 * @author dbarreca
 */
public class RequestSampleValidator  extends AbstractValidator<RequestSampleDTO> {

    public RequestSampleValidator(RequestSampleDTO objectToValidate) {
        super(objectToValidate);
    }


    @Override
    protected boolean objectIsValid() {
        boolean isValid = true;

        String sampleName = objectToValidate.getSampleName();
        if (sampleName == null || sampleName.trim().isEmpty()) {
            sampleName = RequestFormDTO.DEFAULT_NAME;
        } else {
            sampleName = NameFilter.legalize(sampleName);
        }

        objectToValidate.setSampleName(sampleName);

        String libraryName = objectToValidate.getLibrary();
        if (libraryName == null || libraryName.trim().isEmpty()) {
            libraryName = RequestFormDTO.DEFAULT_LIBRARY;
        } else {
            libraryName = NameFilter.legalize(libraryName.trim().toUpperCase());
        }
        objectToValidate.setLibrary(libraryName);

        String i5Index = objectToValidate.getI5Index();
        i5Index = (i5Index == null || i5Index.trim().isEmpty()) ? RequestFormDTO.DEFAULT_INDEX : i5Index.trim().toUpperCase();
        if (!i5Index.matches(RequestFormDTO.INDEX_REGEXP)) {
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "I5 Index", "I5 Index in sample " + sampleName + " is not valid"));
            i5Index = RequestFormDTO.DEFAULT_INDEX;
        }
        objectToValidate.setI5Index(i5Index);

        String i7Index = objectToValidate.getI7Index();
        i7Index = (i7Index == null || i7Index.trim().isEmpty()) ? RequestFormDTO.DEFAULT_INDEX : i7Index.trim().toUpperCase();
        if (!i7Index.matches(RequestFormDTO.INDEX_REGEXP)) {
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "I7 Index", "I7 Index in sample " + sampleName + " is not valid"));
            i7Index = RequestFormDTO.DEFAULT_INDEX;
        }
        objectToValidate.setI7Index(i7Index);

        String primerIndex = objectToValidate.getPrimerSequence();
        primerIndex = (primerIndex == null || primerIndex.trim().isEmpty()) ? RequestFormDTO.DEFAULT_INDEX : primerIndex.trim().toUpperCase();
        if (!primerIndex.matches(RequestFormDTO.INDEX_REGEXP)) {
            isValid = false;
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Primer sequence", "Primer sequence in sample " + sampleName + " is not valid"));
            primerIndex = RequestFormDTO.DEFAULT_INDEX;
        }
        objectToValidate.setPrimerSequence(primerIndex);
        
        return isValid;
    }
    
}
