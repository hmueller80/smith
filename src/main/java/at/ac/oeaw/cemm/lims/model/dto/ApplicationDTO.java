/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

/**
 *
 * @author dbarreca
 */
public class ApplicationDTO {
    private Integer readLength;
    private String readMode;
    private String instrument;
    private String applicationName;
    private Integer depth;

    public ApplicationDTO(Integer readLength, String readMode, String instrument, String applicationName, Integer depth) {
        this.readLength = readLength;
        this.readMode = readMode;
        this.instrument = instrument;
        this.applicationName = applicationName;
        this.depth = depth;
    }

   
    
    public Integer getReadLength() {
        return readLength;
    }

    public String getReadMode() {
        return readMode;
    }

    public String getInstrument() {
        return instrument;
    }

   
    public String getApplicationName() {
        return applicationName;
    }

     public Integer getDepth() {
        return depth;
    }
    
    
}
