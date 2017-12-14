/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao.request_form;

import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestSampleEntity;
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
public class RequestSampleDAO {
    public List<RequestSampleEntity> getSamplesByLibId(Integer libId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(RequestSampleEntity.class)
                .add(Restrictions.eq("libraryId.id", libId));
        
        return query.list();
        
    }

    public RequestSampleEntity getSampleById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return (RequestSampleEntity) session.get(RequestSampleEntity.class, id);
    }

    public void saveOrUpdate(RequestSampleEntity sampleEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(sampleEntity);
        session.flush();
    }
    
    public void delete(RequestSampleEntity sampleEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(sampleEntity);
        session.flush();
    }
}
