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
import it.iit.genomics.cru.smith.entity.Library;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
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

    public int getSamplesCount(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String, Object> filters) {
        Integer samples = null;

        try {
            samples = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<Integer>() {
                @Override
                public Integer execute() throws Exception {
                    return sampleDAO.getSamplesCount(first, pageSize, sortField, sortOrder, filters);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }

    public List<Sample> getSamples(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String, Object> filters) {
        List<Sample> samples = null;

        try {
            samples = TransactionManager.doInTransaction(
                    new TransactionManager.TransactionCallable<List<Sample>>() {
                @Override
                public List<Sample> execute() throws Exception {
                    return sampleDAO.getSamples(first, pageSize, sortField, sortOrder, filters);
                }
            });
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

}
