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
public interface RunDTO {

    Integer getId();

    UserDTO getOperator();
    
    String getFlowCell();
    
    String getRunFolder();
    
}
