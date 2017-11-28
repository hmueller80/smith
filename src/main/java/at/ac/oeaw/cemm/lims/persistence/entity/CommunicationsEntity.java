/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author hmuller
 */
@Entity
@Table(name = "communications")
public class CommunicationsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommunicationsKeyEntity communicationsPK;

    public CommunicationsEntity() {
    }

    public CommunicationsEntity(CommunicationsKeyEntity communicationsPK) {
        this.communicationsPK = communicationsPK;
    }

    public CommunicationsEntity(int userId, int collaboratorId) {
        this.communicationsPK = new CommunicationsKeyEntity(userId, collaboratorId);
    }

    public CommunicationsKeyEntity getCommunicationsPK() {
        return communicationsPK;
    }

    public void setCommunicationsPK(CommunicationsKeyEntity communicationsPK) {
        this.communicationsPK = communicationsPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (communicationsPK != null ? communicationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommunicationsEntity)) {
            return false;
        }
        CommunicationsEntity other = (CommunicationsEntity) object;
        if ((this.communicationsPK == null && other.communicationsPK != null) || (this.communicationsPK != null && !this.communicationsPK.equals(other.communicationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName()+"[ communicationsPK=" + communicationsPK + " ]";
    }
    
}
