/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.requestform;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;

/**
 *
 * @author dbarreca
 */
public class Requestor {
    private UserDTO user;
    private UserDTO pi;
    private Affiliation affiliation;

    public Requestor(UserDTO user, UserDTO pi) {
        this.user = user;
        this.pi = pi;
        this.affiliation = new Affiliation();
    }
    
    
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getPi() {
        return pi;
    }

    public void setPi(UserDTO pi) {
        this.pi = pi;
    }

    public Affiliation getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }
    
    
}
