package it.iit.genomics.cru.smith.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "library")
public class Library implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LibraryId id;

    @ManyToOne
    @JoinColumn(name = "sample_id", insertable = false, updatable = false)
    private Sample sample;
    
    @Column(name = "libraryName")
    private String libraryName;

    public Library() {
    }

    public Library(LibraryId id, Sample sample, String libname) {
        this.id = id;
        this.sample = sample;
        this.libraryName = libname;
    }

    public LibraryId getId() {
        return this.id;
    }

    public void setId(LibraryId id) {
        this.id = id;
    }

    public Sample getSample() {
        return this.sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    
    

}
