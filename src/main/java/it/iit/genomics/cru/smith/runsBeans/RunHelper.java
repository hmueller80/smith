package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.dataBean.DataBean;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.MultipleRequest;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SampleRunId;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)RunHelper.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations on sample runs.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class RunHelper {
    //DataBean dataBean;
    //static List<SampleRun> allRuns = getRunsList();
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init RunHelper");
        }        
    }
    
    //public RunHelper(){
    //    FacesContext context = FacesContext.getCurrentInstance();   
    //    dataBean = ((DataBean) context.getApplication().evaluateExpressionGet(context, "#{dataBean}", DataBean.class)); 
    //}

    /**
    * Returns list of sample runs.
    *
    * @author Francesco Venco
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getRunsList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun order by run_id desc");
            //runsList = (List<SampleRun>) q.list();
            Iterator it = q.iterate();
            runsList = new ArrayList<SampleRun>();
            while(it.hasNext()){
                SampleRun s = (SampleRun)it.next();
                //s.dump();
                s.getId();
                //s.dump();
                //s.getUser().getLogin();
                runsList.add(s);
            }
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<MultipleRequest> mrs = s.getMultipleRequests();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                //System.out.println("Multiple request size " + mrs.size());
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }
    
    /**
    * Returns list of sample runs.
    *
    * @author Francesco Venco
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getLazyRunsList() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun order by run_id desc");
            //runsList = (List<SampleRun>) q.list();
            
            Iterator it = q.iterate();
            runsList = new ArrayList<SampleRun>();
            while(it.hasNext()){
                SampleRun s = (SampleRun)it.next();
                //s.dump();
                //s.getId();
                //s.dump();
                //s.getUser().getLogin();
                runsList.add(s);
            }
            
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<MultipleRequest> mrs = s.getMultipleRequests();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                //System.out.println("Multiple request size " + mrs.size());
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }

    /**
    * Returns list of sample runs belonging to a given group leader.
    *
    * @author Francesco Venco
    * @param groupid
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getRunsListByGroupId(int groupid) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.sample.user.pi ='" + groupid + "'");
            runsList = (List<SampleRun>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                String s3 = s.getSequencingIndexes().getIndex();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }

    /**
    * Returns list of sample runs that have been run on the same flow cell (same run id).
    *
    * @author Francesco Venco
    * @param runId
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getRunsList(int runId) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.id.runId = '" + runId + "'");
            runsList = (List<SampleRun>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<MultipleRequest> mrs = s.getMultipleRequests();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                //System.out.println("Multiple request size " + mrs.size());
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }
    
    /**
    * Returns list of sample runs that have been run on the same flow cell.
    *
    * @author Francesco Venco
    * @param FCID - the flow cell barcode
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getRunsByFCIDList(String FCID) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery ("from SampleRun as s where s.flowcell ='" + FCID+ "'");
            runsList = (List<SampleRun>) q.list();
            for(SampleRun sr : runsList){
                sr.getId();
            }
            //for for lazy initialization
            
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<MultipleRequest> mrs = s.getMultipleRequests();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                //System.out.println("Multiple request size " + mrs.size());
                u = s.getUser();
                login = u.getLogin();
            }
            */
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }

    /**
    * Returns list of pooled samples.
    *
    * @author Francesco Venco
    * @param reqId
    * @return List<MultipleRequest>
    * @since 1.0
    */
    public static List<MultipleRequest> getRequestList0(int reqId) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<MultipleRequest> reqList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from MultipleRequest as ms where ms.id.requestId = '" + reqId + "'");
            reqList = (List<MultipleRequest>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < reqList.size(); i++) {
                reqList.get(i).getId().getRequestId();
                reqList.get(i).getId().getSampleId();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return reqList;
    }
    
    /**
    * Returns list of pooled samples.
    *
    * @author Francesco Venco
    * @param reqId
    * @return List<MultipleRequest>
    * @since 1.0
    */
    public static List<Library> getPoolMembers(int reqId) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Library> reqList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Library as ms where ms.id.libraryId = '" + reqId + "'");
            reqList = (List<Library>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < reqList.size(); i++) {
                reqList.get(i).getId().getRequestId();
                reqList.get(i).getId().getSampleId();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return reqList;
    }

    /**
    * Returns list of queued samples (sample status queued).
    *
    * @author Francesco Venco
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getQueuedSampleRunList() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.sample.status='queued'");
            runsList = (List<SampleRun>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                s2 = s.getName();
                s2 = s.getDescription();
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }

    /**
    * Returns list of running samples (sample status running).
    *
    * @author Francesco Venco
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getRunningSampleRunList() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.sample.status='running'");
            runsList = (List<SampleRun>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                s2 = s.getName();
                s2 = s.getDescription();
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }

    /**
    * Returns list of queued samples for a given user.
    *
    * @author Francesco Venco
    * @param userId
    * @return List<SampleRun>
    * @since 1.0
    */
    public static List<SampleRun> getQueuedSampleRunListForUser(Integer userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> runsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.sample.status='queued'"
                    + " and s.sample.user.id='" + userId + "'");
            runsList = (List<SampleRun>) q.list();
            //for for lazy initialization
            /*
            for (int i = 0; i < runsList.size(); i++) {
                SampleRun csr = runsList.get(i);
                User u = csr.getUser();
                String login = u.getLogin();
                Reagent r1 = csr.getReagentByClustergenerationReagentCode();
                Reagent r2 = csr.getReagentBySamplePrepReagentCode();
                Reagent r3 = csr.getReagentBySequencingReagentCode();
                Sample s = csr.getsample();
                String s3 = s.getSequencingIndexes().getIndex();
                Integer samId = s.getId();
                SampleRunId id = csr.getId();
                Application a = s.getApplication();
                String s2 = a.getApplicationname();
                Set<Lane> lanes = csr.getLanes();
                //System.out.println("Lanes = " + lanes);
                s2 = s.getName();
                s2 = s.getDescription();
                u = s.getUser();
                login = u.getLogin();
            }
            */
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return runsList;
    }
    
    /**
    * Returns list of queued samples for a given user.
    *
    * @author Heiko Muller
    * @param runId
    * @param sampleId
    * @return SampleRun
    * @since 1.0
    */
    public static SampleRun getSampleRun(int runId, int sampleId) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> runsList = null;
        SampleRun result = null;
        SampleRunId id = new SampleRunId();
        id.setRunId(runId);
        id.setSamId(sampleId);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun as s where s.id.runId = '" + runId + "' and s.id.samId='" + sampleId + "'");
            runsList = (List<SampleRun>) q.list();
            if(runsList != null && !runsList.isEmpty()){
                result = runsList.get(0);
            }
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            session.close();
        }
        return result;
    }
    
    /**
    * Returns list of queued samples for a given user.
    *
    * @author Francesco Venco
    * @param runId
    * @param sampleId
    * @param lanename
    * @return List<SampleRun>
    * @since 1.0
    */
    public static Lane getLane(int runId, int sampleId, int lanename) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Lane> lanes = null;
        Lane result = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Lane as l where l.samplerun.id.runId = '" + runId + "' and l.samplerun.id.samId='" + sampleId + "' and l.laneName='" + lanename +  "'");
            lanes = q.list();
            if(lanes != null && !lanes.isEmpty()){
                result = lanes.get(0);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            session.close();
        }
        return result;
    }

    /**
    * Returns id for the next run.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public static int getNextRunId() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> runsList = null;
        int result = 1;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SampleRun order by run_id desc");
            runsList = (List<SampleRun>) q.list();
            if (runsList.size() > 0) {
                result = (runsList.get(0)).getRunId();
                result++;
            }

            //for for lazy initialization
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
        
        return result;
    }

}
