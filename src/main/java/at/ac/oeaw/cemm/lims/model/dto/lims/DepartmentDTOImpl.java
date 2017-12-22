/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
public class DepartmentDTOImpl implements DepartmentDTO {
    
    private final String name;
    private String address="";
    private String webPage="";

    protected DepartmentDTOImpl() {
        this.name = DepartmentDTO.DEFAULT_DEPT;
    }
    
    protected DepartmentDTOImpl(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.name);
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
        final DepartmentDTOImpl other = (DepartmentDTOImpl) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
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
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }
    
    
    
}
