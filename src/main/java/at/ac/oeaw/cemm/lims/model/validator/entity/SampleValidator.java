/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.validator.entity;

import at.ac.oeaw.cemm.lims.model.validator.AbstractValidator;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorMessage;
import at.ac.oeaw.cemm.lims.model.validator.ValidatorSeverity;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;

/**
 *
 * @author dbarreca
 */
public class SampleValidator extends AbstractValidator<Sample> {

    public SampleValidator(Sample objectToValidate) {
        super(objectToValidate);
    }

    @Override
    public boolean objectIsValid() {
 
        boolean isValid = true;
        
        isValid = isValid && stringNotEmpty(objectToValidate.getCostCenter(),true,ValidatorSeverity.FAIL,"Cost Center");
        


        if (!objectToValidate.getLibrarySynthesisNeeded()) {           
            isValid = isValid && validPositiveNumber(objectToValidate.getBionalyzerBiomolarity(),ValidatorSeverity.FAIL,"Bioanalyzer Biomolarity");
        } else {
            objectToValidate.setBioanalyzerDate(null);
            objectToValidate.setBionalyzerBiomolarity(null);
            objectToValidate.setSequencingIndexes(new SequencingIndex("none"));
            messages.add(new ValidatorMessage(ValidatorSeverity.WARNING,"Bioanalyzer Date","Bioanalyzer Date was reset since library synth. is needed"));
            messages.add(new ValidatorMessage(ValidatorSeverity.WARNING,"Bioanalyzer Molarity","Bioanalyzer Molarity was reset since library synth. is needed"));
            messages.add(new ValidatorMessage(ValidatorSeverity.WARNING,"Sequencing Index","Sequencing Index was reset since library synth. is needed"));
        }
        
        isValid = isValid && stringNotEmpty(objectToValidate.getName(),true,ValidatorSeverity.FAIL,"Sample Name");
        isValid = isValid && validPositiveNumber(objectToValidate.getConcentration(),ValidatorSeverity.FAIL,"Sample Concentration");
        isValid = isValid && stringNotEmpty(objectToValidate.getOrganism(), false,ValidatorSeverity.FAIL,"Organism");
        isValid = isValid && stringNotEmpty(objectToValidate.getType(), false,ValidatorSeverity.FAIL,"Sample Type");
        isValid = isValid && validPositiveNumber(objectToValidate.getTotalAmount(), ValidatorSeverity.FAIL,"Total Amount");
        isValid = isValid && validPositiveNumber(objectToValidate.getBulkFragmentSize(), ValidatorSeverity.FAIL,"Bulk Fragment Size");
        
        if (Application.CHIP_SEQ.equals(objectToValidate.getApplication().getApplicationname())){
             isValid = isValid
                     && stringNotEmpty(objectToValidate.getAntibody(),true,ValidatorSeverity.FAIL,"Antibody");
        }else {
            objectToValidate.setAntibody("");
            messages.add(new ValidatorMessage(ValidatorSeverity.WARNING,"Anitbody","Antibody was reset"));
        }
       
        if (stringNotEmpty(objectToValidate.getDescription(),true,ValidatorSeverity.WARNING,"Description")){
            isValid = isValid && stringMatchesPattern(objectToValidate.getDescription(),
                    "[a-zA-Z_0-9£$&%?'.]*",
                    ValidatorSeverity.FAIL,"Description");
        }
        
        stringNotEmpty(objectToValidate.getComment(),true,ValidatorSeverity.WARNING,"Comment");
        
        return isValid;
    }
   
  

}
