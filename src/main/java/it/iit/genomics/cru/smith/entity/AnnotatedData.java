package it.iit.genomics.cru.smith.entity;

// Generated Nov 19, 2013 10:50:39 AM by Hibernate Tools 3.2.1.GA
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

@Entity
@Table(name = "annotateddata")
public class AnnotatedData implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "processed_id")
    private Integer processedId;

    @ManyToOne
    @JoinColumn(name = "rawdata_id")
    private RawData rawData;

    @ManyToOne
    @JoinColumn(name = "aligned_id")
    private AlignedData alignedData;

    @ManyToOne
    @JoinColumn(name = "inputdata_id")
    private AnnotatedData annotatedData;

    @Column(name = "file_URL")
    private String fileUrl;

    @Column(name = "assembly")
    private String assembly;

    @Column(name = "fileformat")
    private String fileFormat;

    @Column(name = "analgo")
    private String analgo;

    @Column(name = "anparams")
    private String anparams;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "inputdata_id")
    private Set<AnnotatedData> annotatedDatas = new HashSet<AnnotatedData>(0);

    public AnnotatedData() {
    }

    public AnnotatedData(RawData rawdata, String fileUrl, String assembly,
            String fileformat, String analgo) {
        this.rawData = rawdata;
        this.fileUrl = fileUrl;
        this.assembly = assembly;
        this.fileFormat = fileformat;
        this.analgo = analgo;
    }

    public AnnotatedData(RawData rawdata, AlignedData aligneddata,
            AnnotatedData annotateddata, String fileUrl, String assembly,
            String fileformat, String analgo, String anparams, String name,
            String description, Set<AnnotatedData> annotateddatas) {
        this.rawData = rawdata;
        this.alignedData = aligneddata;
        this.annotatedData = annotateddata;
        this.fileUrl = fileUrl;
        this.assembly = assembly;
        this.fileFormat = fileformat;
        this.analgo = analgo;
        this.anparams = anparams;
        this.name = name;
        this.description = description;
        this.annotatedDatas = annotateddatas;
    }

    public Integer getProcessedId() {
        return this.processedId;
    }

    public void setProcessedId(Integer processedId) {
        this.processedId = processedId;
    }

    public RawData getRawdata() {
        return this.rawData;
    }

    public void setRawdata(RawData rawdata) {
        this.rawData = rawdata;
    }

    public AlignedData getAligneddata() {
        return this.alignedData;
    }

    public void setAligneddata(AlignedData aligneddata) {
        this.alignedData = aligneddata;
    }

    public AnnotatedData getAnnotateddata() {
        return this.annotatedData;
    }

    public void setAnnotateddata(AnnotatedData annotateddata) {
        this.annotatedData = annotateddata;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAssembly() {
        return this.assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public String getFileformat() {
        return this.fileFormat;
    }

    public void setFileformat(String fileformat) {
        this.fileFormat = fileformat;
    }

    public String getAnalgo() {
        return this.analgo;
    }

    public void setAnalgo(String analgo) {
        this.analgo = analgo;
    }

    public String getAnparams() {
        return this.anparams;
    }

    public void setAnparams(String anparams) {
        this.anparams = anparams;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AnnotatedData> getAnnotateddatas() {
        return this.annotatedDatas;
    }

    public void setAnnotateddatas(Set<AnnotatedData> annotateddatas) {
        this.annotatedDatas = annotateddatas;
    }

}
