package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.User;
import java.io.Serializable;

import java.util.List;
import javax.faces.bean.ManagedBean;
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
@ManagedBean(name = "loggedUser")
@SessionScoped
public class LoggedUser implements Serializable {

    private User loggedUser;
    private User loggedUserPI;
    List<Collaboration> colList;

    /**
     * Bean constructor.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public LoggedUser() {
        if(Preferences.getVerbose()){
            System.out.println("init LoggedUser");
        }
        init();
    }
    
    public void init(){
        // retrieve the login of the user from the context
        FacesContext context = FacesContext.getCurrentInstance();
        String logInName = context.getExternalContext().getRemoteUser();
        
        loggedUser = UserHelper.getUserByLoginName(logInName);

        //if some error, set some information for a guest
        if (loggedUser == null) {

            loggedUser = UserHelper.getGuest();
            loggedUser.setUserName("Guest");
            loggedUser.setLogin(Preferences.ROLE_GUEST);
        }


        loggedUserPI = UserHelper.getUserByID(this.getPi());

        //if some error, set the group leader as himself ( done for guesting )
        if (loggedUserPI == null) {
            loggedUserPI = loggedUser;
        }

        //find all the collaborations

        colList = CollaboratorsHelper.getCollaborationForUser(this.getUserId());
    }

    /**
     * Getter for loggedUser.
     *
     * @author Francesco Venco
     * @return User
     * @since 1.0
     */
    public User getLoggedUser() {
        return this.loggedUser;
    }

    /**
     * Setter for loggedUser.
     *
     * @author Francesco Venco
     * @param user
     * @since 1.0
     */
    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    /**
     * Getter for loggedUserPI.
     *
     * @author Francesco Venco
     * @return User
     * @since 1.0
     */
    public User getLoggedUserPI() {
        return this.loggedUserPI;
    }

    /**
     * Setter for loggedUserPI.
     *
     * @author Francesco Venco
     * @param loggedUserPI
     * @since 1.0
     */
    public void setLoggedUserPI(User loggedUserPI) {
        this.loggedUserPI = loggedUserPI;
    }

    /*
     * Return the name of the group leader
     */
    /**
     * Getter for loggedUserPI name.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getLoggedUserPIName() {
        return this.loggedUserPI.getUserName();
    }

    /**
     * Getter for colList.
     *
     * @author Francesco Venco
     * @return List<Collaboration>
     * @since 1.0
     */
    public List<Collaboration> getCollaborations() {
        return this.colList;
    }

    /**
     * Return the login name as a link to the info page.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getLoginLink() {
        return "<a href=\"loggedUserInfo.jsf\">" + getLogin() + "</a>";
    }

    /**
     * Getter for loggedUser is admin.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getIsAdmin() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_ADMIN));
    }

    /**
     * Getter for loggedUser is group leader.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getIsLeader() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER));
    }

    /**
     * Getter for loggedUser is technician.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getIsTech() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_TECHNICIAN));
    }

    /**
     * Getter for loggedUser is guest.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean getIsGuest() {
        return (loggedUser.getUserRole().equals(Preferences.ROLE_GUEST));
    }

    /**
     * Getter for loggedUser id.
     *
     * @author Francesco Venco
     * @return Integer
     * @since 1.0
     */
    public Integer getUserId() {
        return loggedUser.getId();
    }

    /**
     * Setter for loggedUser id.
     *
     * @author Francesco Venco
     * @param userId
     * @since 1.0
     */
    public void setUserId(Integer userId) {
        this.loggedUser.setId(userId);
    }

    /**
     * Getter for loggedUser name.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUsername() {
        return this.loggedUser.getUserName();
    }

    /**
     * Setter for loggedUser name.
     *
     * @author Francesco Venco
     * @param username
     * @since 1.0
     */
    public void setUsername(String username) {
        this.loggedUser.setUserName(username);
    }

    /**
     * Getter for loggedUser login.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getLogin() {
        return this.loggedUser.getLogin();
    }

    /**
     * Setter for loggedUser login.
     *
     * @author Francesco Venco
     * @param login
     * @since 1.0
     */
    public void setLogin(String login) {
        this.loggedUser.setLogin(login);
    }

    /**
     * Getter for loggedUser phone.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getPhone() {
        return this.loggedUser.getPhone();
    }

    /**
     * Setter for loggedUser phone.
     *
     * @author Francesco Venco
     * @param phone
     * @since 1.0
     */
    public void setPhone(String phone) {
        this.loggedUser.setPhone(phone);
    }

    /**
     * Getter for loggedUser mail address.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getMailadress() {
        return this.loggedUser.getMailAddress();
    }

    /**
     * Setter for loggedUser mail address.
     *
     * @author Francesco Venco
     * @param mailadress
     * @since 1.0
     */
    public void setMailadress(String mailadress) {
        this.loggedUser.setMailAddress(mailadress);
    }

    /**
     * Getter for loggedUser Principal Investigator (PI).
     *
     * @author Francesco Venco
     * @return int - the user id of the PI
     * @since 1.0
     */
    public int getPi() {
        return this.loggedUser.getPi();
    }

    /**
     * Setter for loggedUser Principal Investigator (PI).
     *
     * @author Francesco Venco
     * @param pi
     * @since 1.0
     */
    public void setPi(int pi) {
        this.loggedUser.setPi(pi);
    }

    /**
     * Getter for loggedUser role.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserRole() {
        return this.loggedUser.getUserRole();
    }

    /**
     * Setter for loggedUser role.
     *
     * @author Francesco Venco
     * @param userRole
     * @since 1.0
     */
    public void setUserRole(String userRole) {
        this.loggedUser.setUserRole(userRole);
    }
}
