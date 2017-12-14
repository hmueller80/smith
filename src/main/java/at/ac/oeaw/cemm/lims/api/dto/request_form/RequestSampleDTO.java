/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

/**
 *
 * @author dbarreca
 */
public interface RequestSampleDTO {

    Integer getId();
            
    String getI5Adapter();

    String getI5Index();

    String getI7Adapter();

    String getI7Index();

    String getLibrary();

    String getOrganism();

    String getPrimerName();

    String getPrimerSequence();

    String getPrimerType();

    String getSampleDescription();

    String getSampleName();

    void setI5Adapter(String i5Adapter);

    void setI5Index(String i5Index);

    void setI7Adapter(String i7Adapter);

    void setI7Index(String i7Index);

    void setLibrary(String library);

    void setOrganism(String organism);

    void setPrimerName(String primerName);

    void setPrimerSequence(String primerSequence);

    void setPrimerType(String primerType);

    void setSampleDescription(String sampleDescription);

    void setSampleName(String sampleName);
    
}
