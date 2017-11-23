package at.ac.oeaw.cemm.lims.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LibraryIdEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "library_id")
    private int libraryId;

    @Column(name = "sample_id")
    private int sampleId;

    public LibraryIdEntity() {
    }

    public LibraryIdEntity(int libraryId, int sampleId) {
        this.libraryId = libraryId;
        this.sampleId = sampleId;
    }

    public int getLibraryId() {
        return this.libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public int getSampleId() {
        return this.sampleId;
    }

    public void setSampleId(int sampleId) {
        this.sampleId = sampleId;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }

        LibraryIdEntity castOther = (LibraryIdEntity) other;

        return (this.getLibraryId() == castOther.getLibraryId())
                && (this.getSampleId() == castOther.getSampleId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getLibraryId();
        result = 37 * result + this.getSampleId();
        return result;
    }

}
