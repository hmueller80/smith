/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl.model;

import at.ac.oeaw.cemm.lims.analysis.impl.illuminaxml.XMLParser;
import at.ac.oeaw.cemm.lims.api.analysis.RunParameters;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author dbarreca
 */
@ApplicationScoped
public class IlluminaRunParameters implements RunParameters{

    @Override
    public String getExperimentName(File runFolder) {
   
        File runParametersXML = new File(runFolder, "runParameters.xml");
        if (!runParametersXML.exists()) {
            return "";
        }

       return (new XMLParser()).getExperimentName(runParametersXML.getAbsolutePath());
    }

    @Override
    public String getFlowcellName(File runFolder) {
        String runFolderName = runFolder.getName();
        
        String[] splittedName = runFolderName.split("_");
        String flowCellName = splittedName [splittedName.length - 1];
        
        boolean hasSlot = flowCellName.matches("^(A|B).*");

        return hasSlot ? flowCellName.substring(1) : flowCellName;   
      
    }

    @Override
    public Date getRunDate(File runFolder) {
        DateFormat df = new SimpleDateFormat("yyMMdd");
        String runFolderName = runFolder.getName();
        
        String[] splittedName = runFolderName.split("_");
        
          try {
            return df.parse(splittedName[0]);
        } catch (ParseException ex) {
            return new Date();
        }
        
    }

    @Override
    public boolean isValidRunFolder(File runFolder) {
        return runFolder.isDirectory() 
                    && runFolder.canExecute()
                    && runFolder.getName().length()>=6
                    && runFolder.getName().substring(0,6).matches("[0-9]+")
                    && Arrays.asList(runFolder.list()).contains("RunInfo.xml")
                    && Arrays.asList(runFolder.list()).contains("runParameters.xml");
    }
    
}
