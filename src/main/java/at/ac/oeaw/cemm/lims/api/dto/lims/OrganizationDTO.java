/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.lims;

import java.util.Set;

/**
 *
 * @author dbarreca
 */
public interface OrganizationDTO {
    public final static String DEFAULT_ORGA = "NONE";
    
    String getName();
    String getAddress();
    String getWebPage();
    
    void setAddress(String address);
    void setWebPage(String webPage);
    
    Set<DepartmentDTO> getDepartments();
    
    void addDepartment(DepartmentDTO department);
    
}
