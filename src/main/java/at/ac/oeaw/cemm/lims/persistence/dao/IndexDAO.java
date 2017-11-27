/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.SequencingIndexEntity;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
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
public class IndexDAO {

    public IndexDAO() {
        System.out.println("Initializing IndexDAO");
    }

    public SequencingIndexEntity getIdxBySequence(String sequence) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(SequencingIndexEntity.class)
                .add(Restrictions.eq("index", sequence))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List<SequencingIndexEntity> seqIndex = (List<SequencingIndexEntity>) libraryCriteria.list();
        if (seqIndex != null && seqIndex.size() != 0) {
            return seqIndex.get(0);
        } else {
            return null;
        }

    }

    public List<String> getAllIndexes() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria indexCriteria = session.createCriteria(SequencingIndexEntity.class).setProjection(Projections.distinct(Projections.property("index")));
        return indexCriteria.list();
    }
}
