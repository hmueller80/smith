/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleSheet;

import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author dbarreca
 */
public class TestExcelParser {
    
    @Test
    public void testExcelPaser() throws ParsingException {
        ExcelParser parser = new ExcelParser("/Users/dbarreca/Dev/test_files/1044_MGL010_sampleannotationsheetI.xlsx");
        
        printSheet(parser.getSummarySheet(),"Summary");
        printSheet(parser.getSamplesSheet(),"Samples");
        printSheet(parser.getLibrariesSheet(),"Libraries");
        printSheet(parser.getRequestsSheet(),"Requests");
        
        
        
    }
    
    private void printSheet(ArrayList<ArrayList<String>> sheet, String title) {
        System.out.println("=============== " + title + "=============== ");
        System.out.println();
        StringBuilder sb = new StringBuilder();
        for (List<String> row: sheet) {
            for (String column: row) {
                sb.append(column);
                sb.append(";");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        System.out.println();
    }
}
