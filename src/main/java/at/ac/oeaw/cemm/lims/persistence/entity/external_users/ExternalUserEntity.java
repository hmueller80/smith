/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.external_users;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "external_users")
public class ExternalUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "userid")
    private String userid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "password")
    private String password;
    
    @Column(name = "first_password")
    private String firstPassword;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "externalUsers")
    private Collection<ExternalGroupEntity> externalGroupsCollection;

    public ExternalUserEntity() {
    }

    public ExternalUserEntity(String userid) {
        this.userid = userid;
    }

    public ExternalUserEntity(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<ExternalGroupEntity> getExternalGroupsCollection() {
        return externalGroupsCollection;
    }

    public void setExternalGroupsCollection(Collection<ExternalGroupEntity> externalGroupsCollection) {
        this.externalGroupsCollection = externalGroupsCollection;
    }

    public String getFirstPassword() {
        return firstPassword;
    }

    public void setFirstPassword(String firstPassword) {
        this.firstPassword = firstPassword;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExternalUserEntity)) {
            return false;
        }
        ExternalUserEntity other = (ExternalUserEntity) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.entity.external_users.ExternalUsers[ userid=" + userid + " ]";
    }
    
}
