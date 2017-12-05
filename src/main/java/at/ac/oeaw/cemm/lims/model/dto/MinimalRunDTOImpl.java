/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;

import at.ac.oeaw.cemm.lims.api.dto.RunDTO;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
public class MinimalRunDTOImpl implements RunDTO {

    private final Integer id;
    private String flowCell;
    private UserDTO operator;
    private String runFolder;

    public MinimalRunDTOImpl(Integer id, String flowCell, UserDTO operator, String runFolder) {
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
    public boolean equals(Object other) {
        if (other==null) return false;
        if (other instanceof RunDTO) {
            RunDTO otherRun = (RunDTO) other;
            if (!Objects.equals(otherRun.getId(), this.getId())) return false;
            if (!Objects.equals(otherRun.getFlowCell(), this.getFlowCell()))return false;
            if (!Objects.equals(otherRun.getRunFolder(), this.getRunFolder())) return false;
            
            return true;
        
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.flowCell);
        hash = 59 * hash + Objects.hashCode(this.runFolder);
        return hash;
    }
    

}
