/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.persistence.dao.ApplicationDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.IndexDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.LibraryDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import java.util.HashSet;
import java.util.Set;
import at.ac.oeaw.cemm.lims.api.persistence.UploadService;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.LibraryIdEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class RequestUploadServiceImpl implements UploadService {

    @Inject SampleDAO sampleDAO;
    @Inject LibraryDAO libraryDAO;
    @Inject IndexDAO indexDAO;
    @Inject ApplicationDAO applicationDAO;
    @Inject UserDAO userDAO;
    @Inject LazySampleService lazySampleService;
    
    @Override
    public Set<PersistedSampleReceipt> uploadRequest(final RequestDTO request) throws Exception {

        final Set<PersistedSampleReceipt> receipts = new HashSet<>();

        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                UserEntity user = userDAO.getUserByLogin(request.getRequestor());
                if (user == null) {
                    throw new Exception("User with login " + request.getRequestor() + " not found in DB");
                }
                for (LibraryDTO library : request.getLibraries().values()) {
                    Integer libraryId = libraryDAO.getMaxLibraryId() + 1;
                    String finalLibraryName = library.getName() + "_L" + libraryId;

                    for (SampleDTO sample : library.getSamples()) {
                        try {
                            PersistedSampleReceipt receipt = lazySampleService.persistOrUpdateSingleSample(sample, true, user);
                            receipts.add(receipt);
                            LibraryIdEntity libraryIdEntity = new LibraryIdEntity(libraryId, receipt.getId());
                            LibraryEntity libraryEntity = new LibraryEntity(libraryIdEntity,finalLibraryName);
                            try {
                                libraryDAO.persistLibrary(libraryEntity);
                            } catch (Exception e) {
                                throw new Exception("Error while persisting library " + libraryEntity.getLibraryName());
                            }
                        } catch (Exception e) {
                            throw new Exception("Error while persisting sample " + sample.getName());
                        }

                    }
                }

                return null;
            }
        }
        );

        return receipts;
    }

}
