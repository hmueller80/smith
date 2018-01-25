/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.analysis;

import java.io.File;
import java.util.Date;

/**
 *
 * @author dbarreca
 */
public interface RunParameters {
    public boolean isValidRunFolder(File runFolder);
    public String getExperimentName(File runFolder);
    public String getFlowcellName(File runFolder);
    public Date getRunDate(File runFolder);
}
