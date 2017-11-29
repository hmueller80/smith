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
import at.ac.oeaw.cemm.lims.persistence.dao.LaneDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.SampleDAO;
import at.ac.oeaw.cemm.lims.persistence.dao.UserDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.LaneEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.SampleRunIdEntity;
import at.ac.oeaw.cemm.lims.persistence.entity.UserEntity;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class LazyRunService implements RunService {

    @Inject SampleRunDAO runDAO;
    @Inject UserDAO userDAO;
    @Inject SampleDAO sampleDAO;
    @Inject LaneDAO laneDAO;
    @Inject DTOMapper myDTOMapper;
    
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
                            result.add(myDTOMapper.getSampleRunDTOFromEntity(entity));
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
                            runs.add(myDTOMapper.getSampleRunDTOFromEntity(entity));
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
    public Integer getRunsCount(final Map<String, Object> filters) {
        Integer runs = null;

        try {
            Long currentTime = System.currentTimeMillis();
            runs = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    return runDAO.getRunsCount(filters);
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
                    return myDTOMapper.getSampleRunDTOFromEntity(sampleRun);
                }
            });
           
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }     
    }
      
    @Override
    public void bulkDeleteRun(final Integer runId) throws Exception{
        TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                
                List<SampleRunEntity> sampleRuns = runDAO.getSampleRunsById(runId);
                
                for (SampleRunEntity sampleRun: sampleRuns){
                    Integer sampleId = sampleRun.getsamId();
                    
                    runDAO.deleteSampleRun(sampleRun);
                    HibernateUtil.getSessionFactory().getCurrentSession().flush();
                    SampleEntity sampleEntity = sampleDAO.getSampleById(sampleId);
                    if (sampleEntity != null) {
                        if (sampleEntity.getSampleRuns().isEmpty()) {
                            sampleEntity.setStatus(SampleEntity.status_requested);
                            sampleDAO.updateSample(sampleEntity);
                        }
                    }
                    
                }

                return null;
            }
        });
    }

    @Override
    public boolean runExists(final int runId) throws Exception {
        return TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Boolean>() {
            @Override
            public Boolean execute() throws Exception {

                return runDAO.checkRunExistence(runId);

            }
        });
    }

    @Override
    public Set<PersistedEntityReceipt> bulkUploadRuns(final Set<SampleRunDTO> sampleRuns) throws Exception {
        final Set<PersistedEntityReceipt> result = new HashSet<>();

        TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                for (SampleRunDTO sampleRun : sampleRuns) {
                    result.add(presistOrUpdateSampleRun(sampleRun, true));
                }
                return null;
            }
        });

        return result;
    }
    
    @Override
    public PersistedEntityReceipt uploadSingleRun(final SampleRunDTO sampleRun, final boolean isNew) throws Exception {

        return TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<PersistedEntityReceipt>() {
            @Override
            public PersistedEntityReceipt execute() throws Exception {
                return presistOrUpdateSampleRun(sampleRun, isNew);
            }
        });

    }
    
    @Override
    public List<SampleRunDTO> getRunsByFlowCell(final String FCID) {
        final List<SampleRunDTO> runs = new LinkedList<>();

        try {
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {

                    List<SampleRunEntity> runEntities = runDAO.getRunsByFlowcell(FCID);

                    if (runEntities != null) {
                        for (SampleRunEntity entity : runEntities) {
                            runs.add(myDTOMapper.getSampleRunDTOFromEntity(entity));
                        }
                    }

                    return null;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();

        }

        return runs;
    }

    protected PersistedEntityReceipt presistOrUpdateSampleRun(SampleRunDTO sampleRun, boolean isNew) throws Exception {

        SampleRunEntity sampleRunEntity = new SampleRunEntity();

        SampleEntity sampleEntity = sampleDAO.getSampleById(sampleRun.getSample().getId());
        if (sampleEntity == null) {
            throw new Exception("Cannot find entity with id " + sampleRun.getSample().getId() + " associated with sampleRun");
        }

        SampleRunIdEntity id = new SampleRunIdEntity();
        id.setSamId(sampleEntity.getId());
        Integer runId = sampleRun.getRunId();
        if (runId == null){
            if (!isNew){
                throw new Exception("Cannot update Sample Run with null ID");
            }else{
                runId = runDAO.getMaxRunId() + 1;                
            }
        }
        id.setRunId(runId);
        sampleRunEntity.setId(id);

        sampleRunEntity.setsample(sampleEntity);

        UserEntity userEntity = userDAO.getUserByID(sampleRun.getOperator().getId());
        if (userEntity == null) {
            throw new Exception("Cannot find user with id " + sampleRun.getSample().getId() + " and login " + sampleRun.getOperator().getId() + " associated with sampleRun");
        }
        sampleRunEntity.setUser(userEntity);

        sampleRunEntity.setControl(sampleRun.getIsControl());
        sampleRunEntity.setFlowcell(sampleRun.getFlowcell());
        sampleRunEntity.setRunFolder(sampleRun.getRunFolder());
        
        //UPDATE SAMPLE ENTITY
        sampleEntity.setStatus(SampleEntity.status_running);
        sampleDAO.updateSample(sampleEntity);
        
        //SAVE OR UPDATE SAMPLE RUN ENTITY
        if (isNew) {
            runDAO.persistSampleRun(sampleRunEntity);
        } else {
            runDAO.updateSampleRun(sampleRunEntity);
        }
               
        HibernateUtil.getSessionFactory().getCurrentSession().flush();
             
        Set<String> existingLanesForSampleRun = new HashSet<>();
        for (LaneEntity laneEntity : laneDAO.getLanesBySampleAndRun(id.getRunId(), id.getSamId())) {
            String laneName = laneEntity.getLaneName();
            if (!sampleRun.getLanes().contains(laneName)) {
                //Lane was removed!
                laneDAO.deleteEntity(laneEntity);
            } else {
                existingLanesForSampleRun.add(laneEntity.getLaneName());
            }
        }

        Set<String> lanesToAdd = sampleRun.getLanes();
        lanesToAdd.removeAll(existingLanesForSampleRun);
        for (String lane: lanesToAdd){
            laneDAO.addLaneForSampleRun(lane,sampleRunEntity);
        }
        
        HibernateUtil.getSessionFactory().getCurrentSession().flush();
        HibernateUtil.getSessionFactory().getCurrentSession().refresh(sampleRunEntity);

        
        return new PersistedEntityReceipt(sampleRunEntity.getId().getRunId(), sampleEntity.getName());

    }

  
}
