package it.iit.genomics.cru.mindex;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)MindexHelper.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class reads complete set of index sequences from the database.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class MindexHelper {
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init MindexHelper");
        }
    }
    
    /**
 * Reads set of index sequences from the database.
 *
 * @author Heiko Muller
 * @return List<SequencingIndex> - all index sequences currently in use by the facility.
 * @since 1.0
 */
    public static List<SequencingIndex> getIndexList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SequencingIndex> indexlist = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from SequencingIndex");
            indexlist = (List<SequencingIndex>) q.list();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return indexlist;
    }
    
}
