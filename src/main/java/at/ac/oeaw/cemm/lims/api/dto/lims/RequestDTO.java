/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import at.ac.oeaw.cemm.lims.api.dto.generic.Request;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public interface RequestDTO extends Request {

    LibraryDTO addOrGetLibrary(LibraryDTO library);

    Map<String, LibraryDTO> getLibrariesMap();

    @Override
    UserDTO getRequestorUser();
    
    @Override
    public Integer getRequestId();
    
    @Override
    public List<? extends Library> getLibraries();
        
}
