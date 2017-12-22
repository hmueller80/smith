/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class OrganizationDTOImpl implements OrganizationDTO{
    
    private final String name;
    private final Set<DepartmentDTO> deparments = new HashSet<>();
    private String address="";
    private String webPage="";

    protected OrganizationDTOImpl(String name, String address, String webPage) {
        this.name = name;
        this.address = address;
        this.webPage = webPage;
    }

    protected OrganizationDTOImpl() {
        this.name = OrganizationDTO.DEFAULT_ORGA;
    }
    
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public String getWebPage() {
        return this.webPage;
    }

    @Override
    public Set<DepartmentDTO> getDepartments() {
        return this.deparments;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    @Override
    public void addDepartment(DepartmentDTO department) {
        this.deparments.add(department);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrganizationDTOImpl other = (OrganizationDTOImpl) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }    
}
