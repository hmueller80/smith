/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class UserDAO {

    public UserDAO() {
        System.out.println("Initializing UserDAO");
    }

    public UserEntity getUserByLogin(String userLogin) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(UserEntity.class).add(Restrictions.eq("login", userLogin));
        UserEntity user = (UserEntity) libraryCriteria.uniqueResult();
        return user;
    }

    public UserEntity getUserByID(Integer id) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return (UserEntity) session.get(UserEntity.class, id);
    }

    public List<UserEntity> getUsersByPI(Integer PIid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        if (PIid == null){
            return new ArrayList<>();
        }
        Criteria userCriteria = session.createCriteria(UserEntity.class).add(Restrictions.eq("pi", PIid));
        return userCriteria.list();
    }
}
