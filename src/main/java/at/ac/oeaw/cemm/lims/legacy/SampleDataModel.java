package at.ac.oeaw.cemm.lims.legacy;

import at.ac.oeaw.cemm.lims.api.dto.lims.SampleDTO;
import at.ac.oeaw.cemm.lims.util.Preferences;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 * @(#)SampleDataModel.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * DataModel for Sample objects.
 *
 * @author Francesco Venco 
 * @version 1.0
 * @since 1.0
 */
public class SampleDataModel extends ListDataModel<SampleDTO> implements SelectableDataModel<SampleDTO>, Serializable {

    /**
    * Constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SampleDataModel() {
        if(Preferences.getVerbose()){
            System.out.println("init SampleDataModel");
        }
    }

    /**
    * Constructor.
    *
    * @author Francesco Venco
    * @param data
    * @since 1.0
    */
    public SampleDataModel(List<SampleDTO> data) {
        super(data);
        if(Preferences.getVerbose()){
            System.out.println("init SampleDataModel");
        }
    }

    /**
    * Getter for row data.
    *
    * @author Francesco Venco
    * @param rowKey
    * @return Sample
    * @since 1.0
    */
    @Override
    public SampleDTO getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        //System.out.println("sample row key " + rowKey);
        List<SampleDTO> samples = (List<SampleDTO>) getWrappedData();

        for (SampleDTO sample : samples) {
            if (sample.getId().toString().equals(rowKey)) {
                return sample;
            }
        }

        return null;
    }

    /**
    * Getter for row key.
    *
    * @author Francesco Venco
    * @param sample
    * @return Object
    * @since 1.0
    */
    @Override
    public Object getRowKey(SampleDTO sample) {
        return sample.getId();
    }

}
