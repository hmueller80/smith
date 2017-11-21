package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.entity.AttributeValue;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.Samplesheet;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.sampleBeans.AttributevaluesHelper;
import it.iit.genomics.cru.smith.sampleBeans.SampleHelper;
import static it.iit.genomics.cru.smith.samplesSheetBeans.SampleSheetHelper.getDefGenome;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Transaction;

/*
 * @(#)DataDescriptor.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Loads metadata of samples and writes it to files class
 * 
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
public class MetadataImporter {
     /**
     *
     * Class constructor
     * 
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public MetadataImporter(){
        
    }
    
    public static String getMetaBySampleSheetLine(String[] sheet){
        //samplesheet tip //"FCID,Lane,SampleID,SampleRef,Indexed,Description,Control,Recipe,Operator,SampleProject,PI,ExpType";
        
        StringBuilder sb = new StringBuilder();
        
        try {
            Sample sample = (new SampleHelper()).getSampleByNameAndFCID(sheet[0], sheet[2]);
            if (sample == null) {
                return sb.toString();
            }
            
            LinkedHashMap<String, String> meta = getMetaMap(sample, sheet);
            
            Iterator it = meta.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                sb.append(pairs.getKey());
                sb.append("\t");
                sb.append(pairs.getValue());
                sb.append("\n");
            }
        } catch (Exception e) {
            return sb.toString();
        }
        
        return sb.toString();
    }

    public static LinkedHashMap<String, String> getMetaMap(Sample sample, String[] sheet) {
        LinkedHashMap<String, String> meta = new LinkedHashMap<String, String>();
        
        meta.put("SampleID", String.valueOf(sample.getId()));

        if(!"".equals(sample.getUser().getLogin())){
            meta.put("UserLogin", sample.getUser().getLogin());
        }

        if(!"".equals(sample.getName())){
            meta.put("SampleName", sample.getName());
        }

        if(!"".equals(sheet[4])){
            meta.put("Barcode", sheet[4]);
        }

        if(!"".equals(sheet[7])){
            meta.put("ReadMode", sheet[7]);
        }

        if(!"".equals(sample.getCostCenter())){
            meta.put("CostCenter", sample.getCostCenter());
        }

        if(!"".equals(sample.getOrganism())){
            meta.put("Organism", sample.getOrganism());
        }

        if(!"".equals(sheet[5])){
            meta.put("SampleDescription", sheet[5]);
        }

        if(!"".equals(sheet[11])){
            meta.put("Application", sheet[11]);
        }

        if(!"".equals(sample.getAntibody())){
            meta.put("Antibody", sample.getAntibody());
        }

        if(!"".equals(sheet[10])){
            meta.put("Project", sheet[10]);
        }

        User pi = UserHelper.getUserByID(sample.getUser().getPi().intValue());
        if(pi != null){
            meta.put("PILogin", pi.getLogin());
        }

        List<AttributeValue> av = AttributevaluesHelper.getAttributvalues(sample.getId());
        for (AttributeValue av1 : av) {
            if(!"".equals(av1.getValue()) && !"".equals(av1.getAttribute().getName())){
                meta.put(av1.getAttribute().getName(), av1.getValue());
            }
        }
        return meta;
    }
}
