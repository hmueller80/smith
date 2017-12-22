/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.user;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.persistence.service.DTOMapper;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="deptBean")
@ViewScoped
public class DeptBean {
   
    @Inject ServiceFactory services;
    @Inject DTOMapper dtoMapper;
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
    
    private String parentOrga;
    private String name;
    private String address;
    private String url;

    
    public void set(DepartmentDTO department, String parentOrga){
        this.parentOrga = parentOrga;
        this.name = department.getName();
        this.address = department.getAddress();
        this.url = department.getWebPage();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public void submit() {
        NgsLimsUtility.setFailMessage("userPersistMessages", null, "User Error", "You don't have permission to add a new department");
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
}
