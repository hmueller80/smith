package it.iit.genomics.cru.smith.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LibraryId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "library_id")
    private int libraryId;

    @Column(name = "sample_id")
    private int sampleId;

    public LibraryId() {
    }

    public LibraryId(int libraryId, int sampleId) {
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
        if (!(other instanceof MultipleRequestId)) {
            return false;
        }
        LibraryId castOther = (LibraryId) other;

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
