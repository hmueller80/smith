package at.ac.oeaw.cemm.lims.view;

import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @ManagedProperty(value = "#{newRoleManager}")
    NewRoleManager roleManager;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String loginName = context.getExternalContext().getRemoteUser();
        if (!roleManager.currentUser.getLogin().equals(loginName)){
            roleManager.init();
        }
    }

    public void doLogout() {
        HttpSession sess = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        sess.invalidate();
    }

    public String getIsLoggedIn() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return "false";
        }
        return "true";
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

}
