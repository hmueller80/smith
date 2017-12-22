/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.generic;

import java.util.List;

/**
 *
 * @author dbarreca
 */
public interface Library {
     public String getName();
     public List<? extends Sample> getSamples();
}
