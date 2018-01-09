/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;

/**
 *
 * @author dbarreca
 */
public class LaneSampleRow {
    private SampleRunDTO sample;
    private String lane;

    public LaneSampleRow(SampleRunDTO sample, String lane) {
        this.sample = sample;
        this.lane = lane;
    }

    public SampleRunDTO getSample() {
        return sample;
    }

    public String getLane() {
        return lane;
    }
    
    
}
