/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author dbarreca
 */
public class RequestLibraryDTOImpl implements RequestLibraryDTO {
    
    private final Integer id;
    private final String  uuid;
    private String name;
    private String type;
    private String readMode;
    private Integer readLength;
    private Integer lanes;
    private Double volume;
    private Double dnaConcentration;
    private Double totalSize;
    
    @JsonIgnore
    private Map<String, RequestSampleDTO> samples;

    protected RequestLibraryDTOImpl(Integer id) {
        this.id = id;
        uuid = UUID.randomUUID().toString();
        samples = new LinkedHashMap<>();
    }
    
    protected RequestLibraryDTOImpl() {
        id = null;
        uuid = UUID.randomUUID().toString();
        samples = new LinkedHashMap<>();
        type = "WGS";
        readMode = "SR";
        readLength = 50;
        lanes = 1;
        volume = 0.0;
        dnaConcentration = 0.0;
        totalSize = 0.0;
    }
    
    protected RequestLibraryDTOImpl(String uuid){
        id = null;
        this.uuid = uuid;
        samples = new LinkedHashMap<>();
        type = "WGS";
        readMode = "SR";
        readLength = 50;
        lanes = 1;
        volume = 0.0;
        dnaConcentration = 0.0;
        totalSize = 0.0;
    }

    @Override
    public Integer getId() {
        return id;
    }
 
    @Override
    public void setName(String name) {
        this.name = NameFilter.legalize(name.trim().toUpperCase());
        for (RequestSampleDTO sample: samples.values()) {
            sample.setLibrary(this.name);
        }
    }
        
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getReadMode() {
        return readMode;
    }

    @Override
    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    @Override
    public Integer getReadLength() {
        return readLength;
    }

    @Override
    public void setReadLength(Integer readLength) {
        this.readLength = readLength;
    }

    @Override
    public Integer getLanes() {
        return lanes;
    }

    @Override
    public void setLanes(Integer lanes) {
        this.lanes = lanes;
    }

    @Override
    public Double getVolume() {
        return volume;
    }

    @Override
    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @Override
    public Double getDnaConcentration() {
        return dnaConcentration;
    }

    @Override
    public void setDnaConcentration(Double dnaConcentration) {
        this.dnaConcentration = dnaConcentration;
    }

    @Override
    public Double getTotalSize() {
        return totalSize;
    }

    @Override
    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }
    
    @Override
    public List<RequestSampleDTO> getSamples(){
        return new LinkedList<>(samples.values());
    }
    
    @Override
    public RequestSampleDTO getSample(String sampleName){
        return samples.get(sampleName);
    }
    
    @Override
    public void addSample(RequestSampleDTO sample){
        samples.put(sample.getSampleName(), sample);
    }
    
    @Override
    public void resetSamples(){
        samples = new LinkedHashMap<>();
    }

    @Override
    public String getUuid() {
        return uuid;
    }
    
    
    @JsonIgnore
    @Override
    public RequestLibraryDTO getCopyWithoutSamples() {
        RequestLibraryDTO copy = new RequestLibraryDTOImpl(uuid);
        copy.setName(name);
        copy.setType(type);
        copy.setReadMode(readMode);
        copy.setReadLength(readLength);
        copy.setLanes(lanes);
        copy.setVolume(volume);
        copy.setDnaConcentration(dnaConcentration);
        copy.setTotalSize(totalSize);
        
        return copy;
    }
    
    @JsonIgnore
    @Override
    public void setParametersFromCopy(RequestLibraryDTO theCopy) {
        setName(theCopy.getName());
        type = theCopy.getType();
        readMode = theCopy.getReadMode();
        readLength = theCopy.getReadLength();
        lanes = theCopy.getLanes();
        volume=theCopy.getVolume();
        dnaConcentration = theCopy.getDnaConcentration();
        totalSize = theCopy.getTotalSize();
    }
    
}
