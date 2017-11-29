/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dbarreca
 */
public interface SampleService {

    void deleteSample(final SampleDTO sample) throws Exception;

    List<String> getAllIndexes();

    List<String> getAllLibraries();

    SampleDTO getFullSampleById(final int sampleId);

    List<SampleDTO> getSamples(final int first, final int pageSize, final String sortField, final boolean ascending, final Map<String, Object> filters);

    int getSamplesCount(final Map<String, Object> filters);

    PersistedEntityReceipt saveOrUpdateSample(final SampleDTO sample, final boolean isNew) throws Exception;
    
    Boolean checkIdxExistence(final String sequence);

    List<PersistedEntityReceipt> bulkUpdateSamples(List<SampleDTO> samplesToUpdate);
    
    List<SampleDTO> getSamplesByStatus(String status);
    
    List<ApplicationDTO> getAllApplications();
        
    List<SampleDTO> getAllPooledSamples(SampleDTO sample);
}
