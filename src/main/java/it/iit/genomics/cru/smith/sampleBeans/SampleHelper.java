package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.MultipleRequest;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.utils.DateParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

/**
 * @(#)SampleHelper.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations on sample table.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class SampleHelper { 
    
    DataBean dataBean;
    
    public SampleHelper(){
        FacesContext context = FacesContext.getCurrentInstance();   
        dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
    }
    
    //static List<Sample> allSamples = SampleHelper.getSampleList();
    
    /**
    * Saves a sample to the database. Id is assigned by auto increment.
    *
    * @author Heiko Muller
    * @param sample
    * @return boolean - true if sample was saved successfully, false otherwise
    * @since 1.0
    */
    public boolean saveSample(Sample sample){
        Session session = HibernateUtil.getSessionFactory().openSession();  
        boolean outcome = true;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();             
            session.save(sample);
            dataBean.updateSample(sample);
            tx.commit();
            //incrementNextSampleId();
        } catch (Exception e) {
            outcome = false;
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }
        return outcome;
    }
    
    /**
    * Saves a sample to the database. Id is assigned by auto increment.
    *
    * @author Heiko Muller
    * @param sample
    * @return boolean - true if sample was saved successfully, false otherwise
    * @since 1.0
    */
    public void updateSample(Sample sample){
        Session session = HibernateUtil.getSessionFactory().openSession();  
        boolean outcome = true;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();             
            session.update(sample);
            dataBean.updateSample(sample);
            tx.commit();            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                //session.flush();
                session.close();
            }
        }
    }
    
    /**
    * Returns next maximum ID. It is used because auto increment 
    * does not return MAXIMUM value, it return UNIQUE value
    * 
    * @author Heiko Muller
    * @return int - maximum ID that can be used for the next sample
    * @since 1.0
    */
    public static int getNextSampleId(){
        Session session = HibernateUtil.getSessionFactory().openSession();  
        int res = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();             
            Criteria criteria = session.createCriteria(Sample.class).setProjection(Projections.max("id"));
            
            Integer maxId = (Integer)criteria.uniqueResult();
            if (maxId == null){
                res = 0;
            }else{
                res = maxId + 1;
            }
            tx.commit();            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
        return res;
    }
    
    
    /**
    * Saves a sample to the database using SQL.
    * Permits setting the requestId to the desired value instead of using auto increment.
    * This helps to keep old and new databases in phase.
    *
    * @author Heiko Muller
    * @param sample
    * @return boolean - true if sample was saved successfully, false otherwise
    * @since 1.0
    *
    public static boolean saveSampleSQL(Sample sample){
        Session session = HibernateUtil.getSessionFactory().openSession();  
        boolean outcome = true;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();  
            String queryString = getSQLSaveSampleString(sample);
            SQLQuery sqlq = session.createSQLQuery(queryString);
            sqlq.executeUpdate();
            //session.save(sample);
            tx.commit();
            //incrementNextSampleId();
        } catch (Exception e) {
            outcome = false;
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }
        return outcome;
    }
    */
    /**
    * Returns the SQL String for saving a Sample.
    * Permits setting the requestId to the desired value instead of using auto increment.
    * This helps to keep old and new databases in phase.
    *
    * @author Heiko Muller
    * @param sample
    * @return String - the SQL command to save the sample
    * @since 1.0
    
    private static String getSQLSaveSampleString(Sample sample){
        //INSERT INTO ngs.sample (sam_id, requester_user_id, application_id, organism, `type`, antibody, librarysynthesisneeded, concentration, totalamount, bulkfragmentsize, costcenter, status, `name`, comment, description, requestdate, bioanalyzerdate, bionalyzerbiomolarity, timeSeriesStep, experimentName, sequencingIndexId) VALUES (
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ngs.sample (sam_id, requester_user_id, application_id, organism, `type`, antibody, librarysynthesisneeded, concentration, totalamount, bulkfragmentsize, costcenter, status, `name`, comment, description, requestdate, bioanalyzerdate, bionalyzerbiomolarity, submission_id, experimentName, sequencingIndexId) VALUES (");
        sb.append(sample.getId() + ", ");
        sb.append(sample.getUser().getId() + ", ");
        sb.append(sample.getApplication().getApplicationId() + ", '");
        sb.append(sample.getOrganism() + "', '");
        sb.append(sample.getType() + "', '");
        sb.append(sample.getAntibody() + "', ");
        if(sample.getLibrarySynthesisNeeded()){
            sb.append(1 + ", ");
        }else{
            sb.append(0 + ", ");
        }
        
        sb.append(sample.getConcentration().doubleValue() + ", ");
        sb.append(sample.getTotalAmount().doubleValue() + ", ");
        sb.append(sample.getBulkFragmentSize().doubleValue() + ", '");
        sb.append(sample.getCostCenter() + "', '");
        sb.append(sample.getStatus() + "', '");
        sb.append(sample.getName() + "', '");
        sb.append(sample.getComment() + "', '");
        sb.append(sample.getDescription() + "', '");
        String date = DateParser.parseDateToStringUK(sample.getRequestDate(), "YYYY-MM-dd");
        sb.append(date + "', '");
        date = DateParser.parseDateToStringUK(sample.getBioanalyzerDate(), "YYYY-MM-dd");
        sb.append(date + "', ");
        sb.append(sample.getBionalyzerBiomolarity() + ", ");
        //date = DateParser.parseDateToStringUK(sample.getTimeSeriesStep(), "YYYY-MM-dd HH:mm:ss");
        sb.append(null + ", '");
        sb.append(sample.getExperimentName() + "', ");
        sb.append(sample.getSequencingIndexes().getId() + ");");
        return sb.toString();
    }
    */
    /**
    * Return the list of all the samples in the database.
    *
    * @author Francesco Venco
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getSampleList() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //System.out.println("query sample table");
            //SQLQuery sql = session.createSQLQuery("select * from sample order by sam_id desc");
            //System.out.println("sql query sample table");
            //SQLQuery sql = session.createSQLQuery("select * from sample");
            //System.out.println("list sql query");
            //List<Object> l = sql.list();
            //for(Object s : l){
            //    System.out.println(s.toString());
            //}
            //System.out.println("list sql query done");   
            Query q = session.createQuery("from Sample order by sam_id desc");
            System.out.println("listing sample query");
            //sampleList = (List<Sample>) q.list();
            
            Iterator it = q.iterate();
            sampleList = new ArrayList<Sample>();
            while(it.hasNext()){
                Sample s = (Sample)it.next();
                //s.dump();
                s.getId();
                //s.dump();
                //s.getUser().getLogin();
                sampleList.add(s);
            }
            
            //System.out.println("listing query done, committing");
            //for for lazy initialization
            
            //for (Sample sample : sampleList) {
                //System.out.println("getting user login for sample");
                //sample.getUser().getLogin();
                //System.out.println("iterating projects for sample");
                //for (Project project : sample.getProjects()) {
                //    project.getId();
                //}
            //}
            

            tx.commit();
            //System.out.println("committing done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }
    
    /**
    * Return the list of all the samples in the database.
    *
    * @author Francesco Venco
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getLazySampleList() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            System.out.println("query sample table");
            //SQLQuery sql = session.createSQLQuery("select * from sample order by sam_id desc");
            //System.out.println("sql query sample table");
            //SQLQuery sql = session.createSQLQuery("select * from sample");
            //System.out.println("list sql query");
            //List<Object> l = sql.list();
            //for(Object s : l){
            //    System.out.println(s.toString());
            //}
            //System.out.println("list sql query done");   
            Query q = session.createQuery("from Sample order by sam_id desc");
            //System.out.println("listing query");
            //sampleList = (List<Sample>) q.list();
            
            Iterator it = q.iterate();
            sampleList = new ArrayList<Sample>();
            while(it.hasNext()){
                Sample s = (Sample)it.next();
                //s.dump();
                //s.getId();
                //s.dump();
                //s.getUser().getLogin();
                sampleList.add(s);
            }
            
            //System.out.println("listing query done, committing");
            //for for lazy initialization
            
            //for (Sample sample : sampleList) {
                //System.out.println("getting user login for sample");
                //sample.getUser().getLogin();
                //System.out.println("iterating projects for sample");
                //for (Project project : sample.getProjects()) {
                //    project.getId();
                //}
            //}
            

            tx.commit();
            //System.out.println("committing done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }

    /**
    * Return the list of all the samples in the database with status "queued".
    *
    * @author Francesco Venco
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getQueuedSampleList() {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample where status = 'queued'");
            sampleList = (List<Sample>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < sampleList.size(); i++) {
                User u = sampleList.get(i).getUser();
                String login = u.getLogin();
            }
            */

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }

    /**
    * Return the list of all the samples in the database with status "running".
    *
    * @author Francesco Venco
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getRunningSampleList() {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample where status = 'running'");
            sampleList = (List<Sample>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < sampleList.size(); i++) {
                User u = sampleList.get(i).getUser();
                String login = u.getLogin();
            }
            */

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }

    /**
    * Return sample with given id.
    *
    * @author Francesco Venco
    * @param id
    * @return Sample
    * @since 1.0
    */
    public static Sample getSampleByID(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> sampleList = null;
        Transaction tx = null;
        Sample result = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample where id='" + id + "'");
            sampleList = (List<Sample>) q.list();
            if(sampleList != null && sampleList.size() > 0){
                result = sampleList.get(0);
                result.getId();
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        
        return result;
    }
    
    public static Sample getSampleByNameAndFCID(String FCID, String samplename){
        List<SampleRun> sr = RunHelper.getRunsByFCIDList(FCID);
        if(sr != null){
            for(SampleRun s : sr){
                Sample sm = s.getsample();
                if(s.getsample().getName().equals(samplename)){
                    return sm;
                }
            }
        }
        return null;
    }

    /**
    * Return all samples of a given user.
    *
    * @author Francesco Venco
    * @param userId
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getSampleList(int userId) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample as s where s.user.id='" + userId + "' order by sam_id desc");
            sampleList = (List<Sample>) q.list();
            /*
            for (int i = 0; i < sampleList.size(); i++) {
                User u = sampleList.get(i).getUser();
                String login = u.getLogin();
                Set<Project> p = sampleList.get(i).getProjects();
                for (Project p1 : p) {
                    p1.getId();
                }
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }

    /**
    * Return all samples of a given group leader.
    *
    * @author Francesco Venco
    * @param pi
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getSampleListByGroupId(int pi) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample as s where s.user.pi='" + pi + "' order by sam_id desc");
            sampleList = (List<Sample>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < sampleList.size(); i++) {
                User u = sampleList.get(i).getUser();
                String login = u.getLogin();
                Set<Project> p = sampleList.get(i).getProjects();
                for (Project p1 : p) {
                    p1.getId();
                }
            }
            */

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }

    /**
    * Return the list of all the samples in the database with status "requested".
    *
    * @author Francesco Venco
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getRequestedSampleList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample where status = 'requested'");
            sampleList = (List<Sample>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < sampleList.size(); i++) {
                User u = sampleList.get(i).getUser();
                String login = u.getLogin();
            }
            */

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;

    }

    /**
    * Saves or updates an application.
    *
    * @author Francesco Venco
    * @param a - an application
    * @return Application
    * @since 1.0
    */
    public static Application saveOrUpdate (Application a){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Application app = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Application");
            List<Application> applicationList = (List<Application>) q.list();
            boolean isNew = true;
            for (Application oldA : applicationList){
                if (NgsLimsUtility.dump(oldA).equals(NgsLimsUtility.dump(a))) {
                    app = oldA;
                    isNew = false;
                }
            }
            
            if(isNew){
                session.save(a);
                app = a;
            }
            
            
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return app;
    }
    
    /**
    * Returns id of a given sequencing index.
    *
    * @author Francesco Venco
    * @param indexseq
    * @return SequencingIndex
    * @since 1.0
    */
    public static SequencingIndex getSequencingIndexIdBySequence(String indexseq) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SequencingIndex idx = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SequencingIndex seq where seq.index = '" + indexseq + "'");
            List<SequencingIndex> indexList = (List<SequencingIndex>) q.list();
            if(indexList != null && indexList.size() >0){
                idx = indexList.get(0);
            }
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return idx;

    }
    
    /**
    * Returns list of samples that are pooled with a given sample.
    *
    * @author Heiko Muller
    * @param sample
    * @return List<Sample>
    * @since 1.0
    
    public static List<Sample> getPoolMembers0(Sample sample) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> pool = new ArrayList<Sample>();
        
        Transaction tx = null;
        try {
            tx = session.beginTransaction();     
            sample = (Sample) session.get(Sample.class, sample.getId());
            Set<MultipleRequest> mrs = sample.getMultipleRequests();
            if (!mrs.isEmpty()) {                 
                for (MultipleRequest mr : mrs) {
                    int i = mr.getId().getRequestId();
                    List<MultipleRequest> poolMembers = RunHelper.getRequestList0(i);
                    for (MultipleRequest mr2 : poolMembers) {
                        Sample s = mr2.getSample();                        
                        pool.add(s);
                    }
                }
            }else{
                pool.add(sample);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }        
        return pool;
    }
    */
    /**
    * Returns list of samples that are pooled with a given sample.
    *
    * @author Heiko Muller
    * @param sample
    * @return List<Sample>
    * @since 1.0
    */
    public static List<Sample> getPoolMembers(Sample sample) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Sample> pool = new ArrayList<Sample>();
        
        Transaction tx = null;
        try {
            tx = session.beginTransaction();     
            sample = (Sample) session.get(Sample.class, sample.getId());
            Set<Library> mrl = sample.getLibrary();
            if (!mrl.isEmpty()) {                 
                for (Library l : mrl) {
                    int i = l.getId().getLibraryId();
                    List<Library> poolMembers = RunHelper.getPoolMembers(i);
                    for (Library l2 : poolMembers) {
                        Sample s = l2.getSample();                        
                        pool.add(s);
                    }
                }
            }else{
                pool.add(sample);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }        
        return pool;
    }
      
    /**
    * Returns MultipleRequest object for a given sample.
    *
    * @author Francesco Venco
    * @param sample
    * @return Set<MultipleRequest>
    * @since 1.0
    
    public static Set<MultipleRequest> getMultipleRequests(Sample sample) {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        Set<MultipleRequest> mrs = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();   
            sample = (Sample) session.get(Sample.class, sample.getId());
            mrs = sample.getMultipleRequests();
            boolean temp = mrs.isEmpty();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
        return mrs;
    }
    */
    /**
    * Returns Library object for a given sample.
    *
    * @author Heiko Mueller
    * @param sample
    * @return Set<Library>
    * @since 1.0
    */
    public static Set<Library> getLibrary(Sample sample) {
        Session session = HibernateUtil.getSessionFactory().openSession();        
        Set<Library> mrs = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();   
            sample = (Sample) session.get(Sample.class, sample.getId());
            mrs = sample.getLibrary();
            boolean temp = mrs.isEmpty();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
        return mrs;
    }
    
    public static int getNextLibraryId(){
        Session session = HibernateUtil.getSessionFactory().openSession();  
        int res = 0;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();             
            //Criteria criteria = session.createCriteria(Library.class).setProjection(Projections.max("library_id"));
            //Integer maxId = (Integer)criteria.uniqueResult();
            Query q = session.createQuery("from Library");
            List<Library> libList = (List<Library>) q.list();
            
            Integer maxId = 0;
            for(Library l : libList){
                if(l.getId().getLibraryId() > maxId){
                    maxId = l.getId().getLibraryId();
                }
            }
            if (maxId == null){
                res = 0;
            }else{
                res = maxId + 1;
            }
            tx.commit();            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
        //System.out.println("next library id " + res);
        return res;
    }
    
    public SampleHelper getInstance(){
        return this;
    }
} 