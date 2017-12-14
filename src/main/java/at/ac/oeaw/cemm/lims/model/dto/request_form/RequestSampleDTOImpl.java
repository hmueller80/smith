/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;

/**
 *
 * @author dbarreca
 */
public class RequestSampleDTOImpl implements RequestSampleDTO{
    
    private final Integer id;
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
    
    protected RequestSampleDTOImpl(Integer id) {
        this.id = id;
    }
    
    protected RequestSampleDTOImpl() {
        id = null;
    }

    @Override
    public Integer getId() {
        return id;
    }        
    
    @Override
    public String getSampleName() {
        return sampleName;
    }

    @Override
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    @Override
    public String getSampleDescription() {
        return sampleDescription;
    }

    @Override
    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    @Override
    public String getOrganism() {
        return organism;
    }

    @Override
    public void setOrganism(String organism) {
        this.organism = organism;
    }

    @Override
    public String getI7Index() {
        return i7Index;
    }

    @Override
    public void setI7Index(String i7Index) {
        this.i7Index = i7Index;
    }

    @Override
    public String getI7Adapter() {
        return i7Adapter;
    }

    @Override
    public void setI7Adapter(String i7Adapter) {
        this.i7Adapter = i7Adapter;
    }

    @Override
    public String getI5Index() {
        return i5Index;
    }

    @Override
    public void setI5Index(String i5Index) {
        this.i5Index = i5Index;
    }

    @Override
    public String getI5Adapter() {
        return i5Adapter;
    }

    @Override
    public void setI5Adapter(String i5Adapter) {
        this.i5Adapter = i5Adapter;
    }

    @Override
    public String getPrimerType() {
        return primerType;
    }

    @Override
    public void setPrimerType(String primerType) {
        this.primerType = primerType;
    }

    @Override
    public String getPrimerName() {
        return primerName;
    }

    @Override
    public void setPrimerName(String primerName) {
        this.primerName = primerName;
    }

    @Override
    public String getPrimerSequence() {
        return primerSequence;
    }

    @Override
    public void setPrimerSequence(String primerSequence) {
        this.primerSequence = primerSequence;
    }

    @Override
    public String getLibrary() {
        return library;
    }

    @Override
    public void setLibrary(String library) {
        this.library = library;
    }
    
}
