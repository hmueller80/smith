package it.iit.genomics.cru.smith.navigationBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;
import java.security.Principal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @(#)NavigationBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for logged user information.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "navigationBean")
@RequestScoped
public class NavigationBean implements Serializable {
    
    FacesContext context;
    ExternalContext ext;
    LoggedUser loggedUser;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public NavigationBean(){
        if(Preferences.getVerbose()){
            System.out.println("init NavigationBean");
        }        
        context = FacesContext.getCurrentInstance();  
        ext = context.getExternalContext();
        loggedUser = context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class); 
        System.out.println(loggedUser.getLogin());
        if(loggedUser.getLogin().equals("guest")){
            loggedUser.init();
        }
    }
    
    /**
     * Invalidates HttpSession for logout.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void doLogout() {
        HttpSession sess = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        sess.invalidate();
        System.out.println("Logged out");
    }
    
    /**
     * Tests if user is logged in.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getIsLoggedIn(){
        HttpServletRequest request = (HttpServletRequest) ext.getRequest();
        Principal principal = request.getUserPrincipal();
        if(principal == null){
            return "false";
        }
        return "true";
    }
    
    /**
     * Return the login name of the logged user.
     *
     * @author Francesco Venco
     * @return String
     * @since 1.0
     */
    public String getLoginName() {
        return loggedUser.getLogin();
    }
    
    /**
     * Tests if user has add permission.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    
    public boolean getUserHasAddPermission() {
        String login = ext.getRemoteUser();
        if(login == null || login.equals("null")){
            return false;
        }else{
            User u = UserHelper.getUserByLoginName(login);
            if(u.getUserRole().equals("admin") || u.getUserRole().equals("technician")){
                return true;
            }else{
                return false;
            }
        }
        
    }
    
    
}