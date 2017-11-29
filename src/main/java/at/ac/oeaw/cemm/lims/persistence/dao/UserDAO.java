/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
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
        Criteria userCriteria = session.createCriteria(UserEntity.class).add(Restrictions.eq("login", userLogin));
        UserEntity user = (UserEntity) userCriteria.uniqueResult();
        return user;
    }

    public UserEntity getUserByID(Integer id) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return (UserEntity) session.get(UserEntity.class, id);
    }

    public List<UserEntity> getUsersByPI(Integer PIid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        if (PIid == null) {
            return new ArrayList<>();
        }
        Criteria userCriteria = session.createCriteria(UserEntity.class).add(Restrictions.eq("pi", PIid));
        return userCriteria.list();
    }

    public List<UserEntity> getAllUsers() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria userCriteria = session.createCriteria(UserEntity.class);
        return userCriteria.list();
    }

    public List<UserEntity> getAllUsersByRole(String role) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria userCriteria = session.createCriteria(UserEntity.class);
        userCriteria.add(Restrictions.eq("userRole", role));
        return userCriteria.list();
    }

    public List<UserEntity> getUsersByID(List<Integer> ids) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria userCriteria = session.createCriteria(UserEntity.class);
        userCriteria.add(Restrictions.in("id", ids));

        return userCriteria.list();
    }

    public Boolean userExists(String login) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(UserEntity.class)
                .add(Restrictions.eq("login", login))
                .setProjection(Projections.rowCount());
        
        long count = (Long) query.uniqueResult();
        if(count != 0){
            return true;
        } else {
            return false;
        }
    }

    public void updateOrPersistUser(UserEntity userEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(userEntity);

    }

}
