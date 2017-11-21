package it.iit.genomics.cru.smith.entity;

import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "sample")
public class Sample implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String status_requested = "requested";
    public static final String status_queued = "queued";
    public static final String status_confirmed = "confirmed";
    public static final String status_running = "running";
    public static final String status_analyzed = "analyzed";
 
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
    @GenericGenerator(name="IdOrGenerated", strategy="it.iit.genomics.cru.smith.utils.UseIdOrGenerate")
    @Column(name = "sam_id", nullable = false, columnDefinition = "serial")    
    private Integer id; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sequencingIndexId")
    private SequencingIndex sequencingIndexes;
    
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

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "sample", fetch = FetchType.LAZY)
    private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>(0);

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sam_id")
    private Set<SampleRun> sampleRuns = new HashSet<SampleRun>(0);

    @OneToMany(mappedBy = "sample", fetch = FetchType.EAGER)
    private Set<MultipleRequest> multipleRequests = new HashSet<MultipleRequest>(0);
    
    @OneToMany(mappedBy = "sample", fetch = FetchType.EAGER)
    private Set<Library> library = new HashSet<Library>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "projectsamples",
            joinColumns = {
                @JoinColumn(name = "sample_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "project_id")})
    private Set<Project> projects = new HashSet<Project>();
    
    public Sample() {
    }

    public Sample(Application application, User user, String organism,
            String type, Boolean librarysynthesisneeded, Double concentration,
            Double totalamount, Double bulkfragmentsize, String costcenter,
            String status, String name, String comment, String description) {
        this.application = application;
        this.user = user;
        this.organism = organism;
        this.type = type;
        this.librarySynthesisNeeded = librarysynthesisneeded;
        this.concentration = concentration;
        this.totalAmount = totalamount;
        this.bulkFragmentSize = bulkfragmentsize;
        this.costCenter = costcenter;
        this.status = status;
        this.name = name;
        this.comment = comment;
        this.description = description;
    }

    public Sample(Application application, User user,
            SequencingIndex sequencingindexes, String organism, String type,
            String antibody, Boolean librarysynthesisneeded,
            Double concentration, Double totalamount, Double bulkfragmentsize,
            String costcenter, String status, String name, String comment,
            String description, Date requestdate, Date bioanalyzerdate,
            Double bionalyzerbiomolarity, Integer submissionid,
            String experimentName, Set<AttributeValue> attributevalues,
            Set<SampleRun> sampleruns, Set<MultipleRequest> multiplerequests,
            Set<Project> projects) {
        this.application = application;
        this.user = user;
        this.sequencingIndexes = sequencingindexes;
        this.organism = organism;
        this.type = type;
        this.antibody = antibody;
        this.librarySynthesisNeeded = librarysynthesisneeded;
        this.concentration = concentration;
        this.totalAmount = totalamount;
        this.bulkFragmentSize = bulkfragmentsize;
        this.costCenter = costcenter;
        this.status = status;
        this.name = name;
        this.comment = comment;
        this.description = description;
        this.requestDate = requestdate;
        this.bioanalyzerDate = bioanalyzerdate;
        this.bionalyzerBiomolarity = bionalyzerbiomolarity;
        this.submissionId = submissionid;
        this.experimentName = experimentName;
        this.attributeValues = attributevalues;
        this.sampleRuns = sampleruns;
        this.multipleRequests = multiplerequests;
        this.projects = projects;
    }

    public Integer getId() {
        //if(this.id != null){
        //    System.out.println("sample id != null");
            
        //}else{
        //    System.out.println("sample id == null");
        //}
        return this.id;
    }

    public void setId(Integer samId) {
        this.id = samId;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    /*
    public User getUser() {
        if(this.user != null){
            //System.out.println("user != null");
            return this.user;
        }else{
            //System.out.println("user == null, loading from DB");
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Sample s = (Sample)session.load(Sample.class, this.getId());
                
                this.user = s.getUser();
                tx.commit();                
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.close();
            }
        return this.user;
    }
    */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SequencingIndex getSequencingIndexes() {
        return this.sequencingIndexes;
    }

    public void setSequencingIndexes(SequencingIndex sequencingindexes) {
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

    public Set<AttributeValue> getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(Set<AttributeValue> attributevalues) {
        this.attributeValues = attributevalues;
    }

    public Set<SampleRun> getSampleRuns() {
        return this.sampleRuns;
    }

    public void setSampleRuns(Set<SampleRun> sampleruns) {
        this.sampleRuns = sampleruns;
    }

    public Set<MultipleRequest> getMultipleRequests() {
        return this.multipleRequests;
    }
    
    public Set<Library> getLibrary() {
        return this.library;
    }
    
    public String getLibraryName() {
        if(library == null){
            return "";
        }
        if(library.size() == 0){
            return "";
        }
        if(library.size() == 1){
            StringBuilder sb = new StringBuilder();
            Object[] la = library.toArray();
            for(Object o : la){
                sb.append(((Library)o).getLibraryName());
            }
            return sb.toString();
        }
        if(library.size() > 1){
            StringBuilder sb = new StringBuilder();
            Object[] la = library.toArray();
            for(Object o : la){
                sb.append(((Library)o).getLibraryName() + " ");
            }
            return sb.toString();
        }
        return "";
    }
    
    public void setMultipleRequests(Set<MultipleRequest> multiplerequests) {
        this.multipleRequests = multiplerequests;
    }

    /*
    public Set<Project> getProjects() {
        if(this.projects != null){
            System.out.println("project not null");
            return this.projects;
        }else{
            System.out.println("project null");
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Sample s = (Sample)session.load(Sample.class, this.getId());
                this.projects = s.getProjects();
                for (Project project : this.projects) {
                    project.getId();
                }             
                tx.commit();                
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.close();
            }
        
        
        return this.projects;
    }
    */
    
    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

	//
    // public Boolean equalss(Object obj){
    // System.out.println("Test equality");
    // try{
    // Sample s = (Sample) obj;
    // return s.samId.equals(this.samId);
    // }
    // catch(Exception e){
    // return false;
    // }
    // }
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
        Sample other = (Sample) obj;
        Integer i = new Integer(id);
        Integer o = ((Sample)obj).id;
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
