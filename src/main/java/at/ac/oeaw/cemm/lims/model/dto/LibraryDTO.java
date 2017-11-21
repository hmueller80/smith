/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class LibraryDTO {
    private String name;
    private Set<SampleDTO> samples=new HashSet<>();
    
    public LibraryDTO(String name){
        this.name=name;
    }
    
    public void addSample(SampleDTO sample){
        samples.add(sample);
    }

    public String getName() {
        return name;
    }

    public Set<SampleDTO> getSamples() {
        return samples;
    }

}
