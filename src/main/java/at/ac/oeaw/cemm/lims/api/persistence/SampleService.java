/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public interface SampleService {

    void deleteSample(final SampleDTO sample) throws Exception;

    List<String> getAllIndexes(IndexType type);

    List<String> getAllLibraries();

    SampleDTO getFullSampleById(final int sampleId);

    List<SampleDTO> getSamples(final int first, final int pageSize, final String sortField, final boolean ascending, final Map<String, Object> filters);
    
    List<SampleDTO> getSamples(final String sortField, final boolean ascending, final Map<String, Object> filters);

    int getSamplesCount(final Map<String, Object> filters);

    PersistedEntityReceipt updateSample(final SampleDTO sample) throws Exception;
    
    Boolean checkIdxExistence(final String sequence, IndexType type);

    List<PersistedEntityReceipt> bulkUpdateSamples(List<SampleDTO> samplesToUpdate) throws Exception;
    
    List<SampleDTO> getSamplesByStatus(String status);
    
    List<ApplicationDTO> getAllApplications();
        
    List<SampleDTO> getAllPooledSamples(SampleDTO sample);

    public SampleDTO getFullSampleByRequestLibraryName(Integer submissionId, String libraryName, String sampleName);
    
}
