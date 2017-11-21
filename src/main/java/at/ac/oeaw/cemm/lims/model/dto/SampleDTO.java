/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import java.util.Date;

/**
 *
 * @author dbarreca
 */
public class SampleDTO {
    private ApplicationDTO application;
    private String organism;
    private String type;
    private String antibody;
    private boolean syntehsisNeeded;
    private double concentration;
    private double totalAmount;
    private double bulkFragmentSize;
    private String costcenter;
    private String status;
    private String name;
    private String comment;
    private String description;
    private Date requestDate;
    private Date bioanalyzerDate;
    private double bioAnalyzerMolarity;
    private Integer submissionId;
    private String experimentName;
    private IndexDTO index;

    public SampleDTO(){}
    
    public SampleDTO(ApplicationDTO application, String organism, String type, String antibody, boolean syntehsisNeeded, double concentration, double totalAmount, double bulkFragmentSize, String costcenter, String status, String name, String comment, String description, Date requestDate, Date bioanalyzerDate, double bioAnalyzerMolarity, Integer submissionId, String experimentName, IndexDTO index) {
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
    }
    


    public ApplicationDTO getApplication() {
        return application;
    }

    public String getOrganism() {
        return organism;
    }
 
    public String getType() {
        return type;
    }

    public String getAntibody() {
        return antibody;
    }

    public boolean isSyntehsisNeeded() {
        return syntehsisNeeded;
    }

    public double getConcentration() {
        return concentration;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getBulkFragmentSize() {
        return bulkFragmentSize;
    }

    public String getCostcenter() {
        return costcenter;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return description;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Date getBioanalyzerDate() {
        return bioanalyzerDate;
    }

    public double getBioAnalyzerMolarity() {
        return bioAnalyzerMolarity;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public String getExperimentName() {
        return experimentName;
    }
    
    public IndexDTO getIndex() {
        return index;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAntibody(String antibody) {
        this.antibody = antibody;
    }

    public void setSyntehsisNeeded(boolean syntehsisNeeded) {
        this.syntehsisNeeded = syntehsisNeeded;
    }

    public void setConcentration(double concentration) {
        this.concentration = concentration;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBulkFragmentSize(double bulkFragmentSize) {
        this.bulkFragmentSize = bulkFragmentSize;
    }

    public void setCostcenter(String costcenter) {
        this.costcenter = costcenter;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public void setBioanalyzerDate(Date bioanalyzerDate) {
        this.bioanalyzerDate = bioanalyzerDate;
    }

    public void setBioAnalyzerMolarity(double bioAnalyzerMolarity) {
        this.bioAnalyzerMolarity = bioAnalyzerMolarity;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public void setIndex(IndexDTO index) {
        this.index = index;
    }
   
    
}
