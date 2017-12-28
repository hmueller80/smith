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
import java.util.Map;

/**
 *
 * @author hMueller
 */
public class SampleSubmission implements Serializable {
    private String sampleName = "undefined";
    private String sampleDescription = "";
    private String organism = "";
    private String sex = "";
    private String age = "";
    private String tissue = "";
    private String cellType = "";
    private String genotype = "";
    private String familyRelations = "";
    private String phenotype = "";
    private String disease = "";
    private String materialType = "";
    private String source = "";
    private String acquisitionDate = "";
    private String sampleGroup = "";
    private String originalSampleID = "";
    private Integer sampleSubmissionId;

    
    public SampleSubmission(ArrayList<String> row, Map<ColumnNames, Integer> header) {

        sampleName = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.SampleName,row,header);
        sampleDescription = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.SampleDescription, row, header);
        organism = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Organism,row,header);
        sex = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Sex,row,header);
        age = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Age,row,header);
        cellType = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.BioCellType,row,header);
        genotype = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Genotype,row,header);
        familyRelations = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.FamilyRelations,row,header);
        phenotype = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Phenotype,row,header);
        disease = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Disease,row,header);
        materialType = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.MaterialType,row,header);
        source = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.Source,row,header);
        acquisitionDate = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.AcquisitionDate,row,header);
        sampleGroup = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.SampleGroup,row,header);
        originalSampleID = ExcelParserUtils.extractFieldAsString(ExcelParserConstants.OriginalSampleID,row,header);
    }
    
    

    public String getSampleName() {
        return sampleName;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public String getOrganism() {
        return organism;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getTissue() {
        return tissue;
    }

    public String getCellType() {
        return cellType;
    }

    public String getGenotype() {
        return genotype;
    }

    public String getFamilyRelations() {
        return familyRelations;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public String getDisease() {
        return disease;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getSource() {
        return source;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public String getSampleGroup() {
        return sampleGroup;
    }

    public String getOriginalSampleID() {
        return originalSampleID;
    }

    public Integer getSampleSubmissionId() {
        return sampleSubmissionId;
    }

    @Override
    public String toString() {
        return "SampleSubmission{" + "\n\tsampleName=" + sampleName + ",\n\t sampleDescription=" + sampleDescription + ",\n\t organism=" + organism + ",\n\t sex=" + sex + ",\n\t age=" + age + ",\n\t tissue=" + tissue + ",\n\t cellType=" + cellType + ",\n\t genotype=" + genotype + ",\n\t familyRelations=" + familyRelations + ",\n\t phenotype=" + phenotype + ",\n\t disease=" + disease + ",\n\t materialType=" + materialType + ",\n\t source=" + source + ",\n\t acquisitionDate=" + acquisitionDate + ",\n\t sampleGroup=" + sampleGroup + ",\n\t originalSampleID=" + originalSampleID + ",\n\t sampleSubmissionId=" + sampleSubmissionId + '}';
    }
    
    
}
