package it.iit.genomics.cru.smith.defaults;

import it.iit.genomics.cru.smith.entity.Sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 * @(#)Preferences.java
 * 20 JUN 2014
 * Copyright 2014 Computational Research Unit of IIT@SEMM. All rights reserved.
 * Use is subject to MIT license terms.
 * 
 * Class stores customizable parameters of the application.
 * 
 * @author Heiko Muller
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "preferences")
@ApplicationScoped
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    //private static final String[] statusList = {"requested", "queued", "confirmed", "analyzed"};

    private final String[] roleList = {"user", "groupleader", "technician", "admin", "guest"};
    public static final String ROLE_USER = "user";
    public static final String ROLE_GROUPLEADER = "groupleader";
    public static final String ROLE_TECHNICIAN = "technician";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_GUEST = "guest";
    public static List<String> ROLES = new ArrayList<String>();
    static{
        ROLES.add(ROLE_USER);
        ROLES.add(ROLE_GROUPLEADER);
        ROLES.add(ROLE_TECHNICIAN);
        ROLES.add(ROLE_ADMIN);
        ROLES.add(ROLE_GUEST);
    }

    //private static final String[] targetSynthesisList = {"undefined",
    //    "standard", "small sample", "no"};



    public final String[] bools = {"false", "true"};

    private static final String[] institute = {"undefined", "IEO",
        "IIT", "external"};

    private final String[] organisms = {"undefined", "MOUSE", "HUMAN",
        "RAT", "BOVINE", "PORCINE", "CHICKEN", "ZEBRAFISH", "DROSOPHILA",
        "CELEGANS", "CEREVISIAE", "ARABIDOPSIS", "RICE", "TOMATO",
        "ECOLI 2.0", "PSEUDOMONAS", "OTHER"};

    private static final String[] defGenomes = {"undefined", "mm9", "hg19",
        "_RAT", "_BOVINE", "_PORCINE", "_CHICKEN", "_ZEBRAFISH",
        "_DROSOPHILA", "_CELEGANS", "_CEREVISIAE", "_ARABIDOPSIS"
        + "", "_RICE",
        "_TOMATO", "_ECOLI 2.0", "_PSEUDOMONAS", "_OTHER"};

    private static final String[] genomes = {"", "mm9", "hg19", "rn4",
        "bosTau5", "susScr2", "galGal4", "danRer7", "dm3", "ce6",
        "sacCer2", "ARABIDOPSIS", "RICE", "TOMATO", "ECOLI 2.0",
        "PSEUDOMONAS", "OTHER"};

    private final String[] sampletype = {"undefined", "IP", "INPUT"};
    private final String[] readlength = {"50", "75", "100"};
    private static final String[] readlengthrnaseq = {"36", "50", "75", "100"};
    private static final String[] readlengthdnaseq = {"36", "50", "75", "100"};
    private final String[] depth = {"30", "60", "90", "180"};
    private static final String[] depth_SR = {"30", "60", "90", "120", "150"};
    private static final String[] depth_PE = {"35", "70", "105", "140"};
    private final String[] readmode = {"SR", "PE"};
    private final String[] instrument = {"HiSeq2000"};
    private final String[] application = {"undefined",
        "Sample preparation", "Cluster generation", "Sequencing"};
    
    private static final String default_application = "Single read : 30 mio reads : one fiths of a lane : 50 bases";

    public final String[] ngsapplication = {"undefined", "ChIP-Seq",
        "DNA-Seq", "ExomeSeq", "Methylation", "mRNA-Seq", "RRBS",
        "SmallRNA", "RNA-Seq", "CLIP-Seq", "Other"};


    private final Hashtable<String, String> genomeHash;



    private static String      runfolderroot = "/data/Illumina/Runs/"; //"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/Runs/";
    private static String      fastqfolderroot = "/data/Illumina/PublicData/FASTQ/"; //"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/FASTQ/"
    private static String      bamfolderroot = "/data/Illumina/PublicData/BAM/"; //"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/BAM/";
    private static String      bigwigfolderroot = "/data/Illumina/PublicData/BIGWIG/"; //"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/BigWig/";
    private static String      bedfolderroot = "/data/Illumina/PublicData/BED/";
    private static String      samplesheetfolder = "/data/Illumina/PublicData/SampleSheets/";//"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/SampleSheets/";
    private static String      workdir; // = "/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/Temp/"; //TODO
    private static String      galaxyScriptPath = "/home/yvaskin/smith/smith_run_workflow.py";//TODO
    private static int         cutoff = 140716;
    private static String      scratchdir;
    private static String      fastqcexecpath;
    private static String      bcltofastqexecpath;
    private static int         mismatches;
    private static String      fastqhyperlink;
    private static String      executerpathflowcellA;
    private static String      executerpathflowcellB;
    
    private static String subjectRequestReceived = "Your NGS request -> requestIDs";
    private static String subjectFastQReadyAlert = "Your new NGS data are now available";
    private static String sentByMailAddress = "service-ings@ieo.eu"; 
    private static String sentByUnitName = "IIT Genomic Unit"; 
    private static String smtpServer = "smtp.ieo.eu";
    
    private static String PROJECT_STAGE;
    private static String installation;
    private static boolean verbose;
    private static String runfolderscaninterval;
    
    /**
     * Class constructor
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    public Preferences(){
        System.out.println("init Preferences");
        FacesContext context = FacesContext.getCurrentInstance(); 
        
        if(context == null){
            System.out.println("FacesContext null");
        }
        
        installation = context.getExternalContext().getInitParameter("installation");
        PROJECT_STAGE = context.getExternalContext().getInitParameter("javax.faces.PROJECT_STAGE");
        
       
        genomeHash = new Hashtable<String, String>();
        for (int i = 0; i < genomes.length; i++) {
            genomeHash.put(organisms[i], genomes[i]);
        }
    
        if(installation.equals("local") && PROJECT_STAGE.equals("Development")){
            verbose = true;
        }else{
            verbose = false;
        }
        //verbose = true;
        if(installation.equals("remote")){
            runfolderroot = context.getExternalContext().getInitParameter("remoterunfolder");  
            fastqfolderroot = context.getExternalContext().getInitParameter("remotefastqfolder");  
            bamfolderroot = context.getExternalContext().getInitParameter("remotebamfolder");  
            bigwigfolderroot = context.getExternalContext().getInitParameter("remotebigwigfolder"); 
            bedfolderroot = context.getExternalContext().getInitParameter("remotebedfolder"); 
            samplesheetfolder = context.getExternalContext().getInitParameter("remotesamplesheetfolder"); 
            workdir = context.getExternalContext().getInitParameter("remoteworkdir");
            galaxyScriptPath = context.getExternalContext().getInitParameter("remotegalaxyscriptpath");
            cutoff = Integer.parseInt(context.getExternalContext().getInitParameter("remotemonitoringcutoffdate"));
            scratchdir = context.getExternalContext().getInitParameter("remotescratchdir");
            fastqcexecpath = context.getExternalContext().getInitParameter("remotefastqcexecpath");
            bcltofastqexecpath = context.getExternalContext().getInitParameter("remotebcltofastqexecpath");
            mismatches= Integer.parseInt(context.getExternalContext().getInitParameter("remotemismatches"));
            fastqhyperlink = context.getExternalContext().getInitParameter("remotefastqhyperlink");
            executerpathflowcellA = context.getExternalContext().getInitParameter("remoteexecuterpathflowcellA");
            executerpathflowcellB = context.getExternalContext().getInitParameter("remoteexecuterpathflowcellB");
            runfolderscaninterval = context.getExternalContext().getInitParameter("remoterunfolderscaninterval");
        }else if(installation.equals("local")){
            runfolderroot = context.getExternalContext().getInitParameter("localrunfolder");  
            fastqfolderroot = context.getExternalContext().getInitParameter("localfastqfolder");  
            bamfolderroot = context.getExternalContext().getInitParameter("localbamfolder");  
            bigwigfolderroot = context.getExternalContext().getInitParameter("localbigwigfolder"); 
            bedfolderroot = context.getExternalContext().getInitParameter("localbedfolder"); 
            samplesheetfolder = context.getExternalContext().getInitParameter("localsamplesheetfolder"); 
            workdir = context.getExternalContext().getInitParameter("localworkdir");
            galaxyScriptPath = context.getExternalContext().getInitParameter("localgalaxyscriptpath");
            cutoff = Integer.parseInt(context.getExternalContext().getInitParameter("localmonitoringcutoffdate"));
            scratchdir = context.getExternalContext().getInitParameter("localscratchdir");
            fastqcexecpath = context.getExternalContext().getInitParameter("localfastqcexecpath");
            bcltofastqexecpath = context.getExternalContext().getInitParameter("localbcltofastqexecpath");
            mismatches= Integer.parseInt(context.getExternalContext().getInitParameter("localmismatches"));
            fastqhyperlink = context.getExternalContext().getInitParameter("localfastqhyperlink");
            executerpathflowcellA = context.getExternalContext().getInitParameter("localexecuterpathflowcellA");
            executerpathflowcellB = context.getExternalContext().getInitParameter("localexecuterpathflowcellB");
            runfolderscaninterval = context.getExternalContext().getInitParameter("localrunfolderscaninterval");
        }
        
        subjectRequestReceived = context.getExternalContext().getInitParameter("subjectRequestReceived");
        subjectFastQReadyAlert = context.getExternalContext().getInitParameter("subjectFastQReadyAlert");
        sentByMailAddress = context.getExternalContext().getInitParameter("sentByMailAddress");
        sentByUnitName = context.getExternalContext().getInitParameter("sentByUnitName");
        smtpServer = context.getExternalContext().getInitParameter("smtpServer"); 
        testOpeningSlashPresence();
        testClosingSlashPresence();
        dumpConfiguration();
        dumpMailParameters();
    }
    
    /**
     * Getter for depth, the list of depth parameters for single reads.
     * 
     * @author Heiko Muller
     * @return String[] 
     * @since 1.0
     */
    public static String[] getDepth_SR() {
        return depth_SR;
    }

   /**
     * Getter for depth, the list of depth parameters for paired end reads.
     * 
     * @author Heiko Muller
     * @return String[] 
     * @since 1.0
     */
    public static String[] getDepth_PE() {
        return depth_PE;
    }

    /**
     * Getter for bools, true/false.
     * 
     * @author Heiko Muller
     * @return String[] - {"true", "false"}
     * @since 1.0
     */
    public final String[] getBools() {
        return bools;
    }

    /*
    public String[] getStatusList() {
        return statusList;
    }
    */

    /**
     * Getter for targetSynthesisList.
     * 
     * @author Heiko Muller
     * @return String[] - {"true", "false"}
     * @since 1.0
     */
    //public String[] getTargetSynthesisList() {
    //    return targetSynthesisList;
    //}

    /**
     * Getter for institute.
     * 
     * @author Heiko Muller
     * @return String[] - list of institutes.
     * @since 1.0
     */
    public static String[] getInstitute() {
        return institute;
    }

    /**
     * Getter for organisms.
     * 
     * @author Heiko Muller
     * @return String[] - list of organisms.
     * @since 1.0
     */
    public String[] getOrganisms() {
        return organisms;
    }

    /**
     * Getter for sampletype.
     * 
     * @author Heiko Muller
     * @return String[] - list of sample types.
     * @since 1.0
     */
    public String[] getSampletype() {
        return sampletype;
    }

    /**
     * Getter for readlength.
     * 
     * @author Heiko Muller
     * @return String[] - list of readlengths.
     * @since 1.0
     */
    public String[] getReadlength() {
        return readlength;
    }

    /**
     * Getter for readmode.
     * 
     * @author Heiko Muller
     * @return String[] - list of read modes.
     * @since 1.0
     */
    public String[] getReadmode() {
        return readmode;
    }

    /**
     * Getter for instrument.
     * 
     * @author Heiko Muller
     * @return String[] - list of instruments.
     * @since 1.0
     */
    public String[] getInstrument() {
        return instrument;
    }

    /**
     * Getter for application.
     * 
     * @author Heiko Muller
     * @return String[] - list of applications.
     * @since 1.0
     */
    public final String[] getApplication() {
        return application;
    }

