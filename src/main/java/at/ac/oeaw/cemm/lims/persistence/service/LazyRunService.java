/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.persistence.RunService;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleRunDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunEntity;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import at.ac.oeaw.cemm.lims.api.dto.SampleRunDTO;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunIdEntity;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class LazyRunService implements RunService {

    @Inject SampleRunDAO runDAO;
    
    @Override
    public Set<SampleRunDTO> getSampleRunByRunId(final int id) {
        final Set<SampleRunDTO> result = new HashSet<>();
        
        try {
           TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<SampleRunEntity> sampleRun = runDAO.getSampleRunsById(id);
                    if (sampleRun!=null){
                        for (SampleRunEntity entity: sampleRun){
                            result.add(DTOMapper.getSampleRunDTOFromEntity(entity));
                        }
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
    public List<SampleRunDTO> getRuns(final int first, final int pageSize, final String sortField, final boolean ascending, final Map<String, Object> filters) {
        final List<SampleRunDTO> runs = new LinkedList<>();

        try {
            Long currentTime = System.currentTimeMillis();
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<SampleRunEntity> runEntities = runDAO.getRuns(first, pageSize, sortField, ascending, filters);

                    if (runEntities != null) {
                        for (SampleRunEntity entity : runEntities) {
                            runs.add(DTOMapper.getSampleRunDTOFromEntity(entity));
                        }
                    }

                    return null;
                }

            });
            System.out.println("Runs retrieval took " + (System.currentTimeMillis() - currentTime));
        } catch (Exception e) {
            e.printStackTrace();

        }
       

        return runs;
    }

    @Override
    public Integer getRunsCount(final int first, final int pageSize,final String sortField, final boolean ascending,final Map<String, Object> filters) {
        Integer runs = null;

        try {
            Long currentTime = System.currentTimeMillis();
            runs = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    return runDAO.getRunsCount(first, pageSize, sortField, ascending, filters);
                }
            });
            System.out.println("Samples count took "+(System.currentTimeMillis()-currentTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return runs;
    }

    @Override
    public SampleRunDTO getSampleRunById(final int runId,final int samId) {
        try{
            return TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<SampleRunDTO>() {
                @Override
                public SampleRunDTO execute() throws Exception {
                    SampleRunEntity sampleRun = runDAO.getSampleRunById(runId,samId);
                    return DTOMapper.getSampleRunDTOFromEntity(sampleRun);
                }
            });
           
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        
    }


}
