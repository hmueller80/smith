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
    
    IndexDTOImpl(String index, IndexType indexType) {
        this.index = NameFilter.legalizeIndex(index);
        this.indexType= indexType;
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

    @Override
    public void reverseComplementIndex() {
        this.index = getReverseComplement(this.index);
    }

     private String getReverseComplement(String sequence){
        sequence = sequence.toUpperCase();
        StringBuilder sb = new StringBuilder();
        for(int i = sequence.length() - 1; i >= 0; i--){
            char letter = sequence.charAt(i);
            switch(letter){
                case 'A': sb.append('T'); break;
                case 'C': sb.append('G'); break;
                case 'G': sb.append('C'); break;
                case 'T': sb.append('A'); break;
                default: sb.append(letter); break; 
            }
        }
        return sb.toString();
    }
}
