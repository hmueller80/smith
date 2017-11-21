package it.iit.genomics.cru.smith.news;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.News;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
@ManagedBean(name = "newsBean")
@ViewScoped
public class NewsBean implements Serializable{
    
    List<News> news = null;
    String header;
    String body;
    boolean hasNewsPermission = false;
    User user;

    /**
     * Bean constructor
     *
     * @author Heiko Muller
     * @since 1.0
     */
    public NewsBean() {
        if(Preferences.getVerbose()){
            System.out.println("init NewsBean");
        } 
        FacesContext context = FacesContext.getCurrentInstance();
        //loggedUserBean = new LoggedUser();
        String userLogin = context.getExternalContext().getRemoteUser();
        User user = UserHelper.getUserByLoginName(userLogin);
        hasNewsPermission = getHasNewsPermission();
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
        if(hasNewsPermission){
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

    /**
     * Getter for hasNewsPermission field.
     *
     * @author Heiko Muller
     * @return boolean
     * @since 1.0
     */
    public boolean getHasNewsPermission() {
        if(user != null && (user.getUserRole().equals(Preferences.ROLE_TECHNICIAN) || user.getUserRole().equals(Preferences.ROLE_ADMIN))){
            hasNewsPermission = true;
            return true;
        }else{
            hasNewsPermission = false;
            return false;
        }
    }

    /**
     * Setter for hasNewsPermission field.
     *
     * @author Heiko Muller
     * @param newsPermission
     * @since 1.0
     */
    public void setHasNewsPermission(boolean newsPermission) {
        this.hasNewsPermission = newsPermission;
    }

    
    
    
    
    
}
