/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.LaneEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunEntity;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class LaneDAO {
    
    public List<LaneEntity> getLanesBySampleAndRun(int run_id,int sam_id){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(LaneEntity.class)
                .createAlias("samplerun", "samplerun")
                .add(Restrictions.conjunction(Restrictions.eq("samplerun.id.runId", run_id),Restrictions.eq("samplerun.id.samId", sam_id)))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);


        return query.list();
    }

    public void deleteEntity(LaneEntity laneEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(laneEntity);
    }

    public void addLaneForSampleRun(String lane, SampleRunEntity sampleRunEntity) {
        LaneEntity laneToAdd = new LaneEntity();
        laneToAdd.setLaneName(lane);
        laneToAdd.setSamplerun(sampleRunEntity);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(laneToAdd);
    }
}
