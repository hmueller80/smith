package it.iit.genomics.cru.smith.projectsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Collaboration;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 * @(#)CollaborationDataModel.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for CollaborationDataModel.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class CollaborationDataModel extends ListDataModel<Collaboration> implements SelectableDataModel<Collaboration>, Serializable {

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @since 1.0
     */
    public CollaborationDataModel() {
        if(Preferences.getVerbose()){
            System.out.println("init CollaborationDataModel");
        }
    }

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @param data - list of Collaborations
     * @since 1.0
     */
    public CollaborationDataModel(List<Collaboration> data) {
        super(data);
        if(Preferences.getVerbose()){
            System.out.println("init CollaborationDataModel");
        }
    }

     /**
     * Getter for row data
     *
     * @author Francesco Venco
     * @param rowKey
     * @return Collaboration
     * @since 1.0
     */
    @Override
    public Collaboration getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data

        List<Collaboration> cols = (List<Collaboration>) getWrappedData();

        for (Collaboration c : cols) {
            if (c.getUser().getId().toString().equals(rowKey)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Getter for row key
     *
     * @author Francesco Venco
     * @param c
     * @return Object
     * @since 1.0
     */
    @Override
    public Object getRowKey(Collaboration c) {
        return c.getUser().getId();
    }

}
