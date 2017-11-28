/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hmuller
 */
@ManagedBean(name = "newRoleManager")
@SessionScoped
public class NewRoleManager implements Serializable {

    UserDTO currentUser;
    String loginName = "";
    UserDTO pi;
    boolean Admin;
    boolean Technician;
    boolean User;
    boolean GroupLeader;
    boolean Guest;

    @ManagedProperty(value = "#{newLoggedUser}")
    NewLoggedUser loggedUser;

    public NewRoleManager() {
        System.out.println("Initializing NewRoleManager");
    }

    @PostConstruct
    public void init() {
        System.out.println("NewRoleManager post construct");
        loggedUser.init();
        currentUser = loggedUser.getLoggedUser();
        
        if (currentUser == null) {
            Admin = false;
            Technician = false;
            User = false;
            GroupLeader = false;
            Guest = true;
            loginName = Preferences.ROLE_GUEST;
        } else {
            Admin = currentUser.getUserRole().equals(Preferences.ROLE_ADMIN);
            Technician = currentUser.getUserRole().equals(Preferences.ROLE_TECHNICIAN);
            User = currentUser.getUserRole().equals(Preferences.ROLE_USER);
            GroupLeader = currentUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER);
            Guest = currentUser.getUserRole().equals(Preferences.ROLE_GUEST);
            pi = loggedUser.getLoggedUserPI();
            loginName = currentUser.getLogin();
        }
    }

    public void dump() {
        System.out.println("Admin " + Admin);
        System.out.println("Technician " + Technician);
        System.out.println("User " + User);
        System.out.println("GroupLeader " + GroupLeader);
        System.out.println("Guest " + Guest);
    }

    public NewLoggedUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(NewLoggedUser loggedUser) {
        this.loggedUser = loggedUser;
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

    public String getLoginName() {
        return loginName;
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
