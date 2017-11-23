package at.ac.oeaw.cemm.lims.view;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;

/**
 * @(#)LoggedUser.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Bean storing the information on the logged user. Maybe using LDAP in the
 * future. Here we mask all the methods user already has. this.loggedUser is
 * done in the case the logic behind the user itself changes in future, so we do
 * not need to change the xhtml.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newLoggedUser")
@SessionScoped
public class NewLoggedUser implements Serializable {

    private UserDTO loggedUser;
    private UserDTO loggedUserPI;
    private String loginName;

    @ManagedProperty(value = "#{hibernateServiceFactory}")
    private ServiceFactory services;

    public NewLoggedUser() {
        System.out.println("Initializing NewLoggedUser");
        
       
    }

    @PostConstruct
    public void init() {
        // retrieve the login of the user from the context
        System.out.println("NewLoggedUser post construct");
        FacesContext context = FacesContext.getCurrentInstance();
        String loginName = context.getExternalContext().getRemoteUser();

        loggedUser = services.getUserService().getUserByLogin(loginName);
        if (loggedUser == null ){
            System.out.println("Could not find user with name: "+loginName);
        }


        //if some error, set some information for a guest
        if (loggedUser == null) {
            loggedUser = services.getUserService().getUserByLogin("guest");
            loggedUser.setUserRole(Preferences.ROLE_GUEST);
            loggedUserPI = loggedUser;
        }

        loggedUserPI = services.getUserService().getUserByID(this.getPi());
        System.out.println("Logged in as "+loggedUser.getLogin());

    }

    public UserDTO getLoggedUser() {
        return this.loggedUser;
    }

    public UserDTO getLoggedUserPI() {
        return this.loggedUserPI;
    }

    public String getLoggedUserPIName() {
        return this.loggedUserPI.getUserName();
    }

    public String getLoginLink() {
        return "<a href=\"loggedUserInfo.jsf\">" + getLogin() + "</a>";
    }

    public boolean getIsAdmin() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_ADMIN));
    }

    public boolean getIsLeader() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER));
    }

    public boolean getIsTech() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_TECHNICIAN));
    }

    public boolean getIsGuest() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_GUEST));
    }

    public Integer getUserId() {
        return loggedUser.getId();
    }

    public String getUsername() {
        return this.loggedUser.getUserName();
    }

    public String getLogin() {
        return this.loggedUser.getLogin();
    }

    public String getPhone() {
        return this.loggedUser.getPhone();
    }

    public String getMailadress() {
        return this.loggedUser.getMailAddress();
    }

    public int getPi() {
        return this.loggedUser.getPi();
    }

    public String getUserRole() {
        return this.loggedUser.getUserRole();
    }

    public ServiceFactory getServices() {
        return services;
    }

    public void setServices(ServiceFactory services) {
        this.services = services;
    }

}
