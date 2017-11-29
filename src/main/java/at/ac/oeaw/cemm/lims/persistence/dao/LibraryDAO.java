/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.LibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryIdEntity;
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
public class LibraryDAO {

    public LibraryDAO() {
        System.out.println("Initializing LibraryDAO");
    }

    public List<String> getAllLibraryNames() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(LibraryEntity.class).setProjection(Projections.distinct(Projections.property("libraryName")));
        return libraryCriteria.list();
    }

    public Integer getMaxLibraryId() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(LibraryEntity.class).setProjection(Projections.max("id.libraryId"));
        Integer id = (Integer) libraryCriteria.uniqueResult();
        if (id == null) {
            id = 0;
        }
        return id;
    }

    public void persistLibrary(LibraryEntity library) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(library);
    }

    public List<LibraryEntity> getAllLibrariesByLibID(int id) {
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
         Criteria query = session.createCriteria(LibraryEntity.class)
                 .add(Restrictions.eq("id.libraryId", id));
         
         return query.list();
    }
}
