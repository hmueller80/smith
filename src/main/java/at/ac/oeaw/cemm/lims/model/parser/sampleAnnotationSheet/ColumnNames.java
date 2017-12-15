/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import java.util.ArrayList;

/**
 *
 * @author hMueller
 */
public class ColumnNames {
    
    String outputColumnName;
    ArrayList<String> columnNames;
    
    public ColumnNames(String outputColName, String... alternativeColNames) {
        outputColumnName = outputColName;
        columnNames = new ArrayList<String>();
        for(String s : alternativeColNames){
            if(!columnNames.contains(s)){
                columnNames.add(s);
            }
        }
    }

    public String getOutputColumnName() {
        return outputColumnName;
    }

    public ArrayList<String> getColumnNames() {
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
    
    
    
}
