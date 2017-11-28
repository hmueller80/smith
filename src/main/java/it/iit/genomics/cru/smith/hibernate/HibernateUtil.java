package it.iit.genomics.cru.smith.hibernate;

import it.iit.genomics.cru.smith.defaults.Preferences;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

 public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        if(Preferences.getVerbose()){
            System.out.println("init HibernateUtil");
        }
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
