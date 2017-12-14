/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.util.Levenshtein;
import at.ac.oeaw.cemm.lims.util.NameFilter;

/**
 *
 * @author dbarreca
 */
public class RequestLibraryValidator extends AbstractValidator<RequestLibraryDTO> {

    public RequestLibraryValidator(RequestLibraryDTO objectToValidate) {
        super(objectToValidate);
    }

    @Override
    protected boolean objectIsValid() {
       return validateLibrary() &&  validateIndexes();
    }
    
    
    private boolean validateLibrary() {
        boolean isValid = true;
        
        String libraryName = objectToValidate.getName();
        if (stringNotEmpty(libraryName,false,ValidatorSeverity.FAIL,"Library Name")) {
            libraryName = NameFilter.legalize(libraryName.trim());
            objectToValidate.setName(libraryName);
        }else {
            isValid = false;
        }
        
        if (stringNotEmpty(objectToValidate.getReadMode(), false, ValidatorSeverity.FAIL, "Read Mode")) {
            String readMode = objectToValidate.getReadMode().trim().toUpperCase();
            if (!"PE".equals(readMode) && !"SR".equals(readMode)) {
                isValid = false;
                messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Read Mode", "Read Mode must be PE or SR"));
            }else{
                objectToValidate.setReadMode(readMode);
            }
        } else {
            isValid = false;
        }
       
        isValid = isValid && stringNotEmpty(objectToValidate.getType(), false, ValidatorSeverity.FAIL, "Application");
        isValid = isValid && validPositiveNumber(objectToValidate.getReadLength(), ValidatorSeverity.FAIL, "Read Length");
        isValid = isValid && validPositiveNumber(objectToValidate.getLanes(), ValidatorSeverity.FAIL, "Lanes");
        isValid = isValid && validPositiveNumber(objectToValidate.getVolume(), ValidatorSeverity.FAIL, "Volume");
        isValid = isValid && validPositiveNumber(objectToValidate.getDnaConcentration(), ValidatorSeverity.FAIL, "DNA Concentration");
        isValid = isValid && validPositiveNumber(objectToValidate.getTotalSize(), ValidatorSeverity.FAIL, "Total Size");
        
        return isValid;
    }
    
    private boolean validateIndexes() {
        boolean isValid = true;
        
        RequestSampleDTO[] samples = objectToValidate.getSamples().toArray(new RequestSampleDTO[objectToValidate.getSamples().size()]);

        for (int i = 0; i < samples.length - 1; i++) {
            for (int j = i + 1; j < samples.length; j++) {
                RequestSampleDTO thisSample = samples[i];
                RequestSampleDTO otherSample = samples[j];

                if (RequestFormDTO.DEFAULT_INDEX.equals(otherSample.getI7Index()) && RequestFormDTO.DEFAULT_INDEX.equals(otherSample.getI5Index())
                        || RequestFormDTO.DEFAULT_INDEX.equals(thisSample.getI7Index()) && RequestFormDTO.DEFAULT_INDEX.equals(thisSample.getI5Index())) {
                    String failMessage
                            = "There is more than one sample"
                            + " in library " + objectToValidate.getName()
                            + " with empty index ";

                    isValid = false;
                    messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));
                } else {
                    String thisIndex = thisSample.getI7Index() + "_" + thisSample.getI5Index();
                    String otherIndex = otherSample.getI7Index() + "_" + otherSample.getI5Index();

                    Integer distance = Levenshtein.computeLevenshteinDistance(thisIndex, otherIndex);

                    if (distance == 0) {
                        String failMessage
                                = "Indexes in samples " + thisSample.getSampleName()
                                + " and " + otherSample.getSampleName()
                                + " in library " + objectToValidate.getName()
                                + " are equal! ";

                        isValid = false;
                        messages.add(new ValidatorMessage(ValidatorSeverity.FAIL, "Index collision", failMessage));
                    } else if (distance <= 2) {
                        String warnMessage
                                = "Indexes in samples " + thisSample.getSampleName()
                                + " and " + otherSample.getSampleName()
                                + " in library " + objectToValidate.getName()
                                + " have edit distance of " + distance;

                        messages.add(new ValidatorMessage(ValidatorSeverity.WARNING, "Index similarity", warnMessage));
                    }
                }

            }
        }
        
        return isValid;
    }
         
}
