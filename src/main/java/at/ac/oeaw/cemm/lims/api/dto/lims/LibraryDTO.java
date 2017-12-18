/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.generic.Library;
import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface LibraryDTO extends Library{

    void addSample(SampleDTO sample);
    
    Integer getId();
    
    @Override
    public String getName();
    
    @Override
    public List<SampleDTO> getSamples();

}
