package it.iit.genomics.cru.smith.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rawdata")
public class RawData implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raw_id")
    private Integer rawId;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "run_id", referencedColumnName = "run_id"),
        @JoinColumn(name = "sam_id", referencedColumnName = "sam_id")})
    private SampleRun samplerun;

    @Column(name = "folder_URL")
    private String folderUrl;
    @Column(name = "bcltofastqcommnad")
    private String bclToFastqCommnad;

    @Column(name = "fastqcreferencer1")
    private String fastqCReferencer1;

    @Column(name = "fastqcreferencer2")
    private String fastqCReferencer2;

    @OneToMany(mappedBy = "rawData", fetch = FetchType.EAGER)
//    @JoinColumn(name="raw_id")
    private Set<AlignedData> alignedDatas = new HashSet<AlignedData>(0);

    @OneToMany(mappedBy = "rawData", fetch = FetchType.EAGER)
//    @JoinColumn(name="rawdata_id")
    private Set<AnnotatedData> annotatedDatas = new HashSet<AnnotatedData>(0);

    public RawData() {
    }

    public RawData(SampleRun samplerun, String folderUrl,
            String bcltofastqcommnad, String fastqcreferencer1,
            String fastqcreferencer2) {
        this.samplerun = samplerun;
        this.folderUrl = folderUrl;
        this.bclToFastqCommnad = bcltofastqcommnad;
        this.fastqCReferencer1 = fastqcreferencer1;
        this.fastqCReferencer2 = fastqcreferencer2;
    }

    public RawData(SampleRun samplerun, String folderUrl,
            String bcltofastqcommnad, String fastqcreferencer1,
            String fastqcreferencer2, Set<AlignedData> aligneddatas,
            Set<AnnotatedData> annotateddatas) {
        this.samplerun = samplerun;
        this.folderUrl = folderUrl;
        this.bclToFastqCommnad = bcltofastqcommnad;
        this.fastqCReferencer1 = fastqcreferencer1;
        this.fastqCReferencer2 = fastqcreferencer2;
        this.alignedDatas = aligneddatas;
        this.annotatedDatas = annotateddatas;
    }

    public Integer getRawId() {
        return this.rawId;
    }

    public void setRawId(Integer rawId) {
        this.rawId = rawId;
    }

    public SampleRun getsamplerun() {
        return this.samplerun;
    }

    public void setsamplerun(SampleRun samplerun) {
        this.samplerun = samplerun;
    }

    public String getFolderUrl() {
        return this.folderUrl;
    }

    public void setFolderUrl(String folderUrl) {
        this.folderUrl = folderUrl;
    }

    public String getbcltofastqcommnad() {
        return this.bclToFastqCommnad;
    }

    public void setbcltofastqcommnad(String bcltofastqcommnad) {
        this.bclToFastqCommnad = bcltofastqcommnad;
    }

    public String getFastqcreferencer1() {
        return this.fastqCReferencer1;
    }

    public void setFastqcreferencer1(String fastqcreferencer1) {
        this.fastqCReferencer1 = fastqcreferencer1;
    }

    public String getFastqcreferencer2() {
        return this.fastqCReferencer2;
    }

    public void setFastqcreferencer2(String fastqcreferencer2) {
        this.fastqCReferencer2 = fastqcreferencer2;
    }

    public Set<AlignedData> getAligneddatas() {
        return this.alignedDatas;
    }

    public void setAligneddatas(Set<AlignedData> aligneddatas) {
        this.alignedDatas = aligneddatas;
    }

    public Set<AnnotatedData> getAnnotateddatas() {
        return this.annotatedDatas;
    }

    public void setAnnotateddatas(Set<AnnotatedData> annotateddatas) {
        this.annotatedDatas = annotateddatas;
    }

}
