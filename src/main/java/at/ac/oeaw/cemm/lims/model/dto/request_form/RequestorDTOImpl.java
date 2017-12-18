/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;

/**
 *
 * @author dbarreca
 */
public class RequestorDTOImpl implements RequestorDTO {
    private final UserDTO user;
    private final UserDTO pi;
    private final AffiliationDTO affiliation;

     protected RequestorDTOImpl(UserDTO user, UserDTO pi, AffiliationDTO affiliation) {
        this.user = user;
        this.pi = pi;
        this.affiliation = affiliation;
    }
    
    protected RequestorDTOImpl(UserDTO user, UserDTO pi) {
        this.user = user;
        this.pi = pi;
        this.affiliation = new AffiliationDTOImpl();
    }
    
    
    @Override
    public UserDTO getUser() {
        return user;
    }


    @Override
    public UserDTO getPi() {
        return pi;
    }


    @Override
    public AffiliationDTO getAffiliation() {
        return affiliation;
    }

    
}
