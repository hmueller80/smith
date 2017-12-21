/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.OrganizationEntity;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class OrganizationDAO {
        
    public OrganizationEntity getOrganizationByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(OrganizationEntity.class);
        query.add(Restrictions.eq("organizationName", name));
        
        return (OrganizationEntity) query.uniqueResult();
    }
    
    public List<OrganizationEntity> getAllOrganizations(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(OrganizationEntity.class);
        query.addOrder(Order.asc("organizationName"));
        
        return query.list();
    }
    
    
}
