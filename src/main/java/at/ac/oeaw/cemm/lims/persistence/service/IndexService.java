/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.service;

import at.ac.oeaw.cemm.lims.api.dto.lims.BarcodeDTO;
import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.persistence.dao.IndexDAO;
import at.ac.oeaw.cemm.lims.persistence.entity.Barcode;
import at.ac.oeaw.cemm.lims.persistence.entity.BarcodeKit;
import java.util.HashMap;
import java.util.HashSet;
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
public class IndexService {

    @Inject
    IndexDAO indexDAO;
    @Inject
    DTOMapper myDTOMapper;

    public Map<String, Map<IndexType, Set<BarcodeDTO>>> getAllBarcodes() {
        final Map<String, Map<IndexType, Set<BarcodeDTO>>> result = new HashMap<>();

        try {
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<BarcodeKit> barcodes = indexDAO.getAllKits();
                    if (barcodes != null) {
                        for (BarcodeKit entity : barcodes) {
                            BarcodeDTO dto = myDTOMapper.getBarcodeFromKitEntity(entity);
                            Map<IndexType, Set<BarcodeDTO>> barcodesForType = result.get(dto.getKitName());
                            if (barcodesForType == null) {
                                barcodesForType = new HashMap<>();
                                result.put(dto.getKitName(), barcodesForType);
                            }
                            Set<BarcodeDTO> indexes = barcodesForType.get(dto.getIndex().getType());
                            if (indexes == null) {
                                indexes = new HashSet<>();
                                barcodesForType.put(dto.getIndex().getType(), indexes);
                            }
                            indexes.add(dto);
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

    public List<BarcodeDTO> getBarcodesForKit(final String kitName) {
        final List<BarcodeDTO> result = new LinkedList<>();

        try {
            TransactionManager.doInTransaction(new TransactionManager.TransactionCallable<Void>() {
                @Override
                public Void execute() throws Exception {
                    List<BarcodeKit> barcodes = indexDAO.getKit(kitName);
                    if (barcodes != null) {
                        for (BarcodeKit entity : barcodes) {
                            BarcodeDTO dto = myDTOMapper.getBarcodeFromKitEntity(entity);
                            result.add(dto);
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

    public Boolean kitExists(final String kitName) {
        try {
            return TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Boolean>() {
                @Override
                public Boolean execute() throws Exception {

                    return indexDAO.checkKitExistence(kitName);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateOrInsertKit(final String kitName, final List<BarcodeDTO> barcodes, final boolean isNew) throws Exception {
        TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                if (!isNew && !indexDAO.checkKitExistence(kitName)) {
                    throw new Exception("Kit with name " + kitName + " was not found");
                } else if (isNew && indexDAO.checkKitExistence(kitName)) {
                    throw new Exception("Kit with name " + kitName + " is not new");
                }

                Map<String, BarcodeKit> toDelete = new HashMap<>();
                if (!isNew) {
                    for (BarcodeKit barcodeEntity : indexDAO.getKit(kitName)) {
                        toDelete.put(barcodeEntity.getBarcodeKitPK().getSequenceName(), barcodeEntity);
                    }
                }

                for (BarcodeDTO barcode : barcodes) {
                    BarcodeKit entity;
                    Barcode barcodeEntity;
                    if (toDelete.containsKey(barcode.getIndexName())){
                        entity = toDelete.get(barcode.getIndexName());
                        barcodeEntity = entity.getBarcodeId();
                    }else{
                        entity = new BarcodeKit(kitName, barcode.getIndexName());
                        barcodeEntity = new Barcode();
                        entity.setBarcodeId(barcodeEntity);
                    }
                    barcodeEntity.setBarcodeType(barcode.getIndex().getType().toString());
                    barcodeEntity.setSequence(barcode.getIndex().getIndex());
                    
                    indexDAO.saveOrUpdateKit(entity);
                    toDelete.remove(barcode.getIndexName());
                }

                for (BarcodeKit kitToDelete : toDelete.values()) {
                    indexDAO.removeKit(kitToDelete);
                }

                return null;

            }
        });
    }

    public void deleteKit(final String kitName) throws Exception {
        TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                if (!indexDAO.checkKitExistence(kitName)) {
                    throw new Exception("Kit with name " + kitName + " was not found");
                }

                for (BarcodeKit barcodeEntity : indexDAO.getKit(kitName)) {
                    indexDAO.removeKit(barcodeEntity);

                }

                return null;

            }
        });
    }

    public String getDetailedIndexInfo(final String index, final IndexType type) {
        final StringBuilder sb = new StringBuilder();
        
        try{
             TransactionManager.doInTransaction(
                new TransactionManager.TransactionCallable<Void>() {
            @Override
            public Void execute() throws Exception {
                Set<BarcodeKit> kits = indexDAO.getIdxBySequenceAndType(index,type).getBarcodeKitSet();
                for (BarcodeKit kit: kits){
                    sb.append(type).append("_");
                    sb.append(kit.getBarcodeKitPK().getKitName()).append("_");
                    sb.append(kit.getBarcodeKitPK().getSequenceName()).append(" ");
                }

                return null;

            }
        });
        }catch(Exception e){
            
        }
        
        if (sb.toString().isEmpty()){
            return "Unknown";
        }else{
            return sb.toString();
        }
    }
}
