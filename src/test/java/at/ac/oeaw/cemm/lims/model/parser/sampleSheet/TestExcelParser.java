/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleSheet;

import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParser;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.SampleAnnotationParser;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.LibrarySubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SampleSubmission;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.SequencingRequestSubmission;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author dbarreca
 */
public class TestExcelParser {
    
    @Ignore
    public void testExcelPaser() throws ParsingException {
        File file = new File("/Users/dbarreca/Dev/test_files/1044_MGL010_sampleannotationsheetI.xlsx");
        
        ExcelParser parser = new ExcelParser(file);
        
        printSheet(parser.getSummarySheet(),"Summary");
        printSheet(parser.getSamplesSheet(),"Samples");
        printSheet(parser.getLibrariesSheet(),"Libraries");
        printSheet(parser.getRequestsSheet(),"Requests");
        
        
        
    }
    
    @Ignore
    public void testSampleAnnotationPaser() throws ParsingException {
        File file = new File("/Users/dbarreca/Dev/test_files/1044_MGL010_sampleannotationsheetI.xlsx");
        
        SampleAnnotationParser parser = new SampleAnnotationParser(file);
        
        System.out.println("=============== " + "SUMMARY" + "=============== ");
        System.out.println(parser.getSummary().toString());
        
        System.out.println("=============== " + "SAMPLES" + "=============== ");
        for (SampleSubmission submission: parser.getSampleSubmission()) {
                    System.out.println(submission.toString());

        }
        
        System.out.println("=============== " + "LIBRARIES" + "=============== ");
        for (LibrarySubmission submission: parser.getLibrarySubmission()) {
                    System.out.println(submission.toString());

        }
        
        System.out.println("=============== " + "REQUESTS" + "=============== ");
        for (SequencingRequestSubmission submission: parser.getSequencingRequestSubmission()) {
                    System.out.println(submission.toString());
        }    
        
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
