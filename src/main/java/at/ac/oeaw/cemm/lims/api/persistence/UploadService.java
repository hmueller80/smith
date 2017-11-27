/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.persistence;

import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.persistence.service.PersistedEntityReceipt;
import java.util.Set;

/**
 *
 * @author dbarreca
 */
public interface UploadService {

    Set<PersistedEntityReceipt> uploadRequest(final RequestDTO request) throws Exception;
    
}
