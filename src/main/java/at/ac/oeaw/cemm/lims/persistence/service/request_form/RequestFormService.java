/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service.request_form;

import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestorDTO;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.request_form.*;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestSampleEntity;
import at.ac.oeaw.cemm.lims.persistence.service.TransactionManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestFormService {
    @Inject private RequestFormDAO requestFormDAO;
    @Inject private UserDAO userDAO;
    @Inject private SampleDAO sampleDAO;
    @Inject private RequestLibraryDAO requestLibraryDAO;
    @Inject private RequestSampleDAO requestSampleDAO;
    @Inject private RequestDTOMapper dtoMapper;
    
    public Integer saveRequestForm(final RequestFormDTO requestForm, final Boolean isNew) throws Exception {
        return TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Integer>() {
            @Override
            public Integer execute() throws Exception {       
                //1. Take care of requestor
                RequestorDTO requestor = requestForm.getRequestor();
                UserEntity user = userDAO.getUserByID(requestor.getUser().getId());
                if (user == null) throw new Exception("User "+requestor.getUser().getLogin()+" not found in DB");
                
                //2. Save the request
                RequestEntity requestEntity;
                if (isNew) {
                    requestEntity = new RequestEntity();    
                    requestEntity.setId(requestForm.getRequestId());
                    requestEntity.setReqDate(requestForm.getDate());
                    requestEntity.setUserId(user);
                }else {
                    requestEntity = requestFormDAO.getRequestById(requestForm.getRequestId());
                    if (requestEntity == null){
                        throw new Exception ("Request with id "+requestForm.getRequestId()+" not found in DB");
                    }
                }
                requestEntity.setStatus(requestForm.getStatus());
                requestEntity.setAuthFormName(requestForm.getAuthorizationFileName());
                requestEntity.setAnnotationSheetName(requestForm.getSampleAnnotationFileName());
                requestEntity.setBillingContact(requestForm.getBillingInfo().getContact());
                requestEntity.setBillingAddress(requestForm.getBillingInfo().getAddress());
                requestEntity.setBillingCode(requestForm.getBillingInfo().getBillingCode());
               
                requestFormDAO.saveOrUpdate(requestEntity,isNew);
                
                //2. Take care of libraries
                Set<RequestLibraryEntity> librariesToDelete = new HashSet<>(requestLibraryDAO.getLibrariesByReqId(requestEntity.getId()));
                Set<RequestSampleEntity> samplesToDelete = new HashSet<>();
                
                List<RequestLibraryDTO> requestLibraries = requestForm.getLibraries();
                for (RequestLibraryDTO library: requestLibraries) {
                    RequestLibraryEntity libraryEntity;
                    if (library.getId() == null) {
                        libraryEntity = new RequestLibraryEntity();
                    }else{
                        libraryEntity = requestLibraryDAO.getLibraryById(library.getId());
                        if (libraryEntity == null){
                            throw new Exception ("Library with id "+library.getId()+" not found in DB");
                        }
                        librariesToDelete.remove(libraryEntity);
                    }
                    libraryEntity.setRequestId(requestEntity);
                    libraryEntity.setLibName(library.getName());
                    libraryEntity.setReadMode(library.getReadMode());
                    libraryEntity.setReadLength(library.getReadLength().shortValue());
                    libraryEntity.setLanes(library.getLanes().shortValue());
                    libraryEntity.setVolumeBulk(library.getVolume());
                    libraryEntity.setDnaConcentrationBulk(library.getDnaConcentration());
                    libraryEntity.setTotalSize(library.getTotalSize());
                    requestLibraryDAO.saveOrUpdate(libraryEntity);
                    
                    //3. take care of samples
                    samplesToDelete.addAll(requestSampleDAO.getSamplesByLibId(libraryEntity.getId()));
                    List<RequestSampleDTO> requestSamples = library.getSamples();
                    for (RequestSampleDTO sample: requestSamples){
                        RequestSampleEntity sampleEntity;
                        if (sample.getId()==null) {
                            sampleEntity= new RequestSampleEntity();
                        }else {
                            sampleEntity = requestSampleDAO.getSampleById(sample.getId());
                            if (sampleEntity == null){
                                throw new Exception ("Sample with id "+sample.getId()+" not found in DB");
                            }
                            samplesToDelete.remove(sampleEntity);
                        }
                        sampleEntity.setLibraryId(libraryEntity);
                        sampleEntity.setApplicationType(sample.getApplicationName());
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
                }
                
                //4. Delete unused samples
                for (RequestSampleEntity sampleToDelete : samplesToDelete) {
                    requestSampleDAO.delete(sampleToDelete);
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

    public List<RequestFormDTO> getDeleatableRequests() {
        final List<RequestFormDTO> result = new LinkedList<>();

        try {
            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<RequestEntity> requestEntities = requestFormDAO.getDeleatableRequests();

                    if (requestEntities != null) {
                        for (RequestEntity requestEntity : requestEntities) {
                            UserEntity requestor = requestEntity.getUserId();
                            UserEntity pi = userDAO.getUserByID(requestor.getPi());
                            RequestFormDTO request = dtoMapper.getRequestFormDTOFromEntity(requestEntity, requestor, pi, false);
                            result.add(request);
                        }
                    }
                    return null;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public void bulkDeleteRequest(final Integer requestId) {
         try {
            TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    RequestEntity requestEntity = requestFormDAO.getRequestById(requestId);

                    if (requestEntity != null) {
                      for (RequestLibraryEntity library: requestEntity.getRequestLibrarySet()){
                          for (RequestSampleEntity sample: library.getRequestSampleSet()){
                              requestFormDAO.deleteSample(sample);
                          }
                          requestFormDAO.deleteLibrary(library);
                      }
                      requestFormDAO.deleteRrequest(requestEntity);
                    }
                    return null;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<RequestFormDTO> getRequests(final String sortField,final boolean ascending,final Map<String, Object> filters) {
        final List<RequestFormDTO> requests = new LinkedList<>();

        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<RequestEntity> requestEntities = requestFormDAO.getAllRequests(sortField, ascending, filters);

                    if (requestEntities != null) {
                        for (RequestEntity entity : requestEntities) {
                            UserEntity requestor = entity.getUserId();
                            UserEntity pi = userDAO.getUserByID(requestor.getPi());
                            requests.add(dtoMapper.getRequestFormDTOFromEntity(entity, requestor, pi, false));                        }
                    }

                    return null;
                }

            });
            System.out.println("Requests retrieval took " + (System.currentTimeMillis() - currentTime));
        } catch (Exception e) {
            e.printStackTrace();

        }
        
        return requests;
    }

    public List<RequestFormDTO> getRequests(final int first, final int pageSize, final String sortField, final boolean ascending, final Map<String, Object> filters) {
        final List<RequestFormDTO> requests = new LinkedList<>();

        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<RequestEntity> requestEntities = requestFormDAO.getRequestsPaginated(first, pageSize, sortField, ascending, filters);

                    if (requestEntities != null) {
                        for (RequestEntity entity : requestEntities) {
                            UserEntity requestor = entity.getUserId();
                            UserEntity pi = userDAO.getUserByID(requestor.getPi());
                            requests.add(dtoMapper.getRequestFormDTOFromEntity(entity, requestor, pi, false));
                        }
                    }

                    return null;
                }
            });
            System.out.println("Requests retrieval took " + (System.currentTimeMillis() - currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requests;
    }
    
    public Integer getRequestsCount(final Map<String, Object> filters){
        Integer requests = null;

        try {
            Long currentTime = System.currentTimeMillis();
            requests = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    return requestFormDAO.getRequestsCount(filters);
                }
            });
            System.out.println("Requestscount took "+(System.currentTimeMillis()-currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requests;
    }

    public Integer getMaxRequestId() {
           try {
            return TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    Integer maxIdFromSamples = sampleDAO.getMaxRequestId();
                    Integer maxIdFromRequests = requestFormDAO.getMaxRequestId();
                    return Math.max(maxIdFromSamples, maxIdFromRequests);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    

}
