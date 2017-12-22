/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.user;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;

/**
 *
 * @author dbarreca
 */
@ManagedBean
@ApplicationScoped
public class UserConverter implements Converter{
    @Inject ServiceFactory services;
    

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value!=null && !value.trim().isEmpty()){
            return services.getUserService().getUserByLogin(value);
        }else{
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value!=null){
            return ((UserDTO) value).getLogin();
        }else{
            return null;
        }
    }
    
}
