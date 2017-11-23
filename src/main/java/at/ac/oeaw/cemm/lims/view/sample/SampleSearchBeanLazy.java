/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
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
    
    @ManagedProperty(value = "#{hibernateServiceFactory}")
    private ServiceFactory services;
    
    private SampleLazyDataModel lazyModel;
    
    public SampleSearchBeanLazy(){
        System.out.println("Initializing SampleSearchBeanLazy");
    }
    
    @PostConstruct
    public void init() {
         System.out.println("SampleSearchBeanLazy post construct");
        this.lazyModel = new SampleLazyDataModel(services);
    }

    public ServiceFactory getServices() {
        return services;
    }

    public void setServices(ServiceFactory services) {
        this.services = services;
    }
    
    public LazyDataModel<SampleDTO> getLazyModel() {
        return lazyModel;
    }
   
    public List<String> getAllLibraries() {
        return lazyModel.getAllLibraries();
    }
 
}
