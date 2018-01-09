/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.analysis;

/**
 *
 * @author hmueller
 */
public interface AnalysisManager {
    
    
    public void run();
    
    public void resetDemux(String runFolder) throws Exception;
    
}
