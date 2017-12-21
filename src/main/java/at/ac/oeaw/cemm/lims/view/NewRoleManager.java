/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
    Set<Integer> subjectsIds=new HashSet<>();
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
        if (currentUser == null) {
            System.out.println("Could not find user with name: " + loginName);
            currentUser = services.getUserService().getUserByLogin("guest");
            currentUser.setUserRole(Preferences.ROLE_GUEST);
            pi = currentUser;
        } 
        
        Admin = currentUser.getUserRole().equals(Preferences.ROLE_ADMIN);
        Technician = currentUser.getUserRole().equals(Preferences.ROLE_TECHNICIAN);
        User = currentUser.getUserRole().equals(Preferences.ROLE_USER);
        GroupLeader = currentUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER);
        Guest = currentUser.getUserRole().equals(Preferences.ROLE_GUEST);
        
        if(!Guest) {
            pi = services.getUserService().getUserByID(currentUser.getPi());
            subjectsIds.add(currentUser.getId());
            if (GroupLeader) {
                for (UserDTO subject : services.getUserService().getAllUsersByPI(currentUser.getId())) {
                    subjectsIds.add(subject.getId());
                }
            }
        }

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
        return (Admin || Technician);
    }
    
    public boolean hasNewsPermission() {
        return (Admin || Technician);
    }
    
    public boolean getHasRunAddPermission(){
        return (Admin || Technician);
    }
    
     public boolean getHasUserAddPermission(){
        return (Admin || Technician );
    }
                  
    public boolean hasSampleModifyPermission(SampleDTO sample) {
        if (Admin || Technician) {
            return true;
        } else if (User || GroupLeader) {
            if (sample.getStatus().equals(SampleDTO.status_requested) 
                    && subjectsIds.contains(sample.getUser().getId())) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean hasAnnotationSheetModifyPermission(RequestFormDTO requestForm) {
        if (Admin || Technician) {
            return true;
        } else if (User || GroupLeader) {
            if (currentUser.getLogin().equals(requestForm.getRequestor().getUser().getLogin())
                || currentUser.getLogin().equals(requestForm.getRequestor().getPi().getLogin())
                ||  currentUser.getMailAddress().equals(requestForm.getRequestor().getUser().getMailAddress())
                ||  currentUser.getMailAddress().equals(requestForm.getRequestor().getPi().getMailAddress())){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean hasAnnotationSheetDeletePermission() {
        return (Admin || Technician);
    }

    public Set<Integer> getSubjectsIds() {
        return subjectsIds;
    }
   

}
