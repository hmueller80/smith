/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Embeddable
public class DepartmentPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "department_name")
    private String departmentName;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "organization_name")
    private String organizationName;

    public DepartmentPK() {
    }

    public DepartmentPK(String departmentName, String organizationName) {
        this.departmentName = departmentName;
        this.organizationName = organizationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (departmentName != null ? departmentName.hashCode() : 0);
        hash += (organizationName != null ? organizationName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepartmentPK)) {
            return false;
        }
        DepartmentPK other = (DepartmentPK) object;
        if ((this.departmentName == null && other.departmentName != null) || (this.departmentName != null && !this.departmentName.equals(other.departmentName))) {
            return false;
        }
        if ((this.organizationName == null && other.organizationName != null) || (this.organizationName != null && !this.organizationName.equals(other.organizationName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.DepartmentPK[ departmentName=" + departmentName + ", organizationName=" + organizationName + " ]";
    }
    
}
