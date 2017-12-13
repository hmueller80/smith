/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class LibraryDAO {
    
    public LibraryDAO() {
        System.out.println("Initializing LibraryDAO");
    }

    public List<String> getAllLibraryNames() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(LibraryEntity.class).setProjection(Projections.distinct(Projections.property("libraryName")));
        return libraryCriteria.list();
    }

    public void persistLibrary(LibraryEntity library) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Integer libraryId = this.getMaxLibraryId() + 1;
        library.setId(libraryId);
        session.save(library);
        String libraryName = library.getLibraryName() + "_L" + library.getId();
        library.setLibraryName(libraryName);
        session.update(library);
    }

    private Integer getMaxLibraryId() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(LibraryEntity.class).setProjection(Projections.max("id"));
        Integer id = (Integer) libraryCriteria.uniqueResult();
        if (id == null) {
            id = 0;
        }
        return id;
    }

    public LibraryEntity getLibraryByName(String libraryName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(LibraryEntity.class)
                .add(Restrictions.eq("libraryName", libraryName));
        return  (LibraryEntity) query.uniqueResult();
    }
    
    public LibraryEntity getLibraryById(Integer libraryId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(LibraryEntity.class)
                .add(Restrictions.eq("id", libraryId));
        return  (LibraryEntity) query.uniqueResult();
    }

    public void deleteLibrary(LibraryEntity library) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(library);
    }   

    public List<MinimalLibraryEntity> getDeleatableLibraries() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        DetachedCriteria subquery = DetachedCriteria.forClass(SampleEntity.class)
                .createAlias("library", "library",JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.conjunction(
                    Restrictions.ne("status",SampleDTO.status_requested),
                    Restrictions.ne("status",SampleDTO.status_queued)))
                .setProjection(Projections.distinct(Projections.property("library.id")));
        
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.property("sample.submissionId").as("requestId"));
        projList.add(Projections.property("sample.user").as("requestor"));
        projList.add(Projections.property("id").as("libraryId"));
        projList.add(Projections.property("libraryName").as("libraryName"));
        
        Criteria query = session.createCriteria(LibraryEntity.class)
                .createAlias("samples", "sample",JoinType.LEFT_OUTER_JOIN)
                .add(Subqueries.propertyNotIn("id", subquery))
                .addOrder(Order.desc("sample.submissionId"))
                .addOrder(Order.desc("id"))
                .setProjection(Projections.distinct(projList))
                .setResultTransformer(Transformers.aliasToBean(MinimalLibraryEntity.class));
        
        return query.list();
     }

    public List<LibraryEntity> getDeleatableLibrariesInRequest(Integer requestId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        DetachedCriteria subquery = DetachedCriteria.forClass(SampleEntity.class)
                .createAlias("library", "library",JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.conjunction(
                    Restrictions.eq("submissionId", requestId),
                    Restrictions.ne("status",SampleDTO.status_requested),
                    Restrictions.ne("status",SampleDTO.status_queued)))
                .setProjection(Projections.distinct(Projections.property("library.id")));
               
        Criteria query = session.createCriteria(LibraryEntity.class)
                .createAlias("samples", "sample",JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.eq("sample.submissionId", requestId))
                .add(Subqueries.propertyNotIn("id", subquery))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .addOrder(Order.desc("id"));
        return query.list();
    }
    
}
