package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.data.DataHelper;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.AlignedData;
import it.iit.genomics.cru.smith.entity.AnnotatedData;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.RawData;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.news.NewsHelper;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * @(#)UpdateManager.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Class serves to update data paths and analysis parameters after an analysis has finished.
 * 
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "updateManager")
@ApplicationScoped
public class UpdateManager {
    
    ConfigurationManager cm;
    List<DataDescriptor> descriptors;
    Preferences preferences;
    
    /**
     *
     * Class constructor
     * 
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public UpdateManager(){
        if(Preferences.getVerbose()){
            System.out.println("init UpdateManager");
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        //cm = (ConfigurationManager) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerSEMM.class);
        cm = (ConfigurationManager) context.getApplication().evaluateExpressionGet(context, "#{configurationManager}", ConfigurationManagerCEMM.class);
        //preferences = (Preferences) context.getApplication().evaluateExpressionGet(context, "#{preferences}", Preferences.class);
        preferences = cm.getPreferences();
        initDescriptors();
    }
    
    /**
     *
     * Runs updater for each fastq folder
     * 
     * @author Yuriy Vaskin
     * @since 1.0
     */
    public void runUpdater(){
        System.out.println("Updating data tables");
        //List<String> fastqFolders = cm.listFastqFolders();
        //for (String fastqFolder : fastqFolders){
        //    update(fastqFolder);
        //}
    }
    
    /**
     *
     * Updates data corresponding to a given fastq folder.
     * Publishes execution on News page when finished.
     * 
     * @author Yuriy Vaskin
     * @param folder - the fastq folder
     * @since 1.0
     */
    public void update(String folder){
        String fcid = cm.parseFCID(folder);
        List<SampleRun> sampleRuns = RunHelper.getRunsByFCIDList(fcid);
        for (SampleRun sampleRun : sampleRuns){
            RawData rawData = updateRawData(sampleRun, folder);
            if(rawData == null){
                System.err.println("Cannot update raw data for " + sampleRun.getFlowcell());
                continue;
            }
            List<AlignedData> alignedData = updateAlignedData(rawData, folder);
            if(alignedData == null || alignedData.isEmpty()){
                System.err.println("Cannot update alignedData data for " + sampleRun.getFlowcell());
                continue;
            }     
            if(hasAnnotationAnalysis(sampleRun)){
                Map<AlignedData, List<AnnotatedData> > annotatedData = updateAnnotatedData(alignedData, folder);
                if(annotatedData == null || annotatedData.isEmpty()){
                    System.err.println("Cannot update annotatedData data for " + sampleRun.getFlowcell());
                    continue;
                }     
                ImportManager.setAnalyzed(sampleRun);
                NewsHelper.publishUpstreamPerformed(folder);
            }else{
                ImportManager.setAnalyzed(sampleRun);
                NewsHelper.publishUpstreamPerformed(folder);
            }
        }
    }
    
    /**
     *
     * Updates raw data corresponding to a given fastq folder.
     * Publishes execution on News page when finished.
     * 
     * @author Yuriy Vaskin
     * @param sr - SampleRun belonging to the fastq folder
     * @param folder - the fastq folder
     * @return RawData
     * @since 1.0
     */
    private RawData updateRawData(SampleRun sr, String folder){
        RawData res = DataHelper.getRawData(sr);
        if(res != null){
            return res;
        }
        String fastqLink = getDataLink(sr, folder, "FASTQ");
        String fastqcLink1 = getFASTQCLink(sr, folder, "1");
        String fastqcLink2 = "none";
        String rm = cm.getRunReadMode(folder);
        if (!"SR".equals(rm)){
            fastqcLink2 = getFASTQCLink(sr, folder, "2");
        }
        res = new RawData(sr,fastqLink, "CASAVA --mismatches=1", fastqcLink1, fastqcLink2);
        
        ImportManager.save(res);
        NewsHelper.publishFastqGenerated(folder);
        
        return res;
    }
    
