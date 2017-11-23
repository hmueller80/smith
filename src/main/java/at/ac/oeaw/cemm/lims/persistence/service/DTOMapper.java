/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.model.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.persistence.entity.ApplicationEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SequencingIndexEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;

/**
 *
 * @author dbarreca
 */
public class DTOMapper {

    protected static SampleDTO getSampleDTOfromEntity(SampleEntity entity) {
        if (entity == null) {
            return null;
        } else {
            ApplicationDTO application = getApplicationDTOfromEntity(entity.getApplication());
            IndexDTO index = getIndexDTOFromEntity(entity.getSequencingIndexes());
            UserDTO user = getUserDTOFromEntity(entity.getUser());
            
            SampleDTO sample = DTOFactory.getSampleDTO(
                    entity.getId(),
                    application,
                    entity.getOrganism(),
                    entity.getType(),
                    entity.getAntibody(),
                    entity.isLibrarySynthesisNeeded(),
                    entity.getConcentration(),
                    entity.getTotalAmount(),
                    entity.getBulkFragmentSize(),
                    entity.getCostCenter(),
                    entity.getStatus(),
                    entity.getName(),
                    entity.getComment(),
                    entity.getDescription(),
                    entity.getRequestDate(),
                    entity.getBioanalyzerDate(),
                    entity.getBionalyzerBiomolarity(),
                    entity.getSubmissionId(),
                    entity.getExperimentName(),
                    index,
                    user);
            sample.setLibraryName(entity.getLibraryName());
            
            return sample;
        }
    }
    
    

    protected static ApplicationDTO getApplicationDTOfromEntity(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return DTOFactory.getApplicationDTO(
                    entity.getReadlength(),
                    entity.getReadmode(),
                    entity.getInstrument(),
                    entity.getApplicationname(),
                    entity.getDepth());
        }
    }

    protected static IndexDTO getIndexDTOFromEntity(SequencingIndexEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return DTOFactory.getIndexDTO(entity.getIndex());
        }
    }

    protected static UserDTO getUserDTOFromEntity(UserEntity entity) {
         if (entity == null) {
            return null;
        } else {
            return DTOFactory.getUserDTO(
                    entity.getId(),
                    entity.getUserName(),
                    entity.getLogin(),
                    entity.getPhone(), 
                    entity.getMailAddress(), 
                    entity.getPi(),
                    entity.getUserRole());
        }
    }

}
