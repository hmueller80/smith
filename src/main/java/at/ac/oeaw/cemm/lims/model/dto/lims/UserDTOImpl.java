/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
public class UserDTOImpl implements UserDTO, Serializable {

    private Integer id;
    private String userName;
    private String login;
    private String phone;
    private String mailAddress;
    private Integer pi;
    private String userRole; 
    private AffiliationDTO affiliation = null;
    
    public UserDTOImpl(Integer id, String userName, String login, String phone, String mailAddress, Integer pi, String userRole, AffiliationDTO affiliation) {
        this.id = id;
        this.userName = userName;
        this.login = login;
        this.phone = phone;
        this.mailAddress = mailAddress;
        this.pi = pi;
        this.userRole = userRole;
        this.affiliation = affiliation;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getMailAddress() {
        return mailAddress;
    }

    @Override
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    @Override
    public Integer getPi() {
        return pi;
    }

    @Override
    public void setPi(Integer pi) {
        this.pi = pi;
    }

    @Override
    public String getUserRole() {
        return userRole;
    }

    @Override
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Override
    public String getFirstName() {
        String firstName = "";
        if (userName != null) {
            if (userName.indexOf(",") > -1) {
                firstName = userName.split(",")[0];
            } else {
                firstName = userName;
            }
        }
        return firstName;
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof UserDTO ){
            if (this.id == ((UserDTO) other).getId() ){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public AffiliationDTO getAffiliation() {
        return affiliation;
    }

    @Override
    public void setAffiliation(AffiliationDTO affiliation) {
        this.affiliation = affiliation;
    }

}
