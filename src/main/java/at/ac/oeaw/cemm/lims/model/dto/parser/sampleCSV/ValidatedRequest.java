/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.parser.sampleCSV;

import at.ac.oeaw.cemm.lims.model.dto.RequestDTO;

/**
 *
 * @author dbarreca
 */
public class ValidatedRequest {
    private RequestDTO requestObj;
    private BuilderValidationStatus validationStatus;

    public ValidatedRequest(RequestDTO requestObj, BuilderValidationStatus validationStatus) {
        this.requestObj = requestObj;
        this.validationStatus = validationStatus;
    }

 
    
    public RequestDTO getRequestObj() {
        if (validationStatus.isFailed){
            return null;
        }else{
            return requestObj;
        }
    }

   
    public BuilderValidationStatus getValidationStatus() {
        return validationStatus;
    }

  
    
}
