/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.request_form;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "request_sample")
public class RequestSampleEntity implements Serializable {

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @Size(max = 1000)
    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 1000)
    @Column(name = "organism")
    private String organism;
    
    @Size(max = 100)
    @Column(name = "index_i7")
    private String indexI7;
    
    @Size(max = 1000)
    @Column(name = "adapter_i7")
    private String adapterI7;
    
    @Size(max = 100)
    @Column(name = "index_i5")
    private String indexI5;
    
    @Size(max = 1000)
    @Column(name = "adapter_i5")
    private String adapterI5;
    
    @Size(max = 100)
    @Column(name = "primer_index")
    private String primerIndex;
    
    @Size(max = 1000)
    @Column(name = "primer_name")
    private String primerName;
    
    @Size(max = 1000)
    @Column(name = "primer_type")
    private String primerType;
    
    @Size(max = 1000)
    @Column(name = "application")
    private String applicationType;
    
    @JoinColumn(name = "library_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RequestLibraryEntity libraryId;

    public RequestSampleEntity() {
    }

    public RequestSampleEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getIndexI7() {
        return indexI7;
    }

    public void setIndexI7(String indexI7) {
        this.indexI7 = indexI7;
    }

    public String getAdapterI7() {
        return adapterI7;
    }

    public void setAdapterI7(String adapterI7) {
        this.adapterI7 = adapterI7;
    }

    public String getIndexI5() {
        return indexI5;
    }

    public void setIndexI5(String indexI5) {
        this.indexI5 = indexI5;
    }

    public String getAdapterI5() {
        return adapterI5;
    }

    public void setAdapterI5(String adapterI5) {
        this.adapterI5 = adapterI5;
    }

    public String getPrimerIndex() {
        return primerIndex;
    }

    public void setPrimerIndex(String primerIndex) {
        this.primerIndex = primerIndex;
    }

    public String getPrimerName() {
        return primerName;
    }

    public void setPrimerName(String primerName) {
        this.primerName = primerName;
    }

    public String getPrimerType() {
        return primerType;
    }

    public void setPrimerType(String primerType) {
        this.primerType = primerType;
    }

    public RequestLibraryEntity getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(RequestLibraryEntity libraryId) {
        this.libraryId = libraryId;
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
        if (!(object instanceof RequestSampleEntity)) {
            return false;
        }
        RequestSampleEntity other = (RequestSampleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String libType) {
        this.applicationType = libType;
    }
    
    
    
}
