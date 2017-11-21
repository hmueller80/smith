package it.iit.genomics.cru.smith.userBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)CollaboratorsHelper.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Class performs database operations on Collaboration table.
 * 
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class CollaboratorsHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init CollaboratorsHelper");
        }
    }

    /**
     * Return the list of collaborations for a user using the user's id
     * 
     * @author Francesco Venco
     * @param id - the user id
     * @return List<Collaboration>
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static List<Collaboration> getCollaborationForUser(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Collaboration> colList = null;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Collaboration as col where col.user.id='" + id + "'");
            colList = q.list();
            //lazy init
            //for (Collaboration c : colList) {
            //    c.getProject().getUser().getPi();
            //}
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colList;
    }

    /**
     * Return the list of collaborations for a project.
     * 
     * @author Francesco Venco
     * @param id - the project id
     * @return List<Collaboration>
     * @since 1.0
     */
    public static List<Collaboration> getCollaborationForProject(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Collaboration> colList = null;
        try {
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from Collaboration as col where col.project.id='" + id + "'");
            colList = (List<Collaboration>) q.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colList;
    }

}
