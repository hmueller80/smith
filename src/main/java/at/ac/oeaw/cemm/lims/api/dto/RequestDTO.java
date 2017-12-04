/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;

import java.util.Map;

/**
 *
 * @author dbarreca
 */
public interface RequestDTO {

    LibraryDTO addOrGetLibrary(LibraryDTO library);

    Map<String, LibraryDTO> getLibraries();

    UserDTO getRequestor();
    
    Integer getRequestId();
    
}
