/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao.request_form;

import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestLibraryEntity;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestLibraryDAO {

    public List<RequestLibraryEntity> getLibrariesByReqId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(RequestLibraryEntity.class);
        query.add(Restrictions.eq("requestId.id", id));        
        
        return query.list();
    }

    public RequestLibraryEntity getLibraryById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();        
        return (RequestLibraryEntity) session.get(RequestLibraryEntity.class, id);
    }

    public void saveOrUpdate(RequestLibraryEntity libraryEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(libraryEntity);
        session.flush();
    }

    public void delete(RequestLibraryEntity library) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(library);
        session.flush();
    }
    
}
