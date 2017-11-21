package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @(#)SamplesCreatedBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for created samples.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@ViewScoped
public class SamplesCreatedBean  implements Serializable{

    private String ids;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SamplesCreatedBean() {
        if(Preferences.getVerbose()){
            System.out.println("init SamplesCreatedBean");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ids = (String) context.getExternalContext().getRequestParameterMap().get("sids");
        if(Preferences.getVerbose()){
            System.out.println(ids);
        }
        
    }

    /**
    * Getter for ids.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getIds() {
        return ids;
    }

    /**
    * Setter for ids.
    *
    * @author Francesco Venco
    * @param ids
    * @since 1.0
    */
    public void setIds(String ids) {
        this.ids = ids;
    }
}
