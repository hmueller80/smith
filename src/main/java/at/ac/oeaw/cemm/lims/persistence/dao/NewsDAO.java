/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.NewsEntity;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

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
    
}
