/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.oeaw.cemm.lims.api.dto.LaneDTO;
import at.ac.oeaw.cemm.lims.api.dto.RunDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;

/**
 *
 * @author dbarreca
 */
public class MinimalRunDTOImpl implements RunDTO {
    private final Integer id;
    private String flowCell;
    private UserDTO operator;
    private String runFolder;
    private Map<String,LaneDTO> lanes = new HashMap<>();

    public MinimalRunDTOImpl(Integer id, String flowCell, UserDTO operator,String runFolder) {
        this.id = id;
        this.flowCell = flowCell;
        this.operator = operator;
        this.runFolder = runFolder;
    }
    
    
    public Integer getId() {
        return id;
    }

    public String getFlowCell() {
        return flowCell;
    }

    public UserDTO getOperator() {
        return operator;
    }


	@Override
	public String getRunFolder() {
		return runFolder;
	}


	@Override
	public Set<LaneDTO> getLanes() {
		return new HashSet<>(lanes.values());
	}


	@Override
	public LaneDTO getLane(String lane) {
		if (lanes.containsKey(lane)) {
			return lanes.get(lane);
		}
		return null;
	}


	@Override
	public void addSample(String laneName, SampleDTO sample) {
		LaneDTO lane = lanes.get(laneName);
		if (lane==null) {
			lane = new LaneDTOImpl(laneName);
			lanes.put(lane.getName(), lane);
		}
		lane.addSample(sample);
		
	}

  
    
}
