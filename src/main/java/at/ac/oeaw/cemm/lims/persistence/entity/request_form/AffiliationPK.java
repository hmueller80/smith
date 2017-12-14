/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.request_form;

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
public class AffiliationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "organization_name")
    private String organizationName;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "department")
    private String department;

    public AffiliationPK() {
    }

    public AffiliationPK(String organizationName, String department) {
        this.organizationName = organizationName;
        this.department = department;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (organizationName != null ? organizationName.hashCode() : 0);
        hash += (department != null ? department.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AffiliationPK)) {
            return false;
        }
        AffiliationPK other = (AffiliationPK) object;
        if ((this.organizationName == null && other.organizationName != null) || (this.organizationName != null && !this.organizationName.equals(other.organizationName))) {
            return false;
        }
        if ((this.department == null && other.department != null) || (this.department != null && !this.department.equals(other.department))) {
            return false;
        }
        return true;
    }

}
