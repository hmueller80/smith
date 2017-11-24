/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.SampleService;
import at.ac.oeaw.cemm.lims.persistence.dao.ApplicationDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.IndexDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.LibraryDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.ApplicationEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SequencingIndexEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.hibernate.Hibernate;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class LazySampleService implements SampleService {

    @Inject SampleDAO sampleDAO;
    @Inject LibraryDAO libraryDAO;
    @Inject IndexDAO indexDAO;
    @Inject ApplicationDAO applicationDAO;
    @Inject UserDAO userDAO;
    
    @Override
    public SampleDTO getSampleById(final int sampleId) {
        SampleEntity sampleEntity = null;

        try {
            sampleEntity = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<SampleEntity>() {
                @Override
                public SampleEntity execute() throws Exception {
                    return sampleDAO.getSampleById(sampleId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return DTOMapper.getSampleDTOfromEntity(sampleEntity);
    }
    
    @Override
     public SampleDTO getFullSampleById(final int sampleId) {
        SampleEntity sample = null;

        try {
            sample = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<SampleEntity>() {
                @Override
                public SampleEntity execute() throws Exception {
                    SampleEntity sample = sampleDAO.getSampleById(sampleId);
                    Hibernate.initialize(sample.getApplication());
                    Hibernate.initialize(sample.getSequencingIndexes());
                    return sample;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DTOMapper.getSampleDTOfromEntity(sample);
    }
     
    @Override
    public int getSamplesCount(final int first, final int pageSize, final String sortField, final boolean ascending, final Map<String, Object> filters) {
        Integer samples = null;

        try {
            Long currentTime = System.currentTimeMillis();
            samples = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    return sampleDAO.getSamplesCount(first, pageSize, sortField, ascending, filters);
                }
            });
            System.out.println("Samples count took "+(System.currentTimeMillis()-currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }

    @Override
    public List<SampleDTO> getSamples(final int first, final int pageSize, final String sortField, final boolean ascending, final Map<String, Object> filters) {
        final List<SampleDTO> samples = new LinkedList<>();

        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<SampleEntity> sampleEntities = sampleDAO.getSamples(first, pageSize, sortField, ascending, filters);

                    for (SampleEntity entity : sampleEntities) {
                        samples.add(DTOMapper.getSampleDTOfromEntity(entity));
                    }
                    
                    return null;
                }

            });
            System.out.println("Samples retrieval took " + (System.currentTimeMillis() - currentTime));

            return samples;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }

    @Override
    public List<String> getAllLibraries() {
        List<String> result = new LinkedList<String>();
        try {
            result = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<List<String>>() {
                @Override
                public List<String> execute() throws Exception {
                    return libraryDAO.getAllLibraryNames();
                }
            }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getAllIndexes() {
        List<String> result = new LinkedList<String>();
        try {
            result = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<List<String>>() {
                @Override
                public List<String> execute() throws Exception {
                    return indexDAO.getAllIndexes();
                }
            }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    @Override
    public PersistedSampleReceipt saveOrUpdateSample(final SampleDTO sample, final boolean isNew) throws Exception {
        
        PersistedSampleReceipt receipt = TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<PersistedSampleReceipt>() {
            @Override
            public PersistedSampleReceipt execute() throws Exception {
                UserEntity user = userDAO.getUserByID(sample.getUser().getId());
                return persistOrUpdateSingleSample(sample,isNew,user);
            }
        });
        
        return receipt;
    }
    
    @Override
    public void deleteSample(final SampleDTO sample) throws Exception {
        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                Integer sampleId = sample.getId();
                if (sampleId!=null){
                    SampleEntity sampleEntity = sampleDAO.getSampleById(sampleId);
                    if (sampleEntity!=null){
                        sampleDAO.deleteSample(sampleEntity);
                    }
                }
                return null;
            }
        });
    }
    
    @Override
    public Boolean checkIdxExistence(final String sequence) {
        try {
            return TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Boolean>() {
                @Override
                public Boolean execute() throws Exception {
                    return (indexDAO.getIdxBySequence(sequence) != null);
                }
            });
        } catch (Exception ex) {
           return false;
        }
     }
    
    @Override
    public List<PersistedSampleReceipt> bulkUpdateSamples(final List<SampleDTO> samplesToUpdate) {
        final List<PersistedSampleReceipt> receipts = new ArrayList<> ();
        
        try{
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    for (SampleDTO sample: samplesToUpdate) {
                        UserEntity user = userDAO.getUserByID(sample.getUser().getId());
                        receipts.add(persistOrUpdateSingleSample(sample,false,user));
                    }
                    
                    return null;
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return receipts;
    }
    
    
    
    
    protected PersistedSampleReceipt persistOrUpdateSingleSample(SampleDTO sample, boolean isNew, UserEntity user) throws Exception {
        if (user == null) {
            throw new Exception("Cannot create a sample with null user");
        }
        SampleEntity sampleEntity = new SampleEntity();

        sampleEntity.setUser(user);
        
        SequencingIndexEntity seqIndexEntity = indexDAO.getIdxBySequence(sample.getIndex().getIndex());
        if (seqIndexEntity == null) {
            throw new Exception("Index with sequence " + sample.getIndex().getIndex() + " not found in DB");
        }
        sampleEntity.setSequencingIndexes(seqIndexEntity);
        
        sampleEntity.setApplication(persistApplication(sample.getApplication()));
        
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
            if (isNew) {
                sampleDAO.persistSample(sampleEntity);
            } else {
                sampleEntity.setId(sample.getId());
                sampleDAO.updateSample(sampleEntity);
            }
            return new PersistedSampleReceipt(sampleEntity.getId(), sampleEntity.getName());
        } catch (Exception e) {
            throw new Exception("Error while persisting sample " + sample.getName(),e);
        }
    }
    
    protected ApplicationEntity persistApplication(ApplicationDTO  application) throws Exception {
        ApplicationEntity existingApplicationEntity = applicationDAO.getApplicationByParams(
                application.getReadLength(),
                application.getReadMode(),
                application.getInstrument(),
                application.getApplicationName(),
                application.getDepth());

        if (existingApplicationEntity != null) {
            return existingApplicationEntity;
        } else {
            try {
                ApplicationEntity applicationEntity = new ApplicationEntity();
                applicationEntity.setApplicationname(application.getApplicationName());
                applicationEntity.setReadlength(application.getReadLength());
                applicationEntity.setReadmode(application.getReadMode());
                applicationEntity.setInstrument(application.getInstrument());
                applicationEntity.setDepth(application.getDepth());
                      
                
                applicationDAO.persistApplication(applicationEntity);
                
                return applicationEntity;
            } catch (Exception e) {
                throw new Exception("Error while persisting application " + application.getApplicationName());
            }
        }
    }

  
}
