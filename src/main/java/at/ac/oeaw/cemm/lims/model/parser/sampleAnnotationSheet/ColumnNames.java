/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author hMueller
 */
public class ColumnNames {
    
    private final String outputColumnName;
    private final Set<String> columnNames;
    private final Boolean required;
    private final DataType columnType;
    
    public enum DataType {
        STRING,
        INTEGER,
        DOUBLE,
        DATE
    }
    
    public ColumnNames(String outputColName, String... alternativeColNames) {
        outputColumnName = outputColName;
        columnNames = new HashSet<String>();
        for (String s : alternativeColNames) {
            columnNames.add(s);
        }
        this.required = false;
        this.columnType = DataType.STRING;
    }

    public ColumnNames(String outputColName, Boolean required, String... alternativeColNames) {
        outputColumnName = outputColName;
        columnNames = new HashSet<String>();
        for (String s : alternativeColNames) {
            columnNames.add(s);
        }
        this.required = required;
        this.columnType = DataType.STRING;
    }
    
      public ColumnNames(String outputColName, Boolean required, DataType columnType, String... alternativeColNames) {
        outputColumnName = outputColName;
        columnNames = new HashSet<String>();
        for (String s : alternativeColNames) {
            columnNames.add(s);
        }
        this.required = false;
        this.columnType = columnType;
    }

    public Boolean isRequired() {
        return required;
    }

    public DataType getColumnType() {
        return columnType;
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
            return isSameOutputName;            
        }
        if (other instanceof String) {
            return columnNames.contains(((String) other).trim());
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.outputColumnName);
    }
    
}
