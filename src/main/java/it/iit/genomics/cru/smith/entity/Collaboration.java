package it.iit.genomics.cru.smith.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "collaboration")
public class Collaboration implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CollaborationId id;

    //@ManyToOne(cascade=CascadeType.REMOVE)
    @ManyToOne
    @JoinColumn(name = "col_project_id", insertable = false, updatable = false)
    //@JoinColumn(name = "col_project_id")
    @MapsId("projectId")
    private Project project;

    //@ManyToOne(cascade=CascadeType.REMOVE)
    @ManyToOne
    @JoinColumn(name = "collaborator_id", insertable = false, updatable = false)
    //@JoinColumn(name = "collaborator_id")
    @MapsId("userId")
    private User user;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "modify_permission")
    private byte modifyPermission;

    public Collaboration() {
    }

    public Collaboration(CollaborationId id,
            byte modifyPermission) {
        this.id = id;
//		this.project = project;
//		this.user = user;
        this.modifyPermission = modifyPermission;
    }

    public CollaborationId getId() {
        return this.id;
    }

    public void setId(CollaborationId id) {
        this.id = id;
    }

//	public void setUser(User user) {
//		this.user = user;
//	}
    public byte getModifyPermission() {
        return this.modifyPermission;
    }

    public void setModifyPermission(byte modifyPermission) {
        this.modifyPermission = modifyPermission;
    }

}
