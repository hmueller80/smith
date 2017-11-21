package it.iit.genomics.cru.smith.hibernate;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateAppListener implements ServletContextListener {

    /* Application Startup Event */
    public void contextInitialized(ServletContextEvent ce) {
        if(Preferences.getVerbose()){
            System.out.println("init HibernateAppListener");
        }

        if (true) {

            try {
                Class.forName("hibernate.HibernateUtil").newInstance();
                Logger.getLogger(this.getClass().getName()).info(
                        "Hibernate session factory created.");
            } catch (Exception e) {
                // e.printStackTrace();
                Logger.getLogger(this.getClass().getName()).severe(
                        "FAILED HibernateAppListener.contextInitialize()");
            }
        } else {
            Logger.getLogger(this.getClass().getName())
                    .info("Hibernate session factory NOT created because annotations will be loaded directly from file system.");
        }
    }

    /* Application Shutdown Event */
    public void contextDestroyed(ServletContextEvent ce) {
    }
}
