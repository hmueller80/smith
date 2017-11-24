/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;

/**
 *
 * @author dbarreca
 */
public interface SampleRunDTO {

    public Integer getId();
    
    public SampleDTO getSample();
    public UserDTO getOperator();
    public String getFlowcell();
    public String getLanesString();
    public Boolean getIsControl();
    public String getRunFolder();

    
}
