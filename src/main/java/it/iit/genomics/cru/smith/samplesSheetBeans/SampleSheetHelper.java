package it.iit.genomics.cru.smith.samplesSheetBeans;

//import it.iit.genomics.cru.analysis.SamplesheetToCommandsSEMM;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Project;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.Samplesheet;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import it.iit.genomics.cru.smith.runsBeans.RunHelper;
import it.iit.genomics.cru.smith.userBeans.UserHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Transaction;

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
public class SampleSheetHelper {
    
    private static final String[] defGenomes = Preferences.getDefGenomes();
    private static final String[] genomes = Preferences.getGenomes();
    private static final Hashtable<String, String> genomeHash;
    private static final String[] organisms = {"undefined", "MOUSE", "HUMAN",
        "RAT", "BOVINE", "PORCINE", "CHICKEN", "ZEBRAFISH", "DROSOPHILA",
        "CELEGANS", "CEREVISIAE", "ARABIDOPSIS", "RICE", "TOMATO",
        "ECOLI 2.0", "PSEUDOMONAS", "OTHER"};
    
    static{
        if(Preferences.getVerbose()){
            System.out.println("init SampleSheetHelper");
        }
        
        genomeHash = new Hashtable<String, String>();
        for (int i = 0; i < genomes.length; i++) {
            genomeHash.put(organisms[i], genomes[i]);
        }
        
    }
    
    public static String getDefGenome(Sample s) {
        for (int i = 0; i < organisms.length; i++) {
            if (s.getOrganism().equals(organisms[i])) {
                return genomes[i];
            }
        }
        return "other";
    }
    
