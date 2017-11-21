/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author hmueller
 */
public class SampleRunDataModel extends ListDataModel<SampleRun> implements SelectableDataModel<SampleRun>, Serializable {
    
    public SampleRunDataModel() {
        if(Preferences.getVerbose()){
            System.out.println("init SampleRunDataModel");
        }
    }
    
    public SampleRunDataModel(List<SampleRun> data) {
        super(data);
        if(Preferences.getVerbose()){
            System.out.println("init SampleRunDataModel");
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
    public SampleRun getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        //System.out.println("SampleRun row key " + rowKey);
        List<SampleRun> runs = (List<SampleRun>) getWrappedData();

        for (SampleRun sr : runs) {
            if (sr.getId().toString().equals(rowKey)) {
                return sr;
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
    public Object getRowKey(SampleRun run) {
        return run.getId().toString();
    }
}
