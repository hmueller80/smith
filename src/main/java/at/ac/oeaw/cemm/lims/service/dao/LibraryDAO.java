/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service.dao;

import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 *
 * @author dbarreca
 */
public class LibraryDAO {
    
  
     public List<String> getAllLibraryNames() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(Library.class).setProjection(Projections.distinct(Projections.property("libraryName")));
        return libraryCriteria.list();
    }
     
    public Integer getMaxLibraryId() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(Library.class).setProjection(Projections.max("id.libraryId"));
        Integer id = (Integer) libraryCriteria.uniqueResult();
        if (id==null){
            id=0;
        }
        return id;
    }
    
    public void persistLibrary(Library library) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(library);
    }
}