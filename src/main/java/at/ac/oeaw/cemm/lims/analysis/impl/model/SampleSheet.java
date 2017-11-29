/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl.model;

import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.model.parser.runCSV.RunFormCSVHeader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author dbarreca
 */
public class SampleSheet {
    protected static final char SEPARATOR=',';

    private final List<SampleSheetRow> samples=new LinkedList<>();    
    
    public SampleSheet(){};
    
    public SampleSheet(File sampleSheetFile) throws Exception {
        CSVParser parser = new CSVParser(
                new FileReader(sampleSheetFile), 
                CSVFormat.RFC4180.withHeader(SampleSheetCSVHeader.class)
                        .withRecordSeparator(SEPARATOR)
                        .withSkipHeaderRecord());
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record: records){
            samples.add(new SampleSheetRow(record));
        }
    }
    
  
    public void addSampleRun(SampleRunDTO sampleRun, boolean indexReversal){
        for (String lane: sampleRun.getLanes()){
            samples.add(new SampleSheetRow(sampleRun,lane,indexReversal));
        }
    }
    
    
    public void toFile(String filePath) throws Exception {
        File outFile = new File(filePath);
        if (!outFile.exists() && samples.size()>0){
            outFile.setReadable(true, false);
            outFile.setWritable(true, false);
            FileOutputStream fos = new FileOutputStream(outFile );
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(SampleSheetCSVHeader.getLine());
            bw.newLine();
            for (SampleSheetRow sampleLine: samples){
               bw.write(sampleLine.toString());
                bw.newLine();
            }
            bw.close();
            
        }
    }
      
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (samples != null && samples.size() > 0) {
            sb.append(SampleSheetCSVHeader.getLine());
            sb.append("\n");
            for (SampleSheetRow sampleLine : samples) {
                sb.append(sampleLine.toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

     
}
