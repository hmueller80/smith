package it.iit.genomics.cru.smith.sampleBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Sample;
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
public class SampleDataModel extends ListDataModel<Sample> implements SelectableDataModel<Sample>, Serializable {

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
    public SampleDataModel(List<Sample> data) {
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
    public Sample getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        //System.out.println("sample row key " + rowKey);
        List<Sample> samples = (List<Sample>) getWrappedData();

        for (Sample sample : samples) {
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
    public Object getRowKey(Sample sample) {
        return sample.getId();
    }

}
