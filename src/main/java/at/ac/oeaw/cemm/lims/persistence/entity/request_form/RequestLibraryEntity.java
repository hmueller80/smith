/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.request_form;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "request_library")
public class RequestLibraryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 1000)
    @Column(name = "lib_name")
    private String libName;
    
    @Size(max = 1000)
    @Column(name = "lib_type")
    private String libType;
    
    @Size(max = 2)
    @Column(name = "read_mode")
    private String readMode;
    
    @Column(name = "read_length")
    private Short readLength;
    
    @Column(name = "lanes")
    private Short lanes;
    
    @Column(name = "volume")
    private Double volume;
    
    @Column(name = "dna_concentration")
    private Double dnaConcentration;
    
    @Column(name = "total_size")
    private Double totalSize;
    
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RequestEntity requestId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libraryId")
    private Set<RequestSampleEntity> requestSampleSet;

    public RequestLibraryEntity() {
    }

    public RequestLibraryEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getLibType() {
        return libType;
    }

    public void setLibType(String libType) {
        this.libType = libType;
    }

    public String getReadMode() {
        return readMode;
    }

    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    public Short getReadLength() {
        return readLength;
    }

    public void setReadLength(Short readLength) {
        this.readLength = readLength;
    }

    public Short getLanes() {
        return lanes;
    }

    public void setLanes(Short lanes) {
        this.lanes = lanes;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getDnaConcentration() {
        return dnaConcentration;
    }

    public void setDnaConcentration(Double dnaConcentration) {
        this.dnaConcentration = dnaConcentration;
    }

    public Double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }

    public RequestEntity getRequestId() {
        return requestId;
    }

    public void setRequestId(RequestEntity requestId) {
        this.requestId = requestId;
    }

    public Set<RequestSampleEntity> getRequestSampleSet() {
        return requestSampleSet;
    }

    public void setRequestSampleSet(Set<RequestSampleEntity> requestSampleSet) {
        this.requestSampleSet = requestSampleSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestLibraryEntity)) {
            return false;
        }
        RequestLibraryEntity other = (RequestLibraryEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
