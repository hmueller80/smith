package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.userBeans.CollaboratorsHelper;
import it.iit.genomics.cru.smith.userBeans.LoggedUser;
//import it.iit.genomics.cru.smith.userBeans.LoggedUserBean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)ProjectsSearchBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for project searches.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
public class ProjectsSearchBean implements Serializable {

    private DataModel projectsList;
    private boolean hasAddPermission;
    LoggedUser loggedUser;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public ProjectsSearchBean() {
        if(Preferences.getVerbose()){
            System.out.println("init ProjectsSearchBean");
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
        FacesContext context = FacesContext.getCurrentInstance();
        loggedUser = ((LoggedUser) context.getApplication().evaluateExpressionGet(context, "#{loggedUser}", LoggedUser.class)); 
        //LoggedUser user = new LoggedUser();
        if (loggedUser.getIsAdmin()) {
            hasAddPermission = true;
            projectsList = new ListDataModel(ProjectHelper.getProjectsList());
        } else {
            hasAddPermission = loggedUser.getIsLeader();
            int pi = loggedUser.getPi();
            List<Project> temp = ProjectHelper.getProjectsListByGroupId(pi);
            //projectsList = new ListDataModel(ProjectHelper.getProjectsListByGroupId(pi));
            List<Collaboration> cols = CollaboratorsHelper.getCollaborationForUser(loggedUser.getUserId());
            for (Collaboration c : cols) {
                if (c.getProject().getUser().getPi() != pi) {
                    Project p = ProjectHelper.loadProject(c.getProject().getId());
                    temp.add(p);
                }
            }
            projectsList = new ListDataModel(temp);

        }
    }

    /**
    * Returns list of projects.
    * 
    * @author Francesco Venco
    * @return DataModel
    * @since 1.0
    */
    public DataModel getProjectsList() {
        return this.projectsList;
    }

    /**
    * Returns hasAddPermission.
    * 
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean getHasAddPermission() {
        return hasAddPermission;
    }

}
