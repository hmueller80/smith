/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import java.util.Date;

/**
 *
 * @author dbarreca
 */
public interface RequestDTOFactory {


    RequestFormDTO getRequestFormDTO(RequestorDTO requestor);

    RequestFormDTO getRequestFormDTO(Integer id, RequestorDTO requestor, Date date, String status);

    RequestLibraryDTO getRequestLibraryDTO(boolean nameEditabe);

    RequestLibraryDTO getEmptyRequestLibraryDTO(boolean nameEditabe);

    RequestSampleDTO getRequestSampleDTO(boolean nameEditabe);

    RequestorDTO getRequestorDTO(UserDTO requestor, UserDTO pi);
    
    RequestLibraryDTO getRequestLibraryDTO(Integer id,boolean nameEditabe);

    RequestSampleDTO getRequestSampleDTO(Integer id,boolean nameEditabe);

}
