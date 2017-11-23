package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "samplerun")
public class SampleRunEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private SampleRunIdEntity id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sam_id", insertable = false, updatable = false)
    private SampleEntity sample;

    @ManyToOne
    @JoinColumn(name = "operator_user_id")
    private UserEntity user;

    @Column(name = "flowcell")
    private String flowcell;

	//@Column(name = "lane")
    //private int lane;
    @Column(name = "runfolder")
    private String runFolder;

    @Column(name = "iscontrol")
    private boolean isControl;

   
    @OneToMany(mappedBy = "samplerun", fetch = FetchType.EAGER)
    private Set<LaneEntity> lanes = new HashSet<LaneEntity>(0);

    public SampleRunEntity() {
    }

    public SampleRunEntity(int id) {
        this.id = new SampleRunIdEntity(id, 0);

        this.sample = sample;
        this.user = user;
    
        this.flowcell = "";
        //this.lane = 0;
        this.runFolder = "";
        this.isControl = false;
    }

    public SampleRunEntity(SampleRunIdEntity id, SampleEntity sample, UserEntity user,
            String runfolder, boolean iscontrol) {
        this.id = id;
        this.sample = sample;
        this.user = user;
        this.flowcell = flowcell;
        //this.lane = lane;
        this.runFolder = runfolder;
        this.isControl = iscontrol;
    }

    public SampleRunEntity(int rid, int sid) {
        this.id = new SampleRunIdEntity(rid, sid);

    }

    public String getAllLanesToString1() {
        String toRet = "";
        for (LaneEntity l : lanes) {
            toRet = toRet + " " + l.getLaneName();
        }
        return toRet;
    }
    
    
    public String getAllLanesToString(){
             
        StringBuilder result = new StringBuilder();      
        Set<LaneEntity> ls = this.getLanes();
        String toRet = "";
            for (LaneEntity l : ls) {
                toRet = toRet + " " + l.getLaneName();
                result.append(l.getLaneName() + " ");
            }
        return result.toString().trim();
      
    }

    public SampleRunIdEntity getId() {
        return this.id;
    }

    public void setId(SampleRunIdEntity id) {
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

    public SampleEntity getsample() {
        return this.sample;
    }

    public void setsample(SampleEntity sample) {
        this.sample = sample;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    public String getFlowcell() {
        return this.flowcell;
    }

    public void setFlowcell(String flowcell) {
        this.flowcell = flowcell;
    }

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

    /**
     * @return the lanes
     */
    public Set<LaneEntity> getLanes() {
        return lanes;
    }

    /**
     * @param lanes the lanes to set
     */
    public void setLanes(Set<LaneEntity> lanes) {
        this.lanes = lanes;
    }

}
