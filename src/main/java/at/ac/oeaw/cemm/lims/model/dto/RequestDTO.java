/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public class RequestDTO {
    private String requestor;
    private Map<String,LibraryDTO> libraries=new HashMap<>();
    
    public RequestDTO(String requestor){
        this.requestor=requestor;
    }
    
   
    public LibraryDTO addOrGetLibrary(LibraryDTO library){
        if (!libraries.containsKey(library.getName())){
            libraries.put(library.getName(), library);
        }
  
        return libraries.get(library.getName());
    }

    public String getRequestor() {
        return requestor;
    }

    public Map<String,LibraryDTO> getLibraries() {
        return libraries;
    }
    
}
