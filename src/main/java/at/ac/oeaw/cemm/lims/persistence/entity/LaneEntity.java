package at.ac.oeaw.cemm.lims.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author fvenco
 */
@Entity
@Table(name = "lane")
public class LaneEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lane_id")
    private Integer id;

    @Column(name = "lane_name")
    private String laneName;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "run_id", referencedColumnName = "run_id"),
        @JoinColumn(name = "sam_id", referencedColumnName = "sam_id")})
    private SampleRunEntity samplerun;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the laneName
     */
    public String getLaneName() {
        return laneName;
    }

    /**
     * @param laneName the laneName to set
     */
    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    /**
     * @return the samplerun
     */
    public SampleRunEntity getSamplerun() {
        return samplerun;
    }

    /**
     * @param samplerun the samplerun to set
     */
    public void setSamplerun(SampleRunEntity samplerun) {
        this.samplerun = samplerun;
    }

}
