/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.BidiMap;

/**
 *
 * @author hMueller
 */
public class SampleAnnotationParser extends ExcelParser {

    private SubmissionSummary summary;
    private ArrayList<SampleSubmission> sampleSubmission;
    private ArrayList<LibrarySubmission> librarySubmission;
    private ArrayList<SequencingRequestSubmission> sequencingRequestSubmission;

    public SampleAnnotationParser(File file) throws ParsingException {
        super(file);
        summary = parseSubmissionSummary();
        sampleSubmission = parseSampleSubmission();
        librarySubmission = parseLibrarySubmission();
        sequencingRequestSubmission = parseSequencingRequestSubmission();
    }

    public SubmissionSummary getSummary() {
        return summary;
    }

    public ArrayList<SampleSubmission> getSampleSubmission() {
        return sampleSubmission;
    }

    public ArrayList<LibrarySubmission> getLibrarySubmission() {
        return librarySubmission;
    }

    public ArrayList<SequencingRequestSubmission> getSequencingRequestSubmission() {
        return sequencingRequestSubmission;
    }
    
    

    private ArrayList<SequencingRequestSubmission> parseSequencingRequestSubmission() throws ParsingException {
        sequencingRequestSubmission = new ArrayList<>();
        ArrayList<Integer> headers = findHeaderRows(ExcelParserConstants.requestColumnNames, requestsSheet);

        if (headers.size() == 1) {
            int headerrowindex = headers.get(0);
            Map<ColumnNames, Integer> header = findColumnIndices(ExcelParserConstants.requestColumnNames, requestsSheet.get(headerrowindex));
  
            for (int i = headerrowindex + 1; i < requestsSheet.size(); i++) {
                ArrayList<String> row = requestsSheet.get(i);
                SequencingRequestSubmission rs = new SequencingRequestSubmission(row, header);
                sequencingRequestSubmission.add(rs);
            }
            return sequencingRequestSubmission;

        } else {
            throw new ParsingException("Request Submission", "Zero or multiple header lines found");
        }
    }

    private ArrayList<LibrarySubmission> parseLibrarySubmission() throws ParsingException {
        librarySubmission = new ArrayList<>();
        ArrayList<Integer> headers = findHeaderRows(ExcelParserConstants.libraryColumnNames, librariesSheet);

        if (headers.size() == 1) {
            System.out.println("one library header found");
            int headerrowindex = headers.get(0);
            Map<ColumnNames, Integer> header = findColumnIndices(ExcelParserConstants.libraryColumnNames, librariesSheet.get(headerrowindex));

            for (int i = headerrowindex + 1; i < librariesSheet.size(); i++) {
                ArrayList<String> row = librariesSheet.get(i);

                LibrarySubmission ls = new LibrarySubmission(row, header);

                librarySubmission.add(ls);
            }

            return librarySubmission;

        } else {
            throw new ParsingException("Library Submission", "Zero or multiple header lines found");
        }
    }

    private ArrayList<SampleSubmission> parseSampleSubmission() throws ParsingException {
        sampleSubmission = new ArrayList<SampleSubmission>();
        ArrayList<Integer> headers = findHeaderRows(ExcelParserConstants.sampleColumnNames, samplesSheet);

        if (headers.size() == 1) {
            int headerrowindex = headers.get(0);
            Map<ColumnNames, Integer> header = findColumnIndices(ExcelParserConstants.sampleColumnNames, samplesSheet.get(headerrowindex));

            for (int i = headerrowindex + 1; i < samplesSheet.size(); i++) {
                ArrayList<String> row = samplesSheet.get(i);

                SampleSubmission ss = new SampleSubmission(row, header);

                sampleSubmission.add(ss);
            }

            return sampleSubmission;

        } else {
            throw new ParsingException("Sample Submission", "Zero or multiple header lines found");
        }
    }

    private SubmissionSummary parseSubmissionSummary() {

        SubmissionSummary sm = new SubmissionSummary(summarySheet);
        if (sm == null) {
            System.out.println("unparsable summary");
        }

        return sm;
    }


    protected static BidiMap<ColumnNames, Integer> findColumnIndices(ArrayList<ColumnNames> headerColumns, ArrayList<String> headerRow) {
        BidiMap<ColumnNames, Integer> result = new DualHashBidiMap<>();
        for (ColumnNames column : headerColumns) {
            for (int i = 0; i < headerRow.size(); i++) {
                if (column.matches(headerRow.get(i))) {
                    result.put(column, i);
                }
            }
        }

        return result;
    }

    protected static ArrayList<Integer> findHeaderRows(ArrayList<ColumnNames> headerColumns, ArrayList<ArrayList<String>> rows) {
        ArrayList<Integer> headers = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<String> row = rows.get(i);
            boolean isHeader = true;
            for (ColumnNames column : headerColumns) {
                if (column.isRequired() && !row.contains(column)) {
                    isHeader = false;
                    break;
                }
            }
            if (isHeader) {
                headers.add(i);
            }
        }

        return headers;
    }
}