    /**
     *
     * Updates aligned data corresponding to a given fastq folder.
     * Publishes execution on News page when finished.
     * 
     * @author Yuriy Vaskin
     * @param rd - RawData belonging to the fastq folder
     * @param folder - the fastq folder
     * @return List<AlignedData>
     * @since 1.0
     */
    private List<AlignedData> updateAlignedData(RawData rd, String folder){
        List<AlignedData> res = DataHelper.getAlignedData(rd);
        if(res != null && !res.isEmpty()){
            return res;
        }
        List<DataDescriptor> alignedDescriptors = getDesriptors(rd.getsamplerun().getsample().getApplication().getApplicationname(), "aligned");
        if(!hasDerivedFolder(alignedDescriptors, folder)){
            return res;
        }
        
        for (DataDescriptor dd : alignedDescriptors){
            String dataLink = getDataLink(rd.getsamplerun(), folder, dd.getFormat());
            String genome = preferences.getDefGenome(rd.getsamplerun().getsample());
            AlignedData ad = new AlignedData(rd, dataLink, genome, dd.getFormat(), dd.getAlgorithm());
            res.add(ad);
            
        }
        ImportManager.save(res);
        
        return res;
    }
    
    /**
     *
     * Updates aligned data corresponding to a given fastq folder.
     * Publishes execution on News page when finished.
     * 
     * @author Yuriy Vaskin
     * @param ads - List of AlignedData belonging to the fastq folder
     * @param folder - the fastq folder
     * @return Map<AlignedData, List<AnnotatedData> >
     * @since 1.0
     */
    private Map<AlignedData, List<AnnotatedData>> updateAnnotatedData(List<AlignedData> ads, String folder){
        Map<AlignedData, List<AnnotatedData> > res = DataHelper.getAnnotatedData(ads);
        if(res != null && !res.isEmpty()){
            return res;
        }
        for (AlignedData ad : ads){
            List<DataDescriptor> annotatedDescriptors = getDesriptors(ad.getrawdata().getsamplerun().getsample().getApplication().getApplicationname(), "annotated");
            if(annotatedDescriptors.isEmpty()){
                continue;
            }
            if(!hasDerivedFolder(annotatedDescriptors, folder)){
                return res;
            }
            ArrayList<AnnotatedData> annotatedDatas = new ArrayList<AnnotatedData>();
            for (DataDescriptor dd : annotatedDescriptors){
                String dataLink = getDataLink(ad.getrawdata().getsamplerun(), folder, dd.getFormat());
                String genome = preferences.getDefGenome(ad.getrawdata().getsamplerun().getsample());
                AnnotatedData annData = new AnnotatedData(ad.getrawdata(), dataLink, genome, dd.getFormat(), dd.getAlgorithm());
                annotatedDatas.add(annData);
            }
            if(!annotatedDatas.isEmpty()){
                res.put(ad, annotatedDatas);
            }
        }
        ImportManager.save(res);
        
        return res;
    }

    /**
     *
     * Tests if a given folder contains specific sub directories
     * 
     * @author Yuriy Vaskin
     * @param descriptors - List of DataDescriptors
     * @param folder - the fastq folder
     * @return boolean - true if sub folders are present, false otherwise
     * @since 1.0
     */
    private boolean hasDerivedFolder(List<DataDescriptor> descriptors, String folder) {
        boolean resultReady = true;
        for (DataDescriptor dd : descriptors){
            String [] folders = cm.listFolders(dd.getFolder());
            resultReady &= cm.hasDerivedFolder(folder, folders);
        }
        return resultReady;
    }
    
    /**
     *
     * Getter for the hyperlink to data in the specified format
     * 
     * @author Yuriy Vaskin
     * @param sr - SampleRun for a specific Sample
     * @param folder - the fastq folder
     * @param format - data format (FASTQ, BAM, BIGWIG, BED)
     * @return String - the hyperlink
     * @since 1.0
     */
    public static String getDataLink(SampleRun sr, String folder, String format){
        String res = "";
        res = "http://hilt.iit.ieo.eu/data/" + format +"/" + folder + "/Project_" + sr.getsample().getUser().getLogin() + "/";
        return res;
    }
    
    /**
     *
     * Getter for the hyperlink to the FastQC report
     * 
     * @author Yuriy Vaskin
     * @param sr - SampleRun for a specific Sample
     * @param folder - the fastq folder
     * @param pair - 1 for single read, 2 for paired end
     * @return String - the hyperlink
     * @since 1.0
     */
    public static String getFASTQCLink (SampleRun sr, String folder, String pair){
        String res = "";
        String idx = sr.getsample().getSequencingIndexes().getIndex();
        String indexed = ("none".equals(idx) ? "": idx);
        String sampleId = sr.getsample().getName();
        String lane = sr.getAllLanesToString();
        res = "http://hilt.iit.ieo.eu/data/FASTQ/" + folder + "/Project_" + sr.getsample().getUser().getLogin() + "/Sample_"+sampleId+"/"+sampleId+"_"+indexed+"_L00"+lane+"_R"+pair+"_fastqc/fastqc_report.html";
        return res;
    }
    
