/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.NewsDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.DTOFactory;
import at.ac.oeaw.cemm.lims.persistence.entity.ApplicationEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SequencingIndexEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.LaneEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.NewsEntity;
import java.util.HashSet;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class DTOMapper {
    @Inject private DTOFactory myDTOFactory;
    
    protected SampleDTO getSampleDTOfromEntity(SampleEntity entity) {
        if (entity == null) {
            return null;
        } else {
            ApplicationDTO application = getApplicationDTOfromEntity(entity.getApplication());
            IndexDTO index = getIndexDTOFromEntity(entity.getSequencingIndexes());
            UserDTO user = getUserDTOFromEntity(entity.getUser());
            
            SampleDTO sample = myDTOFactory.getSampleDTO(
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
    
    

    protected ApplicationDTO getApplicationDTOfromEntity(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return myDTOFactory.getApplicationDTO(
                    entity.getReadlength(),
                    entity.getReadmode(),
                    entity.getInstrument(),
                    entity.getApplicationname(),
                    entity.getDepth());
        }
    }

    protected IndexDTO getIndexDTOFromEntity(SequencingIndexEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return myDTOFactory.getIndexDTO(entity.getIndex());
        }
    }

    protected UserDTO getUserDTOFromEntity(UserEntity entity) {
         if (entity == null) {
            return null;
        } else {
            return myDTOFactory.getUserDTO(
                    entity.getId(),
                    entity.getUserName(),
                    entity.getLogin(),
                    entity.getPhone(), 
                    entity.getMailAddress(), 
                    entity.getPi(),
                    entity.getUserRole());
        }
    }

    protected SampleRunDTO getSampleRunDTOFromEntity(SampleRunEntity sampleRun) {
        SampleDTO sampleDTO = getSampleDTOfromEntity(sampleRun.getsample());
        UserDTO operatorDTO = getUserDTOFromEntity(sampleRun.getUser());
        Set<String> lanes = new HashSet<>();
        for (LaneEntity lane:sampleRun.getLanes()){
            lanes.add(lane.getLaneName());
        }
        
        return myDTOFactory.getSampleRunDTO(sampleRun.getId().getRunId(), sampleDTO, operatorDTO, sampleRun.getFlowcell(), lanes, sampleRun.getRunFolder(),sampleRun.getIsControl());
    }
    
     protected NewsDTO getNewsDTOFromEntity(NewsEntity news) {
        
        return myDTOFactory.getNewsDTO(news.getId(), news.getHeader(), news.getBody(), news.getDate());
    }

}
