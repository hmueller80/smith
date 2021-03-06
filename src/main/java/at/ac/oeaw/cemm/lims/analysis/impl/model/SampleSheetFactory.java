package at.ac.oeaw.cemm.lims.analysis.impl.model;

import at.ac.oeaw.cemm.lims.analysis.impl.model.SampleSheet;
import at.ac.oeaw.cemm.lims.api.dto.lims.SampleRunDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import java.io.File;
import java.util.List;

/**
 * @(#)SampleSheetHelper.java 20 JUN 2014 Copyright 2014 Computational
 * Research Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license
 * terms.
 *
 * Performs database operations regarding sample sheets.
 *
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
public class SampleSheetFactory {
    
    
    public static SampleSheet readSamplesheet(String filePath){
        File f = new File(filePath);
        if (!f.exists()){
            return null;
        } 
       
        try{
            return new SampleSheet(f);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error while parsing file "+filePath);
            return null;
        }

    }
    
    public static SampleSheet createSamplesheet(List<SampleRunDTO> runs, boolean indexReversal){            
          
        SampleSheet sampleSheet = new SampleSheet();        
        
        for (SampleRunDTO run: runs){
            if (!RequestFormDTO.NO_DEMUX_INDEX.equalsIgnoreCase(run.getSample().getCompoundIndex())){
                sampleSheet.addSampleRun(run,indexReversal);
            }
        }
        
        return sampleSheet;
    }
}
