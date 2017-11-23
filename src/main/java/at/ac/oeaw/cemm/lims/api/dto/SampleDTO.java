/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;

import java.util.Date;

/**
 *
 * @author dbarreca
 */
public interface SampleDTO {
    public static final String status_requested = "requested";
    public static final String status_queued = "queued";
    public static final String status_confirmed = "confirmed";
    public static final String status_running = "running";
    public static final String status_analyzed = "analyzed";
    
    String getAntibody();

    ApplicationDTO getApplication();

    Double getBioAnalyzerMolarity();

    Date getBioanalyzerDate();

    Double getBulkFragmentSize();

    String getComment();

    Double getConcentration();

    String getCostcenter();

    String getDescription();

    String getExperimentName();

    Integer getId();

    IndexDTO getIndex();

    String getName();

    String getOrganism();

    Date getRequestDate();

    String getStatus();

    Integer getSubmissionId();

    Double getTotalAmount();

    String getType();

    Boolean isSyntehsisNeeded();
    
    UserDTO getUser();
    
    String getLibraryName();
    
    void setAntibody(String antibody);

    void setApplication(ApplicationDTO application);

    void setBioAnalyzerMolarity(Double bioAnalyzerMolarity);

    void setBioanalyzerDate(Date bioanalyzerDate);

    void setBulkFragmentSize(double bulkFragmentSize);

    void setComment(String comment);

    void setConcentration(double concentration);

    void setCostcenter(String costcenter);

    void setDescription(String description);

    void setExperimentName(String experimentName);

    void setIndex(IndexDTO index);

    void setName(String name);

    void setOrganism(String organism);

    void setRequestDate(Date requestDate);

    void setStatus(String status);

    void setSubmissionId(Integer submissionId);

    void setSyntehsisNeeded(boolean syntehsisNeeded);

    void setTotalAmount(double totalAmount);

    void setType(String type);
    
    void setUser(UserDTO user);
    
    void setLibraryName(String libraryName);
    
}
