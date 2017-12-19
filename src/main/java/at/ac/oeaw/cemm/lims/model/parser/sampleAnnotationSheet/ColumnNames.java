/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hMueller
 */
public class ColumnNames {
    
    private String outputColumnName;
    private Set<String> columnNames;
    private final Boolean required;
    
    public ColumnNames(String outputColName, String... alternativeColNames) {
        outputColumnName = outputColName;
        columnNames = new HashSet<String>();
        for (String s : alternativeColNames) {
            columnNames.add(s);
        }
        this.required = false;
    }

    public ColumnNames(String outputColName, Boolean required, String... alternativeColNames) {
        outputColumnName = outputColName;
        columnNames = new HashSet<String>();
        for (String s : alternativeColNames) {
            columnNames.add(s);
        }
        this.required = required;
    }

    public Boolean isRequired() {
        return required;
    }
    
    public String getOutputColumnName() {
        return outputColumnName;
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }
    
    public boolean matches(String queryName){
        queryName = queryName.trim();
        for(String s : columnNames){
            if(s.equals(queryName)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof ColumnNames){
            boolean isSameOutputName = ((ColumnNames) other).getOutputColumnName().equals(this.getOutputColumnName());
            boolean isSameRequired = ((ColumnNames) other).isRequired().equals(this.isRequired());
            boolean haveSameNames = this.columnNames.equals(((ColumnNames) other).getColumnNames());
            return isSameOutputName && isSameRequired && haveSameNames;            
        }
        if (other instanceof String) {
            return columnNames.contains(((String) other).trim());
        }
        
        return false;
    }
    
}
