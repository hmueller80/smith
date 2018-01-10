/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.lims.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.BarcodeDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.NewsDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.DTOFactory;
import at.ac.oeaw.cemm.lims.api.dto.lims.DepartmentDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryToRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.OrganizationDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.ApplicationEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SequencingIndexEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.LaneEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalRunEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.NewsEntity;
import java.util.HashSet;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import at.ac.oeaw.cemm.lims.api.dto.lims.RunDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.Barcode;
import at.ac.oeaw.cemm.lims.persistence.entity.BarcodeKit;
import at.ac.oeaw.cemm.lims.persistence.entity.DepartmentEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalRequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.OrganizationEntity;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class DTOMapper {

    @Inject
    private DTOFactory myDTOFactory;

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
            sample.setLibraryName(entity.getLibrary().getLibraryName());

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

    private AffiliationDTO getAffiliationFromEntity(DepartmentEntity entity) {
        OrganizationEntity orga = entity.getOrganization();
        OrganizationDTO orgaDTO = getOrganizationFromEntity(orga);
        DepartmentDTO deptDTO = getDepartmentFromEntity(entity);
        
        AffiliationDTO result = myDTOFactory.getAffiliationDTO(orgaDTO,deptDTO);

        return result;
    }
    
    public UserDTO getUserDTOFromEntity(UserEntity entity) {
        if (entity == null) {
            return null;
        } else {
            AffiliationDTO affiliation = getAffiliationFromEntity(entity.getDepartment());
                
            return myDTOFactory.getUserDTO(
                    entity.getId(),
                    entity.getUserName(),
                    entity.getLogin(),
                    entity.getPhone(),
                    entity.getMailAddress(),
                    entity.getPi(),
                    entity.getUserRole(),
                    affiliation);
        }
    }
    
  

    protected SampleRunDTO getSampleRunDTOFromEntity(SampleRunEntity sampleRun) {
        SampleDTO sampleDTO = getSampleDTOfromEntity(sampleRun.getsample());
        UserDTO operatorDTO = getUserDTOFromEntity(sampleRun.getUser());
        Set<String> lanes = new HashSet<>();
        for (LaneEntity lane : sampleRun.getLanes()) {
            lanes.add(lane.getLaneName());
        }

        return myDTOFactory.getSampleRunDTO(sampleRun.getId().getRunId(), sampleDTO, operatorDTO, sampleRun.getFlowcell(), lanes, sampleRun.getRunFolder(), sampleRun.getIsControl());
    }

    protected NewsDTO getNewsDTOFromEntity(NewsEntity news) {

        return myDTOFactory.getNewsDTO(news.getId(), news.getHeader(), news.getBody(), news.getDate());
    }

    protected RunDTO getMinimalRunDTOFromEntity(MinimalRunEntity runEntity) {
        UserDTO operator = getUserDTOFromEntity(runEntity.getOperator());
        return myDTOFactory.getRunDTO(runEntity.getId(), runEntity.getFlowCell(), operator, runEntity.getRunFolder());
    }

    protected RequestDTO getMinimalRequestDTOFromEntity(MinimalLibraryEntity requestEntity) {
        UserDTO requestor = getUserDTOFromEntity(requestEntity.getRequestor());
        return myDTOFactory.getRequestDTO(requestor, requestEntity.getRequestId());
    }

    protected LibraryDTO getLibraryDTOFromEntity(LibraryEntity libraryEntity, boolean loadSamples) {
        LibraryDTO library = myDTOFactory.getLibraryDTO(libraryEntity.getLibraryName(), libraryEntity.getId());
        if (loadSamples) {
            for (SampleEntity sampleEntity : libraryEntity.getSamples()) {
                library.addSample(getSampleDTOfromEntity(sampleEntity));
            }
        }

        return library;
    }
    
    protected LibraryDTO getLibraryDTOFromMinimalEntity(MinimalLibraryEntity libraryEntity) {
        LibraryDTO library = myDTOFactory.getLibraryDTO(libraryEntity.getLibraryName(), libraryEntity.getLibraryId());
        
        return library;
    }
    
    protected LibraryToRunDTO getLibraryToRunDTOFromMinimalEntity(MinimalLibraryEntity minimalLibrary, LibraryEntity fullLibraryEntity) {
        UserDTO user = getUserDTOFromEntity(minimalLibrary.getRequestor());
        LibraryDTO fullLibrary = getLibraryDTOFromEntity(fullLibraryEntity,true);
        
        LibraryToRunDTO library = myDTOFactory.getLibraryToRun(fullLibrary, 
                user, 
                minimalLibrary.getRequestId(),
                minimalLibrary.getReadMode(), 
                minimalLibrary.getReadLength());
        
        return library;
    }

    public RequestDTO getRequestDTOFromEntity(MinimalRequestEntity request) {
        UserDTO requestor = getUserDTOFromEntity(request.getRequestor());
        return myDTOFactory.getRequestDTO(requestor, request.getRequestId());
    }

    public OrganizationDTO getOrganizationFromEntity(OrganizationEntity orgaEntity) {
        OrganizationDTO orgaDTO = myDTOFactory.getOrganizationDTO(orgaEntity.getOrganizationName());
        orgaDTO.setAddress(orgaEntity.getAddress());
        orgaDTO.setWebPage(orgaEntity.getUrl());
        for (DepartmentEntity department: orgaEntity.getDepartmentSet()){
            orgaDTO.addDepartment(getDepartmentFromEntity(department));
        }
        
        return orgaDTO;
    }
    
    public DepartmentDTO getDepartmentFromEntity(DepartmentEntity department){
        DepartmentDTO departmentDTO = myDTOFactory.getDepartmentDTO(department.getDepartmentPK().getDepartmentName());
        departmentDTO.setAddress(department.getAddress());
        departmentDTO.setWebPage(department.getUrl());
        return departmentDTO;
    }
    
    public IndexDTO getIndexDTOFromBarcodeEntity(Barcode barcodeEntity) throws Exception{
        IndexDTO indexDTO = myDTOFactory.getIndexDTO(barcodeEntity.getSequence());
        indexDTO.setType(IndexType.decode(barcodeEntity.getBarcodeType()));
        
        return indexDTO;
    }
    
    public BarcodeDTO getBarcodeFromKitEntity(BarcodeKit kitEntity) throws Exception {
        IndexDTO index = getIndexDTOFromBarcodeEntity(kitEntity.getBarcodeId());
        BarcodeDTO barcode = myDTOFactory.getBarcodeDTO(kitEntity.getBarcodeKitPK().getKitName(), kitEntity.getBarcodeKitPK().getSequenceName(), index);
        
        return barcode;
    }

}
