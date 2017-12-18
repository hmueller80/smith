/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.io.Serializable;

/**
 *
 * @author dbarreca
 */
class ApplicationDTOImpl implements ApplicationDTO, Serializable {
    private Integer readLength;
    private String readMode;
    private String instrument;
    private String applicationName;
    private Integer depth;

    private ApplicationDTOImpl() {};
    
    ApplicationDTOImpl(Integer readLength, String readMode, String instrument, String applicationName, Integer depth) {
        this.readLength = readLength;
        this.readMode = NameFilter.legalizeReadMode(readMode);
        this.instrument = instrument;
        this.applicationName = applicationName;
        this.depth = depth;
    }

    ApplicationDTOImpl(String applicationName, String instrument) {
        this.applicationName = applicationName ==null ? "undefined" : applicationName;
        this.instrument = instrument == null ? "HiSeq2000" : instrument;
        
        if (ApplicationDTO.CHIP_SEQ.equals(applicationName)) {
            //"Single read : 30 mio reads : one fiths of a lane : 50 bases"
            readMode = "SR";
            depth = 30;
            readLength =50;
        } else if (ApplicationDTO.DNA_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 100 bases";
            this.readMode = "PE";
            this.depth = 70;
            this.readLength=100;

        } else if (ApplicationDTO.EXO_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 100 bases";
            this.readMode = "PE";
            this.depth =70;
            this.readLength=100;

        } else if (ApplicationDTO.MRNA_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 50 bases";
            this.readMode = "PE";
            this.depth=70;
            this.readLength=50;

        } else if (ApplicationDTO.RNA_SEQ.equals(applicationName)) {
            //"Paired end : 35 mio reads : one fourth of a lane : 50 bases";
            this.readMode = "PE";
            this.depth=35;
            this.readLength=50;

        } else {
            //"Single read : 30 mio reads : one fiths of a lane : 50 bases";
            this.readMode = "SR";
            this.depth=30;
            this.readLength=50;
        }
    }

   
    
    @Override
    public Integer getReadLength() {
        return readLength;
    }

    @Override
    public String getReadMode() {
        return readMode;
    }

    @Override
    public String getInstrument() {
        return instrument;
    }

   
    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
     public Integer getDepth() {
        return depth;
    }

    @Override
    public void setReadLength(Integer readLength) {
        this.readLength = readLength;
    }

    @Override
    public void setReadMode(String readMode) {
        this.readMode = NameFilter.legalizeReadMode(readMode);;
    }

    @Override
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public void setDepth(Integer depth) {
        this.depth = depth;
    }
    
    @Override
    public ApplicationDTO getCopy(){
       ApplicationDTO newApp = new ApplicationDTOImpl();
       newApp.setApplicationName(applicationName!=null ? applicationName : "undefined");
       newApp.setReadLength(readLength);
       newApp.setDepth(depth);
       newApp.setInstrument(instrument!=null ? instrument: "HiSeq2000");
       newApp.setReadMode(readMode!=null ? readMode : "SR");
       
       return newApp;
    }
     
    
    
    
}
