/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.external_users;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "external_groups")
public class ExternalGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExternalGroupPK externalGroupsPK;
    @JoinColumn(name = "userid", referencedColumnName = "userid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExternalUserEntity externalUsers;

    public ExternalGroupEntity() {
    }

    public ExternalGroupEntity(ExternalGroupPK externalGroupsPK) {
        this.externalGroupsPK = externalGroupsPK;
    }

    public ExternalGroupEntity(String userid, String groupid) {
        this.externalGroupsPK = new ExternalGroupPK(userid, groupid);
    }

    public ExternalGroupPK getExternalGroupsPK() {
        return externalGroupsPK;
    }

    public void setExternalGroupsPK(ExternalGroupPK externalGroupsPK) {
        this.externalGroupsPK = externalGroupsPK;
    }

    public ExternalUserEntity getExternalUsers() {
        return externalUsers;
    }

    public void setExternalUsers(ExternalUserEntity externalUsers) {
        this.externalUsers = externalUsers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (externalGroupsPK != null ? externalGroupsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExternalGroupEntity)) {
            return false;
        }
        ExternalGroupEntity other = (ExternalGroupEntity) object;
        if ((this.externalGroupsPK == null && other.externalGroupsPK != null) || (this.externalGroupsPK != null && !this.externalGroupsPK.equals(other.externalGroupsPK))) {
            return false;
        }
        return true;
    }
    
}
