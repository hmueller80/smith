package it.iit.genomics.cru.smith.news;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.News;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)NewsHelper.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Performs database operations on news table.
 *
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
public class NewsHelper {

    static {
        if (Preferences.getVerbose()) {
            System.out.println("init NewsHelper");
        }
    }

    /**
     * Generates news about finished run and submits it to database.
     *
     * @author Yuriy Vaskin
     * @param folder - the run folder name
     * @since 1.0
     */
    public static void publishRunFinished(String folder) {
        News news = new News();
        news.setId(getId());
        news.setDate(new Date(System.currentTimeMillis()));
        //System.out.println(news.getDate());
        news.setHeader(folder);
        news.setBody("Run " + folder + " has finished.");
        publish(news);
    }

    /**
     * Generates news about finished fastq data generation and submits it to
     * database.
     *
     * @author Yuriy Vaskin
     * @param folder - the run folder name
     * @since 1.0
     */
    public static void publishFastqGenerated(String folder) {
        News news = new News();
        news.setId(getId());
        news.setDate(new Date(System.currentTimeMillis()));
        news.setHeader(folder);
        news.setBody("FASTQ for " + folder + " has been generated.");
        publish(news);
    }

    /**
     * Generates news about finished upstream analysis and submits it to
     * database.
     *
     * @author Yuriy Vaskin
     * @param folder - the run folder name
     * @since 1.0
     */
    public static void publishUpstreamPerformed(String folder) {
        News news = new News();
        news.setId(getId());
        news.setDate(new Date(System.currentTimeMillis()));
        news.setHeader(folder);
        news.setBody("Upstream analysis for " + folder + " has been performed.");
        publish(news);
    }

    /**
     * Generates news about starting fastq analysis and submits it to database.
     *
     * @author Heiko Muller
     * @param folder - the run folder name
     * @since 1.0
     */
    public static void publishFastqAnalysisStarted(String folder) {
        News news = new News();
        news.setId(getId());
        news.setDate(new Date(System.currentTimeMillis()));
        news.setHeader(folder);
        news.setBody("Fastq analysis for " + folder + " has started.");
        publish(news);
    }

    /**
     * Generates news about error during BclToFastq configuration setup and
     * submits it to database.
     *
     * @author Heiko Muller
     * @param folder - the run folder name
     * @since 1.0
     */
    public static void publishBclToFastqConfigError(String folder) {
        News news = new News();
        news.setId(getId());
        news.setDate(new Date(System.currentTimeMillis()));
        news.setHeader(folder);
        news.setBody("BclToFastq config error for " + folder + ". Analysis has not been launched.");
        publish(news);
    }

    /**
     * Generates news about Fastq data delivery submits it to database.
     *
     * @author Heiko Muller
     * @param folder - the run folder name
     * @since 1.0
     */
    public static void publishFastqDataHaveBeenDelivered(String folder) {
        News news = new News();
        news.setId(getId());
        news.setDate(new Date(System.currentTimeMillis()));
        news.setHeader(folder);
        news.setBody("Fastq data for " + folder + " have been delivered.");
        publish(news);
    }

    /**
     * return news id for next news.
     *
     * @author Yuriy Vaskin
     * @return int - the id for the next news object to be persisted.
     * @since 1.0
     */
    private static int getId() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        int res = -1;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from News order by news_id desc");
            List<News> temp = q.list();
            if (temp != null && temp.size() > 0) {
                res = temp.get(0).getId() + 1;
            }
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        return res;
    }

    /**
     * Persists a News object if another News object with the same news body is
     * not already present. This check avoids news duplication
     *
     * @author Yuriy Vaskin
     * @author Heiko Muller
     * @param news - the News object to be written to database.
     * @since 1.0
     */
    public static void publish(News news) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from News where body='" + news.getBody() + "'");
            List<News> temp = q.list();
            if (temp == null) {
                session.save(news);
            } else if (temp.size() == 0) {
                session.save(news);
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
     * Deletes a news object.
     *
     * @author Heiko Muller
     * @param news - the News object to be written to database.
     * @since 1.0
     */
    public static void delete(News news) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            session.delete(news);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();

        } finally {
            session.close();
        }
    }

    /**
     * Refreshes database connection to prevent timeout in case auto-reconnect
     * fails.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public static void refresh() {
        org.hibernate.Session session = null;
        News news = null;
        Transaction ta = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            ta = session.beginTransaction();

            News n1 = new News();
            n1.setDate(new Date(System.currentTimeMillis()));
            n1.setBody("body");
            n1.setHeader("header");
            session.save(n1);
            session.delete(n1);
            ta.commit();

        } catch (HibernateException rbe) {
            if (ta != null) {
                ta.rollback();
            }
            rbe.printStackTrace();

        } finally {
            if (session != null) {
                session.flush();
                session.close();
            }
        }

    }

    /**
     * Tests if news with body newsbody exists.
     *
     * @author Heiko Muller
     * @param newsbody
     * @return boolean
     * @since 1.0
     */
    public static boolean newsExists(String newsbody) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean result = false;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from News where body='" + newsbody + "'");
            List<News> temp = q.list();
            if (temp != null && temp.size() > 0) {
                result = true;
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();

        } finally {
            session.close();
        }
        return result;
    }

    public static void resetRun(int runid) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        String fail = "";
        try {
            tx = session.beginTransaction();
            List<SampleRun> runlist = RunHelper.getRunsList(runid);
            String runfolder = "";
            if (runlist.size() == 1) {
                SampleRun sr = runlist.get(0);
                runfolder = sr.getRunFolder();

                Query q = session.createQuery("from News where header='" + runfolder + "'");
                List<News> nl = q.list();
                if (nl.size() > 1) {
                    for (News n : nl) {
                        //delete all news regarding analysis steps except the one that says run finished
                        if (!n.getBody().equals("Fastq analysis for " + runfolder + " has started.")) {
                            session.delete(n);
                        }
                    }
                    NgsLimsUtility.setSuccessMessage("ResetRunButton", "ResetRun", "Reset run succeeded", "Reset run succeeded");
                }
            }
            NgsLimsUtility.setFailMessage("ResetRunButton", "ResetRun", "Reset run failed", "Reset run failed");

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();

        } finally {
            session.close();
        }
    }
}
