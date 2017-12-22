/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.user;

import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import at.ac.oeaw.cemm.lims.view.NgsLimsUtility;
import java.util.HashSet;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(name="orgaBean")
@ViewScoped
public class OrgaBean {
   
    @Inject ServiceFactory services;
    @Inject DTOFactory dtoFactory;
    
    @ManagedProperty(value="#{newRoleManager}")
    NewRoleManager roleManager;
    
    private OrganizationDTO orga;
    private DepartmentDTO dept;

    
    public void set(OrganizationDTO orga, DepartmentDTO department){
        this.orga = orga;
        this.dept = department;
    }

 
    public void setOrga(OrganizationDTO orga) {
        this.orga = orga;
    }

    public OrganizationDTO getOrga() {
        return orga;
    }

    public DepartmentDTO getDept() {
        return dept;
    }

    
    public void setDept(DepartmentDTO dept) {
        this.dept = dept;
    }
    
    public Set<DepartmentDTO> getDepartments(){
        return orga.getDepartments();
    }
    
    public Set<OrganizationDTO> getOrgas(){
        Set<OrganizationDTO> orgas = new HashSet<>(services.getUserService().getAllOrganizations());
        orgas.remove(orga);
        orgas.add(orga);
        return orgas;
    }
      
        
    public String getOrgaName(){
        return orga.getName();
    }
    
    public String getOrgaAddress() {
        return orga.getAddress();
    }
    
    public String getOrgaURL() {
        return orga.getWebPage();
    }
    
 
    
    public String getDeptName(){
        return dept.getName();
    }
    
    public String getDeptAddress() {
        return dept.getAddress();
    }
    
    public String getDeptURL() {
        return dept.getWebPage();
    }
    
    public void setOrgaName(String name){
        if (!name.equals(orga.getName())){
            orga = dtoFactory.getOrganizationDTO(name);
            dept = dtoFactory.getDepartmentDTO(DepartmentDTO.DEFAULT_DEPT);
            orga.addDepartment(dept);
        }
    }
    
    public void setOrgaAddress(String address) {
        orga.setAddress(address);
    }
    
    public void setOrgaURL(String url) {
        orga.setWebPage(url);
    }
    
    public void setDeptName(String name){
        if (!name.equals(dept.getName())){
            dept = dtoFactory.getDepartmentDTO(name);
            orga.addDepartment(dept);
        }
    }
    
    public void setDeptAddress(String address) {
        dept.setAddress(address);
    }
    
    public void setDeptURL(String url) {
        dept.setWebPage(url);
    }
    
    
    public void submitOrga() {
        NgsLimsUtility.setFailMessage("userPersistMessages", null, "User Error", "You don't have permission to add a new organization");
    }
    
    public void submitDept() {
        NgsLimsUtility.setFailMessage("userPersistMessages", null, "User Error", "You don't have permission to add a new organization");
    }
    
     public void deleteOrga() {
        NgsLimsUtility.setFailMessage("userPersistMessages", null, "User Error", "You don't have permission to add a new organization");
        //orga = dtoFactory.getOrganizationDTO(OrganizationDTO.DEFAULT_ORGA);
        //orga.addDepartment(dtoFactory.getDepartmentDTO(DepartmentDTO.DEFAULT_DEPT));
    }
    
    public void deleteDept() {
        NgsLimsUtility.setFailMessage("userPersistMessages", null, "User Error", "You don't have permission to add a new organization");
        //orga.getDepartments().remove(dept);
        //dept = dtoFactory.getDepartmentDTO(DepartmentDTO.DEFAULT_DEPT);
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }

   
    
    
}
