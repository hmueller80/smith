/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service;

/**
 *
 * @author dbarreca
 */
public class PersistedSampleReceipt {
    private Integer Id;
    private String sampleName;

    public PersistedSampleReceipt(Integer Id, String sampleName) {
        this.Id = Id;
        this.sampleName = sampleName;
    }

    public Integer getId() {
        return Id;
    }

    public String getSampleName() {
        return sampleName;
    }
    
    
}
