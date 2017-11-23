package at.ac.oeaw.cemm.lims.view;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;
import java.security.Principal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;


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
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;

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
       
    }
    
    @PostConstruct
    public void init(){
        if(Preferences.getVerbose()){
            System.out.println("post construct NavigationBean");
        }
        System.out.println(roleManager.getLoginName());
        if(roleManager.getLoginName().equals("guest")){
            roleManager.init();
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
        return roleManager.getLoginName();
    }
    
    /**
     * Tests if user has add permission.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    
    public boolean getUserHasAddPermission() {
        return roleManager.hasAddPermission();
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager (NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    
    
}