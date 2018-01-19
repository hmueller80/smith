/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.external_users;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "usergroup")
public class ExternalGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUserGroup")
    private Integer idUserGroup;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    
    @Size(max = 255)
    @Column(name = "userName")
    private String userName;

    public ExternalGroupEntity() {
    }

    public ExternalGroupEntity(Integer idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    public ExternalGroupEntity(Integer idUserGroup, String name) {
        this.idUserGroup = idUserGroup;
        this.name = name;
    }

    public Integer getIdUserGroup() {
        return idUserGroup;
    }

    public void setIdUserGroup(Integer idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserGroup != null ? idUserGroup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExternalGroupEntity)) {
            return false;
        }
        ExternalGroupEntity other = (ExternalGroupEntity) object;
        if ((this.idUserGroup == null && other.idUserGroup != null) || (this.idUserGroup != null && !this.idUserGroup.equals(other.idUserGroup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "at.ac.oeaw.cemm.lims.persistence.dao.external_users.Usergroup[ idUserGroup=" + idUserGroup + " ]";
    }
    
}
