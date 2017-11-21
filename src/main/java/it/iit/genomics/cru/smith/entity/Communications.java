/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hmuller
 */
@Entity
@Table(name = "communications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Communications.findAll", query = "SELECT c FROM Communications c"),
    @NamedQuery(name = "Communications.findByUserId", query = "SELECT c FROM Communications c WHERE c.communicationsPK.userId = :userId"),
    @NamedQuery(name = "Communications.findByCollaboratorId", query = "SELECT c FROM Communications c WHERE c.communicationsPK.collaboratorId = :collaboratorId")})
public class Communications implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CommunicationsPK communicationsPK;

    public Communications() {
    }

    public Communications(CommunicationsPK communicationsPK) {
        this.communicationsPK = communicationsPK;
    }

    public Communications(int userId, int collaboratorId) {
        this.communicationsPK = new CommunicationsPK(userId, collaboratorId);
    }

    public CommunicationsPK getCommunicationsPK() {
        return communicationsPK;
    }

    public void setCommunicationsPK(CommunicationsPK communicationsPK) {
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
        if (!(object instanceof Communications)) {
            return false;
        }
        Communications other = (Communications) object;
        if ((this.communicationsPK == null && other.communicationsPK != null) || (this.communicationsPK != null && !this.communicationsPK.equals(other.communicationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.iit.genomics.cru.smith.entity.Communications[ communicationsPK=" + communicationsPK + " ]";
    }
    
}
