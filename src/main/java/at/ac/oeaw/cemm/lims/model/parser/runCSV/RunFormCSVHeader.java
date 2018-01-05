/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.runCSV;

/**
 *
 * @author dbarreca
 */
public enum RunFormCSVHeader {
    /*00*/ Lane, 
    /*01*/ Sample,
    /*02*/ BSFID,
    /*03*/ Flowcell,
    /*04*/ Cluster,
    /*05*/ Sequencing,
    /*06*/ Date;
    
    public static char getSeparator(){
        return ',';
    }
    
    public static String getHeaderLine(){
        StringBuilder sb = new StringBuilder();
        sb.append(Lane).append(getSeparator());
        sb.append(Sample).append(getSeparator());
        sb.append(BSFID).append(getSeparator());
        sb.append(Flowcell).append(getSeparator());
        sb.append(Cluster).append(getSeparator());
        sb.append(Sequencing).append(getSeparator());
        sb.append(Date);
 
        return sb.toString();
    }
}