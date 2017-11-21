package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.userBeans.CollaboratorsHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;
import it.iit.genomics.cru.smith.userBeans.UserDataModel;
import it.iit.genomics.cru.smith.userBeans.UserHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @(#)AddCollaboratorBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for adding a collaborator.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ViewScoped
@ManagedBean(name = "addCollaboratorBean")
public class AddCollaboratorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserDataModel usList;
    private User[] selectedCollaborators;
    private int projectId;
    protected ResourceBundle bundle;
    private LoggedUser loggedUser;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public AddCollaboratorBean() {
        if(Preferences.getVerbose()){
            System.out.println("init AddCollaboratorBean");
        } 
        
        init();
    }

    /**
     * init
     *
     * @author Francesco Venco
     * @since 1.0
     */
    private void init() {
        bundle = ResourceBundle.getBundle("it.iit.genomics.cru.smith.Messages.Messages");
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUser = (LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class); 
        String pid = (String) context.getExternalContext().getRequestParameterMap().get("pid");
        if (pid != null) {
            setProjectId(Integer.parseInt(pid));
        } // this should never happen and will return an error
        else {
            setProjectId(-1);
        }

        //setLoggedUser(new LoggedUser());
        loadUserList();

    }

    /**
     * Loads user list
     *
     * @author Francesco Venco
     * @since 1.0
     */
    private void loadUserList() {
        List<User> allUsersSet;
        List<Collaboration> collaborations;
        List<User> finalList = new ArrayList<User>();
        allUsersSet = UserHelper.getUsersList();
        collaborations = CollaboratorsHelper.getCollaborationForProject(getProjectId());
        System.out.println("Collaborators in list: " + collaborations);

        for (User u : allUsersSet) {
            boolean toBeSkipped = false;
            for (Collaboration c : collaborations) {
                if (u.getId().equals(c.getUser().getId())) {
                    toBeSkipped = true;
                    break;
                }
            }
            if (!toBeSkipped) {
                finalList.add(u);
            }
        }
        setUsList(new UserDataModel(finalList));
    }

    /**
     * Adds a user as a collaborator for a set of selected project samples.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void addSelected() {
        for (User u : selectedCollaborators) {
            SelectSamplesForProjectBean.addCollaborationForUser(u, projectId);
        }
        loadUserList();
        selectedCollaborators = new User[0];
    }

    /**
     * Getter for usList field.
     *
     * @author Francesco Venco
     * @return UserDataModel
     * @since 1.0
     */
    public UserDataModel getUsList() {
        return usList;
    }

    /**
     * Setter for usList field.
     *
     * @author Francesco Venco
     * @param usList
     * @since 1.0
     */
    public void setUsList(UserDataModel usList) {
        this.usList = usList;
    }

    /**
     * Getter for selectedCollaborators field.
     *
     * @author Francesco Venco
     * @return User[]
     * @since 1.0
     */
    public User[] getSelectedCollaborators() {
        return selectedCollaborators;
    }

    /**
     * Setter for selectedCollaborators field.
     *
     * @author Francesco Venco
     * @param selectedCollaborators the selectedCollaborators to set
     * @since 1.0
     */
    public void setSelectedCollaborators(User[] selectedCollaborators) {
        this.selectedCollaborators = selectedCollaborators;
    }

    /**
     * Getter for projectId field.
     *
     * @author Francesco Venco
     * @return int - the projectId
     * @since 1.0
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Setter for projectId field.
     *
     * @author Francesco Venco
     * @param projectId the projectId to set
     * @since 1.0
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Getter for loggedUser field.
     *
     * @author Francesco Venco
     * @return LoggedUser - the loggedUser
     * @since 1.0
     */
    public LoggedUser getLoggedUser() {
        return loggedUser;
    }

    /**
     * Setter for loggedUser field.
     *
     * @author Francesco Venco
     * @param loggedUser the loggedUser to set
     * @since 1.0
     */
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

}
