package it.iit.genomics.cru.smith.data;

import it.iit.genomics.cru.smith.defaults.Preferences;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)RawDataSearchBean.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Backing bean for faces DataModel of RawData objects
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
public class RawDataSearchBean {

    private DataModel rawDataList;

     /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @version 1.0
     * @since 1.0
     */
    public RawDataSearchBean() {
        if(Preferences.getVerbose()){
            System.out.println("init RawDataSearchBean");
        }
        init();
    }

    /**
     * Reads in list of RawData objects from the database.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void init() {
        rawDataList = new ListDataModel(DataHelper.getRawDataList());
    }

    /**
     * Getter for the DataModel field.
     *
     * @author Francesco Venco
     * @return DataModel
     * @since 1.0
     */
    public DataModel getRawDataList() {
        return this.rawDataList;
    }

}
