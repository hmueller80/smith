/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.service;

import at.ac.oeaw.cemm.lims.service.dao.ApplicationDAO;
import at.ac.oeaw.cemm.lims.service.dao.IndexDAO;
import at.ac.oeaw.cemm.lims.service.dao.LibraryDAO;
import at.ac.oeaw.cemm.lims.service.dao.SampleDAO;
import it.iit.genomics.cru.smith.entity.Application;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import org.hibernate.Hibernate;
import org.primefaces.model.SortOrder;

/**
 *
 * @author dbarreca
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class LazySampleService {

    @Inject
    SampleDAO sampleDAO;
    @Inject
    LibraryDAO libraryDAO;
    @Inject
    IndexDAO indexDAO;
    @Inject
    ApplicationDAO applicationDAO;

    public Sample getSampleById(final int sampleId) {
        Sample sample = null;

        try {
            sample = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Sample>() {
                @Override
                public Sample execute() throws Exception {
                    return sampleDAO.getSampleById(sampleId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sample;
    }
    
     public Sample getFullSampleById(final int sampleId) {
        Sample sample = null;

        try {
            sample = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Sample>() {
                @Override
                public Sample execute() throws Exception {
                    Sample sample = sampleDAO.getSampleById(sampleId);
                    Hibernate.initialize(sample.getApplication());
                    Hibernate.initialize(sample.getSequencingIndexes());
                    return sample;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sample;
    }
    
    public int getSamplesCount(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String, Object> filters) {
        Integer samples = null;

        try {
            Long currentTime = System.currentTimeMillis();
            samples = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    return sampleDAO.getSamplesCount(first, pageSize, sortField, sortOrder, filters);
                }
            });
            System.out.println("Samples count took "+(System.currentTimeMillis()-currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }

    public List<Sample> getSamples(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String, Object> filters) {
        List<Sample> samples = null;

        try {
            Long currentTime = System.currentTimeMillis();
            samples = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<List<Sample>>() {
                @Override
                public List<Sample> execute() throws Exception {
                    return sampleDAO.getSamples(first, pageSize, sortField, sortOrder, filters);
                }
            });
            System.out.println("Samples retrieval took "+(System.currentTimeMillis()-currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }

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
    
    public PersistedSampleReceipt saveOrUpdateSample(final Sample sample, final boolean isNew) throws Exception {
        
        PersistedSampleReceipt receipt = TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<PersistedSampleReceipt>() {
            @Override
            public PersistedSampleReceipt execute() throws Exception {
         
                SequencingIndex seqIndexEntity = indexDAO.getIdxBySequence(sample.getSequencingIndexes().getIndex());
                if (seqIndexEntity == null) {
                    throw new Exception("Index with sequence " + sample.getSequencingIndexes().getIndex() + " not found in DB");
                }
                sample.setSequencingIndexes(seqIndexEntity);
                Application existingApplicationEntity = applicationDAO.getApplicationByParams(
                                sample.getApplication().getReadlength(),
                                sample.getApplication().getReadmode(),
                                sample.getApplication().getInstrument(),
                                sample.getApplication().getApplicationname(),
                                sample.getApplication().getDepth());
              
                if (existingApplicationEntity != null) {
                    sample.setApplication(existingApplicationEntity);
                } else {
                    try {
                        applicationDAO.persistApplication(sample.getApplication());
                    } catch (Exception e) {
                        throw new Exception("Error while persisting application " + sample.getApplication().getApplicationname());
                    }
                }
                
                try {
                    if (isNew){
                        sampleDAO.persistSample(sample);
                    }else{
                        sampleDAO.updateSample(sample);
                    }
                    return new PersistedSampleReceipt(sample.getId(), sample.getName());
                } catch (Exception e) {
                    throw new Exception("Error while persisting sample " + sample.getName());
                }
             
            }
        });
        
        return receipt;
    }
    
    public void deleteSample(final Sample sample) throws Exception {
        TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                Integer sampleId = sample.getId();
                if (sampleId!=null){
                    sampleDAO.deleteSampleWithId(sample);
                }
                return null;
            }
        });
    }

}
