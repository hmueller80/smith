/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
class RequestDTOImpl implements RequestDTO {   
    
    private Integer requestId;
    private UserDTO requestor;
    private Map<String,LibraryDTO> libraries=new HashMap<>();
    
    public RequestDTOImpl(UserDTO requestor,Integer requestId){
        this.requestor=requestor;
        this.requestId = requestId;
    }
    
   
    @Override
    public LibraryDTO addOrGetLibrary(LibraryDTO library){
        if (!libraries.containsKey(library.getName())){
            libraries.put(library.getName(), library);
        }
  
        return libraries.get(library.getName());
    }

    @Override
    public UserDTO getRequestor() {
        return requestor;
    }

    @Override
    public Map<String,LibraryDTO> getLibraries() {
        return libraries;
    }

    @Override
    public Integer getRequestId() {
        return requestId;
    }

    @Override
    public boolean equals(Object obj) {
       if (obj == null) return false;
       if (obj instanceof RequestDTO){
           return Objects.equals(((RequestDTO) obj).getRequestId(), this.getRequestId());
       }
       return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.requestId);
        return hash;
    }      
    
}
