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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "aligneddata")
public class AlignedData implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "processed_id")
    private Integer processedId;

    @ManyToOne
    @JoinColumn(name = "raw_id")
    private RawData rawData;

    @Column(name = "file_URL")
    private String fileUrl;

    @Column(name = "assembly")
    private String assembly;

    @Column(name = "fileformat")
    private String fileFormat;

    @Column(name = "alalgo")
    private String alalgo;

    @Column(name = "alparams")
    private String alparams;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

//	@Column(name = "annotateddatas")
//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "stock")
    @OneToMany(mappedBy = "alignedData", fetch = FetchType.EAGER)
//    @JoinColumn(name="aligned_id")
    private Set<AnnotatedData> annotatedDatas = new HashSet<AnnotatedData>(0);

    public AlignedData() {
    }

    public AlignedData(RawData rawdata, String fileUrl, String assembly,
            String fileformat, String alalgo) {
        this.rawData = rawdata;
        this.fileUrl = fileUrl;
        this.assembly = assembly;
        this.fileFormat = fileformat;
        this.alalgo = alalgo;
    }

    public AlignedData(RawData rawdata, String fileUrl, String assembly,
            String fileformat, String alalgo, String alparams, String name,
            String description, Set<AnnotatedData> annotateddatas) {
        this.rawData = rawdata;
        this.fileUrl = fileUrl;
        this.assembly = assembly;
        this.fileFormat = fileformat;
        this.alalgo = alalgo;
        this.alparams = alparams;
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

    public RawData getrawdata() {
        return this.rawData;
    }

    public void setsawdata(RawData rawData) {
        this.rawData = rawData;
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

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getAlalgo() {
        return this.alalgo;
    }

    public void setAlalgo(String alalgo) {
        this.alalgo = alalgo;
    }

    public String getAlparams() {
        return this.alparams;
    }

    public void setAlparams(String alparams) {
        this.alparams = alparams;
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

    public void setAnnotatedDatas(Set<AnnotatedData> annotatedDatas) {
        this.annotatedDatas = annotatedDatas;
    }

}
