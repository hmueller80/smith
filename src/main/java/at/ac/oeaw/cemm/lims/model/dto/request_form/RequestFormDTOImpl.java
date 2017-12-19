/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.generic.User;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
public class RequestFormDTOImpl implements RequestFormDTO {
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private final Integer id;
    private final RequestorDTO requestor;    
    private final Date date;
    private final String status;
    
    private Map<String, RequestLibraryDTO> libraries;

    protected RequestFormDTOImpl(Integer id, RequestorDTO requestor, Date date, String status){
        this.id = id;
        this.requestor = requestor;
        this.date = date;
        this.status = status;
        libraries = new LinkedHashMap<>();
    }
    
    protected RequestFormDTOImpl(RequestorDTO requestor) {
        id = null;
        this.requestor = requestor;
        libraries = new LinkedHashMap<>();
        date = new Date();
        this.status = RequestFormDTO.STATUS_NEW;
    }
    
    @Override
    public Integer getRequestId() {
        return id;
    }
 
    @Override
    public Date getDate() {
        return date;
    }
    
    @Override
    public void resetLibraries() {
      for (RequestLibraryDTO library: libraries.values()) {
          library.resetSamples();
      }
    }

    @Override
    public List<RequestLibraryDTO> getLibraries() {
        return new LinkedList<>(libraries.values());
    }
    
    @Override
    public RequestLibraryDTO getLibraryByUUID(String uuid) {
        return libraries.get(uuid);
    }

    @Override
    public void addLibrary(RequestLibraryDTO library) {
        libraries.put(library.getUuid(), library);
    }

    @Override
    public RequestorDTO getRequestor() {
        return requestor;
    }

    @Override
    public void removeEmptyLibraries() {
        Iterator<Entry<String,RequestLibraryDTO>> iter = libraries.entrySet().iterator();
        while (iter.hasNext()){
            Entry<String,RequestLibraryDTO> entry = iter.next();
            if (entry.getValue().getSamples().isEmpty()){
                iter.remove();
            }
        }
        
    }
    
    @JsonIgnore
    @Override
    public User getRequestorUser() {
        return requestor.getUser();
    }

    @Override
    public String getDateAsString() {
        return dateFormatter.format(date);
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.requestor.getUser().getId());
        hash = 29 * hash + Objects.hashCode(this.date);
        hash = 29 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RequestFormDTOImpl other = (RequestFormDTOImpl) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.requestor.getUser().getId(), other.requestor.getUser().getId())) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }

    
    
    
}
