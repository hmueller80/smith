package it.iit.genomics.cru.smith.reagentsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)ReagentsSearchBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for reagent searches.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "reagentsSearchBean")
@RequestScoped
public class ReagentsSearchBean  implements Serializable{
    
    private DataModel reagentsList;
    
    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public ReagentsSearchBean(){
        if(Preferences.getVerbose()){
            System.out.println("init ReagentsSearchBean");
        }
        
        init();
    }
    
    /**
     * init
     *
     * @author Francesco Venco
     * @since 1.0
     */
    private void init(){
       reagentsList = new ListDataModel(ReagentHelper.getReagentsList());        
    }
    
    /**
     * Getter for reagentList
     *
     * @author Francesco Venco
     * @return DataModel
     * @since 1.0
     */
    public DataModel getReagentsList(){
        return this.reagentsList;
    }
    
    /**
     * Tests if logged user has permission to add new reagents.
     *
     * @author Francesco Venco
     * @return boolean
     * @since 1.0
     */
    public boolean hasNewReagentPermission(){
        FacesContext context = FacesContext.getCurrentInstance();        
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //LoggedUser lu = new LoggedUser();
        return lu.getIsTech() || lu.getIsAdmin();       
        
    }
    
   
    
}
