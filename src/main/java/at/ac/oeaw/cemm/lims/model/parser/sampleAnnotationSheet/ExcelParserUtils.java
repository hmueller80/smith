/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public class ExcelParserUtils {
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    
    public static String extractFieldAsString(String fieldName, ArrayList<String> row, Map<ColumnNames, Integer> header) {
        String fieldValue = "";
        ColumnNames columnToExtract = new ColumnNames(fieldName,fieldName);
        
        if (header.containsKey(columnToExtract) && row.size() > header.get(columnToExtract)) {
            fieldValue = row.get(header.get(columnToExtract));
        }

        return fieldValue;
    }

    public static String removeUnits(String s) {
        StringBuilder sb = new StringBuilder();
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i]==',') c[i]='.';
      
            if (Character.isDigit(c[i])|| c[i] == '.') {
                sb.append(c[i]);
            }
        }
        return sb.toString();
    }

    public static String getDateAsString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
    
    public static Date getStringAsDate(String dateString) {
        try {
           return new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
