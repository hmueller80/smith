/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.sample;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import it.iit.genomics.cru.smith.userBeans.RoleManager;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author dbarreca
 */
public class SampleLazyDataModel extends LazyDataModel<SampleDTO>{

    @Inject private ServiceFactory services;
    
    private RoleManager roleManager=null;
    
    @Override
    public SampleDTO getRowData(String sampleId) {
        return services.getSampleService().getFullSampleById(Integer.parseInt(sampleId));
    }
 
    @Override
    public Object getRowKey(SampleDTO sample) {
        return sample.getId();
    }
 
    @Override
    public List<SampleDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        boolean ascending = SortOrder.ASCENDING.equals(sortOrder);
        System.out.println("firt "+first);
        System.out.println("pagesize "+pageSize);
        System.out.println("sortfield "+sortField);
        System.out.println("FILTERS");
        System.out.println("Filters are:");
        for (String filter: filters.keySet()){
            System.out.println(filter+":"+filters.get(filter).toString());
        }
        
        this.setRowCount(services.getSampleService().getSamplesCount(filters));
        List<SampleDTO> result =  services.getSampleService().getSamples(first, pageSize, sortField, ascending, filters);
        return result;
    }

    public List<String> getAllLibraries() {
        return services.getSampleService().getAllLibraries();
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }    

}
