/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.NewsEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class NewsDAO {

    public List<NewsEntity> getAllNews() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(NewsEntity.class).addOrder(Order.desc("date"));
        return query.list();
    }

    public void save(NewsEntity newsEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(newsEntity);
    }
    
    public void delete(NewsEntity newsEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(newsEntity);
    }

    public boolean newsExists(String body) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(NewsEntity.class)
                .add(Restrictions.eq("body", body))
                .setProjection(Projections.rowCount());
        
        long count = (Long) query.uniqueResult();
        if(count != 0){
            return true;
        } else {
            return false;
        }
    }
    
    public NewsEntity getNewsWithBody(String body) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(NewsEntity.class)
                .add(Restrictions.eq("body", body));
        return (NewsEntity) query.uniqueResult();
    }

    public int getMaxId() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria sampleCriteria = session.createCriteria(NewsEntity.class).setProjection(Projections.max("id"));
        Integer id = (Integer) sampleCriteria.uniqueResult();
        if (id == null) {
            id = 0;
        }
        return id;
    }
    
}
