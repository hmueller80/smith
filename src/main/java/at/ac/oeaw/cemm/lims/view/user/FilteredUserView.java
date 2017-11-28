/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.user;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="filteredUserView")
@ViewScoped
public class FilteredUserView {
    @Inject private ServiceFactory services;
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
    
    private List<UserDTO> users=new LinkedList<>();
    private List<UserDTO> filteredUsers=new LinkedList<>();

    @PostConstruct
    public void init() {
        users = services.getUserService().getAllUsers();
        filteredUsers= users;
    }
    
    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public List<UserDTO> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<UserDTO> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }    
    
    
}
