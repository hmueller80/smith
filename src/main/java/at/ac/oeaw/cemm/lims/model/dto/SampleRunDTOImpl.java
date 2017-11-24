/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class SampleRunDTOImpl implements SampleRunDTO {
    private final Integer id;
    private SampleDTO sample;
    private UserDTO operator;
    private String flowCell;
    private Set<String> lanes;
    private boolean isControl = false;
    private String runFolder;

    public SampleRunDTOImpl(
            Integer id, 
            SampleDTO sample, 
            UserDTO operator, 
            String flowCell, 
            Set<String> lanes, 
            String runFolder
    ) {
        this.id = id;
        this.sample = sample;
        this.operator = operator;
        this.flowCell = flowCell;
        this.lanes = lanes;
        this.runFolder = runFolder;
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public SampleDTO getSample() {
        return sample;
    }

    @Override
    public UserDTO getOperator() {
        return operator;
    }

    @Override
    public String getFlowcell() {
        return flowCell;
    }

    @Override
    public String getLanesString() {
        StringBuilder sb = null;
        for (String lane:lanes) {
            if (sb==null){
                sb=new StringBuilder();
            }else{
                sb.append(' ');
            }
            sb.append(lane);
        }   
        return sb==null ? "" : sb.toString();
    }

    @Override
    public Boolean getIsControl() {
        return isControl;
    }

    @Override
    public String getRunFolder() {
        return runFolder;
    }
    
    
}
