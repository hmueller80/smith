/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.entity;

/**
 *
 * @author dbarreca
 */
public class MinimalRunEntity {
    Integer id;
    UserEntity operator;
    String flowCell;
	private String runFolder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getOperator() {
        return operator;
    }

    public void setOperator(UserEntity operator) {
        this.operator = operator;
    }

    public String getFlowCell() {
        return flowCell;
    }

    public void setFlowCell(String flowCell) {
        this.flowCell = flowCell;
    }

    public void setRunFolder(String runFolder) {
		this.runFolder=runFolder;
	}

	public String getRunFolder() {
		return this.runFolder;
	}

 
    
    
}
