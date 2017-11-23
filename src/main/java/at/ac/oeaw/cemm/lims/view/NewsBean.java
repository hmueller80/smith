package at.ac.oeaw.cemm.lims.view;

import it.iit.genomics.cru.smith.entity.News;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)NewsBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for News tab.
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "newNewsBean")
@ViewScoped
public class NewsBean implements Serializable{
    
    List<News> news = null;
    String header;
    String body;

    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
   
    @PostConstruct
    public void init(){
        getNewsList();
    }
    
    /**
     * Reads the news from database and inits news field.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void getNewsList() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        List<News> newsList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from News order by news_id desc");
            newsList = (List<News>) q.list();
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
        news = newsList;        
    }
    
    /**
     * Submits news to the database.
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public void submit() {
        if(roleManager.hasNewsPermission()){
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                News n = new News();
                n.setHeader(header);
                n.setBody(body);
                n.setDate(new Date(System.currentTimeMillis()));
                n.setId(news.size() + 1);
                session.save(n);
                tx.commit();
            } catch (RuntimeException e) {
                e.printStackTrace();
                tx.rollback();
            } finally {
                session.close();
            }
        }  
    }
    
    /**
     * Getter for news item.
     *
     * @author Heiko Muller
     * @return List<News>
     * @since 1.0
     */
    public News getNewsItem(int idx) {
        if(idx < news.size() ){
            return news.get(idx);
        }
        return new News();
    }

    /**
     * Getter for news field.
     *
     * @author Heiko Muller
     * @return List<News>
     * @since 1.0
     */
    public List<News> getNews() {
        return news;
    }

    /**
     * Getter for header field.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getHeader() {
        return header;
    }

    /**
     * Setter for header field.
     *
     * @author Heiko Muller
     * @param header
     * @since 1.0
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Getter for body field.
     *
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public String getBody() {
        return body;
    }

    /**
     * Setter for body field.
     *
     * @author Heiko Muller
     * @param body - the news text
     * @since 1.0
     */
    public void setBody(String body) {
        this.body = body;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
  
}
