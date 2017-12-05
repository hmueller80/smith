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
public class MinimalLibraryEntity {

    private Integer libraryId;
    private Integer requestId;
    private UserEntity requestor;
    private String libraryName;

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

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

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    
    
    
}
