/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.BillingInfoDTO;
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
    public RequestFormDTO getRequestFormDTO(RequestorDTO requestor, BillingInfoDTO billingInfo) {
        return new RequestFormDTOImpl(requestor,billingInfo);
    }
    
    @Override
    public RequestLibraryDTO getRequestLibraryDTO(boolean nameEditabe) {
        return new RequestLibraryDTOImpl(nameEditabe);
    }
    
    @Override
    public RequestSampleDTO getRequestSampleDTO(boolean nameEditabe) {
        return new RequestSampleDTOImpl(nameEditabe);
    }
    
    @Override
    public RequestLibraryDTO getRequestLibraryDTO(Integer id,boolean nameEditabe) {
        return new RequestLibraryDTOImpl(id,nameEditabe);
    }
    
    @Override
    public RequestLibraryDTO getEmptyRequestLibraryDTO(boolean nameEditabe) {
        RequestLibraryDTOImpl theResult = new RequestLibraryDTOImpl(nameEditabe);
        theResult.resetLibraryData();
        return theResult;
    }

    
    @Override
    public RequestSampleDTO getRequestSampleDTO(Integer id, boolean nameEditabe) {
        return new RequestSampleDTOImpl(id,nameEditabe);
    }

    @Override
    public RequestFormDTO getRequestFormDTO(Integer id, RequestorDTO requestor,BillingInfoDTO billingInfo,Date date, String status) {
        return new RequestFormDTOImpl(id,requestor,billingInfo, date, status);
    }

    @Override
    public BillingInfoDTO getBillingInfoDTO(String contact, String address, String code) {
        return new BillingInfoDTOImpl(contact,address,code);
    }

    @Override
    public BillingInfoDTO getEmptyBillingInfoDTO() {
        return new BillingInfoDTOImpl();
    }

}
