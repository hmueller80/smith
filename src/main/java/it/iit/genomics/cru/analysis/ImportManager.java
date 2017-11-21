package it.iit.genomics.cru.analysis;

//import it.iit.genomics.cru.analysis.AnalysisManagerSEMM;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.AlignedData;
import it.iit.genomics.cru.smith.entity.AnnotatedData;
import it.iit.genomics.cru.smith.entity.RawData;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.Samplesheet;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Transaction;

/**
 * @(#)ImportManager.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Class implements a set of database and file operation methods that serve when an analysis run has finished
 * 
 * @author Yuriy Vaskin
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class ImportManager {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init ImportManager");
        }
    }
   
    /**
    * 
    * persists a RawData object
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param rd - a RawData object
    * @since 1.0
    */
    public static void save(RawData rd){
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;           
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            session.save(rd);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
           
        } finally {
            session.close();
        }
    }
    
    /**
    * 
    * persists a list of AlignedData objects
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param ads - AlignedData set
    * @since 1.0
    */
    public static void save(List<AlignedData> ads){
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;           
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            for(AlignedData ad : ads){
                session.save(ad);
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
           
        } finally {
            session.close();
        }
    }
    
    /**
    * 
    * persists a data map composed of AlignedData and AnnotatedData
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param dataMap
    * @since 1.0
    */
    public static void save(Map<AlignedData, List<AnnotatedData>> dataMap){
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;           
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            for(AlignedData ad : dataMap.keySet()){
                for (AnnotatedData annotData : dataMap.get(ad)){
                    session.save(annotData);
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
           
        } finally {
            session.close();
        }
    }
    
    /**
    * 
    * sets Sample status to analyzed when analysis is finished
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sr - SampleRun that has been analyzed
    * @since 1.0
    */
    public static void setAnalyzed(SampleRun sr){
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;           
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            sr.getsample().setStatus("analyzed");
            session.saveOrUpdate(sr.getsample());
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
           
        } finally {
            session.close();
        }
    }
    
    /**
    * 
    * checks if a file exists
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param filePath path to file
    * @return boolean
    * @since 1.0
    */
    public static boolean isFileExists(String filePath){
        File f = new File(filePath);
        return f.exists();
    }
    
    /**
    * 
    * saves the generated analysis commands to a file
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param commands - the generated analysis commands
    * @param outPath
    * @param firstLine - #!/bin/bash line at start of script
    * @param isExecutable
    * @since 1.0
    */
    public static void saveToFile(String commands, String outPath, String firstLine, boolean isExecutable){
        try{ 
            File f = new File(outPath);    
            f.setExecutable(isExecutable, false);
            f.setReadable(true, false);
            f.setWritable(true, false);
            if(!f.exists()){
                
                FileWriter fwr = new FileWriter(f);                
                if(firstLine != ""){
                    fwr.write(firstLine);
                }
                fwr.write(commands);
                fwr.flush();
                fwr.close();            
                //System.out.println(f.getPath());
            }
            //File f2 = new File(outPath);
            //f2.setExecutable(isExecutable, true);  
            
            
           
           
        }catch(UnsupportedOperationException uoe){
            uoe.printStackTrace(); 
        }catch(IOException uoe){}
    }

}
