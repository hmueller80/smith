package it.iit.genomics.cru.smith.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "attributevalue")
public class AttributeValue implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attrvalid")
    private Integer attrValid;

    @ManyToOne
    @JoinColumn(name = "attributeid")
    private Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "sampleid", nullable = false)
    private Sample sample;

    @Column(name = "value")
    private String value;

    @Column(name = "numericvalue")
    private Float numericValue;

    public AttributeValue() {
    }

    public AttributeValue(Attribute attribute, Sample sample) {
        this.attribute = attribute;
        this.sample = sample;
    }

    public AttributeValue(Attribute attribute, Sample sample, String value,
            Float numericvalue) {
        this.attribute = attribute;
        this.sample = sample;
        this.value = value;
        this.numericValue = numericvalue;
    }

    public Integer getAttrValid() {
        return this.attrValid;
    }

    public void setAttrValid(Integer attrvalid) {
        this.attrValid = attrvalid;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Sample getSample() {
        return this.sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Float getNumericValue() {
        return this.numericValue;
    }

    public void setNumericValue(Float numericValue) {
        this.numericValue = numericValue;
    }

}
