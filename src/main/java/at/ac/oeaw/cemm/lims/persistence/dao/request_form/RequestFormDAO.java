/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestSampleEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
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
public class RequestFormDAO {
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    
     private Set<String> querableFields = new HashSet<String>() {
        {
            //INTEGERS
            add("id");
            
            //DATE
            add("reqDate");
            
            //STRINGS
            add("status");
            add("userId.login");
        }
    };

    public void saveOrUpdate(RequestEntity requestEntity) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(requestEntity);
        session.flush();
    }

    public RequestEntity getRequestById(Integer rid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return (RequestEntity) session.get(RequestEntity.class, rid);
    }

    public List<RequestEntity> getDeleatableRequests() {
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
         Criteria query = session.createCriteria(RequestEntity.class);
         query.add(Restrictions.ne("status", RequestFormDTO.STATUS_ACCEPTED));
         query.addOrder(Order.desc("id"));
         
         return query.list();
    }

    public void deleteSample(RequestSampleEntity toDelete) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(toDelete);
    }

    public void deleteLibrary(RequestLibraryEntity toDelete) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(toDelete);
    }

    public void deleteRrequest(RequestEntity toDelete) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(toDelete);
    }

    public Integer getRequestsCount(Map<String, Object> filters) {
        int result = 0;

        Criteria query = assembleQuery(filters);
        query.setProjection(Projections.countDistinct("id"));

        result = ((Long) query.uniqueResult()).intValue();
        
        System.out.println("There are "+result+" requests");
        return result;
    }

    public List<RequestEntity> getRequestsPaginated(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters) {

        //1. Get the ids of the distinct runs satisfying the query
        Criteria query = assembleQuery(sortField,ascending,filters);
        query.setProjection(Projections.distinct(Projections.property("id")));
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List idsToFetch = query.list();

        //2. Fetch the distinct sample runs in the query
        query = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(RequestEntity.class)
                .add(Restrictions.in("id", idsToFetch))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (sortField != null) {
            if (ascending) {
                query.addOrder(Order.asc(sortField));
            } else {
                query.addOrder(Order.desc(sortField));
            }
        }

        List<RequestEntity> resultList = (List<RequestEntity>) query.list();


        System.out.println("Returning "+resultList.size()+" sample runs");
        return resultList;
    }

    public List<RequestEntity> getAllRequests(String sortField, boolean ascending, Map<String, Object> filters) {

        //1. Get the ids of the distinct runs satisfying the query
        Criteria query = assembleQuery(sortField,ascending,filters);
        query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (sortField != null) {
            if (ascending) {
                query.addOrder(Order.asc(sortField));
            } else {
                query.addOrder(Order.desc(sortField));
            }
        }

        List<RequestEntity> resultList = (List<RequestEntity>) query.list();

        System.out.println("Returning "+resultList.size()+" requests");
        return resultList;
    }
    
    private Criteria assembleQuery(Map<String, Object> filters) {
        return assembleQuery(null,null,filters);
    }

    private Criteria assembleQuery(String sortField, Boolean ascending, Map<String, Object> filters) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Criteria query = session.createCriteria(RequestEntity.class)
                .createAlias("userId", "userId", JoinType.LEFT_OUTER_JOIN)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        if (filters != null) {
            Conjunction singleFiltersCriterion = Restrictions.conjunction();
            for (String filteredField : filters.keySet()) {
                if (querableFields.contains(filteredField)) {
                    switch (filteredField) {
                        case "id":
                            singleFiltersCriterion.add(Restrictions.eq(filteredField, Integer.parseInt((String) filters.get(filteredField))));
                            break;
                        case "reqDate": {
                            try {
                                singleFiltersCriterion.add(Restrictions.eq(filteredField, dateFormatter.parse((String) filters.get(filteredField))));
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                        default:
                            singleFiltersCriterion.add(Restrictions.like(filteredField, filters.get(filteredField).toString(), MatchMode.ANYWHERE));
                            break;
                    }
                }
            }
           
            Criterion filterCriterion = Restrictions.conjunction()
                    .add(singleFiltersCriterion);
            
            if (filters.containsKey("restrictToUsers")) {                
                Collection<Integer> restrictedUsers = ( Collection<Integer>) filters.get("restrictToUsers");
                Criterion userCriterion = Restrictions.in("userId.id", restrictedUsers);
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

    
}
