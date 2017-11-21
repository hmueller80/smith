package it.iit.genomics.cru.smith.reagentsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;

import java.text.SimpleDateFormat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)NewReagentFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for adding a reagent.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newReagentFormBean")
@ViewScoped
public class NewReagentFormBean extends ReagentFormBean implements Serializable {

    protected boolean hasNewPermission;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public NewReagentFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init NewReagentFormBean");
        }
        
        init();
    }

    /**
     * init
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void init() {
        formId = "NewReagentDetailsForm";
        df = new SimpleDateFormat("dd-MM-yyyy");
        FacesContext context = FacesContext.getCurrentInstance();
        LoggedUser lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //LoggedUser lu = new LoggedUser();
        operatorLogin = lu.getLogin();
        hasNewPermission = lu.getIsTech() || lu.getIsAdmin();
        loadUsers();
    }

    /**
     * Saves a new Reagent
     *
     * @author Francesco Venco
     * @return String - a redirect to the reagent search page
     * @since 1.0
     */
    public String saveNew() {
        User owner = UserHelper.getUserByLoginName(this.ownerLogin);
        User operator = UserHelper.getUserByLoginName(this.operatorLogin);
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Reagent r = new Reagent();
            r.setReagentBarCode(reagentbarcode);
            if (!checkFormAndSetReagent(r, operator, owner)) {
                throw new RuntimeException("The form is not correct");
            }
            session.save(r);
            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "SaveNew", "Reagent Inserted", "Save succesfull");
            return "reagentsSearch.jsf";
        } catch (RuntimeException e) {
            NgsLimsUtility.setFailMessage(formId, "SaveNew", "Error", e.getMessage() + ownerLogin);
            tx.rollback();
            return null;
        } finally {
            session.close();
        }
    }

    /**
     * Getter for hasNewPermission
     *
     * @author Francesco Venco
     * @return boolean - true if user has permission to add reagents, false otherwise
     * @since 1.0
     */
    public boolean hasNewPermission() {
        return hasNewPermission;
    }

}
