/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface RequestLibraryDTO {

    void addSample(RequestSampleDTO sample);
    
    Integer getId();

    @JsonIgnore
    RequestLibraryDTO getCopyWithoutSamples();

    Double getDnaConcentration();

    Integer getLanes();

    String getName();

    Integer getReadLength();

    String getReadMode();

    RequestSampleDTO getSample(String sampleName);

    List<RequestSampleDTO> getSamples();

    Double getTotalSize();

    String getType();

    String getUuid();

    Double getVolume();

    void resetSamples();

    void setDnaConcentration(Double dnaConcentration);

    void setLanes(Integer lanes);

    void setName(String name);

    @JsonIgnore
    void setParametersFromCopy(RequestLibraryDTO theCopy);

    void setReadLength(Integer readLength);

    void setReadMode(String readMode);

    void setTotalSize(Double totalSize);

    void setType(String type);

    void setVolume(Double volume);
    
}
