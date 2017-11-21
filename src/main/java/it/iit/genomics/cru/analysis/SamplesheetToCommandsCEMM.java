/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.analysis;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author hmueller
 */
public class SamplesheetToCommandsCEMM implements SamplesheetToCommands{
    
    private String              folder;
    private AnalysisManager     am;
    private String[][]          samplesheet;

    @Override
    public String getCommands(String runfolder) {        
        return "cd /scratch/lab_bsf/projects/BSF_runs/libraries/\nbsf_submit_irf_processor.py --irf=" + runfolder + "\n";
    }  
    
    public String getCommands(String runfolder, boolean miseq) {  
        if(miseq){
            return "cd /scratch/lab_bsf/projects/BSF_runs/libraries/\nbsf_submit_irf_processor.py --irf=" + runfolder + " --mode=miseq\n";
        }else{
            return "cd /scratch/lab_bsf/projects/BSF_runs/libraries/\nbsf_submit_irf_processor.py --irf=" + runfolder + "\n";
        }
    }  
    

    
    @Override
    public String getCommandsPE(String runfolder) {
        return "";
    }

    @Override
    public String getCommandsSR(String runfolder) {
        return "";
    }

    @Override
    public String getRunDate(File f) {
        return f.getName().split("_")[0];
    }

    @Override
    public String getRunDate() {
        return folder.split("_")[0];
    }

    @Override
    public String[][] getSamplesheet() {
        return samplesheet;
    }

    @Override
    public String[][] parseSampleSheet(String sheet) {
        String[] sa0 = sheet.split("\n");
        //System.out.println("lines " + sa0.length);
        String[] sa = new String[sa0.length - 1];
        for (int i = 0; i < sa.length; i++) {
            sa[i] = sa0[i + 1].trim();
        }

        String[][] s = new String[sa.length][12];
        for (int i = 0; i < s.length; i++) {
            String[] t = sa[i].split(",");
            //System.out.println("t.length" + t.length);
            for (int j = 0; j < t.length; j++) {
                s[i][j] = t[j].trim();
                if (j == 4) {
                    if (t[j].equals("")) {
                        s[i][j] = "NoIndex";
                    }
                }
            }
        }
        return s;
    }

    @Override
    public void setAnalysisManager(AnalysisManager am) {
        this.am = am;
    }

    @Override
    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public void setSampleSheet(String samplesheet) {
        this.samplesheet = parseSampleSheet(samplesheet);
    }
    
    @Override
    public Map<String, String> getMetaNamesAndData(){
        Map<String, String> res = new LinkedHashMap<String, String>();
        for (int i = 0; i < this.samplesheet.length; i++) {
            res.put(this.samplesheet[i][2] + "_" + this.samplesheet[i][4] + "_L00" + this.samplesheet[i][1] + ".bed.meta", MetadataImporter.getMetaBySampleSheetLine(this.samplesheet[i]));
        }
        return res;
    }
    
    
}
