package it.iit.genomics.cru.smith.reagentsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.Date;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)ReagentHelper.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations on reagent table.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class ReagentHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init ReagentHelper");
        }
    }

    /**
     * Returns list of reagents.
     *
     * @author Francesco Venco
     * @return List<Reagent>
     * @since 1.0
     */
    public static List<Reagent> getReagentsList() {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Reagent> reagentsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Reagent");
            reagentsList = (List<Reagent>) q.list();
            //for for lazy initialization
            for (int i = 0; i < reagentsList.size(); i++) {
                Reagent r = reagentsList.get(i);
                User u1 = r.getUserByOperatorUserId();
                User u2 = r.getUserByOwnerId();
                r.getSamplerunsForClustergenerationReagentCode().size();
                r.getSamplerunsForSampleprepReagentCode().size();
                r.getSamplerunsForSequencingReagentCode().size();
                String login1 = u1.getLogin();
                String login2 = u2.getLogin();
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return reagentsList;
    }
    
    /**
     * Returns reagent by its barcode.
     *
     * @author Francesco Venco
     * @param barcode
     * @return Reagent
     * @since 1.0
     */
    public static Reagent getReagentsByBarcode(String barcode) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<Reagent> reagentsList = null;
        Reagent result = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Reagent where reagentbarcode='" + barcode + "'");
            reagentsList = (List<Reagent>) q.list();
            //for for lazy initialization
            if(reagentsList != null && reagentsList.size() > 0){
                result = reagentsList.get(0);
            }
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
    
    /**
     * Returns reagent by its barcode.
     *
     * @author Francesco Venco
     * @param reagent
     * @since 1.0
     */
    public static void saveOrUpdateReagent(Reagent reagent) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(reagent);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.close();
            }
        }
    }
    
    public static Reagent getDefaultReagent(){
        Reagent result = new Reagent();
        result.setApplication("default");
        result.setCatalogueNumber("default");
        result.setCostCenter("default");
        result.setExpirationDate(new Date(System.currentTimeMillis()));
        result.setInstitute("default");
        result.setPrice(0);
        return result;
    }

}
