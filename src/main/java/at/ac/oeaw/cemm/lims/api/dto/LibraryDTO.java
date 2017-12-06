/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;

import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface LibraryDTO {

    void addSample(SampleDTO sample);

    String getName();

    List<SampleDTO> getSamples();
    
    Integer getId();
    
}
