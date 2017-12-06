/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dbarreca
 */
class SampleDTOImpl implements SampleDTO, Serializable {
    
    private final Integer id;
    private ApplicationDTO application;
    private String organism;
    private String type;
    private String antibody;
    private Boolean syntehsisNeeded=false;
    private Double concentration;
    private Double totalAmount;
    private Double bulkFragmentSize;
    private String costcenter;
    private String status;
    private String name;
    private String comment;
    private String description;
    private Date requestDate;
    private Date bioanalyzerDate;
    private Double bioAnalyzerMolarity;
    private Integer submissionId;
    private String experimentName;
    private IndexDTO index;
    private UserDTO user;
    private String libraryName;

    SampleDTOImpl(Integer id){
        this.id=id;
    }
    
    SampleDTOImpl(Integer id,
            ApplicationDTO application, 
            String organism, 
            String type, 
            String antibody, 
            Boolean syntehsisNeeded, 
            Double concentration, 
            Double totalAmount, 
            Double bulkFragmentSize, 
            String costcenter, 
            String status, 
            String name, 
            String comment, 
            String description, 
            Date requestDate, 
            Date bioanalyzerDate, 
            Double bioAnalyzerMolarity, 
            Integer submissionId, 
            String experimentName, 
            IndexDTO index,
            UserDTO user) {
        
        this.id = id;
        this.application = application;
        this.organism = organism;
        this.type = type;
        this.antibody = antibody;
        this.syntehsisNeeded = syntehsisNeeded;
        this.concentration = concentration;
        this.totalAmount = totalAmount;
        this.bulkFragmentSize = bulkFragmentSize;
        this.costcenter = costcenter;
        this.status = status;
        this.name = name;
        this.comment = comment;
        this.description = description;
        this.requestDate = requestDate;
        this.bioanalyzerDate = bioanalyzerDate;
        this.bioAnalyzerMolarity = bioAnalyzerMolarity;
        this.submissionId = submissionId;
        this.experimentName = experimentName;
        this.index = index;
        this.user = user;
    }
    
    @Override
    public ApplicationDTO getApplication() {
        return application;
    }

    @Override
    public String getOrganism() {
        return organism;
    }
 
    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getAntibody() {
        return antibody;
    }

    @Override
    public Boolean isSyntehsisNeeded() {
        return syntehsisNeeded;
    }

    @Override
    public Double getConcentration() {
        return concentration;
    }

    @Override
    public Double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public Double getBulkFragmentSize() {
        return bulkFragmentSize;
    }

    @Override
    public String getCostcenter() {
        return costcenter;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getRequestDate() {
        return requestDate;
    }

    @Override
    public Date getBioanalyzerDate() {
        return bioanalyzerDate;
    }

    @Override
    public Double getBioAnalyzerMolarity() {
        return bioAnalyzerMolarity;
    }

    @Override
    public Integer getSubmissionId() {
        return submissionId;
    }

    @Override
    public String getExperimentName() {
        return experimentName;
    }
    
    @Override
    public IndexDTO getIndex() {
        return index;
    }
    
    @Override
    public String getLibraryName() {
        return libraryName;
    }

    @Override
    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    @Override
    public void setOrganism(String organism) {
        this.organism = organism;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setAntibody(String antibody) {
        this.antibody = antibody;
    }

    @Override
    public void setSyntehsisNeeded(Boolean syntehsisNeeded) {
        this.syntehsisNeeded = syntehsisNeeded;
    }

    @Override
    public void setConcentration(Double concentration) {
        this.concentration = concentration;
    }

    @Override
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public void setBulkFragmentSize(Double bulkFragmentSize) {
        this.bulkFragmentSize = bulkFragmentSize;
    }

    @Override
    public void setCostcenter(String costcenter) {
        this.costcenter = costcenter;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public void setBioanalyzerDate(Date bioanalyzerDate) {
        this.bioanalyzerDate = bioanalyzerDate;
    }

    @Override
    public void setBioAnalyzerMolarity(Double bioAnalyzerMolarity) {
        this.bioAnalyzerMolarity = bioAnalyzerMolarity;
    }

    @Override
    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    @Override
    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    @Override
    public void setIndex(IndexDTO index) {
        this.index = index;
    }
    
    @Override
    public void setLibraryName(String libraryName) {
        this.libraryName=libraryName;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public UserDTO getUser() {
        return user;
    }

    @Override
    public void setUser(UserDTO user) {
        this.user=user;
    }
    
    
    
}
