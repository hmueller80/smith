/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

/**
 *
 * @author dbarreca
 */
public interface BillingInfoDTO {
    
    String getContact();
    void setContact(String contact);
    
    String getAddress();
    void setAddress(String address);
    
    String getBillingCode();
    void setBillingCode(String code);
    
    
}
