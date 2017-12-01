/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import java.util.List;
import java.util.Map;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public interface RunService {

    public Set<SampleRunDTO> getSampleRunByRunId(int id);

    public List<SampleRunDTO> getRuns(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters);
    
    public List<SampleRunDTO> getRuns(String sortField, boolean ascending, Map<String, Object> filters);
  
    public Integer getRunsCount(Map<String, Object> filters);

    public SampleRunDTO getSampleRunById(int runId, int samId);
    
    public void bulkDeleteRun(Integer runId) throws Exception;
    
    public boolean runExists(int runId) throws Exception;

    public Set<PersistedEntityReceipt> bulkUploadRuns(Set<SampleRunDTO> sampleRuns, boolean allNew) throws Exception;

    public List<SampleRunDTO> getRunsByFlowCell(String FCID);
    
}
