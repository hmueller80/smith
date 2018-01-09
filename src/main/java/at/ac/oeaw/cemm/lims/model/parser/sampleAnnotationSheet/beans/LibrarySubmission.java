/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.beans;

import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ColumnNames;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParserConstants;
import at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.ExcelParserUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author hMueller
 */
public class LibrarySubmission implements Serializable {
    private String libraryName = "";
    private String libraryLabel = "";
    private String sampleName = "";
    private String barcodeSequencei7 = "";
    private String libraryAdapteri7 = "";
    private String barcodeSequencei5 = "";
    private String libraryAdapteri5 = "";
    private String sequencingPrimerType = "";
    private String customSequencingPrimerName = "";
    private String customSequencingPrimerSequence = "";
    private String libraryType = "";
    private String libraryKits = "";
    private String libraryDetails = "";
    private String libraryPerson = "";
    private Date libraryDate;
    private String libraryVolume = "";
    private String libraryDNAConcentration = "";
    private String libraryTotalSize = "";
    private String libraryInsertSize = "";
    private String libraryComment = "";
    private String additionalComment = "";
    private String bioinformaticsProtocol = "";
    private String bioinformaticsGenome = "";
    private String bioinformaticsGermlineControl = "";
    private String bioinformaticsComment = "";
    private String libraryDNAAmount = "";
    private Integer librarySubmissionId;

    public LibrarySubmission(ArrayList<String> row, Map<ColumnNames, Integer> header) {

        libraryName = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryName,row,header);
        libraryLabel = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryLabel,row,header);
        sampleName = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.SampleName,row,header);
        barcodeSequencei7 = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BarcodeSequencei7,row,header);
        libraryAdapteri7 = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryAdapteri7,row,header);
        barcodeSequencei5 = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BarcodeSequencei5,row,header);
        libraryAdapteri5 = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryAdapteri5,row,header);
        sequencingPrimerType = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.SequencingPrimerType,row,header);
        customSequencingPrimerName = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.CustomSequencingPrimerName,row,header);
        customSequencingPrimerSequence = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.CustomSequencingPrimerSequence,row,header);
        libraryType = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryType,row,header);
        libraryKits = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryKits,row,header);
        libraryDetails = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryDetails,row,header);
        libraryPerson = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryPerson,row,header);
        libraryDate = new Date(System.currentTimeMillis());
        libraryVolume = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryVolume,row,header));
        libraryDNAConcentration = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryDNAConcentration,row,header));
        libraryTotalSize = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryTotalSize,row,header));
        libraryInsertSize = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryInsertSize,row,header));
        libraryComment = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryComment,row,header);
        additionalComment = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.AdditionalComment,row,header);
        bioinformaticsProtocol = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BioinformaticsProtocol,row,header);
        bioinformaticsGenome = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BioinformaticsGenome,row,header);
        bioinformaticsGermlineControl = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BioinformaticsGermlineControl,row,header);
        bioinformaticsComment = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BioinformaticsComment,row,header);
        libraryDNAAmount = ExcelParserUtils.removeUnits(ExcelParserUtils.extractFieldAsString(ExcelParserConstants.LibraryDNAAmount,row,header));
              
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibraryLabel() {
        return libraryLabel;
    }

    public String getSampleName() {
        return sampleName;
    }

    public String getBarcodeSequencei7() {
        return barcodeSequencei7;
    }

    public String getLibraryAdapteri7() {
        return libraryAdapteri7;
    }

    public String getBarcodeSequencei5() {
        return barcodeSequencei5;
    }

    public String getLibraryAdapteri5() {
        return libraryAdapteri5;
    }

    public String getSequencingPrimerType() {
        return sequencingPrimerType;
    }

    public String getCustomSequencingPrimerName() {
        return customSequencingPrimerName;
    }

    public String getCustomSequencingPrimerSequence() {
        return customSequencingPrimerSequence;
    }

    public String getLibraryType() {
        return libraryType;
    }

    public String getLibraryKits() {
        return libraryKits;
    }

    public String getLibraryDetails() {
        return libraryDetails;
    }

    public String getLibraryPerson() {
        return libraryPerson;
    }

    public Date getLibraryDate() {
        return libraryDate;
    }

    public String getLibraryVolume() {
        return libraryVolume;
    }

    public String getLibraryDNAConcentration() {
        return libraryDNAConcentration;
    }

    public String getLibraryTotalSize() {
        return libraryTotalSize;
    }

    public String getLibraryInsertSize() {
        return libraryInsertSize;
    }

    public String getLibraryComment() {
        return libraryComment;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public String getBioinformaticsProtocol() {
        return bioinformaticsProtocol;
    }

    public String getBioinformaticsGenome() {
        return bioinformaticsGenome;
    }

    public String getBioinformaticsGermlineControl() {
        return bioinformaticsGermlineControl;
    }

    public String getBioinformaticsComment() {
        return bioinformaticsComment;
    }

    public String getLibraryDNAAmount() {
        return libraryDNAAmount;
    }

    public Integer getLibrarySubmissionId() {
        return librarySubmissionId;
    }

    @Override
    public String toString() {
        return "LibrarySubmission{" + "\n\tlibraryName=" + libraryName + ",\n\t libraryLabel=" + libraryLabel + ",\n\t sampleName=" + sampleName + ",\n\t barcodeSequencei7=" + barcodeSequencei7 + ",\n\t libraryAdapteri7=" + libraryAdapteri7 + ",\n\t barcodeSequencei5=" + barcodeSequencei5 + ",\n\t libraryAdapteri5=" + libraryAdapteri5 + ",\n\t sequencingPrimerType=" + sequencingPrimerType + ",\n\t customSequencingPrimerName=" + customSequencingPrimerName + ",\n\t customSequencingPrimerSequence=" + customSequencingPrimerSequence + ",\n\t libraryType=" + libraryType + ",\n\t libraryKits=" + libraryKits + ",\n\t libraryDetails=" + libraryDetails + ",\n\t libraryPerson=" + libraryPerson + ",\n\t libraryDate=" + libraryDate + ",\n\t libraryVolume=" + libraryVolume + ",\n\t libraryDNAConcentration=" + libraryDNAConcentration + ",\n\t libraryTotalSize=" + libraryTotalSize + ",\n\t libraryInsertSize=" + libraryInsertSize + ",\n\t libraryComment=" + libraryComment + ",\n\t additionalComment=" + additionalComment + ",\n\t bioinformaticsProtocol=" + bioinformaticsProtocol + ",\n\t bioinformaticsGenome=" + bioinformaticsGenome + ",\n\t bioinformaticsGermlineControl=" + bioinformaticsGermlineControl + ",\n\t bioinformaticsComment=" + bioinformaticsComment + ",\n\t libraryDNAAmount=" + libraryDNAAmount + ",\n\t librarySubmissionId=" + librarySubmissionId + '}';
    }
    
    
}
