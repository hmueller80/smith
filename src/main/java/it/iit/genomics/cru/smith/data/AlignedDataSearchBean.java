package it.iit.genomics.cru.smith.data;

import it.iit.genomics.cru.smith.defaults.Preferences;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * @(#)AlignedDataSearchBean.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Backing bean for faces DataModel of AlignedData objects
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
@ManagedBean
@RequestScoped
public class AlignedDataSearchBean {

    private DataModel dataList;

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @version 1.0
     * @since 1.0
     */
    public AlignedDataSearchBean() {
        if(Preferences.getVerbose()){
            System.out.println("init AlignedDataSearchBean");
        }
        init();
    }

    /**
     * Reads in list of AlignedData objects from the database.
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public void init() {
        dataList = new ListDataModel(DataHelper.getAlignedDataList());
    }

    /**
     * Getter for the DataModel field.
     *
     * @author Francesco Venco
     * @return DataModel
     * @since 1.0
     */
    public DataModel getDataList() {
        return this.dataList;
    }
}
