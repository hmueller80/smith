package it.iit.genomics.cru.smith.runsBeans;

import it.iit.genomics.cru.smith.defaults.NgsLimsUtility;
import it.iit.genomics.cru.smith.defaults.Preferences;
import it.iit.genomics.cru.smith.entity.Lane;
import it.iit.genomics.cru.smith.entity.Reagent;
import it.iit.genomics.cru.smith.entity.Sample;
import it.iit.genomics.cru.smith.entity.SampleRun;
import it.iit.genomics.cru.smith.entity.SampleRunId;
import it.iit.genomics.cru.smith.entity.SequencingIndex;
import it.iit.genomics.cru.smith.entity.User;
import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @(#)SampleRunFormBean.java 20 JUN 2014 Copyright 2014 Computational Research
 * Unit of IIT@SEMM. All rights reserved. Use is subject to MIT license terms.
 *
 * Backing bean for sample run form.
 *
 * @author Francesco Venco
 * @version 1.0
 * @since 1.0
 */
public class SampleRunFormBean implements Serializable {

    protected String formId;
    protected SampleRunId id;
    protected int sampleID;
    protected int runID;
    protected Sample sample;
    protected User user;
    protected Reagent sequencingReag;
    protected String sequencingBarcode;
    protected Reagent clusterReag;
    protected String clusterBarcode;
    protected Reagent sampleprepReag;
    protected String preparationBarcode;
    protected String flowcell;
    protected String lane;
    protected int laneRows;
    protected Set<Lane> lanes;
    protected String sequencingindex;
    protected String runfolder;
    protected boolean iscontrol;
    protected List<Reagent> reagentsList;
    //protected String[] barcodes;
    //protected List<String> barcodes;
    protected List<String> barcodesCluster;
    protected List<String> barcodesPreparation;
    protected List<String> barcodesSequencing;
    private ArrayList<String> possibleIndexes;
    protected SampleRun loadedSampleRun;

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public SampleRunFormBean() {
        if(Preferences.getVerbose()){
            System.out.println("init SampleRunFormBean");
        }
        init();
        load();
    }

    /**
    * Bean constructor.
    *
    * @author Francesco Venco
    * @param rid - run id
    * @param sid - sample id
    * @since 1.0
    */
    public SampleRunFormBean(int rid, int sid) {
        sampleID = sid;
        runID = rid;
        load();
        loadReagents();
    }

    /**
    * init
    *
    * @author Francesco Venco
    * @since 1.0
    */
    public void init() {
        loadReagents();
        FacesContext context = FacesContext.getCurrentInstance();
        String sid = (String) context.getExternalContext().getRequestParameterMap().get("sid");
        if (sid != null) {
            sampleID = Integer.parseInt(sid);
        } else {
            sampleID = -1;
        }

        String rid = (String) context.getExternalContext().getRequestParameterMap().get("rid");
        if (rid != null) {
            runID = Integer.parseInt(rid);
        } else {
            runID = -1;
        }
        
        flowcell = "none";
        runfolder = "none";

        //load indexes
        loadPossibleIndexes();

    }

    /**
    * Loads reagents.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    protected void loadReagents() {
        //load all the reagents
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            reagentsList = session.createQuery("from Reagent").list();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        barcodesSequencing = new ArrayList<String>();
        barcodesSequencing.add("none");
        barcodesCluster = new ArrayList<String>();
        barcodesCluster.add("none");
        barcodesPreparation = new ArrayList<String>();
        barcodesPreparation.add("none");
        for (Reagent r : reagentsList) {
            //barcodes[i] = reagentsList.get(i).getReagentbarcode();
            if (r.getSupportedReactions() != 0) {
                if (r.getApplication().equals("Sample preparation")) {
                    barcodesPreparation.add(r.getReagentBarCode());
                } else if (r.getApplication().equals("Sequencing")) {
                    barcodesSequencing.add(r.getReagentBarCode());
                } else {
                    barcodesCluster.add(r.getReagentBarCode());
                }

            }
            //barcodes[i] = barcodes[i] + " - NO USES LEFT";           
        }

    }

    /**
    * Loads data for id, loadedSampleRun, sample, user from database.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    protected void load() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            id = new SampleRunId(runID, sampleID);
            loadedSampleRun = (SampleRun) session.get(SampleRun.class, id);
            // load the sample
            sample = (Sample) session.get(Sample.class, sampleID);
            System.out.println("Loading sample " + sample.getName());
            // load the user
            user = (User) session.get(User.class, loadedSampleRun.getUser().getId());
            // set remaining data
            setCurrentSamplerunData(loadedSampleRun);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
        } finally {
            session.close();
        }

    }

    /**
    * Loads barcodes.
    *
    * @author Francesco Venco
    * @param query
    * @param barcodes
    * @return List<String> - list of selected barcodes
    * @since 1.0
    */
    public List<String> completeBarcode(String query, List<String> barcodes) {
        List<String> selectedBarcodes = new ArrayList<String>();
        for (String barcode : barcodes) {
            if (barcode.startsWith(query)) {
                selectedBarcodes.add(barcode);
            }
        }
        return selectedBarcodes;
    }

    /**
    * Returns list of barcodes of sequencing reagents used in a run.
    *
    * @author Francesco Venco
    * @param query
    * @return List<String> - list of used sequencing reagent barcodes
    * @since 1.0
    */
    public List<String> completeSequencing(String query) {
        return completeBarcode(query, barcodesSequencing);
    }

    /**
    * Returns list of barcodes of sample preparation reagents used in a run.
    *
    * @author Francesco Venco
    * @param query
    * @return List<String> - list of used sample preparation reagent barcodes
    * @since 1.0
    */
    public List<String> completePreparation(String query) {
        return completeBarcode(query, barcodesPreparation);
    }

    /**
    * Returns list of barcodes of cluster generation reagents used in a run.
    *
    * @author Francesco Venco
    * @param query
    * @return List<String> - list of used cluster generation reagent barcodes
    * @since 1.0
    */
    public List<String> completeCluster(String query) {
        return completeBarcode(query, barcodesCluster);
    }

    /**
    * Setter for current sample run data.
    *
    * @author Francesco Venco
    * @param samplerun
    * @since 1.0
    */
    protected void setCurrentSamplerunData(SampleRun samplerun) {

        if (samplerun != null) {
            Reagent seq = samplerun.getReagentBySequencingReagentCode();
            if (seq != null) {
                sequencingBarcode = seq.getReagentBarCode();
            } else {
                sequencingBarcode = "none";
            }
            Reagent clust = samplerun.getReagentByClustergenerationReagentCode();
            if (clust != null) {
                clusterBarcode = clust.getReagentBarCode();
            } else {
                clusterBarcode = "none";
            }
            Reagent prep = samplerun.getReagentBySamplePrepReagentCode();
            if (prep != null) {
                preparationBarcode = prep.getReagentBarCode();
            } else {
                preparationBarcode = "none";
            }
            flowcell = samplerun.getFlowcell();
            lanes = samplerun.getLanes();
            lane = "";
            for (Lane l : lanes) {
                lane = lane + "\n" + l.getLaneName();
            }
            laneRows = lanes.size();
            sequencingindex = samplerun.getsample().getSequencingIndexes().getIndex();
            runfolder = samplerun.getRunFolder();
            iscontrol = samplerun.getIsControl();
        }

    }

    /**
    * Updates sample run data.
    *
    * @author Francesco Venco
    * @param samplerun
    * @param session
    * @since 1.0
    */
    public void setSamplerunData(SampleRun samplerun, Session session) {

        samplerun.setFlowcell(flowcell);
        samplerun.setControl(iscontrol);
        //samplerun.setLane(lane);
        samplerun.setRunFolder(runfolder);
        SequencingIndex si = (SequencingIndex) session.createQuery("from SequencingIndex s where s.index='" + sequencingindex + "'").list().get(0);
        sample.setSequencingIndexes(si);
        samplerun.setUser(user);
        samplerun.setId(id);
        samplerun.setReagentByClustergenerationReagentCode(clusterReag);
        samplerun.setReagentBySamplePrepReagentCode(sampleprepReag);
        samplerun.setReagentBySequencingReagentCode(sequencingReag);
        samplerun.setsample(sample);

    }

    /**
    * Updates sample run data.
    *
    * @author Francesco Venco
    * @param samplerun
    * @param session
    * @param othersequencingindex
    * @since 1.0
    */
    public void setSamplerunData(SampleRun samplerun, Session session, String othersequencingindex) {

        samplerun.setFlowcell(flowcell);
        samplerun.setControl(iscontrol);
        //samplerun.setLanes(lanes);
        samplerun.setRunFolder(runfolder);
        SequencingIndex si = (SequencingIndex) session.createQuery("from SequencingIndex s where s.index='" + othersequencingindex + "'").list().get(0);
        sample.setSequencingIndexes(si);
        samplerun.setUser(user);
        samplerun.setId(id);
        samplerun.setReagentByClustergenerationReagentCode(clusterReag);
        samplerun.setReagentBySamplePrepReagentCode(sampleprepReag);
        samplerun.setReagentBySequencingReagentCode(sequencingReag);
        samplerun.setsample(sample);

    }

    /**
    * Form validator.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    protected boolean validateForm() {

        boolean formIsCorrect = true;

        if (flowcell == null || flowcell.equals("")) {
            formIsCorrect = false;
            NgsLimsUtility.setFailMessage(formId, "flowcell", "Flowcell is undefined", "Undefined");
        }

        /*if (sequencingindex.equals("none")) {
         formIsCorrect = false;
         NgsLimsUtility.setFailMessage(formId, "sequencingindex", "Sequencing index is undefined", "Undefined");
         }*/
        if (runfolder == null || runfolder.equals("")) {
            formIsCorrect = false;
            NgsLimsUtility.setFailMessage(formId, "runfolder", "Run folder is undefined", "Undefined");
        }

        if (lane == null || lane.equals("")) {
            formIsCorrect = false;
            NgsLimsUtility.setFailMessage(formId, "lane", "Lane is undefined", "Undefined");
        }

        return formIsCorrect;
    }

    /**
    * Form validator.
    *
    * @author Francesco Venco
    * @param si
    * @return boolean
    * @since 1.0
    */
    protected boolean validateForm(SampleIndex si) {

        boolean formIsCorrect = true;

        if (flowcell == null || flowcell.equals("")) {
            formIsCorrect = false;
            NgsLimsUtility.setFailMessage(formId, "flowcell", "Flowcell is undefined", "Undefined");
        }

        /*if (si.getSequencingIndex().equals("none")) {
         formIsCorrect = false;
         NgsLimsUtility.setFailMessage(formId, "sequencingindex", "Sequencing index is undefined for sample " + si.getId(), "Undefined");
         }*/
        if (runfolder == null || runfolder.equals("")) {
            formIsCorrect = false;
            NgsLimsUtility.setFailMessage(formId, "runfolder", "Run folder is undefined", "Undefined");
        }

        return formIsCorrect;
    }

    /**
    * Detracts 1 from the number of remaining reactions if reagent has been used in the current run.
    *
    * @author Francesco Venco
    * @param tested
    * @param component
    * @param supportedReaction
    * @return boolean - true if number of reactions was decreased successfully, false otherwise
    * @since 1.0
    */
    protected boolean testAndDecreaseReagent(Reagent tested, String component, String supportedReaction) {
        return testAndUpdateReagent(tested, component, supportedReaction, false, -1);
    }

    /**
    * Detracts 1 from the number of remaining reactions if reagent has been used in the current run.
    *
    * @author Francesco Venco
    * @param tested
    * @param component
    * @param supportedReaction
    * @param silent
    * @param increase
    * @return boolean - true if number of reactions was decreased successfully, false otherwise
    * @since 1.0
    */
    protected boolean testAndUpdateReagent(Reagent tested, String component, String supportedReaction, boolean silent, int increase) {
        boolean success = true;
        if (tested == null) {
            return success;
        }
        try {
            int remainingUses = tested.getSupportedReactions() + increase
                    - tested.getSamplerunsForClustergenerationReagentCode().size()
                    - tested.getSamplerunsForSampleprepReagentCode().size()
                    - tested.getSamplerunsForSequencingReagentCode().size();
            System.out.println("Remaining uses" + remainingUses);
            if (!tested.getApplication().equals(supportedReaction)) {
                success = false;
                if (!silent) {
                    NgsLimsUtility.setFailMessage(formId, component, "Wrong Reagent", "Reaction not supported");
                }
            } else if (remainingUses >= 0) {
                if (!silent) {
                    NgsLimsUtility.setWarningMessage(formId, component, "Reagent uses remaining " + remainingUses, remainingUses + " remaining uses");
                }
            } else {
                success = false;
                if (!silent) {
                    NgsLimsUtility.setFailMessage(formId, component, "Reagent finished", "No remaining uses");
                }
            }
        } catch (Exception e) {
            if (!silent) {
                NgsLimsUtility.setFailMessage(formId, component, "Reagent not found", "Not found ");
            }
            tested = null;
            return false;
        }
        return success;

    }

    /**
    * Getter for reagentsList.
    *
    * @author Francesco Venco
    * @return List<Reagent>
    * @since 1.0
    */
    public List<Reagent> getReagentsList() {
        return this.reagentsList;
    }

    /**
    * Setter for reagentsList.
    *
    * @author Francesco Venco
    * @param reagentsList
    * @since 1.0
    */
    public void setReagentsList(List<Reagent> reagentsList) {
        this.reagentsList = reagentsList;
    }

    /**
    * Getter for id.
    *
    * @author Francesco Venco
    * @return SampleRunId
    * @since 1.0
    */
    public SampleRunId getId() {
        return this.id;
    }

    /**
    * Setter for id.
    *
    * @author Francesco Venco
    * @param id
    * @since 1.0
    */
    public void setId(SampleRunId id) {
        this.id = id;
    }

    /**
    * Getter for sample.
    *
    * @author Francesco Venco
    * @return Sample
    * @since 1.0
    */
    public Sample getSample() {
        return this.sample;
    }

    /**
    * Setter for sample.
    *
    * @author Francesco Venco
    * @param sample
    * @since 1.0
    */
    public void setSample(Sample sample) {
        this.sample = sample;
    }

    /**
    * Getter for user.
    *
    * @author Francesco Venco
    * @return User
    * @since 1.0
    */
    public User getUser() {
        return this.user;
    }

    /**
    * Setter for user.
    *
    * @author Francesco Venco
    * @param user
    * @since 1.0
    */
    public void setUser(User user) {
        this.user = user;
    }
    /*public Reagent getReagentBySequencingReagentCode() {
     return this.reagentBySequencingReagentCode;
     }
    
     public void setReagentBySequencingReagentCode(Reagent reagentBySequencingReagentCode) {
     this.reagentBySequencingReagentCode = reagentBySequencingReagentCode;
     }
     public Reagent getReagentByClustergenerationReagentCode() {
     return this.reagentByClustergenerationReagentCode;
     }
    
     public void setReagentByClustergenerationReagentCode(Reagent reagentByClustergenerationReagentCode) {
     this.reagentByClustergenerationReagentCode = reagentByClustergenerationReagentCode;
     }
     public Reagent getReagentBySampleprepReagentCode() {
     return this.reagentBySampleprepReagentCode;
     }
    
     public void setReagentBySampleprepReagentCode(Reagent reagentBySampleprepReagentCode) {
     this.reagentBySampleprepReagentCode = reagentBySampleprepReagentCode;
     }*/

    /**
    * Getter for flowcell.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getFlowcell() {
        return this.flowcell;
    }

    /**
    * Setter for flowcell.
    *
    * @author Francesco Venco
    * @param flowcell
    * @since 1.0
    */
    public void setFlowcell(String flowcell) {
        this.flowcell = flowcell;
    }

    /*public int getLane() {
     return this.lane;
     }

     public void setLane(int lane) {
     this.lane = lane;
     }*/
    public String getSequencingindex() {
        return this.sequencingindex;
    }

    /**
    * Setter for sequencingindex.
    *
    * @author Francesco Venco
    * @param sequencingindex
    * @since 1.0
    */
    public void setSequencingindex(String sequencingindex) {
        this.sequencingindex = sequencingindex;
    }

    /**
    * Getter for runfolder.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getRunfolder() {
        return this.runfolder;
    }

    /**
    * Setter for runfolder.
    *
    * @author Francesco Venco
    * @param runfolder
    * @since 1.0
    */
    public void setRunfolder(String runfolder) {
        this.runfolder = runfolder;
    }

    /**
    * Getter for iscontrol.
    *
    * @author Francesco Venco
    * @return boolean
    * @since 1.0
    */
    public boolean isIscontrol() {
        return this.iscontrol;
    }

    /**
    * Setter for iscontrol.
    *
    * @author Francesco Venco
    * @param iscontrol
    * @since 1.0
    */
    public void setIscontrol(boolean iscontrol) {
        this.iscontrol = iscontrol;
    }

    /**
    * Setter for sequencingBarcode.
    *
    * @author Francesco Venco
    * @param sequencingBarcode
    * @since 1.0
    */
    public void setSequencingBarcode(String sequencingBarcode) {
        this.sequencingBarcode = sequencingBarcode;
    }

    /**
    * Getter for sequencingBarcode.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getSequencingBarcode() {
        return this.sequencingBarcode;
    }

    /**
    * Setter for clusterBarcode.
    *
    * @author Francesco Venco
    * @param clusterBarcode
    * @since 1.0
    */
    public void setClusterBarcode(String clusterBarcode) {
        this.clusterBarcode = clusterBarcode;
    }

    /**
    * Getter for clusterBarcode.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getClusterBarcode() {
        return this.clusterBarcode;
    }

    /**
    * Setter for preparationBarcode.
    *
    * @author Francesco Venco
    * @param preparationBarcode
    * @since 1.0
    */
    public void setPreparationBarcode(String preparationBarcode) {
        this.preparationBarcode = preparationBarcode;
    }

    /**
    * Getter for preparationBarcode.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getPreparationBarcode() {
        return this.preparationBarcode;
    }

    /**
    * Getter for sampleID.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getSampleID() {
        return this.sampleID;
    }

    /**
    * Getter for runID.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getRunID() {
        return this.runID;
    }

    /* public String[] getBarcodes(){
     return this.barcodes;
     }*/
    public List<String> getBarcodesCluster() {
        return this.barcodesCluster;
    }

    /**
    * Getter for barcodesSequencing.
    *
    * @author Francesco Venco
    * @return List<String>
    * @since 1.0
    */
    public List<String> getBarcodesSequencing() {
        return this.barcodesSequencing;
    }

    /**
    * Getter for barcodesPreparation.
    *
    * @author Francesco Venco
    * @return List<String>
    * @since 1.0
    */
    public List<String> getBarcodesPreparation() {
        return this.barcodesPreparation;
    }

    /**
    * Getter for possibleIndexes.
    *
    * @author Francesco Venco
    * @return ArrayList<String>
    * @since 1.0
    */
    public ArrayList<String> getPossibleIndexes() {
        return possibleIndexes;
    }

    /**
     * @param possibleIndexes the possibleIndexes to set
     */
    public void setPossibleIndexes(ArrayList<String> possibleIndexes) {
        this.possibleIndexes = possibleIndexes;
    }

    /**
    * Loads for possible Index sequences.
    *
    * @author Francesco Venco
    * @since 1.0
    */
    private void loadPossibleIndexes() {
        Session session = HibernateUtil.getSessionFactory().openSession();;
        List<SequencingIndex> temp = new ArrayList<SequencingIndex>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            temp = (List<SequencingIndex>) session.createQuery("from SequencingIndex").list();
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        possibleIndexes = new ArrayList<String>();
        for (SequencingIndex s : temp) {
            possibleIndexes.add(s.getIndex());
        }
    }

    /**
    * Getter for lane.
    *
    * @author Francesco Venco
    * @return String
    * @since 1.0
    */
    public String getLane() {
        return lane;
    }

    /**
    * Setter for lane.
    *
    * @author Francesco Venco
    * @param lane
    * @since 1.0
    */
    public void setLane(String lane) {
        this.lane = lane;
    }

    /**
    * Getter for list of lanes.
    *
    * @author Francesco Venco
    * @return ArrayList<String>
    * @since 1.0
    */
    public ArrayList<String> getListOfLanes() {
        ArrayList<String> toRet = new ArrayList<String>();
        Scanner c = new Scanner(lane);
        while (c.hasNext()) {
            String l = c.next();
            toRet.add(l);
        }

        return toRet;
    }

    /**
    * Getter for laneRows.
    *
    * @author Francesco Venco
    * @return int
    * @since 1.0
    */
    public int getLaneRows() {
        return laneRows;
    }

    /**
    * Setter for laneRows.
    *
    * @author Francesco Venco
    * @param laneRows
    * @since 1.0
    */
    public void setLaneRows(int laneRows) {
        this.laneRows = laneRows;
    }
}
