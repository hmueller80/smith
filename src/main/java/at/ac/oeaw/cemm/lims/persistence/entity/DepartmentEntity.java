/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "department")
public class DepartmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected DepartmentPK departmentPK;
    
    @JoinColumn(name = "organization_name", referencedColumnName = "organization_name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private OrganizationEntity organization;
    
    @Size(max = 200)
    @Column(name = "address")
    private String address;
    
    @Size(max = 2048)
    @Column(name = "url")
    private String url;
    
    @OneToMany(mappedBy = "department")
    private Set<UserEntity> userEntitySet;

    public DepartmentEntity() {
    }

    public DepartmentEntity(DepartmentPK departmentPK) {
        this.departmentPK = departmentPK;
    }

    public DepartmentEntity(String departmentName, String organizationName) {
        this.departmentPK = new DepartmentPK(departmentName, organizationName);
    }

    public DepartmentPK getDepartmentPK() {
        return departmentPK;
    }

    public void setDepartmentPK(DepartmentPK departmentPK) {
        this.departmentPK = departmentPK;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public Set<UserEntity> getUserEntitySet() {
        return userEntitySet;
    }

    public void setUserEntitySet(Set<UserEntity> userEntitySet) {
        this.userEntitySet = userEntitySet;
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
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (departmentPK != null ? departmentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepartmentEntity)) {
            return false;
        }
        DepartmentEntity other = (DepartmentEntity) object;
        if ((this.departmentPK == null && other.departmentPK != null) || (this.departmentPK != null && !this.departmentPK.equals(other.departmentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.Department[ departmentPK=" + departmentPK + " ]";
    }
    
}
