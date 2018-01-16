package at.ac.oeaw.cemm.lims.analysis.impl.model;
// Generated Aug 29, 2011 3:51:18 PM by Hibernate Tools 3.2.1.GA

import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
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
    
    public SampleSheetRow(SampleRunDTO sampleRun, String lane, boolean indexReversal, ServiceFactory services) {
        
       
        //Sample Run Data
        this.lane = Integer.parseInt(lane);

        //Sample Data
        SampleDTO sample = sampleRun.getSample();
        this.sampleId = sample.getName();
        this.libraryName = sample.getLibraryName();
        this.librarySize = String.valueOf(sample.getBulkFragmentSize().intValue());
        this.sampleComment = sample.getComment();
        
        String idxI7 = ("none".equalsIgnoreCase(sample.getIndexI7().getIndex()) ? "": sample.getIndexI7().getIndex());
        String idxI5 = ("none".equalsIgnoreCase(sample.getIndexI5().getIndex()) ? "": sample.getIndexI5().getIndex());
        
        this.i7 = idxI7;
        
        if (!i7.isEmpty()){
            this.i7Comment = services.getIndexService().getDetailedIndexInfo(i7,IndexType.i7);
        }else{
            this.i7Comment = "";
        }

        if (indexReversal){
            this.i5 = getReverseComplement(idxI5);
            if (!i5.isEmpty()) {
                this.i5Comment = "reverse complement: "+ services.getIndexService().getDetailedIndexInfo(idxI5, IndexType.i5);
            } else {
                this.i5Comment = "";
            }
            
        }else{
            this.i5 = idxI5;
            if (!i5.isEmpty()) {
                this.i5Comment = services.getIndexService().getDetailedIndexInfo(idxI5,IndexType.i5);
            } else {
                this.i5Comment = "";
            }
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
