/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunIdEntity;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 *
 * @author dbarreca
 */
public class SampleRunDAO {

     private Set<String> querableFields = new HashSet<String>() {
        {
            //INTEGERS
            add("id.runId");
            add("id.samId");
            
            //BOOLEANS
            add("isControl");
            
            //STRINGS
            add("lane.laneName");
            add("user.login");
            add("sampleUser.login");
            add("flowcell");
            add("sampleIndex.index");
            add("runFolder");
        }
    };
    
    
    public List<SampleRunEntity> getSampleRunsById(int runId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(SampleRunEntity.class)
                .add(Restrictions.eq("id.runId",runId))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return query.list();
    }

    public Integer getRunsCount(Map<String, Object> filters) {
        int result = 0;

        Criteria query = assembleQuery(filters);
        query.setProjection(Projections.countDistinct("id"));

        result = ((Long) query.uniqueResult()).intValue();
        
        System.out.println("There are "+result+" sample runs");
        return result;
    }

    public List<SampleRunEntity> getRuns(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters) {
        List<SampleRunEntity> resultList = null;

        //1. Get the ids of the distinct runs satisfying the query
        Criteria query = assembleQuery(sortField,ascending,filters);
        query.setProjection(Projections.distinct(Projections.property("id")));
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List idsToFetch = query.list();
        
        //2. Fetch the distinct sample runs in the query
        query = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(SampleRunEntity.class)
                .add(Restrictions.in("id", idsToFetch))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        if (sortField != null) {
            if (ascending) {
                query.addOrder(Order.asc(sortField));
            } else {
                query.addOrder(Order.desc(sortField));
            }
        }
          
        resultList = (List<SampleRunEntity>) query.list();
        

        System.out.println("Returning "+resultList.size()+" sample runs");
        return resultList;
    }
    
    public SampleRunEntity getSampleRunById(int runId, int samId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        SampleRunIdEntity sampleRunId = new SampleRunIdEntity(runId,samId);
        return (SampleRunEntity) session.get(SampleRunEntity.class, sampleRunId);
    }
    
     private Criteria assembleQuery(Map<String, Object> filters) {
        return assembleQuery(null,null,filters);
    }

    
    private Criteria assembleQuery(String sortField, Boolean ascending, Map<String, Object> filters) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Criteria query = session.createCriteria(SampleRunEntity.class)
                .createAlias("lanes", "lane", JoinType.LEFT_OUTER_JOIN)
                .createAlias("user", "user", JoinType.LEFT_OUTER_JOIN)
                .createAlias("sample", "sample", JoinType.LEFT_OUTER_JOIN)
                .createAlias("sample.user", "sampleUser", JoinType.LEFT_OUTER_JOIN)
                .createAlias("sample.sequencingIndexes", "sampleIndex", JoinType.LEFT_OUTER_JOIN)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (filters != null) {
            Conjunction singleFiltersCriterion = Restrictions.conjunction();
            for (String filteredField : filters.keySet()) {
                if (querableFields.contains(filteredField)) {
                    switch (filteredField) {
                        case "id.runId":
                        case "id.samId":
                            singleFiltersCriterion.add(Restrictions.eq(filteredField, Integer.parseInt((String) filters.get(filteredField))));
                            break;
                        case "isControl":
                            singleFiltersCriterion.add(Restrictions.eq(filteredField, Boolean.parseBoolean((String) filters.get(filteredField))));
                            break;
                        default:
                            singleFiltersCriterion.add(Restrictions.like(filteredField, filters.get(filteredField).toString(), MatchMode.ANYWHERE));
                            break;
                    }
                }
            }
            
            Disjunction globalFiltersCriterion = Restrictions.disjunction();
            if (filters.containsKey("globalFilter")) {
                String globalFilter = filters.get("globalFilter").toString();
                if (!globalFilter.trim().isEmpty()) {
                    for (String filteredField : querableFields) {
                        try{
                            switch (filteredField) {
                                case "id.runId":
                                case "id.samId":
                                    globalFiltersCriterion.add(Restrictions.eq(filteredField, Integer.parseInt((String) filters.get(filteredField))));
                                    break;
                                case "isControl":
                                    globalFiltersCriterion.add(Restrictions.eq(filteredField, Boolean.parseBoolean((String) filters.get(filteredField))));
                                    break;
                                default:
                                    globalFiltersCriterion.add(Restrictions.like(filteredField, filters.get(filteredField).toString(), MatchMode.ANYWHERE));
                                    break;
                            }
                        }catch(NumberFormatException e){}
                    }
                }
            }
            
           
            Criterion filterCriterion = Restrictions.conjunction()
                    .add(singleFiltersCriterion)
                    .add(globalFiltersCriterion);
            
            if (filters.containsKey("restrictToUsers")) {                
                List<Integer> restrictedUsers = ( List<Integer>) filters.get("restrictToUsers");
                Criterion userCriterion = Restrictions.in("sampleUser.id", restrictedUsers);
                filterCriterion = Restrictions.conjunction().add(filterCriterion).add(userCriterion);
            }
  
            query.add(filterCriterion);
        }
        
        if (sortField != null) {
            if (ascending) {
                query.addOrder(Order.asc(sortField));
            } else {
                query.addOrder(Order.desc(sortField));
            }
        }
   
        return query;
    }

    public void deleteSampleRun(int runId, int sampleId) {
        SampleRunEntity toDelete = getSampleRunById(runId, sampleId);
        deleteSampleRun(toDelete);
    }
    
    public void deleteSampleRun(SampleRunEntity toDelete) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(toDelete);
    }

    public Boolean checkRunExistence(int runId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(SampleRunEntity.class)
                .add(Restrictions.eq("id.runId", runId))
                .setProjection(Projections.rowCount());
        
        long count = (Long) query.uniqueResult();
        if(count != 0){
            return true;
        } else {
            return false;
        }
    }

    public void persistSampleRun(SampleRunEntity sampleRunEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.save(sampleRunEntity);
    }

    public void updateSampleRun(SampleRunEntity sampleRunEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.update(sampleRunEntity);
    }
    
}
