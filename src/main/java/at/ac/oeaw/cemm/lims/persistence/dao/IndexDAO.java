/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.persistence.dao;

import at.ac.oeaw.cemm.lims.api.dto.lims.IndexType;
import at.ac.oeaw.cemm.lims.persistence.HibernateUtil;
import at.ac.oeaw.cemm.lims.persistence.entity.Barcode;
import at.ac.oeaw.cemm.lims.persistence.entity.BarcodeKit;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class IndexDAO {

    public IndexDAO() {
        System.out.println("Initializing IndexDAO");
    }

    public Barcode getIdxBySequenceAndType(String sequence, IndexType type) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        Criteria indexCriteria = session.createCriteria(Barcode.class)
                .add(Restrictions.eq("sequence", sequence))
                .add(Restrictions.eq("barcodeType", type.toString()))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List<Barcode> seqIndex = (List<Barcode>) indexCriteria.list();
        if (seqIndex != null && seqIndex.size() != 0) {
            return seqIndex.get(0);
        } else {
            return null;
        }

    }

    public List<String> getAllIndexes(IndexType type) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria indexCriteria = session.createCriteria(Barcode.class)
                .add(Restrictions.eq("barcodeType", type.toString()))
                .setProjection(Projections.distinct(Projections.property("sequence")));
        return indexCriteria.list();
    }
    
    public List<BarcodeKit> getAllKits() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return session.createCriteria(BarcodeKit.class).list();
    }
    
    public void saveOrUpdateKit(BarcodeKit kit) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        Barcode barcode = kit.getBarcodeId();
        Criteria barcodeCriteria = session.createCriteria(Barcode.class);
        barcodeCriteria.add(Restrictions.eq("sequence", barcode.getSequence()));
        barcodeCriteria.add(Restrictions.eq("barcodeType", barcode.getBarcodeType()));
        Barcode barcodeFound = (Barcode) barcodeCriteria.uniqueResult();
        if (barcodeFound == null) {
            session.persist(barcode);
            barcodeFound = barcode;
            session.update(barcodeFound);
        }
        kit.setBarcodeId(barcodeFound);
        session.saveOrUpdate(kit);
    }

    public List<BarcodeKit> getKit(String kitName) {
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        return session.createCriteria(BarcodeKit.class).add(Restrictions.eq("barcodeKitPK.kitName", kitName)).list();
    }

    public Boolean checkKitExistence(String kitName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Criteria query = session.createCriteria(BarcodeKit.class)
                .add(Restrictions.eq("barcodeKitPK.kitName", kitName))
                .setProjection(Projections.rowCount());
        
        long count = (Long) query.uniqueResult();
        if(count != 0){
            return true;
        } else {
            return false;
        }
    }

    public void removeKit(BarcodeKit kitToDelete) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.delete(kitToDelete);
    }
}
