package it.iit.genomics.cru.smith.entity;

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
public class Application implements java.io.Serializable {

    public static final long serialVersionUID = 1L;
    public static final String CHIP_SEQ = "ChIP-Seq";
    public static final String DNA_SEQ = "DNA-Seq";
    public static final String EXO_SEQ = "ExomeSeq";
    public static final String MRNA_SEQ = "mRNA-Seq";
    public static final String RNA_SEQ = "RNA-Seq";
       
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
    private Set<Sample> samples = new HashSet<Sample>(0);

    public Application() {
    }

    public Application(Integer readlength, String readmode, String instrument,
            String applicationname, Integer depth, Set<Sample> samples) {
        this.readLength = readlength;
        this.readMode = readmode;
        this.instrument = instrument;
        this.applicationName = applicationname;
        this.depth = depth;
        this.samples = samples;
    }
    
    public Application(String appName) {
        this(appName,null);
    }
    
    public Application(String appName,String instrument) {
        this.applicationName = appName ==null ? "undefined" : appName;
        this.instrument = instrument == null ? "HiSeq2000" : instrument;
        
        if (CHIP_SEQ.equals(applicationName)) {
            //"Single read : 30 mio reads : one fiths of a lane : 50 bases"
            this.readMode = "SR";
            this.setDepth(30);
            this.setReadlength(50);
        } else if (DNA_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 100 bases";
            this.readMode = "PE";
            this.setDepth(70);
            this.setReadlength(100);

        } else if (EXO_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 100 bases";
            this.readMode = "PE";
            this.setDepth(70);
            this.setReadlength(100);

        } else if (MRNA_SEQ.equals(applicationName)) {
            //"Paired end : 70 mio reads : half a lane : 50 bases";
            this.readMode = "PE";
            this.setDepth(70);
            this.setReadlength(50);

        } else if (RNA_SEQ.equals(applicationName)) {
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

    public Set<Sample> getSamples() {
        return this.samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }
    
    public boolean isMatching(Application app){        
        if(app.depth.intValue() == this.depth.intValue() && app.readMode.equals(this.readMode) && app.readLength.intValue() == this.readLength.intValue()){             
            return true;
        }else{            
            return false;
        }
    }
    
    public Application createCopy(){
       Application newApp = new Application();
       newApp.setApplicationname(applicationName!=null ? applicationName : "undefined");
       newApp.setReadlength(readLength);
       newApp.setDepth(depth);
       newApp.setInstrument(instrument!=null ? instrument: "HiSeq2000");
       newApp.setReadmode(readMode!=null ? readMode : "SR");
       
       return newApp;
    }

}
