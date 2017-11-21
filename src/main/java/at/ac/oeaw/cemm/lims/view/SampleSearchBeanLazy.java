/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import at.ac.oeaw.cemm.lims.model.SampleLazyDataModel;
import it.iit.genomics.cru.smith.entity.Sample;
import at.ac.oeaw.cemm.lims.service.LazySampleService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class SampleSearchBeanLazy implements Serializable{
    private LazyDataModel<Sample> lazyModel;
    
    @ManagedProperty("#{lazySampleService}")
    private LazySampleService sampleService;
    
    @PostConstruct
    public void init() {
        lazyModel = new SampleLazyDataModel(sampleService);
    }
 
    public LazyDataModel<Sample> getLazyModel() {
        return lazyModel;
    }

    public LazySampleService getSampleService() {
        return sampleService;
    }

    public void setSampleService(LazySampleService sampleService) {
        this.sampleService = sampleService;
    }
    
    public List<String> getAllLibraries() {
        return sampleService.getAllLibraries();
    }
 
}
