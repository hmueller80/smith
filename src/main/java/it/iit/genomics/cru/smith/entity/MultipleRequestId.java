package it.iit.genomics.cru.smith.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MultipleRequestId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "request_id")
    private int requestId;

    @Column(name = "sample_id")
    private int sampleId;

    public MultipleRequestId() {
    }

    public MultipleRequestId(int requestId, int sampleId) {
        this.requestId = requestId;
        this.sampleId = sampleId;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
        MultipleRequestId castOther = (MultipleRequestId) other;

        return (this.getRequestId() == castOther.getRequestId())
                && (this.getSampleId() == castOther.getSampleId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getRequestId();
        result = 37 * result + this.getSampleId();
        return result;
    }

}
