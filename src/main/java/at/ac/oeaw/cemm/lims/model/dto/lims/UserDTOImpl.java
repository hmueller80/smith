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
    private String firstName;
    private String lastName;
    private String login;
    private String phone;
    private String mailAddress;
    private Integer pi;
    private String userRole; 
    private AffiliationDTO affiliation = null;
    
    public UserDTOImpl(Integer id, String firstName, String lastName, String login, String phone, String mailAddress, Integer pi, String userRole, AffiliationDTO affiliation) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.phone = phone;
        this.mailAddress = mailAddress;
        this.pi = pi;
        this.userRole = userRole;
        this.affiliation = affiliation;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public boolean equals(Object other){
        if (other instanceof UserDTOImpl ){
            if (this.id == null){
                return ((UserDTOImpl) other).getId()==null;
            }
            if (this.id.equals(((UserDTOImpl) other).getId())){
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (this.id == null) {
            return 23*Objects.hashCode(this.login);
        }else{
            return 23 * hash + Objects.hashCode(this.id);
        }
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
