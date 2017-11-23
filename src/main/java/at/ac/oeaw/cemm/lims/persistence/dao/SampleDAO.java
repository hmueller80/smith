/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
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

    public int getSamplesCount(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters)
            throws HibernateException {

        int result = 0;

        Criteria query = assembleQuery(sortField, ascending, filters);
        query.setProjection(Projections.rowCount());
        result = ((Long) query.uniqueResult()).intValue();

        return result;
    }

    public List<SampleEntity> getSamples(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters)
            throws HibernateException {

        List<SampleEntity> resultList = null;

        Criteria query = assembleQuery(sortField, ascending, filters);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        resultList = query.list();

        return resultList;

    }

    private Criteria assembleQuery(String sortField, boolean ascending, Map<String, Object> filters) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Criteria query = session.createCriteria(SampleEntity.class).createAlias("library", "library", JoinType.LEFT_OUTER_JOIN)
                .createAlias("user", "user", JoinType.LEFT_OUTER_JOIN);

        if (filters != null) {
            Conjunction singleFiltersCriterion = Restrictions.conjunction();
            Disjunction globalFiltersCriterion = Restrictions.disjunction();
            for (String filteredField : filters.keySet()) {
                if (querableFields.contains(filteredField)) {
                    if (filteredField.equals("id") || filteredField.equals("submissionId")) {
                        singleFiltersCriterion.add(Restrictions.eq(filteredField, Integer.parseInt((String) filters.get(filteredField))));
                    } else if (filteredField.equals("library.libraryName")) {
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
                        }
                    } else {
                        singleFiltersCriterion.add(Restrictions.like(filteredField, filters.get(filteredField).toString(), MatchMode.ANYWHERE));
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

}
