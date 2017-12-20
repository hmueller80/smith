/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.dto.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.generic.Sample;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author dbarreca
 */
public class RequestSampleDTOImpl implements RequestSampleDTO, Sample{
    
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
    public String getName() {
        return sampleName;
    }

    @Override
    public void setName(String sampleName) {
        this.sampleName =  NameFilter.legalizeSampleName(sampleName);
    }

    @Override
    public String getSampleDescription() {
        return sampleDescription;
    }

    @Override
    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = NameFilter.legalize(sampleDescription);
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
        this.i7Index = NameFilter.legalizeIndex(i7Index);
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
        this.i5Index = NameFilter.legalizeIndex(i5Index);
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
        this.primerSequence = NameFilter.legalizeIndex(primerSequence);
    }

    @Override
    public String getLibrary() {
        return library;
    }

    @Override
    public void setLibrary(String library) {
        this.library = NameFilter.legalizeLibrary(library);
    }

    @JsonIgnore
    @Override
    public String getCompoundIndex() {
        if (i7Index.equalsIgnoreCase(RequestFormDTO.DEFAULT_INDEX)){
            if (i5Index.equalsIgnoreCase(RequestFormDTO.DEFAULT_INDEX)){
                return RequestFormDTO.DEFAULT_INDEX;
            }else{
                return null;
            }
        }else{
            if (i5Index.equalsIgnoreCase(RequestFormDTO.DEFAULT_INDEX)){
                return i7Index.toUpperCase().trim();
            }else{
                return (i7Index+i5Index).toUpperCase().trim();
            }
        }
    }
    
}
