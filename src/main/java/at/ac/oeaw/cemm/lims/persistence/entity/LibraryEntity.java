package at.ac.oeaw.cemm.lims.persistence.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "library")
public class LibraryEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LibraryIdEntity id;

    @ManyToOne
    @JoinColumn(name = "sample_id", insertable = false, updatable = false)
    private SampleEntity sample;
    
    @Column(name = "libraryName")
    private String libraryName;

    public LibraryEntity() {
    }
    
     public LibraryEntity(LibraryIdEntity id, String libname) {
        this.id = id;
        this.libraryName = libname;
    }


    public LibraryEntity(LibraryIdEntity id, SampleEntity sample, String libname) {
        this.id = id;
        this.sample = sample;
        this.libraryName = libname;
    }

    public LibraryIdEntity getId() {
        return this.id;
    }

    public void setId(LibraryIdEntity id) {
        this.id = id;
    }

    public SampleEntity getSample() {
        return this.sample;
    }

    public void setSample(SampleEntity sample) {
        this.sample = sample;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    
    

}
