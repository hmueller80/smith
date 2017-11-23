/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
class RequestDTOImpl implements RequestDTO
{
    private String requestor;
    private Map<String,LibraryDTO> libraries=new HashMap<>();
    
    public RequestDTOImpl(String requestor){
        this.requestor=requestor;
    }
    
   
    @Override
    public LibraryDTO addOrGetLibrary(LibraryDTO library){
        if (!libraries.containsKey(library.getName())){
            libraries.put(library.getName(), library);
        }
  
        return libraries.get(library.getName());
    }

    @Override
    public String getRequestor() {
        return requestor;
    }

    @Override
    public Map<String,LibraryDTO> getLibraries() {
        return libraries;
    }
    
}
