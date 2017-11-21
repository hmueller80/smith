package it.iit.genomics.cru.smith.data;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.AlignedData;
import it.iit.genomics.cru.smith.entity.AnnotatedData;
import it.iit.genomics.cru.smith.entity.RawData;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)DataHelper.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Class performs database operations regarding RawData, AlignedData, and AnnotatedData.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class DataHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init DataHelper");
        }
    }

    /**
     * Returns list of RawData
     *
     * @author Francesco Venco
     * @return List<RawData>
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static List<RawData> getRawDataList() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        List<RawData> rawList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from RawData");
            rawList = (List<RawData>) q.list();
            // for for lazy initialization
            
            for (int i = 0; i < rawList.size(); i++) {
                RawData data = rawList.get(i);
                if (data.getbcltofastqcommnad().isEmpty()) {
                    data.setbcltofastqcommnad("CASAVA");
                }
                if (data.getFastqcreferencer1().isEmpty()) {
                    data.setFastqcreferencer1("r1");
                }
                if (data.getFastqcreferencer2().isEmpty()) {
                    data.setFastqcreferencer2("r2");
                }
                SampleRun run = data.getsamplerun();
                Sample s = run.getsample();
                s.getName();
            }
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return rawList;
    }
    
    /**
     * Returns RawData for a given SampleRun
     *
     * @author Francesco Venco
     * @param sr
     * @return RawData
     * @since 1.0
     */
    public static RawData getRawData(SampleRun sr) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<RawData> rawList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from RawData as rd where rd.samplerun.id.runId="+sr.getRunId()+" and rd.samplerun.id.samId="+sr.getsamId());
            rawList = (List<RawData>) q.list();
            // for for lazy initialization
           
            for (int i = 0; i < rawList.size(); i++) {
                RawData data = rawList.get(i);
                if (data.getbcltofastqcommnad().isEmpty()) {
                    data.setbcltofastqcommnad("CASAVA");
                }
                if (data.getFastqcreferencer1().isEmpty()) {
                    data.setFastqcreferencer1("r1");
                }
                if (data.getFastqcreferencer2().isEmpty()) {
                    data.setFastqcreferencer2("r2");
                }
                SampleRun run = data.getsamplerun();
                Sample s = run.getsample();
                s.getName();
            }
           
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        if (rawList != null && !rawList.isEmpty()) {
            return rawList.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * Returns list of AlignedData for given RawData
     *
     * @author Francesco Venco
     * @param rd
     * @return List<AlignedData>
     * @since 1.0
     */
    public static List<AlignedData> getAlignedData(RawData rd) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        List<AlignedData> dataList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from AlignedData where raw_id="+rd.getRawId());
            dataList = (List<AlignedData>) q.list();
            // for for lazy initialization
            
            for (int i = 0; i < dataList.size(); i++) {
                AlignedData data = dataList.get(i);
                RawData r = data.getrawdata();
                SampleRun run = r.getsamplerun();
                Sample s = run.getsample();
                s.getName();
            }
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return dataList;
    }
    
    /**
     * Returns map of AlignedData and associated AnnotatedData
     *
     * @author Francesco Venco
     * @param aligneddata
     * @return Map<AlignedData,List<AnnotatedData>>
     * @since 1.0
     */
    public static Map<AlignedData,List<AnnotatedData> > getAnnotatedData(List<AlignedData> aligneddata) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        HashMap<AlignedData, List<AnnotatedData>> dataMap = new HashMap<AlignedData, List<AnnotatedData>>();
        
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (AlignedData alignedData : aligneddata){
                Query q = session.createQuery("from AnnotatedData where aligned_id="+alignedData.getProcessedId());
                List<AnnotatedData> dataList = null;
                dataList = (List<AnnotatedData>) q.list();
                // for for lazy initialization
                
                for (int i = 0; i < dataList.size(); i++) {
                    AnnotatedData data = dataList.get(i);
                    RawData r = data.getRawdata();
                    SampleRun run = r.getsamplerun();
                    Sample s = run.getsample();
                    s.getName();
                }
                
                if(!dataList.isEmpty()){
                    dataMap.put(alignedData, dataList);
                }
                tx.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return dataMap;
    }

    /**
     * Returns list of AlignedData
     *
     * @author Francesco Venco
     * @return List<AlignedData>
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static List<AlignedData> getAlignedDataList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        List<AlignedData> dataList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from AlignedData");
            dataList = (List<AlignedData>) q.list();
            // for for lazy initialization
            
            for (int i = 0; i < dataList.size(); i++) {
                AlignedData data = dataList.get(i);
                RawData r = data.getrawdata();
                SampleRun run = r.getsamplerun();
                Sample s = run.getsample();
                s.getName();
            }
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return dataList;
    }

    /**
     * Returns list of AnnotatedData
     *
     * @author Francesco Venco
     * @return List<AnnotatedData>
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static List<AnnotatedData> getAnnotatedDataList() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        ;
        List<AnnotatedData> dataList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from AnnotatedData");
            dataList = (List<AnnotatedData>) q.list();
            // for for lazy initialization
            
            for (int i = 0; i < dataList.size(); i++) {
                AnnotatedData data = dataList.get(i);
                RawData r = data.getRawdata();
                SampleRun run = r.getsamplerun();
                Sample s = run.getsample();
                s.getName();
            }
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return dataList;
    }

}
