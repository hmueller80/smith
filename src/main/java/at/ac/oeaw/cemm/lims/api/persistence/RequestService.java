/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public interface RequestService {

    Set<PersistedEntityReceipt> uploadRequest(RequestDTO request) throws Exception;
    
    public boolean checkRequestExistence(Integer requestId);
           
    List<RequestDTO> getDeleatableLibrariesInRequests();

    public void deleteAllLibrariesInRequest(RequestDTO request) throws Exception;

    public void deleteAllSamplesForLibraryAndRequest(Integer libraryId,Integer requestId) throws Exception;

    public List<LibraryDTO> getEditableLibrariesInRequest(final Integer requestId);

    public LibraryDTO getLibraryByName(String libraryName);

    public void deleteLibraryIfEmpty(String oldLibraryName);

    public RequestDTO getMinimalRequestById(Integer rid);
}
