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
public interface AffiliationDTO {

    String getAddress();

    String getDepartment();

    String getName();

    String getUrl();

    void setAddress(String address);

    void setDepartment(String deptartment);

    void setName(String name);

    void setUrl(String url);
    
}
