package it.iit.genomics.cru.smith.entity;

import it.iit.genomics.cru.smith.hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name = "samplerun")
public class SampleRun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private SampleRunId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sam_id", insertable = false, updatable = false)
    private Sample sample;

    @ManyToOne
    @JoinColumn(name = "operator_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sequencing_reagent_code")
    private Reagent reagentBySequencingReagentCode;

    @ManyToOne
    @JoinColumn(name = "clustergeneration_reagent_code")
    private Reagent reagentByClustergenerationReagentCode;

    @ManyToOne
    @JoinColumn(name = "sampleprep_reagent_code")
    private Reagent reagentBySamplePrepReagentCode;

    @Column(name = "flowcell")
    private String flowcell;

	//@Column(name = "lane")
    //private int lane;
    @Column(name = "runfolder")
    private String runFolder;

    @Column(name = "iscontrol")
    private boolean isControl;

    @OneToMany
    @JoinColumns({
        @JoinColumn(name = "run_id", referencedColumnName = "run_id"),
        @JoinColumn(name = "sam_id", referencedColumnName = "sam_id")})
    private Set<RawData> rawDatas = new HashSet<RawData>(0);

    /*@OneToMany(mappedBy="lane_run_id")
     @JoinColumns( {
     @JoinColumn(name = "lane_run_id", referencedColumnName = "run_id"),
     @JoinColumn(name = "lane_sam_id", referencedColumnName = "sam_id") })
     private Set<Lane> lanes = new HashSet<Lane>(0);*/
    @OneToMany(mappedBy = "samplerun", fetch = FetchType.EAGER)
    private Set<Lane> lanes = new HashSet<Lane>(0);

    public SampleRun() {
    }

    public SampleRun(int id) {
        this.id = new SampleRunId(id, 0);

        this.sample = sample;
        this.user = user;
        this.reagentBySequencingReagentCode = new Reagent();
        this.reagentByClustergenerationReagentCode = new Reagent();
        this.reagentBySamplePrepReagentCode = new Reagent();
        this.flowcell = "";
        //this.lane = 0;
        this.runFolder = "";
        this.isControl = false;
        this.rawDatas = null;
    }

    public SampleRun(SampleRunId id, Sample sample, User user,
            Reagent reagentBySequencingReagentCode,
            Reagent reagentByClustergenerationReagentCode,
            Reagent reagentBySampleprepReagentCode, String flowcell,
            String runfolder, boolean iscontrol) {
        this.id = id;
        this.sample = sample;
        this.user = user;
        this.reagentBySequencingReagentCode = reagentBySequencingReagentCode;
        this.reagentByClustergenerationReagentCode = reagentByClustergenerationReagentCode;
        this.reagentBySamplePrepReagentCode = reagentBySampleprepReagentCode;
        this.flowcell = flowcell;
        //this.lane = lane;
        this.runFolder = runfolder;
        this.isControl = iscontrol;
    }

    public SampleRun(SampleRunId id, Sample sample, User user,
            Reagent reagentBySequencingReagentCode,
            Reagent reagentByClustergenerationReagentCode,
            Reagent reagentBySampleprepReagentCode, String flowcell,
            String runfolder, boolean iscontrol, Set<RawData> rawdatas) {
        this.id = id;
        this.sample = sample;
        this.user = user;
        this.reagentBySequencingReagentCode = reagentBySequencingReagentCode;
        this.reagentByClustergenerationReagentCode = reagentByClustergenerationReagentCode;
        this.reagentBySamplePrepReagentCode = reagentBySampleprepReagentCode;
        this.flowcell = flowcell;
        //this.lane = lane;
        this.runFolder = runfolder;
        this.isControl = iscontrol;
        this.rawDatas = rawdatas;
    }

    public SampleRun(int rid, int sid) {
        this.id = new SampleRunId(rid, sid);

    }

    public String getAllLanesToString1() {
        String toRet = "";
        for (Lane l : lanes) {
            toRet = toRet + " " + l.getLaneName();
        }
        return toRet;
    }
    
    /*
    public String getAllLanesToString(){
        Session session = HibernateUtil.getSessionFactory().openSession();        
        StringBuilder result = new StringBuilder();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            SampleRun sr = (SampleRun)session.load(SampleRun.class, this.id);
            Set<Lane> ls = sr.getLanes();
            String toRet = "";
            for (Lane l : ls) {
                toRet = toRet + " " + l.getLaneName();
                result.append(l.getLaneName() + " ");
            }
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(session.isOpen()){
                session.flush();
                session.close();
            }
        }
        return result.toString().trim();
      
    }
    */
    
    public String getAllLanesToString(){
             
        StringBuilder result = new StringBuilder();      
        Set<Lane> ls = this.getLanes();
        String toRet = "";
            for (Lane l : ls) {
                toRet = toRet + " " + l.getLaneName();
                result.append(l.getLaneName() + " ");
            }
        return result.toString().trim();
      
    }

    public SampleRunId getId() {
        return this.id;
    }

    public void setId(SampleRunId id) {
        this.id = id;
    }

    public int getRunId() {
        return this.id.getRunId();
    }

    public void setRunId(int runId) {
        this.id.setRunId(runId);
    }

    public int getsamId() {
        return this.id.getSamId();
    }

    public void setsamId(int samId) {
        this.id.setSamId(samId);
    }

    public Sample getsample() {
        return this.sample;
    }

    public void setsample(Sample sample) {
        this.sample = sample;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reagent getReagentBySequencingReagentCode() {
        return this.reagentBySequencingReagentCode;
    }

    public void setReagentBySequencingReagentCode(
            Reagent reagentBySequencingReagentCode) {
        this.reagentBySequencingReagentCode = reagentBySequencingReagentCode;
    }

    public Reagent getReagentByClustergenerationReagentCode() {
        return this.reagentByClustergenerationReagentCode;
    }

    public void setReagentByClustergenerationReagentCode(
            Reagent reagentByClustergenerationReagentCode) {
        this.reagentByClustergenerationReagentCode = reagentByClustergenerationReagentCode;
    }

    public Reagent getReagentBySamplePrepReagentCode() {
        return this.reagentBySamplePrepReagentCode;
    }

    public void setReagentBySamplePrepReagentCode(
            Reagent reagentBySampleprepReagentCode) {
        this.reagentBySamplePrepReagentCode = reagentBySampleprepReagentCode;
    }

    public String getFlowcell() {
        return this.flowcell;
    }

    public void setFlowcell(String flowcell) {
        this.flowcell = flowcell;
    }

    /*public int a() {
     return this.lane;
     }

     public void setLane(int lane) {
     this.lane = lane;
     }*/
    public String getRunFolder() {
        return this.runFolder;
    }

    public void setRunFolder(String runfolder) {
        this.runFolder = runfolder;
    }

    public boolean getIsControl() {
        return this.isControl;
    }

    public void setControl(boolean iscontrol) {
        this.isControl = iscontrol;
    }

    public Set<RawData> getRawDatas() {
        return this.rawDatas;
    }

    public void setRawDatas(Set<RawData> rawdatas) {
        this.rawDatas = rawdatas;
    }

    /**
     * @return the lanes
     */
    public Set<Lane> getLanes() {
        return lanes;
    }

    /**
     * @param lanes the lanes to set
     */
    public void setLanes(Set<Lane> lanes) {
        this.lanes = lanes;
    }

}
