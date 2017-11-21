package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)NewProjectBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for creating a new project.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newProjectBean")
@RequestScoped
public class NewProjectBean extends ProjectDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    LoggedUser loggedUserBean;

    /**
    * Bean constructor
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    public NewProjectBean() {
        if(Preferences.getVerbose()){
            System.out.println("init NewProjectBean");
        }
        
        this.init();
    }

    /**
    * init
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    @Override
    protected void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUserBean = (LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class); 
        formId = "NewProjectDetailsForm";
        //loggedUser = new LoggedUser().getLoggedUser();

        user = loggedUserBean.getLoggedUser();
        userName = user.getLogin();
    }

    /**
    * Saves new project to database.
    *
    * @author Francesco Venco
    * @return String - a redirect to the project description page
    * @since 1.0
    */
    public String save() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (!formIsCorrect()) {
                throw new RuntimeException();
            }
            Project project = new Project();
            project.setDescription(description);
            project.setUser(user);
            project.setName(name);
            session.save(project);
            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "save", "Project Saved", "OK");
            session.close();
            return "projectDetails?pid=" + project.getId() + " faces-redirect=true";
        } catch (RuntimeException e) {
            NgsLimsUtility.setFailMessage(formId, "save", "Error", "Error");
            tx.rollback();
            session.close();
            return null;
        }

    }

}
