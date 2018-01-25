/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.view.run;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author dbarreca
 */
public class RunFolder {
    private DateFormat outputDF = new SimpleDateFormat("dd.MM.yyyy");
    
    private final String flowCell;
    private final String runFolderName;
    private final String experimentName;
    private final Date runDate;

    public RunFolder(String flowCell, String runFolderName, String experimentName, Date runDate) {
        this.flowCell = flowCell;
        this.runFolderName = runFolderName;
        this.experimentName = experimentName;
        this.runDate = runDate;
    }

    
    
    public String getDescription(){
        return experimentName+"-"+flowCell+" run on: "+outputDF.format(runDate);
    }

    
    public String getFlowCell() {
        return flowCell;
    }

    public Date getRunDate() {
        return runDate;
    }

    public String getRunFolderName() {
        return runFolderName;
    }

    public String getExperimentName() {
        return experimentName;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.flowCell);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RunFolder other = (RunFolder) obj;
        if (!Objects.equals(this.flowCell, other.flowCell)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
