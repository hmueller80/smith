/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParserConstants;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParserUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class SequencingRequestSubmission implements Serializable {
    private String libraryName = "";
    private String sequencingType = "";
    private String readLength = "";
    private String numberofLanes = "";
    private String specialRequirements = "";
    private String additionalComment = "";
    private String receivingPerson = "";
    private Date receivingDate;
    private String receivingComment = "";
    private String qualityControlPerson = "";
    private Date qualityControlDate;
    private String qualityControlSummary = "";
    private String qualityControlFiles = "";
    private String qualityControlStatus = "";
    private Integer sequencingRequestSubmissionId;
    private String sequencer = "";

    public SequencingRequestSubmission(ArrayList<String> row, Map<String, Integer> header) {

        libraryName = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryName, row, header);
        sequencingType = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.sequencingType, row, header);
        readLength = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.readLength, row, header));
        numberofLanes = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.numberofLanes, row, header));
        specialRequirements = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.specialRequirements, row, header);
        additionalComment = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.AdditionalComment, row, header);
        receivingPerson = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.receivingPerson, row, header);
        receivingDate = new Date(System.currentTimeMillis());
        receivingComment = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.receivingComment, row, header);
        qualityControlPerson = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.qualityControlPerson, row, header);
        qualityControlDate = new Date(System.currentTimeMillis());
        qualityControlSummary = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.qualityControlSummary, row, header);
        qualityControlFiles = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.qualityControlFiles, row, header);
        qualityControlStatus = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.qualityControlStatus, row, header);
        sequencer = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.sequencer, row, header);
    }
    
    public String getLibraryName() {
        return libraryName;
    }

    public String getSequencingType() {
        return sequencingType;
    }

    public String getReadLength() {
        return readLength;
    }

    public String getNumberofLanes() {
        return numberofLanes;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public String getReceivingPerson() {
        return receivingPerson;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public String getReceivingComment() {
        return receivingComment;
    }

    public String getQualityControlPerson() {
        return qualityControlPerson;
    }

    public Date getQualityControlDate() {
        return qualityControlDate;
    }

    public String getQualityControlSummary() {
        return qualityControlSummary;
    }

    public String getQualityControlFiles() {
        return qualityControlFiles;
    }

    public String getQualityControlStatus() {
        return qualityControlStatus;
    }

    public Integer getSequencingRequestSubmissionId() {
        return sequencingRequestSubmissionId;
    }

    public String getSequencer() {
        return sequencer;
    }

    @Override
    public String toString() {
        return "SequencingRequestSubmission{" + "\n\tlibraryName=" + libraryName + ",\n\t sequencingType=" + sequencingType + ",\n\t readLength=" + readLength + ",\n\t numberofLanes=" + numberofLanes + ",\n\t specialRequirements=" + specialRequirements + ",\n\t additionalComment=" + additionalComment + ",\n\t receivingPerson=" + receivingPerson + ",\n\t receivingDate=" + receivingDate + ",\n\t receivingComment=" + receivingComment + ",\n\t qualityControlPerson=" + qualityControlPerson + ",\n\t qualityControlDate=" + qualityControlDate + ",\n\t qualityControlSummary=" + qualityControlSummary + ",\n\t qualityControlFiles=" + qualityControlFiles + ",\n\t qualityControlStatus=" + qualityControlStatus + ",\n\t sequencingRequestSubmissionId=" + sequencingRequestSubmissionId + ",\n\t sequencer=" + sequencer + '}';
    }
    
    
}
