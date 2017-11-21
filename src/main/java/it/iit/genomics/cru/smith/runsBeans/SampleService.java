package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Application;
import java.util.List;
import java.util.UUID;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
 
/**
 * @(#)SampleService.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations needed by Mindex.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "sampleService")
@ApplicationScoped
public class SampleService implements Serializable{
     
    /**
    * Bean constructor
    *
    * @author Heiko Muller
    * @since 1.0
    */
    public SampleService(){
        if(Preferences.getVerbose()){
            System.out.println("init SampleService");
        }
        
    } 
    
    /**
    * Loads requested samples (sample state = requested)
    *
    * @author Heiko Muller
    * @return List<Sample>
    * @since 1.0
    */ 
    public List<Sample> loadRequestedSamples() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Sample where status = 'requested'");
            sampleList = (List<Sample>) q.list();
            //for for lazy initialization
          

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;
    }
    
    /**
    * Loads requested samples (sample state = requested) to be run with a given recipe.
    *
    * @author Heiko Muller
    * @param application
    * @return List<Sample>
    * @since 1.0
    */ 
    public List<Sample> loadRequestedSamplesByApplicationID(String application) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Application app = null;
            Query q = session.createQuery("from Application where applicationname = '" + application + "'");
            app = (Application)q.list().get(0);
            q = session.createQuery("from Sample where status = 'requested' and application_id='" + app.getApplicationId() + "'");
            sampleList = (List<Sample>) q.list();
            //for for lazy initialization
          

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return sampleList;
    }
    
    /**
    * Loads requested samples (sample state = requested) to be run with a given recipe and recipes with identical run properties (depth, length, read mode).
    *
    * @author Heiko Muller
    * @param application
    * @return List<Sample>
    * @since 1.0
    */ 
    public List<Sample> loadRequestedSamplesByMatchingApplication(String application) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = new ArrayList<Sample>();
        Application app = loadRecipe(application);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if(app != null){
                //System.out.println("Selected application " + app.getReadmode() + " " + app.getReadlength() + " " + app.getDepth());
                Query q = session.createQuery("from Sample where status = 'requested'");
                List<Sample> temp = q.list();
                for(Sample s : temp){                    
                    Application a = s.getApplication();
                    //System.out.println("Sample application " + a.getReadmode() + " " + a.getReadlength() + " " + a.getDepth());
                    if(a.isMatching(app)){                        
                        sampleList.add(s);
                    }
                }
            }else{
                System.out.println("loadRequestedSamplesByMatchingApplication: Application not found");
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
        
        return sampleList;
    }
    
    /**
    * Loads requested samples (sample state = requested) to be run with a given recipe and recipes with identical run properties (depth, length, read mode).
    *
    * @author Heiko Muller
    * @param application
    * @return List<Sample>
    * @since 1.0
    */ 
    public List<Sample> loadRequestedAndQueuedSamplesByMatchingApplication(String application) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sample> sampleList = new ArrayList<Sample>();
        Application app = loadRecipe(application);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if(app != null){
                //System.out.println("Selected application " + app.getReadmode() + " " + app.getReadlength() + " " + app.getDepth());
                Query q = session.createQuery("from Sample where status = 'requested' or status = 'queued'");
                List<Sample> temp = q.list();
                for(Sample s : temp){                    
                    Application a = s.getApplication();
                    //System.out.println("Sample application " + a.getReadmode() + " " + a.getReadlength() + " " + a.getDepth());
                    if(a.isMatching(app)){                        
                        sampleList.add(s);
                    }
                }
            }else{
                System.out.println("loadRequestedSamplesByMatchingApplication: Application not found");
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
        
        return sampleList;
    }
    
    /**
    * Loads recipes.
    *
    * @author Heiko Muller
    * @return List<Application>
    * @since 1.0
    */ 
    public List<Application> loadRecipes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Application> appList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Query q = session.createQuery("from Application");            
            appList = (List<Application>) q.list();
            //for for lazy initialization
          

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return appList;
    }
    
    /**
    * Loads recipe with given name.
    *
    * @author Heiko Muller
    * @param applicationname
    * @return Application
    * @since 1.0
    */ 
    public Application loadRecipe(String applicationname) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Application app = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Query q = session.createQuery("from Application where applicationname='" + applicationname + "'");            
            List<Application> temp = (List<Application>) q.list();
            if(temp!= null && temp.size() > 0){
                app = temp.get(0);
            }else{
                System.out.println("Load recipe: Application not found");
            }
            //for for lazy initialization
          

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return app;
    }
    
    /**
    * Loads recipe names.
    *
    * @author Heiko Muller
    * @return List<String>
    * @since 1.0
    */ 
    public List<String> loadRecipeNames() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Application> appList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Query q = session.createQuery("from Application");            
            appList = (List<Application>) q.list();
            //for for lazy initialization
          

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
        ArrayList<String> result = null;
        if(appList != null && appList.size() > 0){
            result = new ArrayList<String>();
            for(Application a : appList){
                result.add(a.getApplicationname());
            }
        }
        return result;
    }
    
    
    /**
    * Finds the index sequences employed in a set of samples.
    *
    * @author Heiko Muller
    * @param samplelist
    * @return ArrayList<SequencingIndex>
    * @since 1.0
    */ 
    public ArrayList<SequencingIndex> findSampleListIndices(List<Sample> samplelist) {        
        ArrayList<SequencingIndex> result = new ArrayList<SequencingIndex>();
        for(int i = 0; i < samplelist.size(); i++){
            SequencingIndex temp = samplelist.get(i).getSequencingIndexes();
            result.add(temp);              
        }        
        return result;
    }
    
    /**
    * Returns a random id.
    *
    * @author Heiko Muller
    * @return String
    * @since 1.0
    */ 
    private String getRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
 
}
