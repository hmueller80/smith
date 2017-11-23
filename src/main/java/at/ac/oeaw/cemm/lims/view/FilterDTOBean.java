/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view;

import at.ac.oeaw.cemm.lims.api.dto.SampleDTO;
import at.ac.oeaw.cemm.lims.api.persistence.ServiceFactory;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @(#)FilterBean.java 20 JUN 2014 Copyright 2014 Computational Research Unit of
 * IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Class serves as backing bean for filtering in dataTables. <p:dataTable
 * filteredValue="#{filterBean.sampleRun}"
 *
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "filterDTOBean")
@SessionScoped
public class FilterDTOBean implements Serializable {

    private List<SampleDTO> sample;

    public FilterDTOBean() {
        System.out.println("Initializing FilterDTOBean");
    }

    public List<SampleDTO> getSample() {
        return sample;
    }

    public void setSample(List<SampleDTO> sample) {
        this.sample = sample;
    }

}
