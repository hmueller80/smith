/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.indexes;

/**
 *
 * @author dbarreca
 */
public class IndexRow {
    private final String label;
    private final String sequence;
    private final boolean isIndex;
    private final String kitName;

    public IndexRow(String label, String sequence, boolean isIndex, String kitName) {
        this.label = label;
        this.sequence = sequence;
        this.isIndex = isIndex;
        this.kitName = kitName;
    }

    public String getLabel() {
        return label;
    }

    public String getSequence() {
        return sequence;
    }

    public boolean isIsIndex() {
        return isIndex;
    }
    
    public String getKitName(){
        return kitName;
    }

}
