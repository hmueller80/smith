/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

/**
 *
 * @author dbarreca
 */
public class PersistedEntityReceipt {
    private Integer Id;
    private String entityName;

    public PersistedEntityReceipt(Integer Id, String entityName) {
        this.Id = Id;
        this.entityName = entityName;
    }

    public Integer getId() {
        return Id;
    }

    public String getEntityName() {
        return entityName;
    }
    
    
}
