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
public interface LibraryToRunDTO {
    
    public String getReadMode();
    
    public Integer getReadLength();
        
    public UserDTO getRequestor();
    
    public Integer getRequestId();
    
    public LibraryDTO getLibrary();
    
    public String getLanes();
    
    public void setLanes(String lanes);
    
    public Set<String> getLanesSet();
    
}
