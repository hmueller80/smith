/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface RequestLibraryDTO extends Library{

    void addSample(RequestSampleDTO sample);
    
    Integer getId();

    @JsonIgnore
    RequestLibraryDTO getCopyWithoutSamples();
    
    @Override
    List<RequestSampleDTO> getSamples();

    @Override
    String getName();
    
    Double getDnaConcentration();

    Integer getLanes();

    Integer getReadLength();

    String getReadMode();

    Double getTotalSize();

    String getApplicationName();

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

    void setApplicationName(String type);

    void setVolume(Double volume);
    
}
