/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public class ExcelParserUtils {

    public static String extractFieldAsString(String fieldName, ArrayList<String> row, Map<String, Integer> header) {
        String fieldValue = "";

        if (header.containsKey(fieldName) && row.size() > header.get(fieldName)) {
            fieldValue = row.get(header.get(fieldName));
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
        DateFormat df = new SimpleDateFormat("dd.MM.YYYY");
        String result = df.format(date);
        return result;
    }
}
