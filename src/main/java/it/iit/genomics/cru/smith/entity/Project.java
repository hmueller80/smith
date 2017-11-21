package it.iit.genomics.cru.smith.entity;

//import java.util.Arrays;
import java.util.HashSet;
//import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class Project implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

//	@ManyToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "projectsamples",
//	joinColumns = { @JoinColumn(name = "project_id")},
//	inverseJoinColumns = { @JoinColumn(name = "sample_id")  })
    @ManyToMany(mappedBy = "projects")
    private Set<Sample> samples = new HashSet<Sample>(0);

    @OneToMany(mappedBy = "project")
    private Set<Collaboration> collaborations = new HashSet<Collaboration>(0);

    public Project() {
    }

    public Project(User user, String name, String description,
            Set<Sample> samples, Set<Collaboration> collaborations) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.samples = samples;
        this.collaborations = collaborations;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Sample> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }

    public Set<Collaboration> getCollaborations() {
        return this.collaborations;
    }

    public void setCollaborations(Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
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
        Project other = (Project) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int hash = 5;
        //hash = 97 * hash + Objects.hashCode(this.id);
        //hash = 97 * hash + Arrays.hashCode();
        hash = 97 * hash + this.id;
        return hash;
    }

}
