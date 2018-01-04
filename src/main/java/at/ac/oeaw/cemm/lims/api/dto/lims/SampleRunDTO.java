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
public interface SampleRunDTO {

    public Integer getRunId();
    
    public SampleDTO getSample();
    public UserDTO getOperator();
    public String getFlowcell();
    public String getLanesString();
    public Boolean getIsControl();
    public String getRunFolder();
    public Set<String> getLanes();
    
    public void setFlowcell(String flowCell);
    public void addLane(String lane);
    public void setIsControl(boolean isControl);
    public void setRunId(Integer id);
    public void setRunFolder(String runFolder);
    public void setLanes(Set<String> lanes);      
}
