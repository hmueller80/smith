package it.iit.genomics.cru.smith.entity;

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
public class SequencingIndex implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idsequencingindexes")
    private int id;

    @Column(name = "index")
    private String index = "none";//default value for index sequence.

    @OneToMany(mappedBy = "sequencingIndexes", fetch = FetchType.EAGER)
//	@JoinColumn(name = "idsequencingindexes")
    private Set<Sample> samples = new HashSet<Sample>(0);

    public SequencingIndex() {
    }

    public SequencingIndex(String index) {
        this.index = index;
    }

    public SequencingIndex(String index, Set<Sample> samples) {
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

    public Set<Sample> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }

}
