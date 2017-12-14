/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;

/**
 *
 * @author dbarreca
 */
public class AffiliationDTOImpl implements AffiliationDTO {
    
    private String name;
    private String department;
    private String address;
    private String url;
    
    protected AffiliationDTOImpl(String name, String department) {
        this.name = name;
        this.department = department;
    }
    
    protected AffiliationDTOImpl() {};
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(String deptartment) {
        this.department = deptartment;
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
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
}
