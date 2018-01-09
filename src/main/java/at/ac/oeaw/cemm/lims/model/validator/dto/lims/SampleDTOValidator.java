/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.dto.lims;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.model.validator.ValidationStatus;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import at.ac.oeaw.cemm.lims.model.validator.dto.generic.SampleValidator;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class SampleDTOValidator extends SampleValidator<SampleDTO> {

    
    @Override
    public boolean validateInternal(SampleDTO objectToValidate, Set<ValidatorMessage> messages) {
 
        boolean isValid = super.validateInternal(objectToValidate, messages);
           
        ValidationStatus appValidation = new ApplicationDTOValidator().isValid(objectToValidate.getApplication());
        isValid = isValid && appValidation.isValid();
        messages.addAll(appValidation.getValidationMessages());
        
        isValid = isValid && stringNotEmpty(objectToValidate.getCostcenter(),true,ValidatorSeverity.FAIL,"Cost Center",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getConcentration(),ValidatorSeverity.FAIL,"Sample Concentration",messages);
        isValid = isValid && stringNotEmpty(objectToValidate.getOrganism(), false,ValidatorSeverity.FAIL,"Organism",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getTotalAmount(), ValidatorSeverity.FAIL,"Total Amount",messages);
        isValid = isValid && validPositiveNumber(objectToValidate.getBulkFragmentSize(), ValidatorSeverity.FAIL,"Bulk Fragment Size",messages);
       
        if (objectToValidate.getDescription()!= null && !objectToValidate.getDescription().isEmpty()){
            isValid = isValid && stringMatchesPattern(objectToValidate.getDescription(),
                    "[a-zA-Z_0-9£$&%?'.]*",
                    ValidatorSeverity.FAIL,"Description",messages);
        }
               
        return isValid;
    }
  
}
