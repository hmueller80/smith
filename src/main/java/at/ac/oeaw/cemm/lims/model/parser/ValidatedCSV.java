/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser;


/**
 *
 * @author dbarreca
 */
public class ValidatedCSV<T> {
    private T requestObj;
    private CSVValidationStatus validationStatus;

    public ValidatedCSV(T requestObj, CSVValidationStatus validationStatus) {
        this.requestObj = requestObj;
        this.validationStatus = validationStatus;
    }

 
    
    public T getRequestObj() {
        if (validationStatus.isFailed()){
            return null;
        }else{
            return requestObj;
        }
    }

   
    public CSVValidationStatus getValidationStatus() {
        return validationStatus;
    }

  
    
}
