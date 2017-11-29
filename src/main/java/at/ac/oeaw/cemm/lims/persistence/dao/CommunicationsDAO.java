/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.CommunicationsEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.CommunicationsKeyEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class CommunicationsDAO {
    
    public List<Integer> getAllCommunicationByUser(int userId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria comCriteria = session.createCriteria(CommunicationsEntity.class)
            .add(Restrictions.eq("communicationsPK.userId", userId))
            .setProjection(Projections.property("communicationsPK.collaboratorId"));
        
        return (List<Integer>) comCriteria.list();
    }

    public void delete(Integer id, Integer collaborator) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(session.load(CommunicationsEntity.class, new CommunicationsKeyEntity(id,collaborator)));
    }
    
    public void save(CommunicationsEntity entity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(entity);
    }
    
}
