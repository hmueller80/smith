/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans.*;
import java.util.ArrayList;



/**
 *
 * @author hMueller
 */
public class SampleAnnotationParser extends ExcelParser {
    
    SubmissionSummary summary;
    ArrayList<SampleSubmission> sampleSubmission;
    ArrayList<LibrarySubmission> librarySubmission;
    ArrayList<SequencingRequestSubmission> sequencingRequestSubmission;

  
    public SampleAnnotationParser(String filename) throws ParsingException {
        super(filename);
        parse();
    }

    
    protected void parse() throws ParsingException {        
        summary = parseSubmissionSummary();
        sampleSubmission = parseSampleSubmission();
        librarySubmission = parseLibrarySubmission();
        sequencingRequestSubmission = parseSequencingRequestSubmission();       
    }
    
    private ArrayList<SequencingRequestSubmission> parseSequencingRequestSubmission() throws ParsingException {
        sequencingRequestSubmission = new ArrayList<>();
        ArrayList<Integer> headers = findRequestHeaderRows(requestsSheet);
        
        if(headers.size() == 1){
            int headerrowindex = headers.get(0);
            ArrayList<Integer> columnindices = findRequestsColumnIndices(requestsSheet.get(headerrowindex));
            for(int i = headerrowindex + 1; i < requestsSheet.size(); i++){
                ArrayList<String> row = requestsSheet.get(i);           
                SequencingRequestSubmission rs = new SequencingRequestSubmission(row, columnindices);
                sequencingRequestSubmission.add(rs);
            }
                    return sequencingRequestSubmission;

        }else{
            throw new ParsingException("Request Submission","Zero or multiple header lines found");
        }
    }
    
    private ArrayList<LibrarySubmission> parseLibrarySubmission() throws ParsingException {
        librarySubmission = new ArrayList<>();
        ArrayList<Integer> headers = findLibrariesHeaderRows(librariesSheet);
        
        if(headers.size() == 1){
            System.out.println("one library header found");
            int headerrowindex = headers.get(0);
            ArrayList<Integer> columnindices = findLibrariesColumnIndices(librariesSheet.get(headerrowindex));
            for(int i = headerrowindex + 1; i < librariesSheet.size(); i++){
                ArrayList<String> row = librariesSheet.get(i);
                
                LibrarySubmission ls = new LibrarySubmission(row, columnindices);
                
                librarySubmission.add(ls);
            }
            
            return librarySubmission;
            
         }else{
            throw new ParsingException("Library Submission","Zero or multiple header lines found");
        }
    }

    private ArrayList<SampleSubmission> parseSampleSubmission() throws ParsingException {
        sampleSubmission = new ArrayList<SampleSubmission>();
        ArrayList<Integer> headers = findSamplesHeaderRows(samplesSheet);
        
        if(headers.size() == 1){
            int headerrowindex = headers.get(0);
            ArrayList<Integer> columnindices = findSamplesColumnIndices(samplesSheet.get(headerrowindex));
            for(int i = headerrowindex + 1; i < samplesSheet.size(); i++){
                ArrayList<String> row = samplesSheet.get(i);
                
                SampleSubmission ss = new SampleSubmission(row, columnindices);
                
                sampleSubmission.add(ss);
            }
            
            return sampleSubmission;

        }else{
            throw new ParsingException("Sample Submission","Zero or multiple header lines found");
        }
    }
    
    
    
