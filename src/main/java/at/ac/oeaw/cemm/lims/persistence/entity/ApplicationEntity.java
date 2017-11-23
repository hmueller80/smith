package at.ac.oeaw.cemm.lims.persistence.entity;

import at.ac.oeaw.cemm.lims.api.dto.ApplicationDTO;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "application")
public class ApplicationEntity implements java.io.Serializable {

    public static final long serialVersionUID = 1L;
    
       
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "readlength")
    private Integer readLength;

    @Column(name = "readmode")
    private String readMode;

    @Column(name = "instrument")
    private String instrument;

    @Column(name = "applicationname")
    private String applicationName;

    @Column(name = "depth")
    private Integer depth;

    @OneToMany(mappedBy = "application")
//    @JoinColumn(name="application_id")
    private Set<SampleEntity> samples = new HashSet<SampleEntity>(0);

    public ApplicationEntity() {
    }

    public ApplicationEntity(Integer readlength, String readmode, String instrument,
            String applicationname, Integer depth, Set<SampleEntity> samples) {
        this.readLength = readlength;
        this.readMode = readmode;
        this.instrument = instrument;
        this.applicationName = applicationname;
        this.depth = depth;
        this.samples = samples;
    }
    
    public ApplicationEntity(String appName) {
        this(appName,null);
    }
    
    public ApplicationEntity(String appName,String instrument) {
        this.applicationName = appName ==null ? "undefined" : appName;
        this.instrument = instrument == null ? "HiSeq2000" : instrument;
        
        if (ApplicationDTO.CHIP_SEQ.equals(applicationName)) {
            //"Single read : 30 mio reads : one fiths of a lane : 50 bases"
            this.readMode = "SR";
            this.setDepth(30);
            this.setReadlength(50);
        } else if (ApplicationDTO.DNA_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 100 bases";
            this.readMode = "PE";
            this.setDepth(70);
            this.setReadlength(100);

        } else if (ApplicationDTO.EXO_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 100 bases";
            this.readMode = "PE";
            this.setDepth(70);
            this.setReadlength(100);

        } else if (ApplicationDTO.MRNA_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 50 bases";
            this.readMode = "PE";
            this.setDepth(70);
            this.setReadlength(50);

        } else if (ApplicationDTO.RNA_SEQ.equals(applicationName)) {
            //"Paired end : 35 mio reads : one fourth of a lane : 50 bases";
            this.readMode = "PE";
            this.setDepth(35);
            this.setReadlength(50);

        } else {
            //"Single read : 30 mio reads : one fiths of a lane : 50 bases";
            this.readMode = "SR";
            this.setDepth(30);
            this.setReadlength(50);
        }
    }

    public Integer getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getReadlength() {
        return this.readLength;
    }

    public void setReadlength(Integer readlength) {
        this.readLength = readlength;
    }

    public String getReadmode() {
        return this.readMode;
    }

    public void setReadmode(String readmode) {
        this.readMode = readmode;
    }

    public String getInstrument() {
        return this.instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getApplicationname() {
        return this.applicationName;
    }

    public void setApplicationname(String applicationname) {
        this.applicationName = applicationname;
    }

    public Integer getDepth() {
        return this.depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Set<SampleEntity> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<SampleEntity> samples) {
        this.samples = samples;
    }
    
    public boolean isMatching(ApplicationEntity app){        
        if(app.depth.intValue() == this.depth.intValue() && app.readMode.equals(this.readMode) && app.readLength.intValue() == this.readLength.intValue()){             
            return true;
        }else{            
            return false;
        }
    }
    
    public ApplicationEntity createCopy(){
       ApplicationEntity newApp = new ApplicationEntity();
       newApp.setApplicationname(applicationName!=null ? applicationName : "undefined");
       newApp.setReadlength(readLength);
       newApp.setDepth(depth);
       newApp.setInstrument(instrument!=null ? instrument: "HiSeq2000");
       newApp.setReadmode(readMode!=null ? readMode : "SR");
       
       return newApp;
    }

}
