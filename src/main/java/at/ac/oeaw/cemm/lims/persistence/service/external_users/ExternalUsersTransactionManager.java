/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service.external_users;

import at.ac.oeaw.cemm.lims.persistence.service.*;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtilExternal;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ExternalUsersTransactionManager {   

    static void doInTransaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static abstract class TransactionCallable<T> {
        public abstract T execute() throws Exception;
    }

    public static <T> T doInTransaction(TransactionCallable<T> callable) throws Exception {
        T result = null;
        Session session = null;
        Transaction txn = null;
        try {
            session = HibernateUtilExternal.getSessionFactory().getCurrentSession();
            txn = session.beginTransaction();
            result = callable.execute();
            txn.commit();
        } catch (Exception e) {
            if ( txn != null && txn.isActive() ) txn.rollback();
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        
        return result;
    }
}