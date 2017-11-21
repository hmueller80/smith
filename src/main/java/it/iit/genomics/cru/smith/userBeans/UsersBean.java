package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;

import it.iit.genomics.cru.smith.entity.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)UsersBean.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for user search page.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "usersBean")
@SessionScoped
public class UsersBean implements Serializable {

    private static final long serialVersionUID = 1L;
    DataModel<User> usList;
    User currentUser;
    String userLogin;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public UsersBean() {
        /*Create a UserHelper to connect to database. 
         * Load the list of all users
         */
        if(Preferences.getVerbose()){
            System.out.println("init UserBean");
        }
        
        usList = new ListDataModel<User>(UserHelper.getUsersList());

        /* Get the current login to be used
         * to retrieve additional informations on the user.
         */
        FacesContext context = FacesContext.getCurrentInstance();
        userLogin = context.getExternalContext().getRemoteUser();

    }

    /**
     * Getter for user list.
     *
     * @author Francesco Venco
     * @return DataModel<User>
     * @since 1.0
     */
    public DataModel<User> getUsList() {
        return new ListDataModel<User>(UserHelper.getUsersList());
    }

    /**
     * Setter for usList.
     *
     * @author Francesco Venco
     * @param m - a data model for user objects.
     * @since 1.0
     */
    public void setUsList(DataModel<User> m) {
        this.usList = m;
    }

    /**
     * Getter for currentUser.
     *
     * @author Francesco Venco
     * @return User
     * @since 1.0
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Setter for currentUser.
     *
     * @author Francesco Venco
     * @param currentUser
     * @since 1.0
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Getter for userLogin.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserLogin() {
        return this.userLogin;
    }

    /**
     * Setter for userLogin.
     *
     * @author Francesco Venco
     * @param login
     * @since 1.0
     */
    public void setUserLogin(String login) {
        this.userLogin = login;
    }
}
