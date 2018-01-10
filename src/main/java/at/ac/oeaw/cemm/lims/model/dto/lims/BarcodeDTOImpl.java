/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.BarcodeDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 *
 * @author dbarreca
 */
public class BarcodeDTOImpl implements BarcodeDTO{
    private String kitName;
    private String indexName;
    
    @JsonDeserialize(as = IndexDTOImpl.class)
    private IndexDTO index;

    public BarcodeDTOImpl(String kitName, String indexName, IndexDTO index) {
        this.kitName = kitName;
        this.indexName = indexName;
        this.index = index;
    }   
    
    private BarcodeDTOImpl(){
        
    }
    
    @Override
    public String getKitName() {
        return kitName;
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    @Override
    public IndexDTO getIndex() {
        return index;
    }

    @Override
    public void setKitName(String kitName) {
        this.kitName = NameFilter.legalize(kitName);
    }

    @Override
    public void setIndexName(String indexName) {
        this.indexName = NameFilter.legalize(indexName);
    }

    @Override
    public void setIndex(IndexDTO index) {
        this.index = index;
    }
    
}
