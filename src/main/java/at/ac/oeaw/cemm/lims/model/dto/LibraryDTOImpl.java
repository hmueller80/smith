/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
class LibraryDTOImpl implements LibraryDTO,Serializable  {
    private Integer id;
    private String name;
    private List<SampleDTO> samples=new LinkedList<>();
 
    LibraryDTOImpl(String name, Integer id){
        this.name=name;
        this.id  = id;
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
    public List<SampleDTO> getSamples() {
        return samples;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LibraryDTOImpl other = (LibraryDTOImpl) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
