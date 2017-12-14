/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao.request_form;

import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.AffiliationEntity;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Session;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class AffiliationDAO {

    public void saveOrUpdate(AffiliationEntity affiliationEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(affiliationEntity);
        session.flush();
     }
    
}
