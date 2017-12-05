package at.ac.oeaw.cemm.lims.persistence.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "library")
public class LibraryEntity implements java.io.Serializable, EntityWithSettableId {

    private static final long serialVersionUID = 1L;

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
    @GenericGenerator(name="IdOrGenerated", strategy="at.ac.oeaw.cemm.lims.persistence.entity.UseIdOrGenerate")
    @Column(name = "library_id", nullable = false, columnDefinition = "serial")    
    private Integer id;
 
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "library")
    private Set<SampleEntity> samples = new HashSet<>();
    
    @Column(name = "libraryName")
    private String libraryName;

    public LibraryEntity() {
    }    
    
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Set<SampleEntity> getSamples() {
        return this.samples;
    }

    public void setSample(Set<SampleEntity> samples) {
        this.samples = samples;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    
    

}
