/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.requestform;

/**
 *
 * @author dbarreca
 */
public class RequestedSample {
    
    private String sampleName;
    private String sampleDescription;
    private String organism;
    private String library;    
    private String i7Index;
    private String i7Adapter;
    private String i5Index;
    private String i5Adapter;
    private String primerType;
    private String primerName;
    private String primerSequence;
    
    
    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public String getI7Index() {
        return i7Index;
    }

    public void setI7Index(String i7Index) {
        this.i7Index = i7Index;
    }

    public String getI7Adapter() {
        return i7Adapter;
    }

    public void setI7Adapter(String i7Adapter) {
        this.i7Adapter = i7Adapter;
    }

    public String getI5Index() {
        return i5Index;
    }

    public void setI5Index(String i5Index) {
        this.i5Index = i5Index;
    }

    public String getI5Adapter() {
        return i5Adapter;
    }

    public void setI5Adapter(String i5Adapter) {
        this.i5Adapter = i5Adapter;
    }

    public String getPrimerType() {
        return primerType;
    }

    public void setPrimerType(String primerType) {
        this.primerType = primerType;
    }

    public String getPrimerName() {
        return primerName;
    }

    public void setPrimerName(String primerName) {
        this.primerName = primerName;
    }

    public String getPrimerSequence() {
        return primerSequence;
    }

    public void setPrimerSequence(String primerSequence) {
        this.primerSequence = primerSequence;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }
    
}
