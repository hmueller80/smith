/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.MinimalRunDTO;

/**
 *
 * @author dbarreca
 */
public class MinimalRunDTOImpl implements MinimalRunDTO {
    private final Integer id;
    private String flowCell;
    private UserDTO operator;

    public MinimalRunDTOImpl(Integer id, String flowCell, UserDTO operator) {
        this.id = id;
        this.flowCell = flowCell;
        this.operator = operator;
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

  
    
}
