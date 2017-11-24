/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import java.util.List;
import java.util.Map;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;

/**
 *
 * @author dbarreca
 */
public interface RunService {

    public SampleRunDTO getRunById(int id);

    public List<SampleRunDTO> getRuns(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters);
    
    public Integer getRunsCount(int first, int pageSize, String sortField, boolean ascending, Map<String, Object> filters);

}
