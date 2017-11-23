/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto;

import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;

/**
 *
 * @author dbarreca
 */
class IndexDTOImpl implements IndexDTO {
    private String index;

    IndexDTOImpl(String index) {
        this.index=index;
    }

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public void setIndex(String index) {
        this.index = index;
    }

}
