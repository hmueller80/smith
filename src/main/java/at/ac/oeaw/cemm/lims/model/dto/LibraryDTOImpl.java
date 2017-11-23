/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
class LibraryDTOImpl implements LibraryDTO  {
    private String name;
    private Set<SampleDTO> samples=new HashSet<>();
    
    LibraryDTOImpl(String name){
        this.name=name;
    }
    
    @Override
    public void addSample(SampleDTO sample){
        samples.add(sample);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<SampleDTO> getSamples() {
        return samples;
    }


}
