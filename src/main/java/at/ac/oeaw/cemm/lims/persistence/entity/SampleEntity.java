package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sample")
public class SampleEntity implements Serializable, EntityWithSettableId {

    private static final long serialVersionUID = 1L;
 
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
    @GenericGenerator(name="IdOrGenerated", strategy="at.ac.oeaw.cemm.lims.persistence.entity.UseIdOrGenerate")
    @Column(name = "sam_id", nullable = false, columnDefinition = "serial")    
    private Integer id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sequencingIndexId")
    private SequencingIndexEntity sequencingIndexes;
    
    @Column(name = "organism")
    private String organism;

    @Column(name = "type")
    private String type;

    @Column(name = "antibody")
    private String antibody;

    @Column(name = "librarysynthesisneeded")
    private Boolean librarySynthesisNeeded;

    @Column(name = "concentration")
    private Double concentration;

    @Column(name = "totalamount")
    private Double totalAmount;

    @Column(name = "bulkfragmentsize")
    private Double bulkFragmentSize;

    @Column(name = "costcenter")
    private String costCenter;

    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    @Column(name = "description")
    private String description;

    @Column(name = "requestdate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date requestDate;

    @Column(name = "bioanalyzerdate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date bioanalyzerDate;

    @Column(name = "bionalyzerbiomolarity")
    private Double bionalyzerBiomolarity;

    @Column(name = "submission_id")    
    private Integer submissionId;

    @Column(name = "experimentName")
    private String experimentName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sam_id")
    private Set<SampleRunEntity> sampleRuns = new HashSet<SampleRunEntity>(0);
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "library_id", unique=true, nullable = false)
    private LibraryEntity library;

    @Override
    public Integer getId() {
      
        return this.id;
    }

    @Override
    public void setId(Integer samId) {
        this.id = samId;
    }

    public ApplicationEntity getApplication() {
        return this.application;
    }

    public void setApplication(ApplicationEntity application) {
        this.application = application;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public SequencingIndexEntity getSequencingIndexes() {
        return this.sequencingIndexes;
    }

    public void setSequencingIndexes(SequencingIndexEntity sequencingindexes) {
        this.sequencingIndexes = sequencingindexes;
    }
    
    public String getSequencingIndex1(){
        if(this.sequencingIndexes == null){
            return "";
        }
        if(this.sequencingIndexes.getIndex().length() <= 8){
            return this.sequencingIndexes.getIndex();
        }
        if(this.sequencingIndexes.getIndex().length() > 8){
            return this.sequencingIndexes.getIndex().substring(0,8);
        }
        return "";
    }
    
    public String getSequencingIndex2(){
        if(this.sequencingIndexes == null){
            return "";
        }        
        if(this.sequencingIndexes.getIndex().length() <= 8){
            return "";
        }        
        if(this.sequencingIndexes.getIndex().length() > 8){
            return this.sequencingIndexes.getIndex().substring(8);
        }
        return "";
    }

    public String getOrganism() {
        return this.organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAntibody() {
        return this.antibody;
    }

    public void setAntibody(String antibody) {
        this.antibody = antibody;
    }

    public Boolean isLibrarySynthesisNeeded() {
        return this.librarySynthesisNeeded;
    }

    public void setLibrarySynthesisNeeded(Boolean librarysynthesisneeded) {
        this.librarySynthesisNeeded = librarysynthesisneeded;
    }
    
    public boolean getLibrarySynthesisNeeded() {
        if(librarySynthesisNeeded == null){
            return false;
        }
        return this.librarySynthesisNeeded.booleanValue();
    }

    public Double getConcentration() {
        return this.concentration;
    }

    public void setConcentration(Double concentration) {
        this.concentration = concentration;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(Double totalamount) {
        this.totalAmount = totalamount;
    }

    public Double getBulkFragmentSize() {
        return this.bulkFragmentSize;
    }

    public void setBulkFragmentSize(Double bulkfragmentsize) {
        this.bulkFragmentSize = bulkfragmentsize;
    }

    public String getCostCenter() {
        return this.costCenter;
    }

    public void setCostCenter(String costcenter) {
        this.costCenter = costcenter;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestdate) {
        this.requestDate = requestdate;
    }

    public Date getBioanalyzerDate() {
        return this.bioanalyzerDate;
    }

    public void setBioanalyzerDate(Date bioanalyzerdate) {
        this.bioanalyzerDate = bioanalyzerdate;
    }

    public Double getBionalyzerBiomolarity() {
        return this.bionalyzerBiomolarity;
    }

    public void setBionalyzerBiomolarity(Double bionalyzerbiomolarity) {
        this.bionalyzerBiomolarity = bionalyzerbiomolarity;
    }

    public Integer getSubmissionId() {
        return this.submissionId;
    }

    public void setSubmissionId(Integer submissionid) {
        this.submissionId = submissionid;
    }

    public String getExperimentName() {
        return this.experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }


    public Set<SampleRunEntity> getSampleRuns() {
        return this.sampleRuns;
    }

    public void setSampleRuns(Set<SampleRunEntity> sampleruns) {
        this.sampleRuns = sampleruns;
    }

    public LibraryEntity getLibrary() {
        return this.library;
    }
    
     public void setLibrary(LibraryEntity library) {
        this.library=library;
    }      
   
   
    @Override
    public int hashCode() {
        //Integer i = new Integer(id);
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SampleEntity other = (SampleEntity) obj;
        Integer i = new Integer(id);
        Integer o = ((SampleEntity)obj).id;
        if (i == null) {
            if (o != null) {
                return false;
            }
        } else if (!i.equals(o)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + this.id;
    }
    
    public void dump(){
        StringBuilder sb = new StringBuilder();
        sb.append(id + "\r\n");
        sb.append(organism + "\r\n");
        sb.append(type + "\r\n");
        sb.append(antibody + "\r\n");
        sb.append(librarySynthesisNeeded.booleanValue() + "\r\n");
        sb.append(concentration + "\r\n");
        sb.append(totalAmount + "\r\n");
        sb.append(bulkFragmentSize + "\r\n");
        sb.append(costCenter + "\r\n");
        sb.append(status + "\r\n");
        sb.append(name + "\r\n");
        sb.append(comment + "\r\n");
        sb.append(description + "\r\n");
        sb.append(requestDate.toString() + "\r\n");
        sb.append(bioanalyzerDate.toString() + "\r\n");
        sb.append(bionalyzerBiomolarity + "\r\n");
        sb.append(submissionId + "\r\n");
        sb.append(experimentName + "\r\n");
        
     
        System.out.println(sb.toString());
    }

}
