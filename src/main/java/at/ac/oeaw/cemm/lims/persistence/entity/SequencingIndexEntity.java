package at.ac.oeaw.cemm.lims.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "sequencingindexes")
public class SequencingIndexEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idsequencingindexes")
    private int id;

    @Column(name = "index")
    private String index = "none";//default value for index sequence.

    @OneToMany(mappedBy = "sequencingIndexes", fetch = FetchType.LAZY)
//	@JoinColumn(name = "idsequencingindexes")
    private Set<SampleEntity> samples = new HashSet<SampleEntity>(0);

    public SequencingIndexEntity() {
    }

    public SequencingIndexEntity(String index) {
        this.index = index;
    }

    public SequencingIndexEntity(String index, Set<SampleEntity> samples) {
        this.index = index;
        this.samples = samples;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int idsequencingindexes) {
        this.id = idsequencingindexes;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Set<SampleEntity> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<SampleEntity> samples) {
        this.samples = samples;
    }

}
