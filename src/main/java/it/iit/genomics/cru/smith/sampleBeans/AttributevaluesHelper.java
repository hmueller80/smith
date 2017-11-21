package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Attribute;
import it.iit.genomics.cru.smith.entity.AttributeValue;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)AttributevaluesHelper.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations on attribute value pairs.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class AttributevaluesHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init AttributevaluesHelper");
        }
    }
    
    
    /**
    * Returns attribute value pairs for a given sample.
    *
    * @author Francesco Venco
    * @param sampleId
    * @return List<AttributeValue>
    * @version 1.0
    * @since 1.0
    */
    @SuppressWarnings("unchecked")
    public static List<AttributeValue> getAttributvalues(int sampleId) {

        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<AttributeValue> pairsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from AttributeValue as at where at.sample.id = '" + sampleId + "'");
            pairsList = (List<AttributeValue>) q.list();
            //for for lazy initialization
            for (int i = 0; i < pairsList.size(); i++) {
                Attribute t = pairsList.get(i).getAttribute();
                t.getName();
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
        return pairsList;

    }

}
