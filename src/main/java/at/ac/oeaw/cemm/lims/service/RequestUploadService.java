/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service;

import at.ac.oeaw.cemm.lims.model.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.model.dto.LibraryDTO;
import at.ac.oeaw.cemm.lims.service.dao.ApplicationDAO;
import at.ac.oeaw.cemm.lims.service.dao.IndexDAO;
import at.ac.oeaw.cemm.lims.service.dao.LibraryDAO;
import at.ac.oeaw.cemm.lims.service.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.model.dto.RequestDTO;
import at.ac.oeaw.cemm.lims.model.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.service.dao.UserDAO;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.LibraryId;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import java.util.HashSet;
import java.util.Set;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class RequestUploadService {

    @Inject
    SampleDAO sampleDAO;
    @Inject
    LibraryDAO libraryDAO;
    @Inject
    IndexDAO indexDAO;
    @Inject
    ApplicationDAO applicationDAO;
    @Inject
    UserDAO userDAO;

    public SequencingIndex getIdxBySequence(final String sequence) {
        SequencingIndex result = null;
        try {
            result = TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<SequencingIndex>() {
                @Override
                public SequencingIndex execute() throws Exception {
                    return indexDAO.getIdxBySequence(sequence);
                }
            }
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public boolean checkIdxExistence(final String sequence) {
        return getIdxBySequence(sequence) != null;
    }

    public Set<PersistedSampleReceipt> uploadRequest(final RequestDTO request) throws Exception {
        
        final Set<PersistedSampleReceipt> receipts = new HashSet<>();
        
        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                User user = userDAO.getUserByLogin(request.getRequestor());
                if (user == null) {
                    throw new Exception("User with login " + request.getRequestor() + " not found in DB");
                }
                for (LibraryDTO library : request.getLibraries().values()) {
                    Integer libraryId = libraryDAO.getMaxLibraryId() + 1;
                    String finalLibraryName = library.getName() + "_L" + libraryId;

                    for (SampleDTO sample : library.getSamples()) {
                        Sample sampleEntity = new Sample();
                        sampleEntity.setUser(user);

                        SequencingIndex seqIndexEntity = indexDAO.getIdxBySequence(sample.getIndex().getIndex());
                        if (seqIndexEntity == null) {
                            throw new Exception("Index with sequence " + sample.getIndex().getIndex() + " not found in DB");
                        }
                        sampleEntity.setSequencingIndexes(seqIndexEntity);

                        ApplicationDTO applicationDTO = sample.getApplication();
                        Application existingApplicationEntity = applicationDAO.getApplicationByParams(
                                applicationDTO.getReadLength(),
                                applicationDTO.getReadMode(),
                                applicationDTO.getInstrument(),
                                applicationDTO.getApplicationName(),
                                applicationDTO.getDepth());
                        if (existingApplicationEntity != null) {
                            sampleEntity.setApplication(existingApplicationEntity);
                        } else {
                            Application newApplicationEntity = new Application();
                            newApplicationEntity.setReadlength(applicationDTO.getReadLength());
                            newApplicationEntity.setReadmode(applicationDTO.getReadMode());
                            newApplicationEntity.setInstrument(applicationDTO.getInstrument());
                            newApplicationEntity.setApplicationname(applicationDTO.getApplicationName());
                            newApplicationEntity.setDepth(applicationDTO.getDepth());
                            try {
                                applicationDAO.persistApplication(newApplicationEntity);
                            } catch (Exception e) {
                                throw new Exception("Error while persisting application " + newApplicationEntity.getApplicationname());
                            }
                            sampleEntity.setApplication(newApplicationEntity);
                        }

                        sampleEntity.setType(sample.getType());
                        sampleEntity.setLibrarySynthesisNeeded(sample.isSyntehsisNeeded());
                        sampleEntity.setOrganism(sample.getOrganism());
                        sampleEntity.setAntibody(sample.getAntibody());
                        sampleEntity.setStatus(sample.getStatus());
                        sampleEntity.setName(sample.getName());
                        sampleEntity.setDescription(sample.getDescription());
                        sampleEntity.setBioanalyzerDate(sample.getBioanalyzerDate());
                        sampleEntity.setBionalyzerBiomolarity(sample.getBioAnalyzerMolarity());
                        sampleEntity.setConcentration(sample.getConcentration());
                        sampleEntity.setTotalAmount(sample.getTotalAmount());
                        sampleEntity.setBulkFragmentSize(sample.getBulkFragmentSize());
                        sampleEntity.setComment(sample.getComment());
                        sampleEntity.setRequestDate(sample.getRequestDate());
                        sampleEntity.setSubmissionId(sample.getSubmissionId());
                        sampleEntity.setExperimentName(sample.getExperimentName());
                        sampleEntity.setCostCenter(sample.getCostcenter());
                        try {
                            sampleDAO.persistSample(sampleEntity);
                            receipts.add(new PersistedSampleReceipt(sampleEntity.getId(),sampleEntity.getName()));
                        } catch (Exception e) {
                            throw new Exception("Error while persisting sample " + sampleEntity.getName());
                        }
                        LibraryId libraryIdEntity = new LibraryId(libraryId, sampleEntity.getId());
                        Library libraryEntity = new Library(libraryIdEntity, sampleEntity, finalLibraryName);
                        try {
                            libraryDAO.persistLibrary(libraryEntity);
                        } catch (Exception e) {
                            throw new Exception("Error while persisting library " + libraryEntity.getLibraryName());
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
