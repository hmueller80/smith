/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import java.util.Date;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestDTOFactoryImpl implements RequestDTOFactory {
    
    @Override
    public RequestorDTO getRequestorDTO(UserDTO requestor, UserDTO pi){
        return new RequestorDTOImpl(requestor,pi);
    }
    
    @Override
    public RequestFormDTO getRequestFormDTO(RequestorDTO requestor) {
        return new RequestFormDTOImpl(requestor);
    }
    
    @Override
    public RequestLibraryDTO getRequestLibraryDTO() {
        return new RequestLibraryDTOImpl();
    }
    
    @Override
    public RequestSampleDTO getRequestSampleDTO() {
        return new RequestSampleDTOImpl();
    }
    
    @Override
    public RequestLibraryDTO getRequestLibraryDTO(Integer id) {
        return new RequestLibraryDTOImpl(id);
    }
    
    @Override
    public RequestLibraryDTO getEmptyRequestLibraryDTO() {
        RequestLibraryDTOImpl theResult = new RequestLibraryDTOImpl();
        theResult.resetLibraryData();
        return theResult;
    }

    
    @Override
    public RequestSampleDTO getRequestSampleDTO(Integer id) {
        return new RequestSampleDTOImpl(id);
    }

    @Override
    public RequestFormDTO getRequestFormDTO(Integer id, RequestorDTO requestor,Date date, String status) {
        return new RequestFormDTOImpl(id,requestor,date, status);
    }

}
