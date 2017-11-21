package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @(#)LoggedUserBean.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Bean storing the information on the logged user.
 * 
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "loggedUserBean")
@SessionScoped
public class LoggedUserBean extends LoggedUser implements Serializable{
        
    public LoggedUserBean(){
        if(Preferences.getVerbose()){
            System.out.println("init LoggedUserBean");
        }
    }
    
}
