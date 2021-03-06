/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.generic.User;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;

/**
 *
 * @author dbarreca
 */
public interface UserDTO extends User {

    @Override
    String getLogin();
    
    @Override
    String getMailAddress();
    
    String getPhone();

    Integer getPi();

    String getUserName();

    String getUserRole();
    
    AffiliationDTO getAffiliation();

    public String getFirstName();
            
    void setLogin(String login);

    void setMailAddress(String mailAddress);

    void setPhone(String phone);

    void setPi(Integer pi);

    void setUserName(String userName);

    void setUserRole(String userRole);
    
    void setId(Integer id);
    
    void setAffiliation(AffiliationDTO affiliation);
    
    Integer getId();          
    
}
