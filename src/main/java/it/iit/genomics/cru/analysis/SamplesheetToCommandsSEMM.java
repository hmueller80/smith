package it.iit.genomics.cru.analysis;

import it.iit.genomics.cru.smith.defaults.Preferences;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Hashtable;
import it.iit.genomics.cru.mindex.*;


/**
 * @(#)SamplesheetToCommands.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Class generates bcl to fastq/fastqc commands from a samplesheet
 * 
 * @author Heiko Muller
 * @author Yuriy Vaskin
 * @version 1.0
 * @since 1.0
 */
public class SamplesheetToCommandsSEMM implements SamplesheetToCommands{

    private String              folder;
    private AnalysisManager     am;
    private String[][]          samplesheet;
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init SamplesheetToCommands");
        }
    }
   
    /**
    * 
    * generates single read bcl to fastq/fastqc commands from a saved samplesheet
    * samplesheet name is "runfoldername".csv
    * 
    * @author Heiko Muller
    * @author Yuriy Vaskin
    * @version 1.0
    * @param runfolder - runfolder name
    * @return String command list
    * @since 1.0
    */
    public String getCommandsSR(String runfolder) {
        folder = runfolder;
        StringBuilder sb = new StringBuilder();
        String[][] sheet = samplesheet;
        sb.append("umask 0027\n\n");
        //sb.append("ls > /scratch/test.txt\n\n");
        //FASTQ
        String bcl = getBclToFastQString(sheet, folder);
        sb.append("#" + bcl + "\n");
        String copy = getCopyToFastQString(sheet, folder);
        sb.append(copy + "\n");
        

        String fciddir = getMakeFlowcellFastQCDirString(sheet, folder);
        String fcidlink = getFlowcellFastQCCopyStringSR(sheet, folder);
        String fastqc = getFastQCStringSR(sheet, folder);
        String fcidfastqc = getFlowcellFastQCStringSR(sheet, folder);
        String fciddel = getFlowcellFastQCDeleteFilesStringSR(sheet, folder);
        String fcidimglnr1 = getFlowcellFastQCImageLinkStringR1(sheet, folder);
        String makeuserdir = getMakeUserDirString(sheet, folder);
        String link = getLinkString(sheet, folder, "FASTQ");
        String cphtml = getFlowcellFastQCCopyHtmlStringSR(folder);
        String ln_undet = getUndeterminedLinkString(sheet, folder);
        sb.append(fciddir + "\n");
        sb.append(fcidlink + "\n");
        sb.append(fastqc + "\n");
        sb.append(fcidfastqc + "\n");
        sb.append(fciddel + "\n");
        sb.append(fcidimglnr1 + "\n");
        sb.append(makeuserdir + "\n");
        sb.append(link + "\n");
        sb.append(ln_undet + "\n");
        sb.append(cphtml + "\n");
        
        String copyBam = getCopyToBAMString(sheet, folder);
        String copyBigWig = getCopyToBigWigString(sheet, folder);
        String copyBed = getCopyToBedString(sheet, folder);

        String galaxy = getGalaxyStringSR(sheet, folder);
        String meta = getCopyMetaString(sheet, folder);

        String linkBAM = getLinkString(sheet, folder, "BAM");
        String linkBigWig = getLinkString(sheet, folder, "BIGWIG");
        String linkBed = getLinkString(sheet, folder, "BED");

        sb.append(copyBam);
        sb.append(copyBigWig);
        sb.append(copyBed);
        sb.append(galaxy);
        sb.append(meta);
        sb.append(linkBAM);
        sb.append(linkBigWig);
        sb.append(linkBed);

        return sb.toString();
    }
    
    public String getCommands(String runfolder) {
        return "";
    }
    
    public String getCommands(String runfolder, boolean miseq) {
        return "";
    }

    /**
    * 
    * generates paired end bcl to fastq/fastqc commands from a saved samplesheet
    * samplesheet name is "runfoldername".csv
    * 
    * @author Heiko Muller
    * @author Yuriy Vaskin
    * @version 1.0
    * @param runfolder - runfolder name
    * @return String command list
    * @since 1.0
    */
    public String getCommandsPE(String runfolder) {
        folder = runfolder;
        
        StringBuilder sb = new StringBuilder();        
        String[][] sheet = samplesheet;
        sb.append("umask 0027\n\n");
        //sb.append("ls > /scratch/test.txt\n\n");
        String bcl = getBclToFastQString(sheet, folder);
        sb.append("#" + bcl + "\n");
        String copy = getCopyToFastQString(sheet, folder);
        sb.append(copy + "\n");
        String fastqc = getFastQCStringPE(sheet, folder);
        
        //FASTQ
        String fciddir = getMakeFlowcellFastQCDirString(sheet, folder);
        String fcidlink = getFlowcellFastQCCopyStringPE(sheet, folder);
        String fcidfastqc = getFlowcellFastQCStringPE(sheet, folder);
        String fciddel = getFlowcellFastQCDeleteFilesStringPE(sheet, folder);
        String fcidimglnr1 = getFlowcellFastQCImageLinkStringR1(sheet, folder);
        String fcidimglnr2 = getFlowcellFastQCImageLinkStringR2(sheet, folder);
        String makeuserdir = getMakeUserDirString(sheet, folder);
        String link = getLinkString(sheet, folder, "FASTQ");
        String cphtml = getFlowcellFastQCCopyHtmlStringPE(folder);
        String ln_undet = getUndeterminedLinkString(sheet, folder);

        sb.append(fciddir + "\n");
        sb.append(fcidlink + "\n");
        sb.append(fastqc + "\n");
        sb.append(fcidfastqc + "\n");
        sb.append(fciddel + "\n");
        sb.append(fcidimglnr1 + "\n");
        sb.append(fcidimglnr2 + "\n");
        sb.append(makeuserdir + "\n");
        sb.append(link + "\n");
        sb.append(ln_undet + "\n");
        sb.append(cphtml + "\n");

        String copyBam = getCopyToBAMString(sheet, folder);
        String copyBigWig = getCopyToBigWigString(sheet, folder);
        String copyBed = getCopyToBedString(sheet, folder);

        String galaxy = getGalaxyStringPE(sheet, folder);
        String meta = getCopyMetaString(sheet, folder);

        String linkBAM = getLinkString(sheet, folder, "BAM");
        String linkBigWig = getLinkString(sheet, folder, "BIGWIG");
        String linkBed = getLinkString(sheet, folder, "BED");

        sb.append(copyBam);
        sb.append(copyBigWig);
        sb.append(copyBed);
        sb.append(galaxy);
        sb.append(meta);
        sb.append(linkBAM);
        sb.append(linkBigWig);
        sb.append(linkBed);
        return sb.toString();
    }
    
    /**
    * Setter for samplesheet.
    * 
    * @author Heiko Muller
    * @param samplesheet - samplesheet as String
    * @since 1.0
    */
    public void setSampleSheet(String samplesheet){
        this.samplesheet = parseSampleSheet(samplesheet);
    }
    
    public void setFolder(String folder){
        this.folder = folder;
    }
 
    /**
    * 
    * Parses a samplesheet passed as a String.
    * Samplesheet headers are removed.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet as String
    * @return String[][] - the parsed samplesheet
    * @since 1.0
    */
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

    /**
    * 
    * Generates bcl to fastq command from samplesheet.
    * Samplesheet headers are removed.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param foldername - runfolder name
    * @return String - bcl to fastq commands
    * @since 1.0
    */
    public String getBclToFastQString(String[][] sheet, String foldername) {
        String s = am.getPreferences().getBcltofastqexecpath() + am.getPreferences().getRunfolderroot() + foldername + "/Data/Intensities/BaseCalls/ --output-dir=" + am.getPreferences().getScratchdir() + foldername + "/ --mismatches=" + am.getPreferences().getMismatches() + " --sample-sheet=" + am.getPreferences().getSampleSheetFolder() + foldername + ".csv\n";
        s = s + "\ncd " + am.getPreferences().getScratchdir() + foldername + "/\nmake > " + am.getPreferences().getWorkdir() + foldername + File.separator + foldername +  "_make_output.txt\n";
        return s;
    }
    
    /**
    * 
    * Generates bcl to fastq command from samplesheet.
    * Samplesheet headers are removed.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param foldername - runfolder name
    * @return String - bcl to fastq commands
    * @since 1.0
    */
    public String getBclToFastQConfigString(String[][] sheet, String foldername) {
        int mm = findPermittedMismatches(sheet);
        //System.out.println("mm " + mm);
        //String s = am.getPreferences().getBcltofastqexecpath() + am.getPreferences().getRunfolderroot() + foldername + "/Data/Intensities/BaseCalls/ --output-dir=" + am.getPreferences().getScratchdir() + foldername + "/ --mismatches=" + am.getPreferences().getMismatches() + " --sample-sheet=" + am.getPreferences().getSampleSheetFolder() + foldername + ".csv";
        String s = am.getPreferences().getBcltofastqexecpath() + am.getPreferences().getRunfolderroot() + foldername + "/Data/Intensities/BaseCalls/ --output-dir=" + am.getPreferences().getScratchdir() + foldername + "/ --mismatches=" + mm + " --sample-sheet=" + am.getPreferences().getSampleSheetFolder() + foldername + ".csv";
        return s;
    }
    
    /**
    * 
    * Finds the number of permitted mismatches for demultiplexing the flowcell given the samplesheet
    * 
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @return int - number of permitted mismatches
    * @since 1.0
    */
    public int findPermittedMismatches(String[][] sheet){
        //create a hash table for lists of indices, one list per lane
        Hashtable<Integer, ArrayList<String>> ht = new Hashtable<Integer, ArrayList<String>>();
        for(int i = 1; i <=8; i++){
            ht.put(i, new ArrayList<String>());
        }
        
        //put indices of given lane into list retrieved from the hash by the lane number
        for(int i = 0; i < sheet.length; i++){
            int lane = Integer.parseInt(sheet[i][1]);
            String index = sheet[i][4];
            if(!index.equals("NoIndex")  && !index.equals("none")){
                ht.get(lane).add(index);
            }
        }
        
        //calculate number of permitted mismatches for each lane
        int mismatches = 2;
        Mindex m = new Mindex();
        for(int i = 1; i <=8; i++){
            ArrayList<String> l = ht.get(i);
            int mm1 = 0;
            int mm2 = 0;
            int lanemismatches = 0;
            if(l.size() > 1){
                mm1 = m.calculateHashSet1FromStringList(l);
                mm2 = m.calculateHashSet2FromStringList(l);
                //System.out.println(mm1 + " " + mm2);
                if(mm1 == 1){
                    lanemismatches = 1;
                }
                if(mm2 == 2){
                    lanemismatches = 2;
                }
                mismatches = Math.min(mismatches, lanemismatches);
            }
            
        }        
        return mismatches;
    }

    /**
    * 
    * Dumps a String[][] object.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sa - String array
    * @since 1.0
    */
    private void dump(String[][] sa) {
        for (int i = 0; i < sa.length; i++) {
            for (int j = 0; j < sa[0].length; j++) {
                System.out.print(sa[i][j] + "\t");
            }
            System.out.println("");
        }
    }
    
    /**
    * 
    * Dumps samplesheet field.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @since 1.0
    */
    public void dumpSamplesheet() {
        for (int i = 0; i < samplesheet.length; i++) {
            for (int j = 0; j < samplesheet[0].length; j++) {
                System.out.print(samplesheet[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    /**
    * 
    * Parses run date from run folder name.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param f - the runfolder directory file
    * @return String - the run date
    * @since 1.0
    */
    public String getRunDate(File f) {
        return f.getName().split("_")[0];
    }

    /**
    * 
    * Parses run date from folder field.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @return String - the run date
    * @since 1.0
    */
    public String getRunDate() {
        return folder.split("_")[0];
    }

    /**
    * 
    * Generates a command that creates a sub folder in the BigWig root folder.
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sheet - samplesheet
    * @param subfoldername - sub folder name
    * @return String - the mkdir command
    * @since 1.0
    */
    private String getCopyToBigWigString(String[][] sheet, String subfoldername) {
        String s = "mkdir " + am.getPreferences().getBigWigfolderroot() + subfoldername;
        return s + "\n";
    }
    
    /**
    * 
    * Generates a command that creates a sub folder in the Bed root folder.
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sheet - samplesheet
    * @param subfoldername - sub folder name
    * @return String - the mkdir command
    * @since 1.0
    */
    private String getCopyToBedString(String[][] sheet, String subfoldername) {
        String s = "mkdir " + am.getPreferences().getBedfolderroot() + subfoldername;
        return s + "\n";
    }

    /**
    * 
    * Generates a command that creates a sub folder in the Bam root folder.
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sheet - samplesheet
    * @param subfoldername - sub folder name
    * @return String - the mkdir command
    * @since 1.0
    */
    private String getCopyToBAMString(String[][] sheet, String subfoldername) {
        String s = "mkdir " + am.getPreferences().getBamfolderroot() + subfoldername;
        return s + "\n";
    }

    /**
    * 
    * Generates a single read python command for the default workflow to be executed on Galaxy.
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the python command
    * @since 1.0
    */
    private String getGalaxyStringSR(String[][] sheet, String f) {
        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            String fastqpath = am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/"; //+ sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_0??.fastq.gz";
            
            String bambase = am.getPreferences().getBamfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1];
            String bigwigpath = am.getPreferences().getBigWigfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" +  sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + ".bigWig";
            String bedpath = am.getPreferences().getBedfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" +  sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + ".bed";
            String fastqPaths = "";
            String workflow = "default";
            if(sheet[i].length >= 12){
                workflow = sheet[i][11];
            }
            s = s + "python " + am.getPreferences().getGalaxyScriptPath() + " --folder=" + fastqpath + " --mode=SR" + " --bam=" + bambase + " --bigWig=" + bigwigpath + " --bed=" + bedpath + " --genome=" + sheet[i][3] + " --workflow="+ workflow + "\n";

        }
        s = s + "\n";
        return s;
    }
    
    /**
    * 
    * 
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String 
    * @since 1.0
    */
    private String getCopyMetaString(String[][] sheet, String f) {
        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            String bedMetaName = sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + ".bed.meta";
            String workDirPath = am.getPreferences().getWorkdir() + f + "/meta/";
            String bedPath = am.getPreferences().getBedfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/";
            s =  s + "cp " + workDirPath + bedMetaName + " " + bedPath + bedMetaName + "\n";
        }
        s = s + "\n";
        return s;
    }

    /**
    * 
    * Generates a paired end python command for the default workflow to be executed on Galaxy.
    * 
    * @author Yuriy Vaskin
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the python command
    * @since 1.0
    */
    private String getGalaxyStringPE(String[][] sheet, String f) {
        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            //python smith_run_workflow.py '/Users/pro/Documents/sandbox/data/reads/e_coli_1000.fq' '/Users/pro/Documents/sandbox/data/reads/' ecoli

            String fastqpath = am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/"; //+ sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_0??.fastq.gz";
            
            String bambase = am.getPreferences().getBamfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" +  sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1];
            String bigwigpath = am.getPreferences().getBigWigfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + ".bigWig";
            String bedpath = am.getPreferences().getBedfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + ".bed";
            
            String workflow = "default";
            if(sheet[i].length >= 12){
                workflow = sheet[i][11];
            }

            s = s + "python " + am.getPreferences().getGalaxyScriptPath() + " --folder=" + fastqpath + " --mode=PE" + " --bam=" + bambase + " --bigWig=" + bigwigpath + " --bed=" + bedpath + " --genome=" + sheet[i][3] + " --workflow=" + workflow + "\n";

        }
        s = s + "\n";
        return s;
    }

    /**
    * 
    * Generates a command that copies the Casava output from the /scratch 
    * working directory to the FASTQ root folder.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getCopyToFastQString(String[][] sheet, String f) {
        String s = "mkdir " + am.getPreferences().getFastqfolderroot() + f;
        String s2 = "cp -R " + am.getPreferences().getScratchdir() + f + "/* " + am.getPreferences().getFastqfolderroot() + f;
        return s + "\n" + s2 + "\n";
    }

    /**
    * 
    * Generates a command that starts fastqc for a single read sample. 
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFastQCStringSR(String[][] sheet, String f) {
        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            s = s + am.getPreferences().getFastqcexecpath() + " " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_0??.fastq.gz\n";
        }
        s = s + "\n";
        return s;
    }

    /**
    * 
    * Generates a command that starts fastqc for a paired end sample. 
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFastQCStringPE(String[][] sheet, String f) {
        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            s = s + am.getPreferences().getFastqcexecpath() + " " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_0??.fastq.gz\n";
        }
        s = s + "\n";
        for (int i = 0; i < sheet.length; i++) {
            s = s + am.getPreferences().getFastqcexecpath() + " " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_0??.fastq.gz\n";
        }

        return s;
    }

    /**
    * 
    * Generates commands that create sub folders with the name of the run date in the user directories . 
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getMakeUserDirString(String[][] sheet, String f) {
        String rundate = getRunDate();
        String s = "";
        String fastqparent = am.getPreferences().getFastqfolderroot();
        //System.out.println("fastqparent " + fastqparent);
        fastqparent = new File(fastqparent).getParentFile().getPath() + File.separator;
        for (int i = 0; i < sheet.length; i++) {
            //System.out.println("entry " + i + " " + sheet[i][2]);
            if (i > 0 && !sheet[i][9].equals(sheet[i - 1][9])) {
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/FASTQ/" + rundate + "\n";
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BAM/" + rundate + "\n";
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BIGWIG/" + rundate + "\n";
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BED/" + rundate + "\n";
            }
            if (i == 0) {
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/FASTQ/" + rundate + "\n";
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BAM/" + rundate + "\n";
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BIGWIG/" + rundate + "\n";
                s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BED/" + rundate + "\n";
            }
        }
        s = s + "\n";
        for (int i = 0; i < sheet.length; i++) {
            s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/FASTQ/" + rundate + "/Sample_" + sheet[i][2] + "\n";
            s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BAM/" + rundate + "/Sample_" + sheet[i][2] + "\n";
            s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BIGWIG/" + rundate + "/Sample_" + sheet[i][2] + "\n";
            s = s + "mkdir " + fastqparent + sheet[i][10] + "/" + sheet[i][9] + "/BED/" + rundate + "/Sample_" + sheet[i][2] + "\n";
        }
        return s;
    }

    /**
    * 
    * Generates a commands that creates the Flowcell_FastQC folder
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getMakeFlowcellFastQCDirString(String[][] sheet, String f) {
        return "mkdir " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC\n";
    }

    /**
    * 
    * Generates commands that copy the first fastq.gz file from each sample 
    * to the Flowcell_FastQC folder for a single read run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCCopyStringSR(String[][] sheet, String f) {

        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            ArrayList<String> lanesforsample = getLanesForSample(sheet, sheet[i][2]);
            if(lanesforsample.size() == 1){
                if (i < 9) {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_00" + (i + 1) + ".fastq.gz &" + "\n";
                } else {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_0" + (i + 1) + ".fastq.gz &" + "\n";
                }
            }else{
                if (i < 9) {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*L00" + lanesforsample.get(0) + "_R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_00" + (i + 1) + ".fastq.gz &" + "\n";
                } else {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*L00" + lanesforsample.get(0) + "_R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_0" + (i + 1) + ".fastq.gz &" + "\n";
                }
            }
        }
        s = s + "\n";
        return s;
    }

    /**
    * 
    * Generates commands that copy the first fastq.gz file from each sample 
    * to the Flowcell_FastQC folder for a paired end run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCCopyStringPE(String[][] sheet, String f) {

        String s = "";
        for (int i = 0; i < sheet.length; i++) {
            ArrayList<String> lanesforsample = getLanesForSample(sheet, sheet[i][2]);
            if(lanesforsample.size() == 1){
                if (i < 9) {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_00" + (i + 1) + ".fastq.gz &" + "\n";
                } else {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_0" + (i + 1) + ".fastq.gz &" + "\n";
                }
            }else{
                if (i < 9) {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*L00" + lanesforsample.get(0) + "_R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_00" + (i + 1) + ".fastq.gz &" + "\n";
                } else {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*L00" + lanesforsample.get(0) + "_R1_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_0" + (i + 1) + ".fastq.gz &" + "\n";
                }
            }
        }
        s = s + "\n";

        for (int i = 0; i < sheet.length; i++) {
            ArrayList<String> lanesforsample = getLanesForSample(sheet, sheet[i][2]);
            if(lanesforsample.size() == 1){
                if (i < 9) {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*R2_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R2_00" + (i + 1) + ".fastq.gz &" + "\n";
                } else {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*R2_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R2_0" + (i + 1) + ".fastq.gz &" + "\n";
                }
            }else{
                if (i < 9) {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*L00" + lanesforsample.get(0) + "_R2_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R2_00" + (i + 1) + ".fastq.gz &" + "\n";
                } else {
                    s = s + "cp " + am.getPreferences().getFastqfolderroot() + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/*L00" + lanesforsample.get(0) + "_R2_001.fastq.gz " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R2_0" + (i + 1) + ".fastq.gz &" + "\n";
                }
            }
        }
        s = s + "\n";

        return s;
    }
    
    private ArrayList<String> getLanesForSample(String[][] sheet, String s) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < sheet.length; i++) {
            if(s.equals(sheet[i][2])){
                result.add(sheet[i][1]);
            }
        }
        return result;
    
    }

    /**
    * 
    * Generates a command that starts fastqc in the Flowcell_FastQC folder for a single read run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCStringSR(String[][] sheet, String f) {
        String s = "";

        s = s + am.getPreferences().getFastqcexecpath() + " " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC" + "/*_R1_0??.fastq.gz\n";
        //}
        s = s + "\n";

        return s;
    }

    /**
    * 
    * Generates a command that starts fastqc in the Flowcell_FastQC folder for a paired end run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCStringPE(String[][] sheet, String f) {
        String s = "";

        s = s + am.getPreferences().getFastqcexecpath() + " " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC" + "/*_R1_0??.fastq.gz\n";
        //}
        s = s + "\n";
        //for(int i = 0; i < sheet.length; i++){
        s = s + am.getPreferences().getFastqcexecpath() + " " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC" + "/*_R2_0??.fastq.gz\n";
        //}

        return s;
    }

    /**
    * 
    * Generates a command that removes the samples from the Flowcell_FastQC folder
    * for a single read run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCDeleteFilesStringSR(String[][] sheet, String f) {

        String s = "";
        //for(int i = 0; i < 1; i++){
        //if(i < 9){
        s = s + "rm " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_0*.fastq.gz" + "\n";
        //}
        //}
        s = s + "\n";

        return s;
    }

    /**
    * 
    * Generates a command that removes the samples from the Flowcell_FastQC folder
    * for a paired end run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param sheet - samplesheet
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCDeleteFilesStringPE(String[][] sheet, String f) {

        String s = "";
        //for(int i = 0; i < 1; i++){
        //if(i < 9){
        s = s + "rm " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R1_0*.fastq.gz" + "\n";
        //}
        //}
        s = s + "\n";

        //for(int i = 0; i < 1; i++){
        //if(i < 9){
        s = s + "rm " + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC/all_samples_R2_0*.fastq.gz" + "\n";
        //}
        //}
        s = s + "\n";

        return s;
    }

    /**
    * 
    * Generates a command that copies the R1-side-by-side.html file 
    * from the work directory to the Flowcell_FastQC directory
    * for a single read run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCCopyHtmlStringSR(String f) {
        String s = "";
        s = "cp " + am.getPreferences().getWorkdir() + f + "/R1-side-by-side.html ";
        s = s + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC" + "/all_samples_R1_fastqc/ \n";
        //}
        s = s + "\n";

        return s;
    }

    /**
    * 
    * Generates commands that copy the R1-side-by-side.html file 
    * and the /R2-side-by-side.html file
    * from the work directory to the Flowcell_FastQC directory
    * for a paired end run.
    * 
    * @author Heiko Muller
    * @version 1.0
    * @param f - run folder name
    * @return String - the command
    * @since 1.0
    */
    private String getFlowcellFastQCCopyHtmlStringPE(String f) {
        String s = "";
        s = "cp " + am.getPreferences().getWorkdir() + f + "/R1-side-by-side.html ";
        s = s + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC" + "/all_samples_R1_fastqc/ \n";
        //}
        s = s + "\n";

        s = s + "cp " + am.getPreferences().getWorkdir() + f + "/R2-side-by-side.html ";
        s = s + am.getPreferences().getFastqfolderroot() + f + "/Flowcell_FastQC" + "/all_samples_R2_fastqc/ \n";
        //}
        s = s + "\n";

        return s;
    }
       
    /**
    * 
    * Generates R1(2)-side-by-side.html
    * 
    * 
    * @author Heiko Muller
    * @version 1.0
    * @return String - the html
    * @since 1.0
    */
    public String getFlowcellFastQCHtmlString() {
        String[][] sheet = samplesheet;
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\r\n<head><title>");
        sb.append(folder + "</title>");
        sb.append(""
                + "<style type=\"text/css\">"
                + "@media screen {"
                + "div.summary {"
                + "width: 18em;"
                + "position:fixed;"
                + "top: 3em;"
                + "margin:1em 0 0 1em;"
                + "}"
                + "div.main {"
                + "display:block;"
                + "position:absolute;"
                + "overflow:auto;"
                + "height:auto;"
                + "width:auto;"
                + "top:4.5em;"
                + "bottom:2.3em;"
                + "left:18em;"
                + "right:0;"
                + "border-left: 1px solid #CCC;"
                + "padding:0 0 0 1em;"
                + "background-color: white;"
                + "z-index:1;"
                + "}"
                + "div.header {"
                + "background-color: #EEE;"
                + "border:0;"
                + "margin:0;"
                + "padding: 0.5em;"
                + "font-size: 200%;"
                + "font-weight: bold;"
                + "position:fixed;"
                + "width:100%;"
                + "top:0;"
                + "left:0;"
                + "z-index:2;"
                + "}"
                + "div.footer {"
                + "background-color: #EEE;"
                + "border:0;"
                + "margin:0;"
                + "padding:0.5em;"
                + "height: 1.3em;"
                + "overflow:hidden;"
                + "font-size: 100%;"
                + "font-weight: bold;"
                + "position:fixed;"
                + "bottom:0;"
                + "width:100%;"
                + "z-index:2;"
                + "}"
                + "img.indented {"
                + " margin-left: 3em;"
                + "}"
                + "}"
                + "@media print {"
                + "img {"
                + "	max-width:100% !important;"
                + "	page-break-inside: avoid;"
                + "}"
                + "h2, h3 {"
                + "	page-break-after: avoid;"
                + "}"
                + "div.header {"
                + "background-color: #FFF;"
                + "}"
                + "}"
                + "body {"
                + "font-family: sans-serif;"
                + "color: #000;"
                + "background-color: #FFF;"
                + "border: 0;"
                + "margin: 0;"
                + "padding: 0;"
                + "}"
                + "div.header {"
                + "border:0;"
                + "margin:0;"
                + "padding: 0.5em;"
                + "font-size: 120%;"
                + "font-weight: bold;"
                + "width:100%;"
                + "}"
                + "#header_title {"
                + "display:inline-block;"
                + "float:left;"
                + "clear:left;"
                + "}"
                + "#header_filename {"
                + "display:inline-block;"
                + "float:right;"
                + "clear:right;"
                + "font-size: 100%;"
                + "margin-right:2em;"
                + "text-align: right;"
                + "}"
                + "div.header h3 {"
                + "font-size: 100%;"
                + "margin-bottom: 0;"
                + "}"
                + "div.summary ul {"
                + "padding-left:0;"
                + "list-style-type:none;"
                + "}"
                + "div.summary ul li img {"
                + "margin-bottom:-0.5em;"
                + "margin-top:0.5em;"
                + "}"
                + "div.main {"
                + "background-color: white;"
                + "}"
                + "div.module {"
                + "padding-bottom:1.5em;"
                + "padding-top:1.5em;"
                + "}"
                + "div.footer {"
                + "background-color: #EEE;"
                + "border:0;"
                + "margin:0;"
                + "padding: 0.5em;"
                + "font-size: 100%;"
                + "font-weight: bold;"
                + "width:100%;"
                + "}"
                + "a {"
                + "color: #000080;"
                + "}"
                + "a:hover {"
                + "color: #800000;"
                + "}"
                + "h2 {"
                + "color: #800000;"
                + "padding-bottom: 0;"
                + "margin-bottom: 0;"
                + "clear:left;"
                + "}"
                + "table { "
                + "margin-left: 3em;"
                + "text-align: center;"
                + "}"
                + "th { "
                + "text-align: center;"
                + "background-color: #000080;"
                + "color: #FFF;"
                + "padding: 0.4em;"
                + "}"
                + "td { "
                + "font-family: monospace; "
                + "text-align: left;"
                + "background-color: #EEE;"
                + "color: #000;"
                + "padding: 0.4em;"
                + "}"
                + "img {"
                + "padding-top: 0;"
                + "margin-top: 0;"
                + "border-top: 0;"
                + "}"
                + "p {"
                + "padding-top: 0;"
                + "margin-top: 0;"
                + "}"
                + "</style>\r\n");

        sb.append("</head><body>\n");

        sb.append("<div class=\"header\">\n");
        sb.append("<div id=\"header_title\"><img src=\"Icons/IITLogo.png\" alt=\"IIT@SEMM\"> FastQC Report</div>\r\n");
        sb.append("<div id=\"header_filename\">\n");
        sb.append(folder + "\r\n");
        sb.append("</div>\r\n");
        sb.append("</div>\r\n");
        sb.append("<div class=\"summary\">\r\n");
        sb.append("<h2>Summary</h2>\r\n"
                + "<ul>\r\n"
                + "<li><img src=\"Icons/tick.png\" alt=\"[PASS]\"> <a href=\"#M0\">Basic Statistics</a></li>\r\n"
                + "<li><img src=\"Icons/tick.png\" alt=\"[PASS]\"> <a href=\"#M1\">Per base sequence quality</a></li>\r\n"
                + "<li><img src=\"Icons/tick.png\" alt=\"[PASS]\"> <a href=\"#M2\">Per sequence quality scores</a></li>\r\n"
                + "<li><img src=\"Icons/warning.png\" alt=\"[WARNING]\"> <a href=\"#M3\">Per base sequence content</a></li>\r\n"
                + "<li><img src=\"Icons/tick.png\" alt=\"[PASS]\"> <a href=\"#M4\">Per base GC content</a></li>\r\n"
                + "<li><img src=\"Icons/error.png\" alt=\"[FAIL]\"> <a href=\"#M5\">Per sequence GC content</a></li>\r\n"
                + "<li><img src=\"Icons/warning.png\" alt=\"[WARNING]\"> <a href=\"#M6\">Per base N content</a></li>\r\n"
                + "<li><img src=\"Icons/tick.png\" alt=\"[PASS]\"> <a href=\"#M7\">Sequence Length Distribution</a></li>\r\n"
                + "<li><img src=\"Icons/error.png\" alt=\"[FAIL]\"> <a href=\"#M8\">Sequence Duplication Levels</a></li>\r\n"
                //+ "<li><img src=\"Icons/error.png\" alt=\"[FAIL]\"> <a href=\"#M9\">Overrepresented sequences</a></li>\n"
                //+ "<li><img src=\"Icons/warning.png\" alt=\"[WARNING]\"> <a href=\"#M10\">Kmer Content</a></li>\n"
                + "</ul>\r\n"
                + "</div>\r\n");
        sb.append("<div class=\"main\">\r\n");
        sb.append("<div class=\"module\"><h2 id=\"M0\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Basic Statistics</h2>\r\n");
        sb.append("<p>To be implemented</p>\r\n");
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M1\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Per base sequence quality</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/per_base_quality/Sample_" + sheet[i][2] + "/per_base_quality.png\" alt=\"Per base quality graph\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M2\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Per sequence quality scores</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/per_sequence_quality/Sample_" + sheet[i][2] + "/per_sequence_quality.png\" alt=\"Per Sequence quality graph\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M3\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Per base sequence content</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/per_base_sequence_content/Sample_" + sheet[i][2] + "/per_base_sequence_content.png\" alt=\"Per base sequence content\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M4\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Per base GC content</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/per_base_gc_content/Sample_" + sheet[i][2] + "/per_base_gc_content.png\" alt=\"Per base GC content graph\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M5\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Per sequence GC content</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/per_sequence_gc_content/Sample_" + sheet[i][2] + "/per_sequence_gc_content.png\" alt=\"Per sequence GC content graph\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M6\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Per base N content</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/per_base_n_content/Sample_" + sheet[i][2] + "/per_base_n_content.png\" alt=\"N content graph\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M7\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Sequence Length Distribution</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/sequence_length_distribution/Sample_" + sheet[i][2] + "/sequence_length_distribution.png\" alt=\"Sequence length distribution\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("<div class=\"module\"><h2 id=\"M8\"><img src=\"Icons/tick.png\" alt=\"[OK]\"> Sequence Duplication Levels</h2>\r\n");
        for (int i = 0; i < sheet.length; i++) {
            sb.append("<p>Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "</p>\r\n");
            sb.append("<p><img class=\"indented\" src=\"Images/duplication_levels/Sample_" + sheet[i][2] + "/duplication_levels.png\" alt=\"Duplication level graph\"></p>\r\n");
        }
        sb.append("</div>\r\n");

        sb.append("</body></html>\r\n\r\n");
        return sb.toString();

    }

    /**
    * 
    * Generates a command that links the image files of individual fastqc folders to the Flowcell_FastQC folder
    * to be displayed by R1-side-by-side.html.
    *  
    * @author Heiko Muller
    * @version 1.0
    * @param ssheet - samplesheet
    * @param f - run folder name
    * @return String - the html
    * @since 1.0
    */
    private String getFlowcellFastQCImageLinkStringR1(String[][] sheet, String f) {
        StringBuilder sb = new StringBuilder();
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/duplication_levels/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/kmer_profiles/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_gc_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_n_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_quality/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_sequence_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_gc_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_quality/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/sequence_length_distribution/\n");
        sb.append("\n");

        for (int i = 0; i < sheet.length; i++) {
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/duplication_levels/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/kmer_profiles/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_gc_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_n_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_quality/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_sequence_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_gc_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_quality/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/sequence_length_distribution/" + "Sample_" + sheet[i][2] + "\n");
        }

        sb.append("\n");

        for (int i = 0; i < sheet.length; i++) {

            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/duplication_levels.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/duplication_levels/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/kmer_profiles.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/kmer_profiles/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/per_base_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/per_base_n_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_n_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/per_base_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/per_base_sequence_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_sequence_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/per_sequence_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/per_sequence_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_fastqc/Images/sequence_length_distribution.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/sequence_length_distribution/Sample_" + sheet[i][2] + "/\n");

            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/duplication_levels.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/duplication_levels/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/kmer_profiles.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/kmer_profiles/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/per_base_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/per_base_n_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_n_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/per_base_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/per_base_sequence_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_base_sequence_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/per_sequence_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/per_sequence_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/per_sequence_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R1_001_fastqc/Images/sequence_length_distribution.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R1_fastqc/Images/sequence_length_distribution/Sample_" + sheet[i][2] + "/\n");

        }

        sb.append("\n");
        return sb.toString();
    }

    /**
    * 
    * Generates a command that links the image files of individual fastqc folders to the Flowcell_FastQC folder
    * to be displayed by R2-side-by-side.html.
    *  
    * @author Heiko Muller
    * @version 1.0
    * @param ssheet - samplesheet
    * @param f - run folder name
    * @return String - the html
    * @since 1.0
    */
    private String getFlowcellFastQCImageLinkStringR2(String[][] sheet, String f) {
        StringBuilder sb = new StringBuilder();
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/duplication_levels/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/kmer_profiles/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_gc_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_n_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_quality/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_sequence_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_gc_content/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_quality/\n");
        sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/sequence_length_distribution/\n");
        sb.append("\n");

        for (int i = 0; i < sheet.length; i++) {
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/duplication_levels/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/kmer_profiles/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_gc_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_n_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_quality/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_sequence_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_gc_content/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_quality/" + "Sample_" + sheet[i][2] + "\n");
            sb.append("mkdir " + am.getPreferences().getFastqfolderroot() + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/sequence_length_distribution/" + "Sample_" + sheet[i][2] + "\n");
        }

        sb.append("\n");

        for (int i = 0; i < sheet.length; i++) {

            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/duplication_levels.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/duplication_levels/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/kmer_profiles.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/kmer_profiles/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/per_base_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/per_base_n_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_n_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/per_base_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/per_base_sequence_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_sequence_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/per_sequence_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/per_sequence_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_fastqc/Images/sequence_length_distribution.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/sequence_length_distribution/Sample_" + sheet[i][2] + "/\n");

            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/duplication_levels.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/duplication_levels/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/kmer_profiles.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/kmer_profiles/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/per_base_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/per_base_n_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_n_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/per_base_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/per_base_sequence_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_base_sequence_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/per_sequence_gc_content.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_gc_content/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/per_sequence_quality.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/per_sequence_quality/Sample_" + sheet[i][2] + "/\n");
            sb.append("ln " + am.getPreferences().getFastqfolderroot() + folder + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/" + sheet[i][2] + "_" + sheet[i][4] + "_L00" + sheet[i][1] + "_R2_001_fastqc/Images/sequence_length_distribution.png /data/Illumina/PublicData/FASTQ/" + folder + "/Flowcell_FastQC/all_samples_R2_fastqc/Images/sequence_length_distribution/Sample_" + sheet[i][2] + "/\n");

        }

        sb.append("\n");
        return sb.toString();
    }

    /**
    * 
    * Generates a command that links the data files to user directories using hard links (ln command).
    *  
    * @author Heiko Muller
    * @version 1.0
    * @param ssheet - samplesheet
    * @param f - run folder name
    * @param format - the file format of the data to be linked
    * @return String - the html
    * @since 1.0
    */
    private String getLinkString(String[][] sheet, String f, String format) {
        String rundate = getRunDate();
        String s = "";
        String rootFolder = "";
        if (format == "FASTQ") {
            rootFolder = am.getPreferences().getFastqfolderroot();
        } else if (format == "BIGWIG") {
            rootFolder = am.getPreferences().getBigWigfolderroot();
        } else if (format == "BED") {
            rootFolder = am.getPreferences().getBedfolderroot();
        } else if (format == "BAM") {
            rootFolder = am.getPreferences().getBamfolderroot();
        } else {
            return "";
        }
        String parent = new File(rootFolder).getParentFile().getPath() + File.separator;
        for (int i = 0; i < sheet.length; i++) {
            s = s + "ln " + rootFolder + f + "/Project_" + sheet[i][9] + "/Sample_" + sheet[i][2] + "/* " + parent + sheet[i][10] + "/" + sheet[i][9] + "/" + format + "/" + rundate + "/Sample_" + sheet[i][2] + "\n";
        }
        s = s + "\n";

        return s;
    }
    
    /**
    * 
    * Generates a command that links the data files to user directories using hard links (ln command).
    *  
    * @author Heiko Muller
    * @version 1.0
    * @param ssheet - samplesheet
    * @param f - run folder name
    * @param format - the file format of the data to be linked
    * @return String - the html
    * @since 1.0
    */
    private String getUndeterminedLinkString(String[][] sheet, String f) {        
        String rundate = getRunDate();
        String s = "";
        String rootFolder = "";        
        rootFolder = am.getPreferences().getFastqfolderroot();
        
        String parent = new File(rootFolder).getParentFile().getPath() + File.separator;
        for (int i = 0; i < sheet.length; i++) {
            if(sheet[i][4].startsWith("AAAAA") || sheet[i][4].startsWith("GGGGG") || sheet[i][4].startsWith("CCCCC") || sheet[i][4].startsWith("TTTTT")){
                s = s + "ln " + rootFolder + f + "/Undetermined_indices/Sample_lane" + sheet[i][1] + "/* " + parent + sheet[i][10] + "/" + sheet[i][9] + "/FASTQ/" + rundate + "/Sample_" + sheet[i][2] + "\n";
            }
        }
        s = s + "\n";

        return s;
    }
    
    /**
    * 
    * Generates a command that links the data files to user directories using hard links (ln command).
    *  
    * @author Heiko Muller
    * @version 1.0
    * @param ssheet - samplesheet
    * @param f - run folder name
    * @return String - the html
    * @since 1.0
    */
    public ArrayList<String> getFastqUserDirectories(String f) {
        String[][] sheet = samplesheet;
        String rundate = getRunDate();        
        String rootFolder = "";
        ArrayList<String> result = new ArrayList<String>();
        String format = "FASTQ";
        rootFolder = am.getPreferences().getFastqfolderroot();        
        String parent = new File(rootFolder).getParentFile().getPath() + File.separator;
        for (int i = 0; i < sheet.length; i++) {
            result.add(parent + sheet[i][10] + "/" + sheet[i][9] + "/" + format + "/" + rundate + "/Sample_" + sheet[i][2]);
        }
        return result;
    }

    /**
    * 
    * Getter for samplesheet field.
    *  
    * @author Heiko Muller
    * @version 1.0
    * @return String[][] - the samplesheet
    * @since 1.0
    */
    public String[][] getSamplesheet() {
        return samplesheet;
    }
    
    public Map<String, String> getMetaNamesAndData(){
        Map<String, String> res = new LinkedHashMap<String, String>();
        for (int i = 0; i < this.samplesheet.length; i++) {
            res.put(this.samplesheet[i][2] + "_" + this.samplesheet[i][4] + "_L00" + this.samplesheet[i][1] + ".bed.meta", MetadataImporter.getMetaBySampleSheetLine(this.samplesheet[i]));
        }
        return res;
    }
    

    /**
    * 
    * Setter for AnalysisManager field.
    *  
    * @author Heiko Muller
    * @version 1.0
    * @param am - the AnalysisManager
    * @since 1.0
    */
    public void setAnalysisManager(AnalysisManager am) {
        this.am = am;
    }
}


    
    