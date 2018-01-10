/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.lims;

import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.util.NameFilter;

/**
 *
 * @author dbarreca
 */
class IndexDTOImpl implements IndexDTO {
    private String index;
    private IndexType indexType;
    
    IndexDTOImpl(String index) {
        this.index = NameFilter.legalizeIndex(index);
    }
    
    private IndexDTOImpl(){}

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public void setIndex(String index) {
        this.index = NameFilter.legalizeIndex(index);
    }

    @Override
    public IndexType getType() {
        return indexType;
    }

    @Override
    public void setType(IndexType type) {
        this.indexType = type;
    }

}
