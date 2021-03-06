/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity.request_form;

import at.ac.oeaw.cemm.lims.persistence.entity.EntityWithSettableId;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author dbarreca
 */
@Entity
@Table(name = "request")

public class RequestEntity implements Serializable, EntityWithSettableId {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
    @GenericGenerator(name="IdOrGenerated", strategy="at.ac.oeaw.cemm.lims.persistence.entity.UseIdOrGenerate")
    @Column(name = "id", nullable = false, columnDefinition = "serial")   
    private Integer id;
    
    @Column(name = "req_date")
    @Temporal(TemporalType.DATE)
    private Date reqDate;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "billing_contact")
    private String billingContact;
      
    @Column(name = "billing_address")
    private String billingAddress;
        
    @Column(name = "billing_code")
    private String billingCode;
          
    @Column(name = "auth_form_name")
    private String authFormName;

    @Column(name = "annotation_sheet_name")
    private String annotationSheetName;
    
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private UserEntity userId;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestId")
    private Set<RequestLibraryEntity> requestLibrarySet;

    public RequestEntity() {
    }

    public RequestEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    public Set<RequestLibraryEntity> getRequestLibrarySet() {
        return requestLibrarySet;
    }

    public void setRequestLibrarySet(Set<RequestLibraryEntity> requestLibrarySet) {
        this.requestLibrarySet = requestLibrarySet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    } 

    public String getBillingContact() {
        return billingContact;
    }

    public void setBillingContact(String billingContact) {
        this.billingContact = billingContact;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCode() {
        return billingCode;
    }

    public void setBillingCode(String billingCode) {
        this.billingCode = billingCode;
    }

    public String getAuthFormName() {
        return authFormName;
    }

    public void setAuthFormName(String authFormName) {
        this.authFormName = authFormName;
    }

    public String getAnnotationSheetName() {
        return annotationSheetName;
    }

    public void setAnnotationSheetName(String annotationSheetName) {
        this.annotationSheetName = annotationSheetName;
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
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
