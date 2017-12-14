/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service.request_form;

import at.ac.oeaw.cemm.lims.api.dto.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestDTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.AffiliationEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestSampleEntity;
import at.ac.oeaw.cemm.lims.persistence.service.DTOMapper;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestDTOMapper {
    @Inject private RequestDTOFactory dtoFactory;
    @Inject private DTOMapper limsMapper;

    public AffiliationDTO getAffiliationFromEntity(AffiliationEntity entity) {
        AffiliationDTO result = dtoFactory.getAffiliationDTO(entity.getAffiliationPK().getOrganizationName(),entity.getAffiliationPK().getDepartment());
        result.setAddress(entity.getAddress());
        result.setUrl(entity.getUrl());
        
        return result;
    }

    public RequestFormDTO getRequestFormDTOFromEntity(RequestEntity requestEntity, UserEntity requestorEntity, UserEntity piEntity) {
        UserDTO user = limsMapper.getUserDTOFromEntity(requestorEntity);
        UserDTO pi = limsMapper.getUserDTOFromEntity(piEntity);
        AffiliationDTO affiliation = getAffiliationFromEntity(requestorEntity.getAffiliation());
        RequestorDTO requestor = dtoFactory.getRequestorDTO(user, pi, affiliation);
        RequestFormDTO requestForm = dtoFactory.getRequestFormDTO(requestEntity.getId(),requestor,requestEntity.getReqDate());
        for (RequestLibraryEntity libraryEntity: requestEntity.getRequestLibrarySet()) {
            RequestLibraryDTO library = getLibraryRequestDTOFromEntity(libraryEntity);
            requestForm.addLibrary(library);
        }
        
        return requestForm;
    }

    private RequestLibraryDTO getLibraryRequestDTOFromEntity(RequestLibraryEntity libraryEntity) {
        RequestLibraryDTO library = dtoFactory.getRequestLibraryDTO(libraryEntity.getId());
        library.setName(libraryEntity.getLibName());
        library.setReadMode(libraryEntity.getReadMode());
        library.setReadLength(libraryEntity.getReadLength().intValue());
        library.setType(libraryEntity.getLibType());
        library.setLanes(libraryEntity.getLanes().intValue());
        library.setVolume(libraryEntity.getVolume());
        library.setDnaConcentration(libraryEntity.getDnaConcentration());
        library.setTotalSize(libraryEntity.getTotalSize());
        for (RequestSampleEntity sampleEntity: libraryEntity.getRequestSampleSet()){
            RequestSampleDTO sample = getRequestSampleDTOFromEntity(sampleEntity);
            sample.setLibrary(library.getName());
            library.addSample(sample);
        }
        
        return library;
    }

    private RequestSampleDTO getRequestSampleDTOFromEntity(RequestSampleEntity sampleEntity) {
        RequestSampleDTO sample = dtoFactory.getRequestSampleDTO(sampleEntity.getId());
        sample.setSampleName(sampleEntity.getName());
        sample.setSampleDescription(sampleEntity.getDescription());
        sample.setOrganism(sampleEntity.getOrganism());
        sample.setI7Index(sampleEntity.getIndexI7());
        sample.setI7Adapter(sampleEntity.getAdapterI7());
        sample.setI5Index(sampleEntity.getIndexI5());
        sample.setI5Adapter(sampleEntity.getAdapterI5());
        sample.setPrimerType(sampleEntity.getPrimerType());
        sample.setPrimerSequence(sampleEntity.getPrimerIndex());
        sample.setPrimerName(sampleEntity.getPrimerName());
        
        return sample;
    }
}
