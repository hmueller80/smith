package it.iit.genomics.cru.smith.entity;
// Generated Aug 29, 2011 3:51:18 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import at.ac.oeaw.cemm.bsf.barcode.IlluminaAdapterSequences;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;

/**
 * 
 */
public class Samplesheet implements java.io.Serializable {

    private int counter;
    private String fcid;
    private int lane;
    private String sampleId;
    private String sampleRef;
    private String indexed;
    private String description;
    private String control;
    private String recipe;
    private String operator;
    private String sampleProject;
    private Date date;
    private String instrument;
    private String experimentType;
    private String i7;
    private String i5;
    private String i5revcompl;
    private String i7Comment;
    private String i5Comment;
    private String i5revcomplComment;
    private String libraryName;
    private String librarySize;
    private String sampleComment;
    

    public Samplesheet() {
    }

    public Samplesheet(int counter, String fcid, int lane, String sampleId) {
        this.counter = counter;
        this.fcid = fcid;
        this.lane = lane;
        this.sampleId = sampleId;
    }

    public Samplesheet(int counter, String fcid, int lane, String sampleId, String sampleRef, String indexed, String description, String control, String recipe, String operator, String sampleProject, Date date, String instrument, String experimentType) {
        this.counter = counter;
        this.fcid = fcid;
        this.lane = lane;
        this.sampleId = sampleId;
        this.sampleRef = sampleRef;
        this.indexed = indexed;
        this.description = description;
        this.control = control;
        this.recipe = recipe;
        this.operator = operator;
        this.sampleProject = sampleProject;
        this.date = date;
        this.instrument = instrument;
        this.experimentType = experimentType;
        //initBarcodes();
    }
    
    public Samplesheet(int counter, String fcid, int lane, String sampleId, String sampleRef, String indexed, String description, String control, String recipe, String operator, String sampleProject, Date date, String instrument, String experimentType, Sample s) {
        this.counter = counter;
        this.fcid = fcid;
        this.lane = lane;
        this.sampleId = sampleId;
        this.sampleRef = sampleRef;
        this.indexed = indexed;
        this.description = description;
        this.control = control;
        this.recipe = recipe;
        this.operator = operator;
        this.sampleProject = sampleProject;
        this.date = date;
        this.instrument = instrument;
        this.experimentType = experimentType;
        initBarcodes();
        libraryName = s.getLibraryName();
        librarySize = s.getBulkFragmentSize().intValue() + "";
        sampleComment = s.getComment();
    }
    
    public void initBarcodes(){
        i7 = this.getBarcodeSequence1();
        i5 = this.getBarcodeSequence2();
        i5revcompl = this.getBarcodeSequence2ReverseComplement();
        i7Comment = IlluminaAdapterSequences.getDetailedIndexInfo(i7);
        i5Comment = IlluminaAdapterSequences.getDetailedIndexInfo(i5);
        i5revcomplComment = IlluminaAdapterSequences.getDetailedIndexInfo(i5revcompl);
    }
    
    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getFcid() {
        return this.fcid;
    }

    public void setFcid(String fcid) {
        this.fcid = fcid;
    }

    public int getLane() {
        return this.lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public String getSampleId() {
        return this.sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleRef() {
        return this.sampleRef;
    }

    public void setSampleRef(String sampleRef) {
        this.sampleRef = sampleRef;
    }

    public String getIndexed() {
        return this.indexed;
    }

    public void setIndexed(String indexed) {
        this.indexed = indexed;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getControl() {
        return this.control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getRecipe() {
        return this.recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSampleProject() {
        return this.sampleProject;
    }

    public void setSampleProject(String sampleProject) {
        this.sampleProject = sampleProject;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInstrument() {
        return this.instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getCSVSEMM() {
        StringBuilder sb = new StringBuilder();
        sb.append(fcid + "," + lane + "," + sampleId + "," + sampleRef + "," + indexed + "," + description + "," + control + "," + recipe + "," + operator + "," + sampleProject);
        return sb.toString();
    }

    public static String getCSVHeaderSEMM() {
        return "FCID,Lane,SampleID,SampleRef,Indexed,Description,Control,Recipe,Operator,SampleProject";        

    }
    
    public String getCSVCEMM() {
        StringBuilder sb = new StringBuilder();
        sb.append(lane + "," + i7 + "," + i5 + "," + sampleId + "," + libraryName + "," + librarySize + "," + i7Comment + "," + i5Comment + "," + sampleComment);
        return sb.toString();
    }
    
    public String getCSVCEMMRevCompl() {
        StringBuilder sb = new StringBuilder();
        sb.append(lane + "," + i7 + "," + i5revcompl + "," + sampleId + "," + libraryName + "," + librarySize + "," + i7Comment + ",reverse complement: " + i5revcomplComment + "," + sampleComment);
        return sb.toString();
    }
    
    public static String getCSVHeaderCEMM() {   
        return "lane,barcode_sequence_1,barcode_sequence_2,sample_name,library_name,library_size,barcode_comment_1,barcode_comment_2,sample_comment";
    }
    
    private String getBarcodeSequence1(){
        if(indexed != null && indexed.length() <= 8){
            return indexed;
        }
        if(indexed != null && indexed.length() == 16){
            return indexed.substring(0,8);
        }
        return "";
    }
    
    private String getBarcodeSequence2(){
        if(indexed != null && indexed.length() <= 8){
            return "";
        }
        if(indexed != null && indexed.length() == 16){
            return indexed.substring(8,16);
        }
        return "";
    }
    
    private String getBarcodeSequence2ReverseComplement(){
        if(indexed != null && indexed.length() <= 8){
            return "";
        }
        if(indexed != null && indexed.length() == 16){
            return getReverseComplement(indexed.substring(8,16));
        }
        return "";
    }
    
    public static String getReverseComplement(String sequence){
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

    /**
     * @return the experimentType
     */
    public String getExperimentType() {
        return experimentType;
    }

    /**
     * @param experimentType the experimentType to set
     */
    public void setExperimentType(String experimentType) {
        this.experimentType = experimentType;
    }

}