    private ArrayList<Integer> findLibrariesColumnIndices(ArrayList<String> headerrow){
        ArrayList<Integer> headers = new ArrayList<Integer>();
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Order));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryName));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryLabel));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.SampleName));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.BarcodeSequencei7, ExcelParserConstants.BarcodeSequence, ExcelParserConstants.BarcodeSequence1));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryAdapteri7, ExcelParserConstants.LibraryAdapter, ExcelParserConstants.LibraryAdapter1));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.BarcodeSequencei5, ExcelParserConstants.BarcodeSequence2));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryAdapteri5, ExcelParserConstants.LibraryAdapter2));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.SequencingPrimerType, ExcelParserConstants.SequencingPrimer));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.CustomSequencingPrimerName, ExcelParserConstants.CustomSequencingPrimerNameold));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.CustomSequencingPrimerSequence));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryType));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryKits));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryDetails));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryPerson));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryDate));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryVolume));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryDNAConcentration));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryTotalSize, ExcelParserConstants.LibraryTotalSizeold));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryInsertSize));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryComment));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.AdditionalComment));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.BioinformaticsProtocol));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.BioinformaticsGenome));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.BioinformaticsGermlineControl));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.BioinformaticsComment));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryDNAAmount, ExcelParserConstants.LibraryDNAAmountng));
        return headers;
    }
    
    private ArrayList<Integer> findSamplesColumnIndices(ArrayList<String> headerrow){
        ArrayList<Integer> headers = new ArrayList<Integer>();
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Order));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.SampleName));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.SampleDescription));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Organism));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Sex));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Age));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Tissue));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.CellType));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Genotype));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.FamilyRelations));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Phenotype));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Disease));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.MaterialType));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Source));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.AcquisitionDate));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.SampleGroup));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.OriginalSampleID));
        return headers;
    }
    
    private ArrayList<Integer> findRequestsColumnIndices(ArrayList<String> headerrow){
        ArrayList<Integer> headers = new ArrayList<Integer>();
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.Order));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.LibraryName));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.sequencingType));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.readLength));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.numberofLanes, ExcelParserConstants.numberofHiSeqLanes));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.specialRequirements));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.additionalComment));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.receivingPerson));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.receivingDate));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.receivingComment));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.qualityControlPerson));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.qualityControlDate));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.qualityControlSummary));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.qualityControlFiles));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.qualityControlStatus));
        headers.add(findIndexForColumnHeader(headerrow, ExcelParserConstants.sequencer));
        
        return headers;
    }
    
    private int findIndexForColumnHeader(ArrayList<String> columheaders, String key){
        for(int i = 0; i < columheaders.size(); i++){
            if(columheaders.get(i).startsWith(key)){
                return i;
            }
        }
        return -1;
    }
    
    private int findIndexForColumnHeader(ArrayList<String> columheaders, String key, String key2){
        for(int i = 0; i < columheaders.size(); i++){
            if(columheaders.get(i).startsWith(key) || columheaders.get(i).equals(key2)){
                return i;
            }
        }
        return -1;
    }
    
    private int findIndexForColumnHeader(ArrayList<String> columheaders, String key, String key2, String key3){
        for(int i = 0; i < columheaders.size(); i++){
            if(columheaders.get(i).startsWith(key) || columheaders.get(i).equals(key2) || columheaders.get(i).equals(key3)){
                return i;
            }
        }
        return -1;
    }
    
    private ArrayList<Integer> findSamplesHeaderRows(ArrayList<ArrayList<String>> rows){
        ArrayList<Integer> headers = new ArrayList<Integer>();
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<String> row = rows.get(i);
            if (row.get(0).equals(ExcelParserConstants.Order) && 
                    row.get(1).equals(ExcelParserConstants.SampleName)){
                headers.add(i);
            }
        }
      
        return headers;
    }
    
    private ArrayList<Integer> findLibrariesHeaderRows(ArrayList<ArrayList<String>> rows){
        ArrayList<Integer> headers = new ArrayList<Integer>();
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<String> row = rows.get(i);
            if (row.get(0).equals(ExcelParserConstants.Order) && 
                    row.get(1).equals(ExcelParserConstants.LibraryName)){
                headers.add(i);
            }
        }
      
        return headers;
    }
    
    private ArrayList<Integer> findRequestHeaderRows(ArrayList<ArrayList<String>> rows){
        ArrayList<Integer> headers = new ArrayList<Integer>();
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<String> row = rows.get(i);
            if (row.get(0).equals(ExcelParserConstants.Order) && 
                    (row.get(1).equals(ExcelParserConstants.LibraryName) || row.get(1).equals(ExcelParserConstants.SampleName))){
                headers.add(i);
            }
        }
      
        return headers;
    }
    
    private SubmissionSummary parseSubmissionSummary(){
        
        SubmissionSummary sm = new SubmissionSummary(summarySheet);
        if(sm == null){
            System.out.println("unparsable summary");
        }
       
        return sm;
    }
    

  
    public SubmissionSummary getSummary() {
        return summary;
    }

}
