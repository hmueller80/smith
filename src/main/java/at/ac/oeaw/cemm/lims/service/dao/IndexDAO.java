/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service.dao;

import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
public class IndexDAO {
    
     public SequencingIndex getIdxBySequence(String sequence) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria libraryCriteria = session.createCriteria(SequencingIndex.class).add(Restrictions.eq("index", sequence));
        List<SequencingIndex> seqIndex = (List<SequencingIndex>) libraryCriteria.list();
        if (seqIndex!=null && seqIndex.size()!=0){
            return seqIndex.get(0);
        }else{
            return null;
        }

    }
     
    public List<String> getAllIndexes() throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria indexCriteria = session.createCriteria(SequencingIndex.class).setProjection(Projections.distinct(Projections.property("index")));
        return indexCriteria.list();
    }
}
