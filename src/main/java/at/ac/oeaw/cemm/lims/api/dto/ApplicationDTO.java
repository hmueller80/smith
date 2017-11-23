/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.api.dto;

/**
 *
 * @author dbarreca
 */
public interface ApplicationDTO {
    public static final String CHIP_SEQ = "ChIP-Seq";
    public static final String DNA_SEQ = "DNA-Seq";
    public static final String EXO_SEQ = "ExomeSeq";
    public static final String MRNA_SEQ = "mRNA-Seq";
    public static final String RNA_SEQ = "RNA-Seq";
    
    String getApplicationName();

    Integer getDepth();

    String getInstrument();

    Integer getReadLength();

    String getReadMode();
    
    ApplicationDTO getCopy();
    
    void setReadLength(Integer readLength);

    public void setReadMode(String readMode);

    public void setInstrument(String instrument);

    public void setApplicationName(String applicationName);

    public void setDepth(Integer depth);
    
}
