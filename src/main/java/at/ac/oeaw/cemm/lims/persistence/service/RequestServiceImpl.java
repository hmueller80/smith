/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.LibraryToRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.persistence.dao.LibraryDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import java.util.HashSet;
import java.util.Set;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalLibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import at.ac.oeaw.cemm.lims.api.persistence.RequestService;
import at.ac.oeaw.cemm.lims.persistence.dao.RequestDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.request_form.RequestFormDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.request_form.RequestLibraryDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.MinimalRequestEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.request_form.RequestEntity;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestServiceImpl implements RequestService {
    
    @Inject RequestDAO requestDAO;
    @Inject RequestFormDAO requestFormDAO;
    @Inject RequestLibraryDAO requestLibraryDAO;
    @Inject LibraryDAO libraryDAO;
    @Inject SampleDAO sampleDAO;
    @Inject UserDAO userDAO;
    @Inject LazySampleService lazySampleService;
    @Inject DTOMapper myDTOMapper;
    
    @Override
    public Set<PersistedEntityReceipt> uploadRequest(final RequestDTO request) throws Exception {

        final Set<PersistedEntityReceipt> receipts = new HashSet<>();

        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                UserEntity user = userDAO.getUserByLogin(request.getRequestorUser().getLogin());
                if (user == null) {
                    throw new Exception("User with login " + request.getRequestorUser() + " not found in DB");
                }
                for (LibraryDTO library : request.getLibrariesMap().values()) {
                    LibraryEntity libraryEntity = new LibraryEntity();
                    libraryEntity.setLibraryName(library.getName());
                    try {
                        libraryDAO.persistLibrary(libraryEntity);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Error while persisting library " + libraryEntity.getLibraryName());
                    }
                    HibernateUtil.getSessionFactory().getCurrentSession().flush();
                    for (SampleDTO sample : library.getSamples()) {
                        sample.setLibraryName(libraryEntity.getLibraryName());
                        try {
                            PersistedEntityReceipt receipt = lazySampleService.persistOrUpdateSingleSample(sample, true, user);
                            receipts.add(receipt);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception("Error while persisting sample " + sample.getName());
                        }
                    }
                }
                
                if (request.getRequestId()!=null){
                    RequestEntity requestEntity = requestFormDAO.getRequestById(request.getRequestId());
                    if (requestEntity!=null){
                        requestEntity.setStatus(RequestFormDTO.STATUS_ACCEPTED);
                        requestFormDAO.saveOrUpdate(requestEntity,false);
                    }
                }


                return null;
            }
        }
        );

        return receipts;
    }
    
       @Override
    public boolean checkRequestExistence(final Integer id) {
        try {
            return TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Boolean>() {
                @Override
                public Boolean execute() throws Exception {
                    return (requestDAO.checkRequestExistence(id));
                }
            });
        } catch (Exception ex) {
           return false;
        }
    }

    @Override
    public List<RequestDTO> getDeleatableLibrariesInRequests() {
        final Map<Integer, RequestDTO> deleatableRequests = new LinkedHashMap<>();


        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    
                    List<MinimalLibraryEntity> deleatableLibraries = libraryDAO.getDeleatableLibraries();
                    
                    if (deleatableLibraries != null) {
                        for (MinimalLibraryEntity entity : deleatableLibraries) {
                            RequestDTO request = deleatableRequests.get(entity.getRequestId());
                            if (request == null) {
                                request = myDTOMapper.getMinimalRequestDTOFromEntity(entity);
                                deleatableRequests.put(request.getRequestId(), request);
                            }
                            request.addOrGetLibrary(myDTOMapper.getLibraryDTOFromMinimalEntity(entity));
                        }
                    }
                   
                    return null;
                }

            });
            System.out.println("Deleatable requests retrieval took " + (System.currentTimeMillis() - currentTime));
        } catch (Exception e) {
            e.printStackTrace();

        }       

        return new LinkedList<>(deleatableRequests.values());
    }
    
    @Override
    public void deleteAllLibrariesInRequest(final RequestDTO request) throws Exception{
        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                System.out.println("Deleting request "+request.getRequestId());
                for (LibraryDTO library : request.getLibrariesMap().values()) {
                    System.out.println("Considering library "+library.getName()+" with id "+library.getId());
                    LibraryEntity libraryEntity = libraryDAO.getLibraryById(library.getId());
                    
                    for (SampleEntity sample : libraryEntity.getSamples()) {
                       System.out.println("Considering sample "+sample.getName()+" with id "+sample.getId()+" and submission ID "+sample.getSubmissionId());

                        if (Objects.equals(sample.getSubmissionId(), request.getRequestId())) {
                            if (SampleDTO.status_requested.equals(sample.getStatus())) {
                                sampleDAO.deleteSample(sample);
                            } else {
                                throw new Exception("Library with Id " + library.getId() + " contains samples in status different than " + SampleDTO.status_requested);
                            }
                        }
                    }

                    HibernateUtil.getSessionFactory().getCurrentSession().flush();
                    HibernateUtil.getSessionFactory().getCurrentSession().refresh(libraryEntity);

                    if (libraryEntity.getSamples().isEmpty()) {
                        libraryDAO.deleteLibrary(libraryEntity);
                    }
                }

                if (!requestDAO.checkRequestExistence(request.getRequestId())) {
                    RequestEntity requestForm = requestFormDAO.getRequestById(request.getRequestId());
                    if (requestForm != null) {
                        requestForm.setStatus(RequestFormDTO.STATUS_NEW);
                        requestFormDAO.saveOrUpdate(requestForm, false);
                    }

                }
                                                      
                return null;
            }
        });        
    }
        
    @Override
    public void deleteAllSamplesForLibraryAndRequest(final Integer libraryId, final Integer requestId) throws Exception {
        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                LibraryEntity library = libraryDAO.getLibraryById(libraryId);

                for (SampleEntity sample : library.getSamples()) {
                    if (Objects.equals(sample.getSubmissionId(), requestId)) {
                        if (SampleDTO.status_requested.equals(sample.getStatus())) {
                            sampleDAO.deleteSample(sample);
                        } else {
                            throw new Exception("Library with Id " + libraryId + " contains samples in status different than " + SampleDTO.status_requested );
                        }
                    }
                }

                HibernateUtil.getSessionFactory().getCurrentSession().flush();
                HibernateUtil.getSessionFactory().getCurrentSession().refresh(library);

                if (library.getSamples().isEmpty()) {
                    libraryDAO.deleteLibrary(library);
                }
                
                if (!requestDAO.checkRequestExistence(requestId)) {
                    RequestEntity requestForm = requestFormDAO.getRequestById(requestId);
                    if (requestForm != null) {
                        requestForm.setStatus(RequestFormDTO.STATUS_NEW);
                        requestFormDAO.saveOrUpdate(requestForm, false);
                    }

                }

                return null;
            }
        });
    }

   

    @Override
    public List<LibraryDTO> getEditableLibrariesInRequest(final Integer requestId) {        
        final List<LibraryDTO> result = new LinkedList<>();

        if (requestId==null) return result;

        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<LibraryEntity> deleatableLibraries = libraryDAO.getDeleatableLibrariesInRequest(requestId);
                    
                    if (deleatableLibraries != null) {
                        for (LibraryEntity entity : deleatableLibraries) {
                            LibraryDTO libraryDTO = myDTOMapper.getLibraryDTOFromEntity(entity, true);
                            result.add(libraryDTO);
                        }
                    }

                    return null;
                }

            });
            System.out.println("Deleatable requests retrieval took " + (System.currentTimeMillis() - currentTime));
        } catch (Exception e) {
            e.printStackTrace();

        }       

        return result;
    }

    @Override
    public LibraryDTO getLibraryByName(final String libraryName) {

        if (libraryName == null || libraryName.isEmpty()) {
            return null;
        }

        try {
            return TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<LibraryDTO>() {
                @Override
                public LibraryDTO execute() throws Exception {

                    LibraryEntity library = libraryDAO.getLibraryByName(libraryName);

                    if (library != null) {
                        return myDTOMapper.getLibraryDTOFromEntity(library, true);
                    }

                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deleteLibraryIfEmpty(final String oldLibraryName) {
        try {
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    LibraryEntity library = libraryDAO.getLibraryByName(oldLibraryName);
                    
                    if (library.getSamples().isEmpty()) {
                        libraryDAO.deleteLibrary(library);
                    }
                    
                    return null;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }

    @Override
    public RequestDTO getMinimalRequestByIdAndRequestor(final Integer rid, final String requestor) {
        try {
            return TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<RequestDTO >() {
                @Override
                public RequestDTO  execute() throws Exception {

                    MinimalRequestEntity request = requestDAO.getMinimalRequestByIdAndRequestor(rid,requestor);

                    if (request != null) {
                        return myDTOMapper.getRequestDTOFromEntity(request);
                    }

                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<RequestDTO> getAllRequests() {
        final List<RequestDTO> result = new LinkedList<>();

        try {
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<MinimalRequestEntity> requests = requestDAO.getAllMinimalRequests();
                    
                    for (MinimalRequestEntity request: requests) {
                        result.add(myDTOMapper.getRequestDTOFromEntity(request));
                    }

                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    @Override
    public List<LibraryToRunDTO> getRunnableLibraries(){
        
        final List<LibraryToRunDTO> result = new LinkedList<>();

        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<MinimalLibraryEntity> deleatableLibraries = libraryDAO.getRunnableLibraries();
                    
                    if (deleatableLibraries != null) {
                        for (MinimalLibraryEntity entity : deleatableLibraries) {
                            LibraryEntity fullLibrary = libraryDAO.getLibraryById(entity.getLibraryId());
                            result.add(myDTOMapper.getLibraryToRunDTOFromMinimalEntity(entity, fullLibrary));
                        }
                    }

                    return null;
                }

            });
            System.out.println("Runnable libraries retrieval took " + (System.currentTimeMillis() - currentTime));
        } catch (Exception e) {
            e.printStackTrace();

        }       

        return result;
    }
}
