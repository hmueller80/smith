/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao.external_users;

import at.ac.oeaw.cemm.lims.persistence.HibernateUtilExternal;
import at.ac.oeaw.cemm.lims.persistence.entity.external_users.ExternalUserEntity;
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
        query.add(Restrictions.eq("userid", login));
                
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
}
