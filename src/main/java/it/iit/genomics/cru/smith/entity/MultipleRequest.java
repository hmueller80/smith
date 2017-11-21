package it.iit.genomics.cru.smith.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "multiplerequest")
public class MultipleRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MultipleRequestId id;

    @ManyToOne
    @JoinColumn(name = "sample_id", insertable = false, updatable = false)
    private Sample sample;

    public MultipleRequest() {
    }

    public MultipleRequest(MultipleRequestId id, Sample sample) {
        this.id = id;
        this.sample = sample;
    }

    public MultipleRequestId getId() {
        return this.id;
    }

    public void setId(MultipleRequestId id) {
        this.id = id;
    }

    public Sample getSample() {
        return this.sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

}
