package it.iit.genomics.cru.smith.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CollaborationId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "col_project_id")
    private Integer projectId;

    @Column(name = "collaborator_id")
    private Integer userId;

//	
//	@Column(name = "collaborator_id")
//	private int collaboratorId;
//	
//	@Column(name = "col_project_id")
//	private int colProjectId;
    public CollaborationId() {
    }

    public CollaborationId(Integer projectId, Integer userId) {
        super();
        this.projectId = projectId;
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((projectId == null) ? 0 : projectId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CollaborationId other = (CollaborationId) obj;
        if (projectId == null) {
            if (other.projectId != null) {
                return false;
            }
        } else if (!projectId.equals(other.projectId)) {
            return false;
        }
        if (userId == null) {
            if (other.userId != null) {
                return false;
            }
        } else if (!userId.equals(other.userId)) {
            return false;
        }
        return true;
    }

//	public CollaborationId(Project project, User user) {
//		super();
//		this.project = project;
//		this.user = user;
//	}
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((project == null) ? 0 : project.hashCode());
//		result = prime * result + ((user == null) ? 0 : user.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		CollaborationId other = (CollaborationId) obj;
//		if (project == null) {
//			if (other.project != null)
//				return false;
//		} else if (!project.equals(other.project))
//			return false;
//		if (user == null) {
//			if (other.user != null)
//				return false;
//		} else if (!user.equals(other.user))
//			return false;
//		return true;
//	}
//	public CollaborationId(int collaboratorId, int colProjectId) {
//		this.collaboratorId = collaboratorId;
//		this.colProjectId = colProjectId;
//	}
//
//	public int getCollaboratorId() {
//		return this.collaboratorId;
//	}
//
//	public void setCollaboratorId(int collaboratorId) {
//		this.collaboratorId = collaboratorId;
//	}
//
//	public int getColProjectId() {
//		return this.colProjectId;
//	}
//
//	public void setColProjectId(int colProjectId) {
//		this.colProjectId = colProjectId;
//	}
//
//	public boolean equals(Object other) {
//		if ((this == other))
//			return true;
//		if ((other == null))
//			return false;
//		if (!(other instanceof CollaborationId))
//			return false;
//		CollaborationId castOther = (CollaborationId) other;
//
//		return (this.getCollaboratorId() == castOther.getCollaboratorId())
//				&& (this.getColProjectId() == castOther.getColProjectId());
//	}
//
//	public int hashCode() {
//		int result = 17;
//
//		result = 37 * result + this.getCollaboratorId();
//		result = 37 * result + this.getColProjectId();
//		return result;
//	}
}
