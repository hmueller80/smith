/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

/**
 *
 * @author hmueller
 */
public class RequestSubmissionForm {
    
    String userName;
    String userLogin;
    String userTel;
    String institute;
    String PI;
    String sampleName;
    String barcodeByName;
    String application;
    String readLength;
    String receipe;
    String sampleType;
    String librarySynthesis;
    String organism;
    String antibody;
    String sampleDescription;
    String bioAnalyzerdate;
    String bioAnalyzernanomolarity;
    String sampleConcentration;
    String totalAmount;
    String bulkFragmentSize;
    String comments;
    String library;
    String submissionid;
    String submissiondate;
    String libraryVolume;

    
    public RequestSubmissionForm() {
    
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getPI() {
        return PI;
    }

    public void setPI(String PI) {
        this.PI = PI;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getBarcodeByName() {
        return barcodeByName;
    }

    public void setBarcodeByName(String barcodeByName) {
        this.barcodeByName = barcodeByName;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getReadLength() {
        return readLength;
    }

    public void setReadLength(String readLength) {
        this.readLength = readLength;
    }

    public String getReceipe() {
        return receipe;
    }

    public void setReceipe(String receipe) {
        this.receipe = receipe;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getLibrarySynthesis() {
        return librarySynthesis;
    }

    public void setLibrarySynthesis(String librarySynthesis) {
        this.librarySynthesis = librarySynthesis;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getAntibody() {
        return antibody;
    }

    public void setAntibody(String antibody) {
        this.antibody = antibody;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    public String getBioAnalyzerdate() {
        return bioAnalyzerdate;
    }

    public void setBioAnalyzerdate(String bioAnalyzerdate) {
        this.bioAnalyzerdate = bioAnalyzerdate;
    }

    public String getBioAnalyzernanomolarity() {
        return bioAnalyzernanomolarity;
    }

    public void setBioAnalyzernanomolarity(String bioAnalyzernanomolarity) {
        this.bioAnalyzernanomolarity = bioAnalyzernanomolarity;
    }

    public String getSampleConcentration() {
        return sampleConcentration;
    }

    public void setSampleConcentration(String sampleConcentration) {
        this.sampleConcentration = sampleConcentration;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBulkFragmentSize() {
        return bulkFragmentSize;
    }

    public void setBulkFragmentSize(String bulkFragmentSize) {
        this.bulkFragmentSize = bulkFragmentSize;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getSubmissionid() {
        return submissionid;
    }

    public void setSubmissionid(String submissionid) {
        this.submissionid = submissionid;
    }

    public String getSubmissiondate() {
        return submissiondate;
    }

    public void setSubmissiondate(String submissiondate) {
        this.submissiondate = submissiondate;
    }

    public String getLibraryVolume() {
        return libraryVolume;
    }

    public void setLibraryVolume(String libraryVolume) {
        this.libraryVolume = libraryVolume;
    }
    
    public boolean barcodeLettersAreValid(){
        if(barcodeByName == null){
            return true;
        }
        if(barcodeByName.trim().equals("")){
            return true;
        }
        char[] test = this.barcodeByName.toUpperCase().toCharArray();
        for(int i = 0; i < test.length; i++){
            if(!(test[i] == 'A' || test[i] == 'C' || test[i] == 'G' || test[i] == 'T')){
                return false;
            }
        }
        return true;
    }
    
    public int getBarcodeLength(){
        if(barcodeByName == null){
            return 0;
        }
        return this.barcodeByName.length();
    }

    public String dump(){
        String s = userName + "\t" + userLogin + "\t" + userTel + "\t" + institute + "\t" + PI + "\t" + sampleName + "\t" + barcodeByName + "\t" + application + "\t" + readLength + "\t" + receipe + "\t" + sampleType + "\t" + librarySynthesis + "\t" + organism + "\t" + antibody + "\t" + sampleDescription + "\t" + bioAnalyzerdate + "\t" + bioAnalyzernanomolarity + "\t" + sampleConcentration + "\t" + totalAmount + "\t" + bulkFragmentSize + "\t" + comments + "\t" + library + "\t" + submissionid + "\t" + submissiondate + "\t" + libraryVolume;
        //System.out.println(userName + "\t" + userLogin + "\t" + userTel + "\t" + institute + "\t" + PI + "\t" + sampleName + "\t" + barcodeByName + "\t" + application + "\t" + readLength + "\t" + receipe + "\t" + sampleType + "\t" + librarySynthesis + "\t" + organism + "\t" + antibody + "\t" + sampleDescription + "\t" + bioAnalyzerdate + "\t" + bioAnalyzernanomolarity + "\t" + sampleConcentration + "\t" + totalAmount + "\t" + bulkFragmentSize + "\t" + comments + "\t" + library + "\t" + submissionid + "\t" + submissiondate); 
        return s;
    }
    
}
