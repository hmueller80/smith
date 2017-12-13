/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.requestform;

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
public class Library {
    private String uuid;
    private String name;
    private String type;
    private String readMode;
    private Integer readLength;
    private Integer lanes;
    private Double volume;
    private Double dnaConcentration;
    private Double totalSize;
    
    @JsonIgnore
    private Map<String, RequestedSample> samples;

    public Library() {
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
    

    public void setName(String name) {
        this.name = NameFilter.legalize(name.trim().toUpperCase());
        for (RequestedSample sample: samples.values()) {
            sample.setLibrary(this.name);
        }
    }
        
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReadMode() {
        return readMode;
    }

    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    public Integer getReadLength() {
        return readLength;
    }

    public void setReadLength(Integer readLength) {
        this.readLength = readLength;
    }

    public Integer getLanes() {
        return lanes;
    }

    public void setLanes(Integer lanes) {
        this.lanes = lanes;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getDnaConcentration() {
        return dnaConcentration;
    }

    public void setDnaConcentration(Double dnaConcentration) {
        this.dnaConcentration = dnaConcentration;
    }

    public Double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }
    
    public List<RequestedSample> getSamples(){
        return new LinkedList<>(samples.values());
    }
    
    public RequestedSample getSample(String sampleName){
        return samples.get(sampleName);
    }
    
    public void addSample(RequestedSample sample){
        samples.put(sample.getSampleName(), sample);
    }
    
    public void resetSamples(){
        samples = new LinkedHashMap<>();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
    @JsonIgnore
    public Library getCopyWithoutSamples() {
        Library copy = new Library();
        copy.setUuid(uuid);
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
    public void setParametersFromCopy(Library theCopy) {
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
