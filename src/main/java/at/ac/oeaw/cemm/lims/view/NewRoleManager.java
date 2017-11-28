/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hmuller
 */
@ManagedBean(name = "newRoleManager")
@SessionScoped
public class NewRoleManager implements Serializable {

    @Inject ServiceFactory services;
    
    UserDTO currentUser;
    UserDTO pi;
    boolean Admin;
    boolean Technician;
    boolean User;
    boolean GroupLeader;
    boolean Guest;

    @PostConstruct
    public void init() {
        System.out.println("NewRoleManager post construct");
        FacesContext context = FacesContext.getCurrentInstance();
        String loginName = context.getExternalContext().getRemoteUser();

        currentUser = services.getUserService().getUserByLogin(loginName);
        if (currentUser == null ){
            System.out.println("Could not find user with name: "+loginName);
            currentUser = services.getUserService().getUserByLogin("guest");
            currentUser.setUserRole(Preferences.ROLE_GUEST);
            pi = currentUser;
        }

        pi = services.getUserService().getUserByID(currentUser.getPi());
        
        Admin = currentUser.getUserRole().equals(Preferences.ROLE_ADMIN);
        Technician = currentUser.getUserRole().equals(Preferences.ROLE_TECHNICIAN);
        User = currentUser.getUserRole().equals(Preferences.ROLE_USER);
        GroupLeader = currentUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER);
        Guest = currentUser.getUserRole().equals(Preferences.ROLE_GUEST);
    }

    public void dump() {
        System.out.println("Admin " + Admin);
        System.out.println("Technician " + Technician);
        System.out.println("User " + User);
        System.out.println("GroupLeader " + GroupLeader);
        System.out.println("Guest " + Guest);
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public UserDTO getPi() {
        return pi;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public boolean isTechnician() {
        return Technician;
    }

    public boolean isUser() {
        return User;
    }

    public boolean isGroupLeader() {
        return GroupLeader;
    }

    public boolean isGuest() {
        return Guest;
    }

    public boolean hasSampleLoadPermission() {
        return (!Technician && !Guest);
    }
    
    public boolean hasNewsPermission() {
        return (Admin || Technician);
    }
    
    public boolean hasAddPermission(){
        return (Admin || Technician);
    }
    
    public boolean getHasRunAddPermission(){
        return (Admin || Technician);
    }
    
     public boolean getHasUserAddPermission(){
        return (Admin);
    }
           
                
    public boolean hasModifyPermission(SampleDTO sample) {
        if (Admin || Technician) {
            return true;
        } else if (Guest) {
            return false;
        } else if (User) {
            if (sample.getStatus().equals(SampleDTO.status_requested)) {
                return true;
            }
        } else if (GroupLeader) {
            if (sample.getStatus().equals(SampleDTO.status_requested)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDeletePermission(SampleDTO sample) {
        if (Admin || Technician) {
            return true;
        } else if (Guest) {
            return false;
        } else if (User) {
            if (sample.getStatus().equals(SampleDTO.status_requested)) {
                return true;
            }
        } else if (GroupLeader) {
            if (sample.getStatus().equals(SampleDTO.status_requested)) {
                return true;
            }
        }
        return false;
    }

}
