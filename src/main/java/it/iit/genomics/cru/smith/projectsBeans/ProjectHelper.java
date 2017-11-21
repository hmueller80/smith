package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)ProjectHelper.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations on project table.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class ProjectHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init ProjectHelper");
        }
    }

    /**
    * Returns list of projects.
    * 
    * @author Francesco Venco
    * @return List<Project>
    * @since 1.0
    */
    @SuppressWarnings("unchecked")
    public static List<Project> getProjectsList() {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Project> projectsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Project");
            projectsList = q.list();
            //for for lazy initialization
            for (int i = 0; i < projectsList.size(); i++) {
                User u = projectsList.get(i).getUser();
                u.getLogin();
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return projectsList;
    }

    /**
    * Returns list of projects of a given group leader.
    * 
    * @author Francesco Venco
    * @param id - the user id of the groupleader
    * @return List<Project>
    * @since 1.0
    */
    public static List<Project> getProjectsListByGroupId(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Project> projectsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Project as p where p.user.pi ='" + id + "'");
            projectsList = (List<Project>) q.list();
            //for for lazy initialization
            for (Project p : projectsList) {
                p.getUser().getLogin();
                for (Sample s : p.getSamples()) {
                    s.getAntibody();
                    s.getApplication();
                    s.getBioanalyzerDate();
                    s.getBulkFragmentSize();
                    s.getComment();
                    s.getConcentration();
                    s.getCostCenter();
                    s.getCostCenter();
                    s.getDescription();
                    s.getExperimentName();
                    s.getOrganism();
                    s.getId();
                    s.getStatus();
                    s.getUser();
                    String s1 = s.getUser().getLogin();
                    s.getType();
                }
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return projectsList;
    }

    /**
    * Returns a project identified by projectId.
    * 
    * @author Francesco Venco
    * @param projectId
    * @return Project
    * @since 1.0
    */
    public static Project loadProject(int projectId) {
        Project project = null;
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            project = (Project) session.load(Project.class, projectId);
            //lazy init
            project.getUser().getLogin();
            for (Sample s : project.getSamples()) {
                s.getAntibody();
                s.getApplication();
                s.getBioanalyzerDate();
                s.getBulkFragmentSize();
                s.getComment();
                s.getConcentration();
                s.getCostCenter();
                s.getCostCenter();
                s.getDescription();
                s.getExperimentName();
                s.getOrganism();
                s.getId();
                s.getStatus();
                s.getUser();
                String s1 = s.getUser().getLogin();
                s.getType();
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
        } finally {
            session.close();
        }
        return project;
    }
}
