/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.AffiliationDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.request_form.*;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.AffiliationEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestSampleEntity;
import at.ac.oeaw.cemm.lims.persistence.service.TransactionManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestFormService {
    @Inject private AffiliationDAO affiliationDAO;
    @Inject private RequestFormDAO requestFormDAO;
    @Inject private UserDAO userDAO;
    @Inject private RequestLibraryDAO requestLibraryDAO;
    @Inject private RequestSampleDAO requestSampleDAO;
    @Inject private RequestDTOMapper dtoMapper;
    
    public Integer saveRequestForm(final RequestFormDTO requestForm) throws Exception {
        return TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Integer>() {
            @Override
            public Integer execute() throws Exception {                                
                //1. Take care of requestor
                RequestorDTO requestor = requestForm.getRequestor();
                AffiliationDTO affiliation = requestor.getAffiliation();
                AffiliationEntity affiliationEntity= new AffiliationEntity(affiliation.getName(),affiliation.getDepartment());
                affiliationEntity.setAddress(affiliation.getAddress());
                affiliationEntity.setUrl(affiliation.getUrl());
                affiliationDAO.saveOrUpdate(affiliationEntity);
                UserEntity user = userDAO.getUserByID(requestor.getUser().getId());
                if (user == null) throw new Exception("User "+requestor.getUser().getLogin()+" not found in DB");
                user.setAffiliation(affiliationEntity);
                userDAO.updateOrPersistUser(user);
                
                //2. Save the request
                RequestEntity requestEntity = new RequestEntity();
                requestEntity.setId(requestForm.getRequestId());
                requestEntity.setReqDate(requestForm.getDate());
                requestEntity.setUserId(user);
                requestFormDAO.saveOrUpdate(requestEntity);
                
                //2. Take care of libraries
                Set<RequestLibraryEntity> librariesToDelete = new HashSet<>(requestLibraryDAO.getLibrariesByReqId(requestEntity.getId()));
                List<RequestLibraryDTO> requestLibraries = requestForm.getLibraries();
                for (RequestLibraryDTO library: requestLibraries) {
                    RequestLibraryEntity libraryEntity;
                    if (library.getId() == null) {
                        libraryEntity = new RequestLibraryEntity();
                    }else{
                        libraryEntity = requestLibraryDAO.getLibraryById(library.getId());
                        librariesToDelete.remove(libraryEntity);
                    }
                    libraryEntity.setRequestId(requestEntity);
                    libraryEntity.setLibName(library.getName());
                    libraryEntity.setLibType(library.getApplicationName());
                    libraryEntity.setReadMode(library.getReadMode());
                    libraryEntity.setReadLength(library.getReadLength().shortValue());
                    libraryEntity.setLanes(library.getLanes().shortValue());
                    libraryEntity.setVolume(library.getVolume());
                    libraryEntity.setDnaConcentration(library.getDnaConcentration());
                    libraryEntity.setTotalSize(library.getTotalSize());
                    requestLibraryDAO.saveOrUpdate(libraryEntity);
                    
                    //3. take care of samples
                    Set<RequestSampleEntity> samplesToDelete = new HashSet<>(requestSampleDAO.getSamplesByLibId(libraryEntity.getId()));
                    List<RequestSampleDTO> requestSamples = library.getSamples();
                    for (RequestSampleDTO sample: requestSamples){
                        RequestSampleEntity sampleEntity;
                        if (sample.getId()==null) {
                            sampleEntity= new RequestSampleEntity();
                        }else {
                            sampleEntity = requestSampleDAO.getSampleById(sample.getId());
                            samplesToDelete.remove(sampleEntity);
                        }
                        sampleEntity.setLibraryId(libraryEntity);
                        sampleEntity.setName(sample.getName());
                        sampleEntity.setDescription(sample.getSampleDescription());
                        sampleEntity.setOrganism(sample.getOrganism());
                        sampleEntity.setIndexI5(sample.getI5Index());
                        sampleEntity.setAdapterI5(sample.getI5Adapter());
                        sampleEntity.setIndexI7(sample.getI7Index());
                        sampleEntity.setAdapterI7(sample.getI7Adapter());
                        sampleEntity.setPrimerIndex(sample.getPrimerSequence());
                        sampleEntity.setPrimerName(sample.getPrimerName());
                        sampleEntity.setPrimerType(sample.getPrimerType());
                        requestSampleDAO.saveOrUpdate(sampleEntity);
                    }
                    
                    //4. Delete unused samples
                    for (RequestSampleEntity sampleToDelete: samplesToDelete){
                        requestSampleDAO.delete(sampleToDelete);
                    }
                    
                }
                
                //5. Delete unused libraries
                for (RequestLibraryEntity libraryToDelete: librariesToDelete) {
                    recursivelyRemoveLibrary(libraryToDelete);
                }

                
                return requestEntity.getId();
            }
        });
      
        
    }
    
    private void recursivelyRemoveLibrary(RequestLibraryEntity library) {
        Iterator iter = library.getRequestSampleSet().iterator();
        while (iter.hasNext()){
            RequestSampleEntity sample = (RequestSampleEntity) iter.next();
            iter.remove();
            requestSampleDAO.delete(sample);
        }

        requestLibraryDAO.delete(library);
    }

    public AffiliationDTO getAffiliationForUser(final Integer id) {
        try {
            return TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<AffiliationDTO>() {
                        @Override
                        public AffiliationDTO execute() throws Exception {
                             UserEntity userEntity = userDAO.getUserByID(id);
                             AffiliationEntity affiliationEntity = userEntity.getAffiliation();
                             return dtoMapper.getAffiliationFromEntity(affiliationEntity);

                        }
                    });
        } catch (Exception ex) {
            return null;
        }
    }

    public RequestFormDTO getFullRequestById(final Integer rid) {
        try {
            return TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable < RequestFormDTO > () {
                        @Override
                        public RequestFormDTO execute() throws Exception {
                            RequestEntity requestEntity = requestFormDAO.getRequestById(rid);
                            if (requestEntity!=null){
                                UserEntity requestor = requestEntity.getUserId();
                                UserEntity pi = userDAO.getUserByID(requestor.getPi());
                                return dtoMapper.getRequestFormDTOFromEntity(requestEntity,requestor,pi);
                            }
                            return null;
                        }
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
