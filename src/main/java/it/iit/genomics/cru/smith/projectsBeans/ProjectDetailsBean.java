package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
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
import java.util.ResourceBundle;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)ProjectDetailsBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for displaying project details.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "projectDetailsBean")
@ViewScoped
public class ProjectDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<Sample> projectsamples;
    private SampleDataModel projectSamplesDM;
    private CollaborationDataModel collaborations;
    private Collaboration selectedCollaboration;
    protected int projectId;
    protected User user;
    protected String name;
    protected String description;
    protected User loggedUser;
    protected String userName;
    protected int newSampleId;
    protected int delSampleId;
    protected String formId;
    private boolean loadingSuccesfull;
    private Sample[] selectedSamples;
    protected ResourceBundle bundle;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public ProjectDetailsBean() {
        if(Preferences.getVerbose()){
            System.out.println("init ProjectDetailsBean");
        }
        
        init();
    }

    /**
    * init
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    protected void init() {

        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        formId = "";
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUser = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)).getLoggedUser(); 
        String pid = (String) context.getExternalContext().getRequestParameterMap().get("pid");
        if (pid != null) {
            projectId = Integer.parseInt(pid);
        } // this should never happen and will return an error
        else {
            projectId = -1;
        }

        //loggedUser = new LoggedUser().getLoggedUser();

        loadingSuccesfull = load();
        projectSamplesDM = new SampleDataModel(projectsamples);
        System.out.println("New bean for project details has been created for " + projectsamples.size() + " samples");
    }

    /**
    * Tests if user has permission to load a project.
    * 
    * @author Francesco Venco
    * @param p - a Project
    * @return boolean - true if logged user has permission to load project data, false otherwise.
    * @since 1.0
    */
    public boolean loggedUserHasLoadPermission(Project p) {
        if (loggedUser.getPi().equals(p.getUser().getPi())) {
            return true;
        }
        if (loggedUser.getUserRole().equals(Preferences.ROLE_ADMIN)) {
            return true;
        }
        System.out.println("Check collaborations; logged user id :" + loggedUser.getId());
        ArrayList<Collaboration> cols = new ArrayList(p.getCollaborations());
        System.out.println("Number of collaborators for project " + cols.size());
        for (Collaboration c : cols) {
            System.out.println("Check collaborations; user id :" + c.getUser().getId());
            if (c.getUser().getId().equals(loggedUser.getId()));
            return true;
        }

        return false;
    }

    /**
    * Loads project.
    * 
    * @author Francesco Venco
    * @return boolean.
    * @since 1.0
    */
    protected boolean load() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Project loadedProject;
        Transaction tx = null;
        boolean toRet = true;
        try {
            tx = session.beginTransaction();
            loadedProject = (Project) session.load(Project.class, projectId);
            //check for the permission
            //TODO more complex!!!
            if (!loggedUserHasLoadPermission(loadedProject)) {
                projectId = 0;
                throw new RuntimeException("permission denied");
            }

            setCurrentProjectData(loadedProject);
            //lazy init
            for (int i = 0; i < projectsamples.size(); i++) {
                int id = projectsamples.get(i).getId();
                String s = projectsamples.get(i).getName();
                String uname = projectsamples.get(i).getUser().getLogin();
            }
            for (Collaboration c : this.collaborations) {
                c.getUser().getLogin();
            }
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            NgsLimsUtility.setFailMessage(formId, "", e.getMessage(), "fail");
            toRet = false;
        } finally {
            session.close();
        }
        return toRet;
    }

    /**
    * Removes selected samples from project.
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    public void removeSelectedSamples() {
        for (Sample s : selectedSamples) {
            removeSample(s.getId());
        }
        load();
        projectSamplesDM = new SampleDataModel(projectsamples);
    }

    /**
    * Removes a selected samples from project.
    * 
    * @author Francesco Venco
    * @param sampleId
    * @since 1.0
    */
    public void removeSample(int sampleId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("Removing from project sample " + sampleId);
        Transaction tx = null;
        Project loadedProject;
        try {
            tx = session.beginTransaction();
            loadedProject = (Project) session.load(Project.class, projectId);
            Sample loadedSample = (Sample) session.load(Sample.class, sampleId);
            Set<Sample> samSet = loadedProject.getSamples();
            Set<Project> projSet = loadedSample.getProjects();
            samSet.remove(loadedSample);
            projSet.remove(loadedProject);

            session.saveOrUpdate(loadedProject);
            session.saveOrUpdate(loadedSample);
            tx.commit();
            System.out.println("...committed");
        } catch (RuntimeException e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    /**
    * Sets current project data for display.
    * 
    * @author Francesco Venco
    * @param p - the project to be displayed.
    * @since 1.0
    */
    protected void setCurrentProjectData(Project p) {

        this.description = p.getDescription();
        this.name = p.getName();
        this.user = p.getUser();
        this.userName = user.getUserName();
        this.projectsamples = new ArrayList(p.getSamples());
        List<Collaboration> temp = new ArrayList(p.getCollaborations());
        this.setCollaborations(new CollaborationDataModel(temp));
    }

    /**
    * Performs form validation.
    * 
    * @author Francesco Venco
    * @return boolean - true if project name is present and project description has no more that 1000 characters, false otherwise
    * @since 1.0
    */
    protected boolean formIsCorrect() {
        boolean toRet = true;
        if (this.description.length() >= 1000) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "", "Text is too long", "");
        }
        if (this.name.equals("")) {
            toRet = false;
            NgsLimsUtility.setFailMessage(formId, "name", "Missing name", "A name is required");
        }
        return toRet;
    }

    /**
    * Tests if logged user has permission to delete project.
    * 
    * @author Francesco Venco
    * @return boolean - true if user has permission to delete project, false otherwise
    * @since 1.0
    */
    public boolean hasDeletePermission() {
        if (!loadingSuccesfull) {
            return false;
        }
        return (loggedUser.getPi().equals(user.getPi())
                && loggedUser.getUserRole().equals(Preferences.ROLE_GROUPLEADER)) || loggedUser.getUserRole().equals(Preferences.ROLE_ADMIN);
    }

    /**
    * Deletes project corresponding to current projectId.
    * 
    * @author Francesco Venco
    * @return String - a redirect to the projects search page
    * @since 1.0
    */
    public String delete() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            // load the project in this session
            Project p = (Project) session.load(Project.class, projectId);

            for (Sample sample : p.getSamples()) {
                sample.getProjects().remove(p);
            }
            session.delete(p);
            tx.commit();
            NgsLimsUtility.setWarningMessage(formId, "", "Project deletion successful", "");
            return "projectsSearch?faces-redirect=true";

        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
            String fail = e.getMessage();
            NgsLimsUtility.setFailMessage(formId, "", "Error: " + fail, "");
            return null;
        } finally {
            session.close();
        }
    }

    /**
    * Deletes project corresponding to current projectId.
    * 
    * @author Francesco Venco
    * @param c
    * @return String - a redirect to the projects search page
    * @since 1.0
    */
    public String getPermissionForUser(Collaboration c) {
        if (c != null && c.getModifyPermission() > 0) {
            return bundle.getString("project.permission.full");
        } else {
            return bundle.getString("project.permission.see");
        }
    }

    /**
    * Changes the permission for a selected collaboration.
    * 
    * @author Francesco Venco
    * @since 1.0
    */
    public void changePermissionForSelectedCollaboration() {
        if(selectedCollaboration != null){
            Session session = HibernateUtil.getSessionFactory().openSession();;
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                // load the collaboration in this session
                CollaborationId id = selectedCollaboration.getId();
                Collaboration c = (Collaboration) session.load(Collaboration.class, id);
                if (c.getModifyPermission() > 0) {
                    c.setModifyPermission((byte) 0);
                } else {
                    c.setModifyPermission((byte) 1);
                }
                tx.commit();

            } catch (RuntimeException e) {
                tx.rollback();
                e.printStackTrace();
                String fail = e.getMessage();
                NgsLimsUtility.setFailMessage(formId, "", "Error: " + fail, "");
            } finally {
                session.close();
            }
            load();
            projectSamplesDM = new SampleDataModel(projectsamples);
        }
    }

    /**
    * Deletes a collaboration.
    * 
    * @author Francesco Venco
    * @return String - a redirect to the project details page
    * @since 1.0
    */
    public String deleteCollaboration() {
        if(selectedCollaboration != null){
            Session session = HibernateUtil.getSessionFactory().openSession();
            int delColUserId = selectedCollaboration.getUser().getId();
            Transaction tx = null;
            try {
                System.out.println("delete collaboration with user " + selectedCollaboration.getUser().getLogin());
                tx = session.beginTransaction();
                // load the collaboration in this session
                CollaborationId id = selectedCollaboration.getId();
                Collaboration c = (Collaboration) session.load(Collaboration.class, id);
                session.delete(c);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                e.printStackTrace();
                String fail = e.getMessage();
                NgsLimsUtility.setFailMessage(formId, "", "Error: " + fail, "");
            } finally {
                session.close();
            }
            //remove also all the samples connected to the user
            //not part of the project anymore
            for (Sample s : projectsamples) {
                if (s.getUser().getId().equals(delColUserId)) {
                    removeSample(s.getId());
                }
            }
            //load();
        }
        return "projectDetails?pid=" + projectId + " faces-redirect=true";
    }

    /**
    * Getter for projectId.
    * 
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getProjectId() {
        return this.projectId;
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

    /**
    * Getter for user.
    * 
    * @author Francesco Venco
    * @return User
    * @since 1.0
    */
    public User getUser() {
        return this.user;
    }

    /**
    * Setter for user.
    * 
    * @author Francesco Venco
    * @param user
    * @since 1.0
    */
    public void setUser(User user) {
        this.user = user;
    }

    /**
    * Getter for project name.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getName() {
        return this.name;
    }

    /**
    * Setter for name.
    * 
    * @author Francesco Venco
    * @param name
    * @since 1.0
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Getter for description.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getDescription() {
        return this.description;
    }

    /**
    * Setter for description.
    * 
    * @author Francesco Venco
    * @param description
    * @since 1.0
    */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Getter for userName.
    * 
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getUserName() {
        return this.userName;
    }

    /**
    * Getter for projectsamples.
    * 
    * @author Francesco Venco
    * @return List<Sample>
    * @since 1.0
    */
    public List<Sample> getProjectsamples() {
        return this.projectsamples;
    }

    /**
    * Getter for newSampleId.
    * 
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getNewSampleId() {
        return this.newSampleId;
    }

    /**
    * Setter for newSampleId.
    * 
    * @author Francesco Venco
    * @param newSampleId
    * @since 1.0
    */
    public void setNewSampleId(int newSampleId) {
        this.newSampleId = newSampleId;
    }


    /**
    * Getter for collaborations.
    * 
    * @author Francesco Venco
    * @return CollaborationDataModel
    * @since 1.0
    */
    public CollaborationDataModel getCollaborations() {
        return collaborations;
    }

    /**
    * Setter for collaborations.
    * 
    * @author Francesco Venco
    * @param collaborations
    * @since 1.0
    */
    public void setCollaborations(CollaborationDataModel collaborations) {
        this.collaborations = collaborations;
    }

  
    /**
    * Getter for selectedSamples.
    * 
    * @author Francesco Venco
    * @return Sample[]
    * @since 1.0
    */
    public Sample[] getSelectedSamples() {
        return selectedSamples;
    }

    /**
    * Setter for selectedSamples.
    * 
    * @author Francesco Venco
    * @param selectedSamples
    * @since 1.0
    */
    public void setSelectedSamples(Sample[] selectedSamples) {
        this.selectedSamples = selectedSamples;
    }

  
    /**
    * Getter for projectSamplesDM.
    * 
    * @author Francesco Venco
    * @return SampleDataModel
    * @since 1.0
    */
    public SampleDataModel getProjectSamplesDM() {
        return projectSamplesDM;
    }

    /**
    * Setter for projectSamplesDM.
    * 
    * @author Francesco Venco
    * @param projectSamplesDM
    * @since 1.0
    */
    public void setProjectSamplesDM(SampleDataModel projectSamplesDM) {
        this.projectSamplesDM = projectSamplesDM;
    }

   
    /**
    * Getter for selectedCollaboration.
    * 
    * @author Francesco Venco
    * @return Collaboration
    * @since 1.0
    */
    public Collaboration getSelectedCollaboration() {
        return selectedCollaboration;
    }

    /**
    * Setter for selectedCollaboration.
    * 
    * @author Francesco Venco
    * @param selectedCollaboration
    * @since 1.0
    */
    public void setSelectedCollaboration(Collaboration selectedCollaboration) {
        this.selectedCollaboration = selectedCollaboration;
    }
}
