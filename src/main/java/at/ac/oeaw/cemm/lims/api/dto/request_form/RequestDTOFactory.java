/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;

/**
 *
 * @author dbarreca
 */
public interface RequestDTOFactory {

    AffiliationDTO getAffiliationDTO();

    RequestFormDTO getRequestFormDTO(RequestorDTO requestor);

    RequestLibraryDTO getRequestLibraryDTO();

    RequestSampleDTO getRequestSampleDTO();

    RequestorDTO getRequestorDTO(UserDTO requestor, UserDTO pi);
    
}
