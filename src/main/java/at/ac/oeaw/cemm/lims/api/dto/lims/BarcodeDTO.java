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
public interface BarcodeDTO {
    
    public String getKitName();
    public String getIndexName();
    public IndexDTO getIndex();
    
    public void setKitName(String kitName);
    public void setIndexName(String indexName);
    public void setIndex(IndexDTO index);
}
