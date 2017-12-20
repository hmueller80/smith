/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.generic;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import at.ac.oeaw.cemm.lims.api.dto.generic.Sample;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.util.Levenshtein;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dbarreca
 */
public class LibraryValidator<T extends Library> extends AbstractValidator<T> {    
    
    private final SampleValidator sampleValidator;
    private final boolean checkSamples;
    
    public <T extends SampleValidator> LibraryValidator(T sampleValidator){
        this.sampleValidator = sampleValidator;
        checkSamples = true;
    }
    
    public <T extends SampleValidator> LibraryValidator(T sampleValidator, boolean checkSamples){
        this.sampleValidator = sampleValidator;
        this.checkSamples = checkSamples;
    }

    @Override
    protected boolean validateInternal(T objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;
        
        if (objectToValidate == null) {
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Library", "Library is null"));
            return false;
        }
        
        //Check That Library Name is Present and contains legal characters
        isValid = isValid && stringMatchesPattern(objectToValidate.getName(),"[A-Za-z0-9_]+",ValidatorSeverity.FAIL,"Library Name",messages);
        if (isValid) {
            if (objectToValidate.getName().contains("__")) {
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Library Name", "Library Name " + objectToValidate.getName() + " has a double underscore"));
            }
        }
        //Check consistency of indexes in library
        isValid = isValid && validateSamples(objectToValidate, messages);
        
        return isValid;
    }
    
    private boolean validateSamples(Library objectToValidate, Set<ValidatorMessage> messages) {
        boolean isValid = true;
        
        if (checkSamples){
            isValid = isValid && collectionNotEmpty(objectToValidate.getSamples(),ValidatorSeverity.FAIL,"Samples in library "+objectToValidate.getName(),messages);
        }
        
        Sample[] samples = objectToValidate.getSamples().toArray(new Sample[objectToValidate.getSamples().size()]);

        for (int i = 0; i < samples.length; i++) {
            Sample thisSample = samples[i];

            if (checkSamples) {
                ValidationStatus thisSampleValidation = sampleValidator.isValid(thisSample);
                isValid = isValid && thisSampleValidation.isValid();
                messages.addAll(thisSampleValidation.getValidationMessages());
            }
            for (int j = i + 1; j < samples.length; j++) {
                try {
                    Sample otherSample = samples[j];

                    isValid = isValid && validateIndexes(thisSample, otherSample, objectToValidate.getName(), messages);
                } catch (Exception ex) {
                    Logger.getLogger(LibraryValidator.class.getName()).log(Level.SEVERE, null, ex);
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Server error", ex.getMessage()));
                    return false;
                }
            }
        }
        
        return isValid;
    }
    
    private boolean validateIndexes( Sample thisSample, Sample otherSample, String libraryName, Set<ValidatorMessage> messages) {

        String thisIndex = thisSample.getCompoundIndex();
        String otherIndex = otherSample.getCompoundIndex();
        if (thisIndex == null){
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index error", "Sample "+thisSample.getName()+" has i5 index but not i7"));
            return false;
        }
        if (otherIndex == null){
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index error", "Sample "+otherSample.getName()+" has i5 index but not i7"));
            return false;
        }
        System.out.println("Checking sample "+thisSample.getName()+" and "+otherSample.getName());
        if (RequestFormDTO.DEFAULT_INDEX.equals(thisIndex)
                || RequestFormDTO.DEFAULT_INDEX.equals(otherIndex)) {
            String failMessage
                    = "There is more than one sample"
                    + " in library " + libraryName
                    + " with empty index ";
            
            messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));
            return false;
            
        } else {
            if (thisIndex.length() != otherIndex.length()) {
                String failMessage
                        = "Indexes in samples " + thisSample.getName()
                        + " and " + otherSample.getName()
                        + " in library " + libraryName
                        + " have different length! ";

                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));                
                return false;
                
            } else {

                Integer distance = Levenshtein.computeLevenshteinDistance(thisIndex, otherIndex);

                if (distance == 0) {
                    String failMessage
                            = "Indexes in samples " + thisSample.getName()
                            + " and " + otherSample.getName()
                            + " in library " + libraryName
                            + " are equal! ";

                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));
                    return false;
                   
                } else if (distance <= 2) {
                    String warnMessage
                            = "Indexes in samples " + thisSample.getName()
                            + " and " + otherSample.getName()
                            + " in library " + libraryName
                            + " have edit distance of " + distance;

                    messages.add(new ValidatorMessage(ValidatorSeverity.WARNING, "Index similarity", warnMessage));
                }
            }
        }
        
        return true;
    }
    
}
