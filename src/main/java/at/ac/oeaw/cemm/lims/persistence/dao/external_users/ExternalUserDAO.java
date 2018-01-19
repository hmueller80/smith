/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao.external_users;

import at.ac.oeaw.cemm.lims.persistence.entity.external_users.ExternalGroupEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.external_users.ExternalUserEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtilExternal;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class ExternalUserDAO {
    
    public ExternalUserEntity getUserById(String login){
        Session session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(ExternalUserEntity.class);
        query.add(Restrictions.eq("userName", login));
                
        return (ExternalUserEntity) query.uniqueResult();
    }
    
    public void saveUser(ExternalUserEntity user){
        Session session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(user);
    }

    public void deleteUser(ExternalUserEntity user) {
        Session session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
        session.delete(user);
    }

    public void saveGroup(ExternalGroupEntity group) {
        Session session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(group);
    }

    public ExternalGroupEntity getGroupByUser(String userId) {
        Session session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(ExternalGroupEntity.class);
        query.add(Restrictions.eq("userName", userId));
                
        return (ExternalGroupEntity) query.uniqueResult();
    }

    public void deleteGroup(ExternalGroupEntity group) {
        Session session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
        session.delete(group);
    }
}
