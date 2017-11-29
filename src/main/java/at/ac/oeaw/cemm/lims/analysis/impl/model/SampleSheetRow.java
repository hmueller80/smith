package at.ac.oeaw.cemm.lims.analysis.impl.model;
// Generated Aug 29, 2011 3:51:18 PM by Hibernate Tools 3.2.1.GA

import at.ac.oeaw.cemm.bsf.barcode.IlluminaAdapterSequences;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import org.apache.commons.csv.CSVRecord;

/**
 * 
 */
public class SampleSheetRow implements java.io.Serializable {

    private final int lane;
    private final String sampleId;
    private final String i7;
    private final String i5;
    private final String i7Comment;
    private final String i5Comment;
    private final String libraryName;
    private final String librarySize;
    private final String sampleComment;
    
    public SampleSheetRow(SampleRunDTO sampleRun, String lane, boolean indexReversal) {
        
       
        //Sample Run Data
        this.lane = Integer.parseInt(lane);

        //Sample Data
        SampleDTO sample = sampleRun.getSample();
        this.sampleId = sample.getName();
        this.libraryName = sample.getLibraryName();
        this.librarySize = String.valueOf(sample.getBulkFragmentSize().intValue());
        this.sampleComment = sample.getComment();
        
        String idx = ("none".equals(sample.getIndex().getIndex()) ? "": sample.getIndex().getIndex());
        this.i7 = this.getBarcodeSequence1(idx);
        this.i7Comment = IlluminaAdapterSequences.getDetailedIndexInfo(i7);

        if (indexReversal){
            this.i5 = this.getBarcodeSequence2ReverseComplement(idx);
            this.i5Comment ="reverse complement: "+ IlluminaAdapterSequences.getDetailedIndexInfo(i5);

        }else{
            this.i5 = this.getBarcodeSequence2(idx);
            this.i5Comment = IlluminaAdapterSequences.getDetailedIndexInfo(i5);
        }
    }

    SampleSheetRow(CSVRecord record) {
        lane = Integer.parseInt(record.get(SampleSheetCSVHeader.lane));
        sampleId = record.get(SampleSheetCSVHeader.sample_name);
        i7 = record.get(SampleSheetCSVHeader.barcode_sequence_1);
        i5 = record.get(SampleSheetCSVHeader.barcode_sequence_2);
        i7Comment = record.get(SampleSheetCSVHeader.barcode_comment_1);
        i5Comment = record.get(SampleSheetCSVHeader.barcode_comment_2);
        libraryName = record.get(SampleSheetCSVHeader.library_name);
        librarySize = record.get(SampleSheetCSVHeader.library_size);
        sampleComment = record.get(SampleSheetCSVHeader.sample_comment);
    }
    
    private String getBarcodeSequence1(String index) {
        if (index != null && index.length() <= 8) {
            return index;
        }
        if (index != null && index.length() == 16) {
            return index.substring(0, 8);
        }
        return "";
    }

    private String getBarcodeSequence2(String index) {
        if (index != null && index.length() <= 8) {
            return "";
        }
        if (index != null && index.length() == 16) {
            return index.substring(8, 16);
        }
        return "";
    }

    private String getBarcodeSequence2ReverseComplement(String index) {
        if (index != null && index.length() <= 8) {
            return "";
        }
        if (index != null && index.length() == 16) {
            return getReverseComplement(index.substring(8, 16));
        }
        return "";
    }
    
    private String getReverseComplement(String sequence){
        sequence = sequence.toUpperCase();
        StringBuilder sb = new StringBuilder();
        for(int i = sequence.length() - 1; i >= 0; i--){
            char letter = sequence.charAt(i);
            switch(letter){
                case 'A': sb.append('T'); break;
                case 'C': sb.append('G'); break;
                case 'G': sb.append('C'); break;
                case 'T': sb.append('A'); break;
                default: return "not a valid sequence"; 
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(lane);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(i7);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(i5);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(sampleId);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(libraryName);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(librarySize);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(i7Comment);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(i5Comment);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(sampleComment);
        return sb.toString();
    }

    public int getLane() {
        return lane;
    }

    public String getSampleId() {
        return sampleId;
    }

    public String getI7() {
        return i7;
    }

    public String getI5() {
        return i5;
    }

    public String getI7Comment() {
        return i7Comment;
    }

    public String getI5Comment() {
        return i5Comment;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibrarySize() {
        return librarySize;
    }

    public String getSampleComment() {
        return sampleComment;
    }
    
   
}
