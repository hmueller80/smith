/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import at.ac.oeaw.cemm.lims.view.NewRoleManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;

/**
 *
 * @author dbarreca
 */
public class RunLazyDataModel extends LazyDataModel<SampleRunDTO>{
    
    @Inject ServiceFactory services;
    
    private NewRoleManager roleManager;
 
    @Override
    public List<SampleRunDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        if (roleManager==null){
            System.out.println("RoleManager is null in RunDataModel");
        }else{
            System.out.println("Role is "+roleManager.getCurrentUser().getUserName());
        }

        if (roleManager!=null && !roleManager.isGuest()){
            if (roleManager.isGroupLeader() || roleManager.isUser()){ 
                filters.put("restrictToUsers",  roleManager.getSubjectsIds());
            }
        }else {
            return new ArrayList<>();
        }
        
        boolean ascending = SortOrder.ASCENDING.equals(sortOrder);
        System.out.println("firt "+first);
        System.out.println("pagesize "+pageSize);
        System.out.println("sortfield "+sortField);
        System.out.println("FILTERS");
        System.out.println("Filters are:");
        for (String filter: filters.keySet()){
            System.out.println(filter+":"+filters.get(filter).toString());
        }
        
        this.setRowCount(services.getRunService().getRunsCount(filters));
        List<SampleRunDTO> result;
        if (pageSize==this.getRowCount()){
            result =  services.getRunService().getRuns(sortField, ascending, filters);
        }else{
           result =  services.getRunService().getRuns(first, pageSize, sortField, ascending, filters);

        }
        return result;
    }

    public NewRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(NewRoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    
    
}
