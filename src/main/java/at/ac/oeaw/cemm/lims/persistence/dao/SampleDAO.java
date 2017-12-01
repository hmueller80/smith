/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
@ApplicationScoped
public class SampleDAO {

    private Set<String> querableFields = new HashSet<String>() {
        {
            add("id");
            add("name");
            add("library.libraryName");
            add("user.login");
            add("status");
            add("organism");
            add("experimentName");
            add("submissionId");
            add("costCenter");
            add("description");
        }
    };

    public SampleDAO() {
        System.out.println("Initializing SampleDAO");
    }

    public SampleEntity getSampleById(int sampleId) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        SampleEntity sample = null;
        sample = (SampleEntity) session.get(SampleEntity.class, sampleId);

        return sample;
    }

    public int getSamplesCount(Map<String, Object> filters)
            throws HibernateException {

        int result = 0;

        Criteria query = assembleQuery(filters);
        query.setProjection(Projections.countDistinct("id"));
                
        result = ((Long) query.uniqueResult()).intValue();

        return result;
    }

    public List<SampleEntity> getSamplesPaginated(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters)
            throws HibernateException {

        List<SampleEntity> resultList = null;

        //1. Get the ids of the distinct samples satisfying the query
        Criteria query = assembleQuery(sortField,ascending,filters);
        query.setProjection(Projections.distinct(Projections.property("id")));
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List idsToFetch = query.list();

         //2. Fetch the distinct sample in the query
        query = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(SampleEntity.class)
                .add(Restrictions.in("id", idsToFetch))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (sortField != null) {
            if (ascending) {
                query.addOrder(Order.asc(sortField));
            } else {
                query.addOrder(Order.desc(sortField));
            }
        }

        resultList = (List<SampleEntity>) query.list();

        return resultList;
    }
    
     public List<SampleEntity> getAllSamples(String sortField, boolean ascending, Map<String, Object> filters)
            throws HibernateException {

        List<SampleEntity> resultList = null;

        Criteria query = assembleQuery(sortField,ascending,filters)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        if (sortField != null) {
            if (ascending) {
                query.addOrder(Order.asc(sortField));
            } else {
                query.addOrder(Order.desc(sortField));
            }
        }

        resultList = (List<SampleEntity>) query.list();

        return resultList;
    }
    
    private Criteria assembleQuery(Map<String, Object> filters) {
        return assembleQuery(null,null,filters);
    }

    
    private Criteria assembleQuery(String sortField, Boolean ascending, Map<String, Object> filters) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Criteria query = session.createCriteria(SampleEntity.class).createAlias("library", "library", JoinType.LEFT_OUTER_JOIN)
                .createAlias("user", "user", JoinType.LEFT_OUTER_JOIN)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (filters != null) {
            Conjunction singleFiltersCriterion = Restrictions.conjunction();
            Disjunction globalFiltersCriterion = Restrictions.disjunction();
            for (String filteredField : filters.keySet()) {
                if (querableFields.contains(filteredField)) {
                    switch (filteredField) {
                        case "id":
                        case "submissionId":
                            singleFiltersCriterion.add(Restrictions.eq(filteredField, Integer.parseInt((String) filters.get(filteredField))));
                            break;
                        case "library.libraryName":
                            Object filterValue = filters.get(filteredField);
                            if (filterValue instanceof String[]) {
                                String[] filterValues = (String[]) filterValue;
                                Disjunction selectedLibraries = Restrictions.disjunction();
                                for (String singleValue : filterValues) {
                                    selectedLibraries.add(Restrictions.eq(filteredField, singleValue));
                                }
                                singleFiltersCriterion.add(selectedLibraries);
                            } else {
                                singleFiltersCriterion.add(Restrictions.like(filteredField, (String) filterValue, MatchMode.ANYWHERE));
                            }   break;
                        default:
                            singleFiltersCriterion.add(Restrictions.like(filteredField, filters.get(filteredField).toString(), MatchMode.ANYWHERE));
                            break;
                    }
                }
            }
            if (filters.containsKey("globalFilter")) {
                String globalFilter = filters.get("globalFilter").toString();
                if (!globalFilter.trim().isEmpty()) {
                    for (String filteredField : querableFields) {
                        if (filteredField.equals("id") || filteredField.equals("submissionId")) {
                            try {
                                globalFiltersCriterion.add(Restrictions.eq(filteredField, Integer.parseInt((String) globalFilter)));
                            } catch (NumberFormatException e) {
                            }
                        } else {
                            globalFiltersCriterion.add(Restrictions.like(filteredField, globalFilter, MatchMode.ANYWHERE));
                        }
                    }
                }
            }

            Criterion filterCriterion = Restrictions.conjunction().add(singleFiltersCriterion).add(globalFiltersCriterion);
            
            if (filters.containsKey("restrictToUsers")) {                
                Collection<Integer> restrictedUsers = ( Collection<Integer>) filters.get("restrictToUsers");
                Criterion userCriterion = Restrictions.in("user.id", restrictedUsers);
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

    public Integer getMaxSampleId() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria sampleCriteria = session.createCriteria(SampleEntity.class).setProjection(Projections.max("id"));
        Integer id = (Integer) sampleCriteria.uniqueResult();
        if (id == null) {
            id = 0;
        }
        return id;
    }

    public void persistSample(SampleEntity sample) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Integer sampleId = this.getMaxSampleId() + 1;
        sample.setId(sampleId);
        session.save(sample);
        String sampleName = sample.getName() + "_S" + sample.getId();
        sample.setName(sampleName);
        session.update(sample);
    }

    public void updateSample(SampleEntity sample) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.update(sample);
    }

    public void deleteSample(SampleEntity sample) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(sample);
    }

    public List<SampleEntity> getSamplesByStatus(String status) {
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
         Criteria query = session.createCriteria(SampleEntity.class)
                 .add(Restrictions.eq("status", status));
         
         return query.list();
    }

}
