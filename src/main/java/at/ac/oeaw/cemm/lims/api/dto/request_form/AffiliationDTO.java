/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;

/**
 *
 * @author dbarreca
 */
public interface AffiliationDTO {

    String getAddress();

    String getDepartmentName();

    DepartmentDTO getDepartment();
    
    String getOrganizationName();
    
    OrganizationDTO getOrganization();

    String getUrl();
}
