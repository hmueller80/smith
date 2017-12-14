/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
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
public class RequestSamplesValidator extends AbstractValidator<List<RequestSampleDTO>> {

    public RequestSamplesValidator(List<RequestSampleDTO> objectToValidate) {
        super(new LinkedList<RequestSampleDTO>(objectToValidate));
    }

    @Override
    protected boolean objectIsValid() {
        return validateObjectInternal();
    }

    private boolean validateObjectInternal() {
        boolean isValid = true;

        Set<String> sampleNames = new HashSet<>();
        
        ListIterator iter = (ListIterator) objectToValidate.iterator();
        while (iter.hasNext()) {
            RequestSampleValidator sampleValidator = new RequestSampleValidator((RequestSampleDTO) iter.next());
            
            RequestSampleDTO validatedSample;
            try {
                validatedSample = sampleValidator.getValidatedObject();
            } catch (ValidatorException ex) {
                isValid = false;
                validatedSample = sampleValidator.forceGetObect();
            }
            messages.addAll(sampleValidator.getMessages());
            iter.set(validatedSample);
           
            String sampleName = validatedSample.getSampleName();
            if (sampleNames.contains(sampleName)) {
                Integer sampleNonce = 1;
                Matcher matcher = RequestFormDTO.NAME_PATTERN.matcher(sampleName);
                String prefix = matcher.matches() ? matcher.group(1) : sampleName;

                while (sampleNames.contains(sampleName)) {
                    sampleName = prefix + RequestFormDTO.DEFAULT_SUFFIX + sampleNonce;
                    sampleNonce += 1;
                }
            }

            sampleNames.add(sampleName);
            validatedSample.setSampleName(sampleName);
        }
                
        return isValid;
    }
}

