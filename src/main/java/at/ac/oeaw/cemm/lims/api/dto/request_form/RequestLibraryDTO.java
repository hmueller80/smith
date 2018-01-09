/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import at.ac.oeaw.cemm.lims.api.dto.generic.RunRequest;

/**
 *
 * @author dbarreca
 */
public interface RequestLibraryDTO extends Library, RunRequest{

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

    @Override
    Integer getReadLength();

    @Override
    String getReadMode();

    Double getTotalSize();

    String getUuid();

    Double getVolume();
    
    Boolean isNameEditable();

    void resetSamples();

    void setDnaConcentration(Double dnaConcentration);

    void setLanes(Integer lanes);

    void setName(String name);

    @JsonIgnore
    void setParametersFromCopy(RequestLibraryDTO theCopy);

    void setReadLength(Integer readLength);

    void setReadMode(String readMode);

    void setTotalSize(Double totalSize);

    void setVolume(Double volume);
    
}
