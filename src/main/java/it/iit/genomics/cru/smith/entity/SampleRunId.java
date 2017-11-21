package it.iit.genomics.cru.smith.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SampleRunId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "run_id")
    private int runId;

    @Column(name = "sam_id")
    private int samId;

    public SampleRunId() {
    }

    public SampleRunId(int runId, int samId) {
        this.runId = runId;
        this.samId = samId;
    }

    public int getRunId() {
        return this.runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getSamId() {
        return this.samId;
    }

    public void setSamId(int samId) {
        this.samId = samId;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof SampleRunId)) {
            return false;
        }
        SampleRunId castOther = (SampleRunId) other;

        return (this.getRunId() == castOther.getRunId())
                && (this.getSamId() == castOther.getSamId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getRunId();
        result = 37 * result + this.getSamId();
        return result;
    }
    
    public String toString(){
        return runId + "_" + samId;
    }

}
