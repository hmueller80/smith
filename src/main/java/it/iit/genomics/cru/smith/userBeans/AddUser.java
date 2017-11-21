package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)AddUser.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class for adding a user to the database.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class AddUser {

    protected String userName = "";
    protected String userLogin = "";
    protected String userEmail = "";
    protected String userPhone = "";
    protected String userRole = "";
    protected Integer userPI;
    protected String userPIName = "";

    /**
     * Class constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public AddUser() {
        if (Preferences.getVerbose()) {
            System.out.println("init AddUser");
        }


    }

    /**
     * Getter for userName
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for userName
     *
     * @author Francesco Venco
     * @param userName
     * @since 1.0
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for userLogin
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * Setter for userLogin
     *
     * @author Francesco Venco
     * @param userLogin
     * @since 1.0
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * Getter for userEmail
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Setter for userEmail
     *
     * @author Francesco Venco
     * @param userEmail
     * @since 1.0
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Getter for userPhone
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * Setter for userPhone
     *
     * @author Francesco Venco
     * @param userPhone
     * @since 1.0
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * Getter for userRole
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * Setter for userRole
     *
     * @author Francesco Venco
     * @param userRole
     * @since 1.0
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * Getter for userPI
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public Integer getUserPI() {
        return userPI;
    }

    /**
     * Setter for userPI
     *
     * @author Francesco Venco
     * @param userPI
     * @since 1.0
     */
    public void setUserPI(Integer userPI) {
        this.userPI = userPI;
    }

    /**
     * Getter for userPIName
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getUserPIName() {
        return userPIName;
    }

    /**
     * Setter for userPIName
     *
     * @author Francesco Venco
     * @param userPIName
     * @since 1.0
     */
    public void setUserPIName(String userPIName) {
        this.userPIName = userPIName;
    }

    /**
     * Returns list of PIs
     *
     * @author Francesco Venco
     * @return List<String>
     * @since 1.0
     */
    public static List<String> getPIList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        List<User> userList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from User where userRole='" + "groupleader"
                    + "'");
            userList = (List<User>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        ArrayList<String> result = new ArrayList<String>();
        for (User u : userList) {
            result.add(u.getUserName());
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Adds a user to database
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void addUser() {
        User pi = UserHelper.getUserByName(userPIName);
        User u = new User(userName, userLogin, userPhone, userEmail, pi.getId(), userRole, null, null, null, null, null, null);
        UserHelper.addUser(u);

       
    }

    
}