    /**
     *
     * Init for DataDescriptors
     * 
     * @author Yuriy Vaskin
     * @since 1.0
     */
    private void initDescriptors(){
        descriptors = new ArrayList<DataDescriptor>();
        
        DataDescriptor defaultDescr = new DataDescriptor("Default", "", "aligned", "BAM", cm.getPreferences().getBamfolderroot(), "CASAVA filter,fastq_quality_trimmer,bwa-mem", "default");
        descriptors.add(defaultDescr);
        
        DataDescriptor chipSeq1 = new DataDescriptor("ChipSeq", "ChIP-Seq", "aligned", "BAM", cm.getPreferences().getBamfolderroot(), "quality_trimmer -t 20 -l 10,bwa-mem,samtools(merge,view -F 4 -q 1 chr1-chrY,rmdup,sort,index)", "default");
        DataDescriptor chipSeq2 = new DataDescriptor("ChipSeq", "ChIP-Seq", "aligned", "BIGWIG", cm.getPreferences().getBigWigfolderroot(), "bamToBed, slopBed -s -l 0 -r 160,genomecov,bedGraphToBigWig", "default");
        DataDescriptor chipSeq3 = new DataDescriptor("ChipSeq", "ChIP-Seq", "aligned", "BED", cm.getPreferences().getBedfolderroot(), "macs14", "default");
        descriptors.add(chipSeq1);
        descriptors.add(chipSeq2);
        descriptors.add(chipSeq3);
        
        DataDescriptor dnaSeq1 = new DataDescriptor("DNASeq", "DNA-Seq", "aligned", "BAM", cm.getPreferences().getBamfolderroot(), "quality_trimmer -t 20 -l 10,bwa-mem,samtools(merge,view -F 4 -q 1 chr1-chrY,rmdup,sort,index)", "default");
        DataDescriptor dnaSeq2 = new DataDescriptor("DNASeq", "DNA-Seq", "aligned", "BIGWIG", cm.getPreferences().getBigWigfolderroot(), "bamToBed,genomecov,bedGraphToBigWig", "default");
        descriptors.add(dnaSeq1);
        descriptors.add(dnaSeq2);
        
        DataDescriptor rnaSeq1 = new DataDescriptor("RNASeq", "RNA-Seq", "aligned", "BAM", cm.getPreferences().getBamfolderroot(), "quality_trimmer -t 20 -l 10,tophat,samtools(sort,index)", "default");
        DataDescriptor rnaSeq2 = new DataDescriptor("RNASeq", "RNA-Seq", "aligned", "BIGWIG", cm.getPreferences().getBigWigfolderroot(), "bamToBed,genomecov,bedGraphToBigWig", "default");
        descriptors.add(rnaSeq1);
        descriptors.add(rnaSeq2);


    }
    
    /**
     *
     * Getter for DataDescriptor for a given application
     * 
     * @author Yuriy Vaskin
     * @param applicationName 
     * @param type
     * @return List<DataDescriptor>
     * @since 1.0
     */
    private List<DataDescriptor> getDesriptors(String applicationName, String type){
        ArrayList<DataDescriptor> res = new ArrayList<DataDescriptor>();
        
        for (DataDescriptor dd : descriptors){
            if(dd.getApplicationName().equals(applicationName) && dd.getType().equals(type)){
                res.add(dd);
            }
        }
        
        if(res.isEmpty() && type.equals("aligned")){
            for (DataDescriptor dd : descriptors){
                if(dd.getDescriptorName().equals("Default")){
                    res.add(dd);
                    break;
                }
            }
        }
        
        return res;
    }
    
    /**
     *
     * Tests if Annotation analysis has been performed
     * 
     * @author Yuriy Vaskin
     * @param sr - SampleRun
     * @return boolean - true if annotation analysis has been performed, false otherwise
     * @since 1.0
     */
    private boolean hasAnnotationAnalysis(SampleRun sr){
        List<DataDescriptor> des = this.getDesriptors(sr.getsample().getApplication().getApplicationname(), "annotated");
        if(des.isEmpty()){
            return false;
        }
        return true;
    }
    
    public ConfigurationManager getConfigurationManager(){
        return cm;
    }
    
}
