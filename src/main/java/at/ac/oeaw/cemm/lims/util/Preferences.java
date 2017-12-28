package at.ac.oeaw.cemm.lims.util;


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
@ManagedBean(name = "preferences", eager=true)
@ApplicationScoped
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;


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

    private final String[] organisms = {"undefined", "MOUSE", "HUMAN",
        "RAT", "BOVINE", "PORCINE", "CHICKEN", "ZEBRAFISH", "DROSOPHILA",
        "CELEGANS", "CEREVISIAE", "ARABIDOPSIS", "RICE", "TOMATO",
        "ECOLI 2.0", "PSEUDOMONAS", "OTHER"};
    private final String[] sampletype = {"undefined", "IP", "INPUT"};
    private final String[] readlength = {"50", "75", "100"};
    private static final String[] depthSR = {"30", "60", "90", "120", "150"};
    private static final String[] depthPE = {"35", "70", "105", "140"};
    private final String[] readmode = {"SR", "PE"};
    private final String[] instrument = {"HiSeq2000"};
    public final String[] ngsapplication = {"undefined", "ChIP-Seq",
        "DNA-Seq", "ExomeSeq", "Methylation", "mRNA-Seq", "RRBS",
        "SmallRNA", "RNA-Seq", "CLIP-Seq", "Other"};

    private static String  runfolderroot = "/data/Illumina/Runs/"; //"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/Runs/";
    private static String samplesheetfolder = "/data/Illumina/PublicData/SampleSheets/";//"/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/SampleSheets/";
    private static String  workdir; // = "/Users/yvaskin/Documents/project/sandbox/smith_run_tests/deploy_tests/Temp/"; //TODO
    private static int cutoff = 140716;
    private static String      executerPath;
    private static String annotationSheetFolder;
    
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
       
        if(installation.equals("local") && PROJECT_STAGE.equals("Development")){
            verbose = true;
        }else{
            verbose = false;
        }

        if(installation.equals("remote")){
            runfolderroot = context.getExternalContext().getInitParameter("remoterunfolder");  
            samplesheetfolder = context.getExternalContext().getInitParameter("remotesamplesheetfolder"); 
            workdir = context.getExternalContext().getInitParameter("remoteworkdir");
            cutoff = Integer.parseInt(context.getExternalContext().getInitParameter("remotemonitoringcutoffdate"));
            executerPath = context.getExternalContext().getInitParameter("remoteexecuterpath");
            runfolderscaninterval = context.getExternalContext().getInitParameter("remoterunfolderscaninterval");
            annotationSheetFolder = context.getExternalContext().getInitParameter("remotesampleannotationfolder");
        }else if(installation.equals("local")){
            runfolderroot = context.getExternalContext().getInitParameter("localrunfolder");  
            samplesheetfolder = context.getExternalContext().getInitParameter("localsamplesheetfolder"); 
            workdir = context.getExternalContext().getInitParameter("localworkdir");
            cutoff = Integer.parseInt(context.getExternalContext().getInitParameter("localmonitoringcutoffdate"));
            executerPath = context.getExternalContext().getInitParameter("localexecuterpath");
            runfolderscaninterval = context.getExternalContext().getInitParameter("localrunfolderscaninterval");
            annotationSheetFolder = context.getExternalContext().getInitParameter("localsampleannotationfolder");
        }
        
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
    public static String[] getDepthSR() {
        return depthSR;
    }

   /**
     * Getter for depth, the list of depth parameters for paired end reads.
     * 
     * @author Heiko Muller
     * @return String[] 
     * @since 1.0
     */
    public static String[] getDepthPE() {
        return depthPE;
    }

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
     * Getter for Executer path flowcell A.
     * 
     * @author Heiko Muller
     * @return String
     * @since 1.0
     */
    public static String getExecuterPath() {
        return executerPath;
    }

    public static String getAnnotationSheetFolder() {
        return annotationSheetFolder;
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
        
    
    /**
     * Tests presence of opening slash in folder path names. If missing, it is added.
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    private void testOpeningSlashPresence(){
        if (!runfolderroot.startsWith("/")) {
            runfolderroot = "/" + runfolderroot;
        }
        if (!samplesheetfolder.startsWith("/")) {
            samplesheetfolder = "/" + samplesheetfolder;
        }
        if (!workdir.startsWith("/")) {
            workdir = "/" + workdir;
        }
        if (!annotationSheetFolder.startsWith("/")) {
            annotationSheetFolder = "/" + annotationSheetFolder;
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
        if(!samplesheetfolder.endsWith("/")){
            samplesheetfolder = samplesheetfolder + "/";
        }
        if(!workdir.endsWith("/")){
            workdir = workdir + "/";
        }     
        if (!annotationSheetFolder.endsWith("/")) {
            annotationSheetFolder = annotationSheetFolder +"/";
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
        System.out.println("SampleSheet folder root " + samplesheetfolder);
        System.out.println("Work directory " + workdir);
        System.out.println("Monitoring cutoff date " + cutoff);  
        System.out.println("runfolderscaninterval " + runfolderscaninterval);
        
    }
    
    /**
     * Dumps the mail parameters.
     * 
     * @author Heiko Muller
     * @since 1.0
     */
    private void dumpMailParameters(){
 
        System.out.println("sentBy mail address " + sentByMailAddress);
        System.out.println("sentBy unit name " + sentByUnitName);
        System.out.println("smtp server " + smtpServer);
        
    }
    
    

}
