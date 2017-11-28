/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.news;

import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.NewsDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name = "singleNewsBean")
@ViewScoped
public class SingleNewsBean {
    private static final String FORM_ID="newsWriterForm"; 
    
    @Inject private ServiceFactory services;
    @Inject private DTOFactory myDTOFactory;
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
    
    private NewsDTO newNews = null;

    @PostConstruct
    public void init(){
        newNews = myDTOFactory.createEmptyNews();
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public NewsDTO getNewNews() {
        return newNews;
    }

    public void setNewNews(NewsDTO newNews) {
        this.newNews = newNews;
    }
    
    public void submit(){
        final String COMPONENT = "NewsPublishButton";
        try{
            services.getNewsService().submitNews(newNews);
        }catch(Exception e){
            e.printStackTrace();
            NgsLimsUtility.setFailMessage(FORM_ID, COMPONENT, "DB error", e.getMessage());
        }
    }
    
    
}
