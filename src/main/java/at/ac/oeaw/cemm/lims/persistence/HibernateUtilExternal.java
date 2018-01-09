package at.ac.oeaw.cemm.lims.persistence;

import at.ac.oeaw.cemm.lims.util.Preferences;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

 public class HibernateUtilExternal {

    private static final SessionFactory sessionFactory;

    static {
        if(Preferences.getVerbose()){
            System.out.println("init HibernateUtil");
        }
        try {
            sessionFactory = new AnnotationConfiguration().configure("hibernate_external.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
