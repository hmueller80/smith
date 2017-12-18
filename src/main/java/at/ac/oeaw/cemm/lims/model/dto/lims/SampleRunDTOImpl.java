/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public class SampleRunDTOImpl implements SampleRunDTO {
    private final Integer id;
    private final SampleDTO sample;
    private final UserDTO operator;
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
            String runFolder,
            Boolean isControl
    ) {
        this.id = id;
        this.sample = sample;
        this.operator = operator;
        this.flowCell = flowCell;
        if (lanes!=null){
            this.lanes = lanes;
        }else{
            this.lanes= new HashSet<>();
        }
        this.runFolder = runFolder;
        this.isControl = isControl;
    }
    
    @Override
    public Integer getRunId() {
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

    @Override
    public Set<String> getLanes() {
        return lanes;
    }

    @Override
    public void setFlowcell(String flowCell) {
        this.flowCell= flowCell;
    }

    @Override
    public void addLane(String lane) {
        this.lanes.add(lane);
    }

    @Override
    public void setIsControl(boolean isControl) {
        this.isControl = isControl;
    }

    @Override
    public void setRunFolder(String runFolder) {
        this.runFolder = runFolder;
    }

    @Override
    public void setLanes(Set<String> lanes) {
        if (lanes == null){
            this.lanes = new HashSet<>();
        }else{
            this.lanes = lanes;
        }
    }
    
    
}
