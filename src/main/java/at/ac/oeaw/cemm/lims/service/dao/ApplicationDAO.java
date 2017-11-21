/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service.dao;

import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
public class ApplicationDAO {
    
    
    public Application getApplicationByParams(Integer readLength, String readMode, String instrument, String applicationName, Integer depth) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
       
        
        Criteria libraryCriteria = session.createCriteria(Application.class)
                .add(Restrictions.eq("readLength", readLength))
                .add(eqOrNull("readMode",readMode))
                .add(Restrictions.eq("instrument", instrument))
                .add(Restrictions.eq("applicationName", applicationName))
                .add(eqOrNull("depth", depth));
        
        List<Application> applications = (List<Application>) libraryCriteria.list();
        if (applications!=null && applications.size()!=0){
            return applications.get(0);
        }else{
            return null;
        }
    }
    
    public void persistApplication(Application application) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(application);
    }
    
    private Criterion eqOrNull(String property, Object value) {
        if (value == null)
            return Restrictions.isNull(property);
       
        return Restrictions.eq(property, value);
    }
}
