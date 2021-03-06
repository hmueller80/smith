/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalRequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestDAO {
        public Boolean checkRequestExistence(Integer id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(SampleEntity.class)
                .add(Restrictions.eq("submissionId", id))
                .setProjection(Projections.rowCount());
        
        long count = (Long) query.uniqueResult();
        if(count != 0){
            return true;
        } else {
            return false;
        }
    }

    public List<LibraryEntity> getLibrariesByRequestId(Integer requestId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(LibraryEntity.class);
        query.createAlias("samples", "sample",JoinType.LEFT_OUTER_JOIN);       
        query.add(Restrictions.eq("sample.submissionId", requestId));
        query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                     
        return query.list();
    }  
    
    public MinimalRequestEntity getMinimalRequestByIdAndRequestor(Integer requestId, String requestor) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                     
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.property("user").as("requestor"));
        projList.add(Projections.property("submissionId").as("requestId"));

        Criteria query = session.createCriteria(SampleEntity.class)
                .createAlias("user", "user")
                .add(Restrictions.eq("submissionId", requestId))
                .add(Restrictions.eq("user.login", requestor))
                .setProjection(Projections.distinct(projList))
                .setResultTransformer(Transformers.aliasToBean(MinimalRequestEntity.class));
               
        return (MinimalRequestEntity) query.uniqueResult();
    }
    
    public List<MinimalRequestEntity> getAllMinimalRequests() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                     
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.property("user").as("requestor"));
        projList.add(Projections.property("submissionId").as("requestId"));

        Criteria query = session.createCriteria(SampleEntity.class)
                .setProjection(Projections.distinct(projList))
                .addOrder(Order.desc("submissionId"))
                .setResultTransformer(Transformers.aliasToBean(MinimalRequestEntity.class));
               
        return query.list();
    }
}
