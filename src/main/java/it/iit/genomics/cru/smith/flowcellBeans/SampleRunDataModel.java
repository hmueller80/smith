package it.iit.genomics.cru.smith.flowcellBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.SampleRun;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 * @(#)SampleRunDataModel.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * DataModel for SampleRun data sets.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class SampleRunDataModel extends ListDataModel<SampleRun> implements SelectableDataModel<SampleRun> {

    /**
     * Bean constructor
     *
     * @author Francesco Venco
     * @param sampleRuns
     * @since 1.0
     */
    public SampleRunDataModel(List<SampleRun> sampleRuns) {
        super(sampleRuns);
        if(Preferences.getVerbose()){
            System.out.println("init SampleRunDataModel");
        }
    }

    /**
     * Getter for RowKey.
     *
     * @author Francesco Venco
     * @param t - a SampleRun
     * @return Object
     * @since 1.0
     */
    @Override
    public Object getRowKey(SampleRun t) {
        return "" + t.getId().getRunId() + t.getId().getSamId();
    }

    /**
     * Getter for RowData.
     *
     * @author Francesco Venco
     * @param rowKey
     * @return SampleRun
     * @since 1.0
     */
    @Override
    public SampleRun getRowData(String rowKey) {
        List<SampleRun> sampleRuns = (List<SampleRun>) getWrappedData();
        for (SampleRun sr : sampleRuns) {
            String candidateRowKey = "" + sr.getId().getRunId() + sr.getId().getSamId();
            if (candidateRowKey.equals(rowKey)) {
                return sr;
            }
        }
        return null;
    }

}
