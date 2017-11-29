/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.ApplicationEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class ApplicationDAO {

    public ApplicationDAO() {
        System.out.println("Initializing applicationDAO");
    }

    public ApplicationEntity getApplicationByParams(Integer readLength, String readMode, String instrument, String applicationName, Integer depth) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Criteria libraryCriteria = session.createCriteria(ApplicationEntity.class)
                .add(Restrictions.eq("readLength", readLength))
                .add(eqOrNull("readMode", readMode))
                .add(Restrictions.eq("instrument", instrument))
                .add(Restrictions.eq("applicationName", applicationName))
                .add(eqOrNull("depth", depth))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<ApplicationEntity> applications = (List<ApplicationEntity>) libraryCriteria.list();
        if (applications != null && applications.size() != 0) {
            return applications.get(0);
        } else {
            return null;
        }
    }

    public void persistApplication(ApplicationEntity application) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(application);
    }

    private Criterion eqOrNull(String property, Object value) {
        if (value == null) {
            return Restrictions.isNull(property);
        }

        return Restrictions.eq(property, value);
    }

    public List<ApplicationEntity> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(ApplicationEntity.class);
        return query.list();
    }
}
