package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.CollaborationId;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.sampleBeans.SampleDataModel;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)SelectSamplesForProjectBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for selecting project samples.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "selectSamplesForProjectBean")
@ViewScoped
public class SelectSamplesForProjectBean implements Serializable {

    private SampleDataModel samples;
    private Sample[] selected;
    private int projectId;
    private Project project;
    private LoggedUser lu;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SelectSamplesForProjectBean() {
        if(Preferences.getVerbose()){
            System.out.println("init SelectSamplesForProjectBean");
        }
        //lu = new LoggedUser();
        
        FacesContext context = FacesContext.getCurrentInstance();
        lu = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class));  
        String pid = (String) context.getExternalContext().getRequestParameterMap().get("pid");
        if (pid != null) {
            projectId = Integer.parseInt(pid);
        } // this should never happen and will return an error
        else {
            projectId = -1;
        }

        initList();
    }

    /**
    * init
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    private void initList() {
        //System.out.println("Init the sample list");
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
        
            List<Sample> allSampleSet;
            if (lu.getIsAdmin()) {
                Query q = session.createQuery("from Sample order by sam_id desc");
                allSampleSet = (List<Sample>) q.list();
                //allSampleSet = SampleHelper.getSampleList();
            } else if (lu.getIsLeader()) {
                Query q = session.createQuery("from Sample as s where s.user.pi='" + lu.getPi() + "' order by sam_id desc");
                allSampleSet = (List<Sample>) q.list();
                //allSampleSet = SampleHelper.getSampleListByGroupId(lu.getPi());
            } else {
                Query q = session.createQuery("from Sample as s where s.user.id='" + lu.getUserId() + "' order by sam_id desc");
                allSampleSet = (List<Sample>) q.list();
                //allSampleSet = SampleHelper.getSampleList(lu.getUserId().intValue());
            }
            //System.out.println("All samples for user: " + allSampleSet);
        
        
            
            ArrayList<Sample> temp = new ArrayList<Sample>();
            for (Sample s : allSampleSet) {
                Set<Project> conPro = s.getProjects();
                boolean toBeRemoved = false;
                for (Project p : conPro) {
                    if (p.getId().intValue() == projectId) {
                        toBeRemoved = true;
                        break;
                    }
                }
                if (!toBeRemoved) {
                    temp.add(s);
                }
            }
            samples = new SampleDataModel(temp);
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }
        
        
        
    }

    /**
    * Adds selected samples to project.
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    public void addSelected() {
        //System.out.println("Add selected called for " + selected.length + " samples");
        for (Sample s : selected) {
            addSampleToProject(s);
            //SelectSamplesForProjectBean.addCollaborationForUser(s.getUser(), project);
            //System.out.println("next");
        }
        initList();
        selected = new Sample[0];
    }

    /**
    * Adds a sample to a project.
    * 
    * @author Francesco Venco
    * @param s - a sample
    * @since 1.0
    */
    public void addSampleToProject(Sample s) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            //System.out.println("Add sample " + s.getId() + " to project " + this.projectId);
            tx = session.beginTransaction();
            //System.out.println("load Sample");
            Sample loadedSample = (Sample) session.load(Sample.class, s.getId());
            //System.out.println("load Project");
            Project loadedProject = (Project) session.load(Project.class, new Integer(projectId));
            //System.out.println("Adding sample to project");
            loadedProject.getSamples().add(loadedSample);
            
            //System.out.println("Adding project to sample");
            loadedSample.getProjects().add(loadedProject);

            /*
             System.out.println("Add collaborator: " + loadedSample.getUser().getId() + " to project " + projectId);
             Collaboration newCollaboration = new Collaboration();
             CollaborationId id = new CollaborationId();        
             id.setProjectId(new Integer(projectId));
             id.setUserId(loadedSample.getUser().getId());
             newCollaboration.setId(id);            
             loadedProject.getCollaborations().add(newCollaboration);

             byte permission = 0;
             newCollaboration.setModifyPermission(permission);
             session.save(newCollaboration);*/
            //System.out.println("Saving loaded project");
            session.saveOrUpdate(loadedProject);
            //System.out.println("Saving loaded sample");
            session.saveOrUpdate(loadedSample);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }

        User user = s.getUser();
        System.out.println("Add collaborator: " + user.getId() + " to project " + projectId);
        session = HibernateUtil.getSessionFactory().openSession();
        tx = null;
        try {
            System.out.println("Add collaborator: " + user.getId() + " to project " + projectId);
            tx = session.beginTransaction();
//          
            Collaboration newCollaboration = new Collaboration();
            CollaborationId id = new CollaborationId();
//          
            id.setProjectId(new Integer(projectId));
            id.setUserId(user.getId());

            newCollaboration.setId(id);
            Project laodedproject = (Project) session.load(Project.class, new Integer(projectId));
            laodedproject.getCollaborations().add(newCollaboration);

            byte permission = 0;
            newCollaboration.setModifyPermission(permission);
            session.save(newCollaboration);
            tx.commit();
        }catch( org.hibernate.NonUniqueObjectException nuoe){
            //exception will be thrown when more than one sample from the same user is added to the project
            System.out.println("Collaboration exists");
        }catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        }finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }

    }

    /**
    * Adds a collaboration for a user regarding a given project.
    * 
    * @author Francesco Venco
    * @param user
    * @param projectId
    * @since 1.0
    */
    public static void addCollaborationForUser(User user, int projectId) {
        //System.out.println("Add collaborator: " + user.getId() + " to project " + projectId);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            //System.out.println("Add collaborator: " + user.getId() + " to project " + projectId);
            tx = session.beginTransaction();
//            User loadedUser = (User) session.load(User.class, user.getId());
            Collaboration newCollaboration = new Collaboration();
            CollaborationId id = new CollaborationId();
//            id.setColProjectId(prId);
//            id.setCollaboratorId(u.getId());
//            Project project = 

//            Project project = (Project) session.createQuery("from Project where id = '"+projectId+"'").list().get(0);
            id.setProjectId(new Integer(projectId));
            id.setUserId(user.getId());

            newCollaboration.setId(id);
            Project project = (Project) session.createQuery("from Project where id = '" + projectId + "'").list().get(0);
            project.getCollaborations().add(newCollaboration);

            byte permission = 0;
            newCollaboration.setModifyPermission(permission);
            session.save(newCollaboration);
            tx.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            tx.rollback();
        } finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }
    }

    /**
    * Adds a collaboration for a user regarding a given project.
    * 
    * @author Francesco Venco
    * @param user
    * @param project
    * @since 1.0
    */
    public static void addCollaborationForUser(User user, Project project) {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            //User loadedUser = (User) session.load(User.class, u.getUserId());
            Collaboration newCollaboration = new Collaboration();
            CollaborationId id = new CollaborationId();
//            id.setColProjectId(prId);
//            id.setCollaboratorId(u.getId());
//            Project project = 

            id.setProjectId(project.getId());
            id.setUserId(user.getId());

            newCollaboration.setId(id);
            byte permission = 0;
            newCollaboration.setModifyPermission(permission);
            session.save(newCollaboration);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
        } finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }
    }

    /**
    * Getter for samples.
    * 
    * @author Francesco Venco
    * @return SampleDataModel
    * @since 1.0
    */
    public SampleDataModel getSamples() {
        return samples;
    }

    /**
    * Setter for samples.
    * 
    * @author Francesco Venco
    * @param samples
    * @since 1.0
    */
    public void setSamples(SampleDataModel samples) {
        this.samples = samples;
    }

    /**
    * Getter for selected samples.
    * 
    * @author Francesco Venco
    * @return Sample[]
    * @since 1.0
    */
    public Sample[] getSelected() {
        return selected;
    }

    /**
    * Setter for selected samples.
    * 
    * @author Francesco Venco
    * @param selected
    * @since 1.0
    */
    public void setSelected(Sample[] selected) {
        this.selected = selected;
    }

    /**
    * Getter for selected projectId.
    * 
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getProjectId() {
        return projectId;
    }

     /**
    * Setter for projectId.
    * 
    * @author Francesco Venco
    * @param projectId
    * @since 1.0
    */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
