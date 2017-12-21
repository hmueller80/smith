/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;

/**
 *
 * @author dbarreca
 */
public class AffiliationDTOImpl implements AffiliationDTO {
    
    private OrganizationDTO orga;
    private DepartmentDTO department;

    protected AffiliationDTOImpl(OrganizationDTO orga, DepartmentDTO department) {
        this.orga = orga;
        this.department = department;
    }

    @Override
    public String getAddress() {
        return (department.getAddress() != null && !department.getAddress().isEmpty()) ? department.getAddress() : orga.getAddress();
    }

    @Override
    public String getDepartmentName() {
        return department.getName();
    }

    @Override
    public DepartmentDTO getDepartment() {
        return department;
    }

    @Override
    public String getOrganizationName() {
        return orga.getName();
    }

    @Override
    public OrganizationDTO getOrganization() {
        return orga;
    }

    @Override
    public String getUrl() {
        return (department.getWebPage()!= null && !department.getWebPage().isEmpty()) ? department.getWebPage() : orga.getWebPage();
    }

    
    
 
    
    
    
}
