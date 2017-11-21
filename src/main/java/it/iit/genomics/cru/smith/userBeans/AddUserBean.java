package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.User;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * @(#)AddUserBean.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Backing bean for adding a user to the database.
 * 
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "addUserBean")
//@Named(value = "addUserBean")
@SessionScoped
public class AddUserBean extends AddUser implements Serializable {
    
    
    
    

    /**
     * Bean constructor
     * 
     * @author Francesco Venco
     * @since 1.0
     */
    public AddUserBean() {
        //super();
        if(Preferences.getVerbose()){
            System.out.println("init AddUserBean");
        }
        
    } 
    
    
    
    
    
    
}
