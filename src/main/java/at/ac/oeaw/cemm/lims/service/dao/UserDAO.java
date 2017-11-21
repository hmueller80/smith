/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service.dao;

import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
public class UserDAO {

    public User getUserByLogin(String userLogin) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(User.class).add(Restrictions.eq("login", userLogin));
        User user = (User) libraryCriteria.uniqueResult();
        return user;
    }

     public User getUserByID(Integer id) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return (User) session.get(User.class, id);
    }
}
