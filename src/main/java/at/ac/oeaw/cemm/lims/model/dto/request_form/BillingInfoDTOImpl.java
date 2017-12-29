/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.BillingInfoDTO;

/**
 *
 * @author dbarreca
 */
public class BillingInfoDTOImpl implements BillingInfoDTO {
    private String contact;
    private String address;
    private String billingCode;
    
    public BillingInfoDTOImpl() {
    }

    public BillingInfoDTOImpl(String contact, String address, String billingCode) {
        this.contact = contact;
        this.address = address;
        this.billingCode = billingCode;
    }

    @Override
    public String getContact() {
        return contact;
    }

    @Override
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getBillingCode() {
        return billingCode;
    }

    @Override
    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }
    
    
}
