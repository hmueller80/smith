/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model;

import it.iit.genomics.cru.smith.entity.Sample;
import at.ac.oeaw.cemm.lims.service.LazySampleService;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author dbarreca
 */
public class SampleLazyDataModel extends LazyDataModel<Sample>{

    private LazySampleService sampleService;
    
    public SampleLazyDataModel(LazySampleService sampleService) {
        this.sampleService=sampleService;
    }
    
    @Override
    public Sample getRowData(String sampleId) {
        return sampleService.getSampleById(Integer.parseInt(sampleId));
    }
 
    @Override
    public Object getRowKey(Sample sample) {
        return sample.getId();
    }
 
    @Override
    public List<Sample> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        this.setRowCount(sampleService.getSamplesCount(first, pageSize, sortField, sortOrder, filters));
        System.out.println("Filters are:");
        for (String filter: filters.keySet()){
            System.out.println(filter+":"+filters.get(filter).toString());
        }
        List<Sample> result =  sampleService.getSamples(first, pageSize, sortField, sortOrder, filters);
        return result;
    }

}
