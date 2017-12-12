/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.requestform;

import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public class Library {
    private String name;
    private String type;
    private String readMode;
    private Integer readLength;
    private Integer lanes;
    private Double volume;
    private Double dnaConcentration;
    private Double totalSize;
    private Map<String, RequestedSample> samples;

    public Library() {
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
    
}