//	public String[] getSeqIndex() {
//		return seqIndex;
//	}
//
//	public Hashtable<String, String> getGroupLeaderEmails() {
//		return groupLeaderEmails;
//	}
//
////	public String[][] getGroupLeaders() {
////		return groupLeaders;
////	}
//
//	public String[] getGroupLeaderAcronym() {
//		return groupLeaderAcronym;
//	}
    
    /**
     * Getter for genomeHash.
     * 
     * @author Heiko Muller
     * @return Hashtable<String, String> - HashTable of genomes.
     * @since 1.0
     */
    public Hashtable<String, String> getGenomeHash() {
        return genomeHash;
    }

    /**
     * Getter for genomes.
     * 
     * @author Heiko Muller
     * @return String[] - list of genomes.
     * @since 1.0
     */
    public static String[] getGenomes() {
        return genomes;
    }

    /**
     * Getter for ngsapplication.
     * 
     * @author Heiko Muller
     * @return String[] - list of ngs applications.
     * @since 1.0
     */
    public String[] getNgsapplication() {
        return ngsapplication;
    }

    /**
     * Getter for readlengthrnaseq.
     * 
     * @author Heiko Muller
     * @return String[] - list of read lengths for RNAseq.
     * @since 1.0
     */
    public static String[] getReadlengthrnaseq() {
        return readlengthrnaseq;
    }

    /**
     * Getter for readlengthdnaseq.
     * 
     * @author Heiko Muller
     * @return String[] - list of read lengths for DNAseq.
     * @since 1.0
     */
    public static String[] getReadlengthdnaseq() {
        return readlengthdnaseq;
    }

    /**
     * Getter for defGenomes.
     * 
     * @author Heiko Muller
     * @return String[]
     * @since 1.0
     */
    public static String[] getDefGenomes() {
        return defGenomes;
    }

    // TODO cambiare questa porcheria
    public String getDefGenome(Sample s) {
        for (int i = 0; i < organisms.length; i++) {
            if (s.getOrganism().equals(organisms[i])) {
                return genomes[i];
            }
        }
        return "other";
    }
    
    /**
     * Getter for depth.
     * 
     * @author Heiko Muller
     * @return String[]
     * @since 1.0
     */
    public String[] getDepth() {
        return depth;
    }

    /**
     * Getter for roleList.
     * 
     * @author Heiko Muller
     * @return String[]
     * @since 1.0
     */
    public String[] getRoleList() {
        return roleList;
    }

    /**
     * Getter for runfolderroot.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getRunfolderroot() {
        return runfolderroot;
    }

    /**
     * Getter for fastqfolderroot.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getFastqfolderroot() {
        return fastqfolderroot;
    }

    /**
     * Getter for bamfolderroot.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getBamfolderroot() {
        return bamfolderroot;
    }

    /**
     * Getter for bigwigfolderroot.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getBigWigfolderroot() {
        return bigwigfolderroot;
    }

    /**
     * Getter for samplesheetfolder.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getSampleSheetFolder() {
        return samplesheetfolder;
    }

    /**
     * Getter for workdir.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getWorkdir() {
        return workdir;
    }

    /**
     * Getter for galaxyScriptPath.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getGalaxyScriptPath() {
        return galaxyScriptPath;
    }

    /**
     * Getter for cutoff.
     * 
     * @author Heiko Muller
     * @return int - cutoff date (140620 = 20th June 2014)
     * @since 1.0
     */
    public static int getCutoff() {
        return cutoff;
    }

    /**
     * Getter for bedfolderroot.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getBedfolderroot() {
        return bedfolderroot;
    }

    /**
     * Getter for serialVersionUID.
     * 
     * @author Heiko Muller
     * @return long
     * @since 1.0
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Getter for scratchdir.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getScratchdir() {
        return scratchdir;
    }

    /**
     * Getter for fastqcexecpath.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getFastqcexecpath() {
        return fastqcexecpath;
    }

    /**
     * Getter for bcltofastqexecpath.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getBcltofastqexecpath() {
        return bcltofastqexecpath;
    }

    /**
     * Getter for mismatches.
     * 
     * @author Heiko Muller
     * @return int
     * @since 1.0
     */
    public static int getMismatches() {
        return mismatches;
    }
    
    /**
     * Getter for subjectRequestReceived.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getSubjectRequestReceived() {
        return subjectRequestReceived;
    }

    /**
     * Getter for subjectFastQReadyAlert.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getSubjectFastQReadyAlert() {
        return subjectFastQReadyAlert;
    }

    /**
     * Getter for sentByMailAddress.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getSentByMailAddress() {
        return sentByMailAddress;
    }

    /**
     * Getter for sentByUnitName.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getSentByUnitName() {
        return sentByUnitName;
    }

    /**
     * Getter for smtpServer.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getSmtpServer() {
        return smtpServer;
    }

    /**
     * Getter for default_application.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getDefault_application() {
        return default_application;
    }
    
    /**
     * Getter for verbose mode. Returns true if installation is local and project stage is Development.
     * 
     * @author Heiko Muller
     * @return boolean
     * @since 1.0
     */
    public static boolean getVerbose() {
        return verbose;
    }

    /**
     * Getter for fastq hyperlink.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getFastqhyperlink() {
        return fastqhyperlink;
    }

    /**
     * Getter for Executer path flowcell A.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getExecuterpathflowcellA() {
        return executerpathflowcellA;
    }

    /**
     * Getter for Executer path flowcell B.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getExecuterpathflowcellB() {
        return executerpathflowcellB;
    }

    
    /**
     * Getter for runfolderscaninterval. Default = 1 hour.
     * 
     * @author Heiko Muller
     * @return long milliseconds
     * @since 2.1
     */
    public static long getRunfolderScanInterval() {        
        long minutes = 60;
        try{            
            minutes = Long.parseLong(runfolderscaninterval);
        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
        }        
        return minutes * 60 * 1000;
    }
    
    public static List<String> getROLES() {
        return ROLES;
    }
    
    
    
    
    
    /**
     * Tests presence of opening slash in folder path names. If missing, it is added.
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    private void testOpeningSlashPresence(){
        if(!runfolderroot.startsWith("/")){
            runfolderroot = "/" + runfolderroot;
        }
        if(!fastqfolderroot.startsWith("/")){
            fastqfolderroot = "/" + fastqfolderroot;
        }
        if(!bamfolderroot.startsWith("/")){
            bamfolderroot = "/" + bamfolderroot;
        }
        if(!bigwigfolderroot.startsWith("/")){
            bigwigfolderroot = "/" + bigwigfolderroot;
        }
        if(!bedfolderroot.startsWith("/")){
            bedfolderroot = "/" + bedfolderroot;
        }
        if(!samplesheetfolder.startsWith("/")){
            samplesheetfolder = "/" + samplesheetfolder;
        }
        if(!workdir.startsWith("/")){
            workdir = "/" + workdir;
        }
        if(!galaxyScriptPath.startsWith("/")){
            galaxyScriptPath = "/" + galaxyScriptPath;
        }
        if(!scratchdir.startsWith("/")){
            scratchdir = "/" + scratchdir;
        }
        
    
    }
    
    /**
     * Tests presence of closing slash in folder path names. If missing, it is added.
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    private void testClosingSlashPresence(){
        if(!runfolderroot.endsWith("/")){
            runfolderroot = runfolderroot + "/";
        }
        if(!fastqfolderroot.endsWith("/")){
            fastqfolderroot = fastqfolderroot + "/";
        }
        if(!bamfolderroot.endsWith("/")){
            bamfolderroot = bamfolderroot + "/";
        }
        if(!bigwigfolderroot.endsWith("/")){
            bigwigfolderroot = bigwigfolderroot + "/";
        }
        if(!bedfolderroot.endsWith("/")){
            bedfolderroot = bedfolderroot + "/";
        }
        if(!samplesheetfolder.endsWith("/")){
            samplesheetfolder = samplesheetfolder + "/";
        }
        if(!workdir.endsWith("/")){
            workdir = workdir + "/";
        }
        if(!galaxyScriptPath.endsWith("/")){
            //galaxyScriptPath = galaxyScriptPath + "/";
        }
        if(!scratchdir.endsWith("/")){
            scratchdir = scratchdir + "/";
        }
        if(!fastqhyperlink.endsWith("/")){
            fastqhyperlink = fastqhyperlink + "/";
        }
    
    }
    
    
    /**
     * Dumps the configuration.
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    private void dumpConfiguration(){
 
        System.out.println("runfolder root " + runfolderroot);
        System.out.println("FastQ folder root " + fastqfolderroot);
        System.out.println("BAM folder root " + bamfolderroot);
        System.out.println("BigWig folder root " + bigwigfolderroot);
        System.out.println("Bed folder root " + bedfolderroot);
        System.out.println("SampleSheet folder root " + samplesheetfolder);
        System.out.println("Work directory " + workdir);
        System.out.println("Galaxy script path " + galaxyScriptPath);
        System.out.println("Monitoring cutoff date " + cutoff);  
        System.out.println("scratch directory " + scratchdir);
        System.out.println("fastqc execution path " + fastqcexecpath);
        System.out.println("bcl to fastq execution path " + bcltofastqexecpath);
        System.out.println("mismatches " + mismatches);
        System.out.println("fastqhyperlink " + fastqhyperlink);
        System.out.println("runfolderscaninterval " + runfolderscaninterval);
        
    }
    
    /**
     * Dumps the mail parameters.
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    private void dumpMailParameters(){
 
        System.out.println("Subject RequestReceived " + subjectRequestReceived);
        System.out.println("subject FastQReadyAlert " + subjectFastQReadyAlert);
        System.out.println("sentBy mail address " + sentByMailAddress);
        System.out.println("sentBy unit name " + sentByUnitName);
        System.out.println("smtp server " + smtpServer);
        
    }
    
    

}
