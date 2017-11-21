package it.iit.genomics.cru.smith.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "reagent")
public class Reagent implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "reagentbarcode")
    private String reagentBarCode;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User userByOwnerId;

    @ManyToOne
    @JoinColumn(name = "operator_user_id")
    private User userByOperatorUserId;

    @Column(name = "application")
    private String application;

    @Column(name = "cataloguenumber")
    private String catalogueNumber;

    @Column(name = "supportedreactions")
    private int supportedReactions;

    @Column(name = "receptiondate")
    @Temporal(TemporalType.DATE)
    private Date receptionDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "expirationdate")
    private Date expirationDate;

    @Column(name = "price")
    private double price;

    @Column(name = "comments")
    private String comments;

    @Column(name = "institute")
    private String institute;

    @Column(name = "costcenter")
    private String costCenter;

    @OneToMany(mappedBy = "reagentByClustergenerationReagentCode")
//    @JoinColumn(name="clustergeneration_reagent_code")
    private Set<SampleRun> samplerunsForClustergenerationReagentCode = new HashSet<SampleRun>(
            0);

    @OneToMany(mappedBy = "reagentBySamplePrepReagentCode")
//    @JoinColumn(name="sampleprep_reagent_code")
    private Set<SampleRun> samplerunsForSampleprepReagentCode = new HashSet<SampleRun>(
            0);

    @OneToMany(mappedBy = "reagentBySequencingReagentCode")
//    @JoinColumn(name="sequencing_reagent_code")
    private Set<SampleRun> samplerunsForSequencingReagentCode = new HashSet<SampleRun>(
            0);

    public Reagent() {
    }

    public Reagent(String reagentbarcode, User userByOwnerId,
            User userByOperatorUserId, String institute, String costcenter) {
        this.reagentBarCode = reagentbarcode;
        this.userByOwnerId = userByOwnerId;
        this.userByOperatorUserId = userByOperatorUserId;
        this.institute = institute;
        this.costCenter = costcenter;
    }

    public Reagent(String reagentbarcode, User userByOwnerId,
            User userByOperatorUserId, String application,
            String cataloguenumber, int supportedreactions,
            Date receptiondate, Date expirationdate, double price,
            String comments, String institute, String costcenter,
            Set<SampleRun> samplerunsForClustergenerationReagentCode,
            Set<SampleRun> samplerunsForSampleprepReagentCode,
            Set<SampleRun> samplerunsForSequencingReagentCode) {
        this.reagentBarCode = reagentbarcode;
        this.userByOwnerId = userByOwnerId;
        this.userByOperatorUserId = userByOperatorUserId;
        this.application = application;
        this.catalogueNumber = cataloguenumber;
        this.supportedReactions = supportedreactions;
        this.receptionDate = receptiondate;
        this.expirationDate = expirationdate;
        this.price = price;
        this.comments = comments;
        this.institute = institute;
        this.costCenter = costcenter;
        this.samplerunsForClustergenerationReagentCode = samplerunsForClustergenerationReagentCode;
        this.samplerunsForSampleprepReagentCode = samplerunsForSampleprepReagentCode;
        this.samplerunsForSequencingReagentCode = samplerunsForSequencingReagentCode;
    }

    public String getReagentBarCode() {
        return this.reagentBarCode;
    }

    public void setReagentBarCode(String reagentbarcode) {
        this.reagentBarCode = reagentbarcode;
    }

    public User getUserByOwnerId() {
        return this.userByOwnerId;
    }

    public void setUserByOwnerId(User userByOwnerId) {
        this.userByOwnerId = userByOwnerId;
    }

    public User getUserByOperatorUserId() {
        return this.userByOperatorUserId;
    }

    public void setUserByOperatorUserId(User userByOperatorUserId) {
        this.userByOperatorUserId = userByOperatorUserId;
    }

    public String getApplication() {
        return this.application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCatalogueNumber() {
        return this.catalogueNumber;
    }

    public void setCatalogueNumber(String cataloguenumber) {
        this.catalogueNumber = cataloguenumber;
    }

    public int getSupportedReactions() {
        return this.supportedReactions;
    }

    public void setSupportedReactions(int supportedreactions) {
        this.supportedReactions = supportedreactions;
    }

    public Date getReceptionDate() {
        return this.receptionDate;
    }

    public void setReceptionDate(Date receptiondate) {
        this.receptionDate = receptiondate;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationdate) {
        this.expirationDate = expirationdate;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInstitute() {
        return this.institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getCostCenter() {
        return this.costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public Set<SampleRun> getSamplerunsForClustergenerationReagentCode() {
        return this.samplerunsForClustergenerationReagentCode;
    }

    public void setSamplerunsForClustergenerationReagentCode(
            Set<SampleRun> samplerunsForClustergenerationReagentCode) {
        this.samplerunsForClustergenerationReagentCode = samplerunsForClustergenerationReagentCode;
    }

    public Set<SampleRun> getSamplerunsForSampleprepReagentCode() {
        return this.samplerunsForSampleprepReagentCode;
    }

    public void setSamplerunsForSampleprepReagentCode(
            Set<SampleRun> samplerunsForSampleprepReagentCode) {
        this.samplerunsForSampleprepReagentCode = samplerunsForSampleprepReagentCode;
    }

    public Set<SampleRun> getSamplerunsForSequencingReagentCode() {
        return this.samplerunsForSequencingReagentCode;
    }

    public void setSamplerunsForSequencingReagentCode(
            Set<SampleRun> samplerunsForSequencingReagentCode) {
        this.samplerunsForSequencingReagentCode = samplerunsForSequencingReagentCode;
    }

}
