/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ViewScoped
public class SampleSearchBeanLazy implements Serializable{
    
  
    @Inject private SampleLazyDataModel lazyModel;
    
    public SampleSearchBeanLazy(){
        System.out.println("Initializing SampleSearchBeanLazy");
    }
    
   
    public LazyDataModel<SampleDTO> getLazyModel() {
        return lazyModel;
    }
   
    public List<String> getAllLibraries() {
        //return lazyModel.getAllLibraries();
        return new ArrayList<String> ();
    }
 
}
