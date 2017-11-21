package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.util.TimerTask;
import java.util.logging.Level;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.logging.Logger;

/**
 * @(#)DailyTask.java 
 * 20 JUN 2014 
 * Copyright 2014 Computational Research Unit of IIT@SEMM. 
 * All rights reserved. Use is subject to MIT license terms.
 * 
 * Class serves to trigger a new run of the AnalysisManager
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
public class DailyTask extends TimerTask {

    AnalysisManager am;
    
    Session session = null;
    Transaction ta = null;

    /**
     * triggers start of the AnalysisManager
     *
     * @author Heiko Muller
     * @version 1.0
     * @since 1.0
     */
    public void run() {
        //Toolkit.getDefaultToolkit().beep();
        if(Preferences.getVerbose()){
            System.out.println("Timer task running");
        }
        //Logger.getLogger(null);
        Logger.getLogger(DailyTask.class.getName()).log(Level.INFO, null, "AnalysisManager running");
        System.out.println("AnalysisManager running");
        try {
            
            am.run();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        //NewsHelper.refresh();
        /*
        try {
                        //System.out.println("daily task");
			session = HibernateUtil.getSessionFactory().openSession();
			ta = session.beginTransaction();
			//Query query = sess.createQuery("FROM News");
                        
			//List<News> cs = query.list();
                        News n = new News();
                        n.setDate(new Date(System.currentTimeMillis()));
                        n.setHeader("H");
                        n.setBody("B");
			//News cs1 = cs.get(0);
			//cs1.setId(100000);
			session.save(n);
			session.delete(n);
			ta.commit();

		} catch (HibernateException rbe) {
			if (ta != null) {
				ta.rollback();
			}
			
			rbe.printStackTrace();

		} finally {
                    if(session != null){
			session.flush();
			session.close();
                    }
		}
                */
        
        
    }

    /**
     * sets the AnalysisManager manager object to be triggered
     *
     * @author Heiko Muller
     * @param man - Analysis manager
     * @since 1.0
     */
    public void setAnalysisManager(AnalysisManager man) {
        am = man;
    }
}

/*
class MyAuthenticator extends Authenticator {

	public PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication ("admin", "admin".toCharArray());
		return new PasswordAuthentication("hmuller", "sugar".toCharArray());
		// return new PasswordAuthentication ("hmuller", "sugar".toCharArray());
	}
}
*/
