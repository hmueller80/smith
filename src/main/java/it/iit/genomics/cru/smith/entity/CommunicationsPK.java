/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author hmuller
 */
@Embeddable
public class CommunicationsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "collaborator_id")
    private int collaboratorId;

    public CommunicationsPK() {
    }

    public CommunicationsPK(int userId, int collaboratorId) {
        this.userId = userId;
        this.collaboratorId = collaboratorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(int collaboratorId) {
        this.collaboratorId = collaboratorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) collaboratorId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommunicationsPK)) {
            return false;
        }
        CommunicationsPK other = (CommunicationsPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.collaboratorId != other.collaboratorId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.iit.genomics.cru.smith.entity.CommunicationsPK[ userId=" + userId + ", collaboratorId=" + collaboratorId + " ]";
    }
    
}
