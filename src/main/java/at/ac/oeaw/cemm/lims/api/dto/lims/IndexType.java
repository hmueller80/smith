/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto.lims;

/**
 *
 * @author dbarreca
 */
public enum IndexType {
    i7,
    i5;
    
    
    
    public static IndexType decode(String indexType) throws Exception{
        if (indexType.trim().equalsIgnoreCase("i7")) return i7;
        if (indexType.trim().equalsIgnoreCase("i5")) return i5;
        
        throw new Exception ("Invalid index type "+indexType);
    }
}