    /**
     * Loads the samplesheet for a flow cell.
     *
     * @author Yuriy Vaskin
     * @param FCID - flow cell barcode
     * @return List<Samplesheet>
     * @since 1.0
     */
    public static List<Samplesheet> loadSamplesheet(String FCID){            
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> sampleRun = null;
        Transaction tx = null;
        List<Samplesheet> list = new LinkedList<Samplesheet>();
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery ("from SampleRun as s where s.flowcell ='" + FCID+ "'");
            sampleRun = (List<SampleRun>) q.list(); 
            for (int i = 0; i < sampleRun.size(); i++){
                SampleRun sr = sampleRun.get(i);
                String genome = getDefGenome(sr.getsample());
                String idx = sr.getsample().getSequencingIndexes().getIndex();
                String readmode = sr.getsample().getApplication().getReadmode();
                String projects = new  String("");
                String samId = new  String("");
                samId += sr.getsamId();
                Set<Project> projectsSet = sr.getsample().getProjects();
                Iterator<Project> iterator = projectsSet.iterator();
                while(iterator.hasNext()) {

                    Project setElement = iterator.next();
                    projects +=setElement.getUser().getLogin();
                    break;


                }
                if(projects.isEmpty()){
                   projects = sr.getUser().getLogin();
                }
                int counter = sr.getId().getRunId();
                String fcid = sr.getFlowcell();
                int lane = -1;
                for (Lane l : sr.getLanes()){
                    lane = Integer.parseInt(l.getLaneName());
                    String sampleId = sr.getsample().getName();
                    String sampleRef = genome;
                    String indexed = ("none".equals(idx) ? "": idx);
                    String description = sr.getsample().getType()+"_"+ sr.getsample().getDescription();
                    String control = (sr.getIsControl() ? "Y" : "N");
                    String recipe = Integer.toString(sr.getsample().getApplication().getReadlength())+readmode+"_"+(indexed == ""?"":"indexed");
                    String operator = sr.getUser().getLogin();
                    String sampleProject = sr.getsample().getUser().getLogin();
                    Date date = sr.getsample().getBioanalyzerDate();
                    String instrument = sr.getsample().getApplication().getInstrument();
                    String experimentType = sr.getsample().getExperimentName();
                //int counter, String fcid, int lane, String sampleId, String sampleRef, String indexed, String description, String control, String recipe, String operator, String sampleProject, Date date, String instrument, String experimentType
                    Samplesheet s = new Samplesheet(counter, fcid, lane, sampleId, sampleRef, indexed, description, control, recipe, operator, sampleProject, date, instrument, experimentType);
                    list.add(s);
                }
                
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();

        return list;
    }
    
    public static List<Samplesheet> loadSamplesheetCEMM(String FCID){            
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SampleRun> sampleRun = null;
        Transaction tx = null;
        List<Samplesheet> list = new LinkedList<Samplesheet>();
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery ("from SampleRun as s where s.flowcell ='" + FCID+ "'");
            sampleRun = (List<SampleRun>) q.list(); 
            for (int i = 0; i < sampleRun.size(); i++){
                SampleRun sr = sampleRun.get(i);
                String genome = getDefGenome(sr.getsample());
                String idx = sr.getsample().getSequencingIndexes().getIndex();
                String readmode = sr.getsample().getApplication().getReadmode();
                String projects = new  String("");
                String samId = new  String("");
                samId += sr.getsamId();
                Set<Project> projectsSet = sr.getsample().getProjects();
                Iterator<Project> iterator = projectsSet.iterator();
                while(iterator.hasNext()) {

                    Project setElement = iterator.next();
                    projects +=setElement.getUser().getLogin();
                    break;


                }
                if(projects.isEmpty()){
                   projects = sr.getUser().getLogin();
                }
                int counter = sr.getId().getRunId();
                String fcid = sr.getFlowcell();
                int lane = -1;
                for (Lane l : sr.getLanes()){
                    lane = Integer.parseInt(l.getLaneName());
                    String sampleId = sr.getsample().getName();
                    String sampleRef = genome;
                    String indexed = ("none".equals(idx) ? "": idx);
                    String description = sr.getsample().getType()+"_"+ sr.getsample().getDescription();
                    String control = (sr.getIsControl() ? "Y" : "N");
                    String recipe = Integer.toString(sr.getsample().getApplication().getReadlength())+readmode+"_"+(indexed == ""?"":"indexed");
                    String operator = sr.getUser().getLogin();
                    String sampleProject = sr.getsample().getUser().getLogin();
                    Date date = sr.getsample().getBioanalyzerDate();
                    String instrument = sr.getsample().getApplication().getInstrument();
                    String experimentType = sr.getsample().getExperimentName();
                //int counter, String fcid, int lane, String sampleId, String sampleRef, String indexed, String description, String control, String recipe, String operator, String sampleProject, Date date, String instrument, String experimentType
                    Samplesheet s = new Samplesheet(counter, fcid, lane, sampleId, sampleRef, indexed, description, control, recipe, operator, sampleProject, date, instrument, experimentType, sr.getsample());
                    list.add(s);
                }
                
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();

        return list;
    }
    
    /**
     * Reads samplesheet for a flow cell from file.
     *
     * @author Yuriy Vaskin
     * @param filePath
     * @return String
     * @since 1.0
     */
    public static String readSamplesheet(String filePath){
        File f = new File(filePath);
        if (!f.exists()){
            return "";
        } 
        StringBuilder sb = new StringBuilder();
    
        try {
            String line = "";
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
        } catch (IOException ioe) {
        }
            
        return sb.toString();

    }
    
    /**
     * Reads samplesheet for a flow cell from file and adds PI and experiment type information.
     *
     * @author Yuriy Vaskin
     * @param filePath
     * @param FCID - flow cell barcode
     * @return String
     * @since 1.0
     */
    public static String readSamplesheetWithPiandExpType(String filePath, String FCID){
        File f = new File(filePath);
        if (!f.exists()){
            return "";
        } 
        StringBuilder sb = new StringBuilder();
    
        try {
            String line = "";
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            if(line != null){
                sb.append(Samplesheet.getCSVHeaderSEMM() + "\r\n");
            }
            while ((line = br.readLine()) != null) {                
                sb.append(line);                
                sb.append("\r\n");
            }
        } catch (IOException ioe) {
        }
            
        return sb.toString();

    }
    
    public static String readSamplesheetCEMM(String filePath){
        File f = new File(filePath);
        if (!f.exists()){
            return "";
        } 
        StringBuilder sb = new StringBuilder();
    
        try {
            String line = "";
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            if(line != null){
                sb.append(Samplesheet.getCSVHeaderCEMM() + "\r\n");
            }
            while ((line = br.readLine()) != null) {                
                sb.append(line);                
                sb.append("\r\n");
            }
        } catch (IOException ioe) {
        }
            
        return sb.toString();

    }
    
    /**
     * Reads samplesheet for a flow cell from database and adds PI and experiment type information.
     *
     * @author Yuriy Vaskin
     * @param FCID - flow cell barcode
     * @return String
     * @since 1.0
     */
    public static String getSamplesheetWithPIandExpType(String FCID){
        StringBuilder sb = new StringBuilder();
        List<Samplesheet> list = loadSamplesheet(FCID);
        if(list != null && list.size() > 0){
            sb.append(list.get(0).getCSVHeaderSEMM() + ",PI,ExpType\r\n");
            for(int i = 0; i < list.size(); i++){
                sb.append(list.get(i).getCSVSEMM() + "," + findPI(list.get(i).getSampleProject()) +","+list.get(i).getExperimentType()+ "\r\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * Reads samplesheet for a flow cell from database.
     *
     * @author Yuriy Vaskin
     * @param FCID - flow cell barcode
     * @return String
     * @since 1.0
     */
    public static String getSamplesheet(String FCID){
        StringBuilder sb = new StringBuilder();
        List<Samplesheet> list = SampleSheetHelper.loadSamplesheet(FCID);
        if(list != null && list.size() > 0){
            sb.append(list.get(0).getCSVHeaderSEMM() + "\r\n");
            for(int i = 0; i < list.size(); i++){
                sb.append(list.get(i).getCSVSEMM() + "\r\n");
            }
        }
        return sb.toString();
    }
    
    public static String getSamplesheetCEMM(String FCID, boolean indexreversal){
        StringBuilder sb = new StringBuilder();
        List<Samplesheet> list = SampleSheetHelper.loadSamplesheetCEMM(FCID);
        if(list != null && list.size() > 0){
            sb.append(list.get(0).getCSVHeaderCEMM() + "\r\n");
            for(int i = 0; i < list.size(); i++){
                if(!indexreversal){
                    sb.append(list.get(i).getCSVCEMM() + "\r\n");
                }else{
                    sb.append(list.get(i).getCSVCEMMRevCompl() + "\r\n");
                }
            }
        }
        return sb.toString();
    }
      
    /**
     * Finds surname of PI for a given user.
     *
     * @author Yuriy Vaskin
     * @param userlogin
     * @return String
     * @since 1.0
     */
    public static String findPI(String userlogin){  
        User user = UserHelper.getUserByLoginName(userlogin);
        
        User pi = UserHelper.getUserByID(user.getPi().intValue());
        
        String res = pi.getUserSurname();
        if(res == ""){
            res = pi.getLogin();
        }
        
        return res;
    }
    
    /**
     * Finds the application for a sample run on a given flow cell.
     *
     * @author Yuriy Vaskin
     * @param FCID
     * @param samplename
     * @return String - the application name
     * @since 1.0
     */
    private static String findSampleApplications(String FCID, String samplename){
        List<SampleRun> sr = RunHelper.getRunsByFCIDList(FCID);
        if(sr != null){
            for(SampleRun s : sr){
                
                Sample sm = s.getsample();
                if(s.getsample().getName().equals(samplename)){
                    return sm.getExperimentName();
                }
            }
        }
        return "udefined";
    }
}
