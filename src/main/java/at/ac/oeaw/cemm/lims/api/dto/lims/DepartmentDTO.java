/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.lims;

/**
 *
 * @author dbarreca
 */
public interface DepartmentDTO {
    public final static String DEFAULT_DEPT = "NONE";

    String getName();
    String getAddress();
    String getWebPage();
    
    void setAddress(String address);
    void setWebPage(String webPage);
}
