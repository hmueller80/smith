/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.request_form;

import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "affiliation")
public class AffiliationEntity implements Serializable {

    @EmbeddedId
    protected AffiliationPK affiliationPK;
    
    @Size(max = 200)
    @Column(name = "address")
    private String address;
    
    @Size(max = 2048)
    @Column(name = "url")
    private String url;
    
    @OneToMany(mappedBy = "affiliation")
    private Set<UserEntity> userEntitySet;

    public AffiliationEntity() {
    }

    public AffiliationEntity(AffiliationPK affiliationPK) {
        this.affiliationPK = affiliationPK;
    }

    public AffiliationEntity(String organizationName, String department) {
        this.affiliationPK = new AffiliationPK(organizationName, department);
    }

    public AffiliationPK getAffiliationPK() {
        return affiliationPK;
    }

    public void setAffiliationPK(AffiliationPK affiliationPK) {
        this.affiliationPK = affiliationPK;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<UserEntity> getUserEntitySet() {
        return userEntitySet;
    }

    public void setUserEntitySet(Set<UserEntity> userEntitySet) {
        this.userEntitySet = userEntitySet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (affiliationPK != null ? affiliationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AffiliationEntity)) {
            return false;
        }
        AffiliationEntity other = (AffiliationEntity) object;
        if ((this.affiliationPK == null && other.affiliationPK != null) || (this.affiliationPK != null && !this.affiliationPK.equals(other.affiliationPK))) {
            return false;
        }
        return true;
    }
   
}
