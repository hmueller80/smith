/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author dbarreca
 */
public class RequestFormDTOImpl implements RequestFormDTO {

    private final Integer id;
    private final RequestorDTO requestor;    
    private final Date date;
    private Map<String, RequestLibraryDTO> libraries;

    protected RequestFormDTOImpl(Integer id, RequestorDTO requestor, Date date){
        this.id = id;
        this.requestor = requestor;
        this.date = date;
        libraries = new LinkedHashMap<>();
    }
    
    protected RequestFormDTOImpl(RequestorDTO requestor) {
        id = null;
        this.requestor = requestor;
        libraries = new LinkedHashMap<>();
        date = new Date();
    }
    
    @Override
    public Integer getId() {
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

  
}
