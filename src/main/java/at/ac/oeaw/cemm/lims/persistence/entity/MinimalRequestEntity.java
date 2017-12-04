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
public class MinimalRequestEntity {

    private Integer requestId;
    private UserEntity requestor;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public UserEntity getRequestor() {
        return requestor;
    }

    public void setRequestor(UserEntity requestor) {
        this.requestor = requestor;
    }
    
    
}
