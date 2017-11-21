package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)ModProjectTextBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for modifying project text.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
public class ModProjectTextBean extends ProjectDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public ModProjectTextBean() {
        if(Preferences.getVerbose()){
            System.out.println("init ModProjectTextBean");
        }
        init();
        formId = "ModProjectTextForm";

    }

    /**
    * Saves modifications in project description and project name.
    *
    * @author Francesco Venco
    * @return String - a redirect to the project description page
    * @since 1.0
    */
    public String save() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (projectId == 0) {
                throw new RuntimeException();
            }
            if (!formIsCorrect()) {
                throw new RuntimeException();
            }
            Project project = (Project) session.load(Project.class, projectId);
            project.setDescription(description);
            project.setName(name);
            tx.commit();
            NgsLimsUtility.setSuccessMessage(formId, "save", "Project Saved", "OK");
            return "projectDetails?pid=" + project.getId() + " faces-redirect=true";
        } catch (RuntimeException e) {
            NgsLimsUtility.setFailMessage(formId, "save", "Error", "Error");
            tx.rollback();
            return null;
        } finally {
            session.close();
        }

    }

}
