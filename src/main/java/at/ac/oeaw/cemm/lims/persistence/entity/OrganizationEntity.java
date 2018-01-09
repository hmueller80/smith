/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "organization")

public class OrganizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "organization_name")
    private String organizationName;
    
    @Size(max = 200)
    @Column(name = "address")
    private String address;
    
    @Size(max = 2048)
    @Column(name = "url")
    private String url;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization", orphanRemoval = true)
    private Set<DepartmentEntity> departmentSet = new HashSet<>();;

    public OrganizationEntity() {
        
    }

    public OrganizationEntity(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public Set<DepartmentEntity> getDepartmentSet() {
        return departmentSet;
    }

    public void setDepartmentSet(Set<DepartmentEntity> departmentSet) {
        this.departmentSet = departmentSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (organizationName != null ? organizationName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganizationEntity)) {
            return false;
        }
        OrganizationEntity other = (OrganizationEntity) object;
        if ((this.organizationName == null && other.organizationName != null) || (this.organizationName != null && !this.organizationName.equals(other.organizationName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.Organization[ organizationName=" + organizationName + " ]";
    }
    
}
