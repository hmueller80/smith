/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hMueller
 */
public class ExcelParserConstants {
    
    //sheet headers
    public static final String summary                              = "Summary";
    public static final String samples                              = "Samples";
    public static final String libraries                            = "Libraries";
    public static final String requests                             = "Sequencing Request";
    public static final Set<String> sheetsToParse = new HashSet<>(Arrays.asList(summary, samples, libraries, requests));
    
    //Help Comments key
    public static final String commentsOnForm                       = "Comments on how to fill out each column of this form";
 
    
    //Summary keys
    public static final String SubmissionName                       = "Submission Name";
    public static final String SubmissionTitle                      = "Submission Title";
    public static final String SubmissionContext                    = "Submission Context";
    public static final String SubmissionDescription                = "Submission Description";
    public static final String SubmissionComment                    = "Submission Comment";
    public static final String SubmissionDate                       = "Submission Date";
    public static final String SubmissionTemplate                   = "Submission Template";

    public static final String RelatedPublications                  = "Related Publications";
    public static final String RelatedDatasets                      = "Related Datasets";

    public static final String ContactPersonName                    = "Contact Person Name";
    public static final String ContactPersonEmail                   = "Contact Person Email";
    public static final String ContactPersonPhone                   = "Contact Person Phone";

    public static final String SubmittingOrganizationDepartment     = "Submitting Organization Department";
    public static final String SubmittingOrganizationName           = "Submitting Organization Name";
    public static final String SubmittingOrganizationAddress        = "Submitting Organization Address";
    public static final String SubmittingOrganizationURL            = "Submitting Organization URL";
    public static final String SubmittingOrganizationUID            = "Submitting Organization UID";

    public static final String BillingContact                       = "Billing Contact";
    public static final String BillingAddress                       = "Billing Address";
    public static final String BillingCode                          = "Billing Code";

    public static final String LabHeadContact                       = "Lab Head Contact";
    public static final String LabAdministrativeContact             = "Lab Administrative Contact";
    public static final String LabExperimentalContact               = "Lab Experimental Contact";
    public static final String LabBioinformaticsContact             = "Lab Bioinformatics Contact";
    public static final String LabURL                               = "Lab URL";
    
    //Samples keys    
    public static final String Order                                = "Order";
    public static final String SampleName                           = "Sample Name";	
    public static final String SampleDescription                    = "Sample Description";	
    public static final String Organism                             = "Organism";	
    public static final String Sex                                  = "Sex";	
    public static final String Age                                  = "Age";	
    public static final String Tissue                               = "Tissue";	
    public static final String BioCellType                             = "Cell Type";	
    public static final String Genotype                             = "Genotype";	
    public static final String FamilyRelations                      = "Family Relations";	
    public static final String Phenotype                            = "Phenotype";	
    public static final String Disease                              = "Disease";	
    public static final String MaterialType                         = "Material Type";	
    public static final String Source                               = "Source";
    public static final String AcquisitionDate                      = "Acquisition Date";	
    public static final String SampleGroup                          = "Sample Group (custom column)";	
    public static final String OriginalSampleID                     = "Original Sample ID (custom column)";
    
    
    //Library keys    
    public static final String LibraryName                          ="Library Name";
    public static final String LibraryLabel                         ="Library Label";

    public static final String BarcodeSequencei7                    = "Barcode Sequence i7"; 
    public static final String BarcodeSequence                      = "Barcode Sequence";
    public static final String BarcodeSequence1                     = "Barcode Sequence 1";
    public static final String BarcodeSequenceForward               = "Barcode Sequence Forward";
    
    public static final String LibraryAdapteri7                     = "Library Adapter i7";
    public static final String LibraryAdapter                       = "Library Adapter";
    public static final String LibraryAdapter1                      = "Library Adapter 1";
    
    public static final String BarcodeSequencei5                    = "Barcode Sequence i5";
    public static final String BarcodeSequence2                     = "Barcode Sequence 2 (Dual Index)";
    public static final String BarcodeSequenceReverse               = "Barcode Sequence Reverse";
    
    public static final String LibraryAdapteri5                     = "Library Adapter i5";
    public static final String LibraryAdapter2                      = "Library Adapter 2 (Dual Index)";
    
    public static final String SequencingPrimerType                 = "Sequencing Primer Type";
    public static final String SequencingPrimer                     = "Sequencing Primer";
    
    
    public static final String CustomSequencingPrimerName           = "Custom Sequencing Primer Name";
    public static final String CustomSequencingPrimerNameold        = "Name of Custom Sequencing Primer";
    
    public static final String CustomSequencingPrimerSequence       = "Custom Sequencing Primer Sequence";
    public static final String LibraryType                          = "Library Type";
    public static final String LibraryKits                          = "Library Kits";
    public static final String LibraryDetails                       = "Library Details";
    public static final String LibraryPerson                        = "Library Person";
    public static final String LibraryDate                          = "Library Date";
    public static final String LibraryVolume                        = "Library Volume";
    public static final String LibraryDNAConcentration              = "Library DNA Concentration";
    
    public static final String LibraryTotalSize                     = "Library Total Size";
    public static final String LibraryTotalSizeold                  = "Library Size";
    
    public static final String LibraryInsertSize                    = "Library Insert Size";
    public static final String LibraryComment                       = "Library Comment";
    public static final String AdditionalComment                    = "Additional Comment";
    public static final String BioinformaticsProtocol               = "Bioinformatics Protocol";
    public static final String BioinformaticsGenome                 = "Bioinformatics Genome";
    public static final String BioinformaticsGermlineControl        = "Bioinformatics Germline Control";
    public static final String BioinformaticsComment                = "Bioinformatics Comment";
    
    public static final String LibraryDNAAmount                     = "Library DNA Amount";
    public static final String LibraryDNAAmountng                   = "Library DNA Amount [ng]";

    
    //request keys
    public static final String libraryName ="Library Name";
    public static final String sequencingType ="Sequencing Type";
    public static final String readLength ="Read Length";
    
    public static final String numberofLanes ="Number of Lanes";
    public static final String numberofHiSeqLanes ="Number of HiSeq Lanes ";
    
    //public static final String phiXControlLaneRequired ="PhiX Control Lane Required";
    public static final String specialRequirements ="Special Requirements";
    public static final String receivingPerson ="Receiving Person";
    public static final String receivingDate ="Receiving Date";
    public static final String receivingComment ="Receiving Comment";
    public static final String qualityControlPerson ="Quality Control Person";
    public static final String qualityControlDate ="Quality Control Date";
    public static final String qualityControlSummary ="Quality Control Summary";
    public static final String qualityControlFiles ="Quality Control Files";
    public static final String qualityControlStatus ="Quality Control Status";
    public static final String sequencer ="Sequencer";
    

    public static ArrayList<ColumnNames> summaryColumnNames;
    public static ArrayList<ColumnNames> sampleColumnNames;
    public static ArrayList<ColumnNames> libraryColumnNames;
    public static ArrayList<ColumnNames> requestColumnNames;

    
    static{
        
        //summary column names
        summaryColumnNames = new ArrayList<ColumnNames>();
        summaryColumnNames.add(new ColumnNames(SubmissionName, SubmissionName));
        summaryColumnNames.add(new ColumnNames(SubmissionTitle, SubmissionTitle));
        summaryColumnNames.add(new ColumnNames(SubmissionContext, SubmissionContext));
        summaryColumnNames.add(new ColumnNames(SubmissionDescription, SubmissionDescription));
        summaryColumnNames.add(new ColumnNames(SubmissionComment, SubmissionComment));
        summaryColumnNames.add(new ColumnNames(SubmissionDate, SubmissionDate));
        summaryColumnNames.add(new ColumnNames(SubmissionTemplate, SubmissionTemplate));
        summaryColumnNames.add(new ColumnNames(RelatedPublications, RelatedPublications));
        summaryColumnNames.add(new ColumnNames(RelatedDatasets, RelatedDatasets));
        summaryColumnNames.add(new ColumnNames(SubmittingOrganizationDepartment, SubmittingOrganizationDepartment));
        summaryColumnNames.add(new ColumnNames(SubmittingOrganizationName, SubmittingOrganizationName));
        summaryColumnNames.add(new ColumnNames(SubmittingOrganizationAddress, SubmittingOrganizationAddress));
        summaryColumnNames.add(new ColumnNames(SubmittingOrganizationURL, SubmittingOrganizationURL));
        summaryColumnNames.add(new ColumnNames(SubmittingOrganizationUID, SubmittingOrganizationUID));
        summaryColumnNames.add(new ColumnNames(ContactPersonName, ContactPersonName));
        summaryColumnNames.add(new ColumnNames(ContactPersonEmail,true, ContactPersonEmail));
        summaryColumnNames.add(new ColumnNames(ContactPersonPhone, ContactPersonPhone));
        summaryColumnNames.add(new ColumnNames(LabURL, LabURL));
        summaryColumnNames.add(new ColumnNames(LabHeadContact, LabHeadContact));
        summaryColumnNames.add(new ColumnNames(LabAdministrativeContact, LabAdministrativeContact));
        summaryColumnNames.add(new ColumnNames(LabExperimentalContact, LabExperimentalContact));
        summaryColumnNames.add(new ColumnNames(LabBioinformaticsContact, LabBioinformaticsContact));
        summaryColumnNames.add(new ColumnNames(BillingContact, BillingContact));
        summaryColumnNames.add(new ColumnNames(BillingAddress, BillingAddress));
        summaryColumnNames.add(new ColumnNames(BillingCode, BillingCode));
        
        //samples column names
        sampleColumnNames = new ArrayList<ColumnNames>();
        sampleColumnNames.add(new ColumnNames(Order,true,ColumnNames.DataType.INTEGER, Order));
        sampleColumnNames.add(new ColumnNames(SampleName,true, SampleName));
        sampleColumnNames.add(new ColumnNames(SampleDescription,true, SampleDescription));
        sampleColumnNames.add(new ColumnNames(Organism,true, Organism)); 
        sampleColumnNames.add(new ColumnNames(Sex, Sex));
        sampleColumnNames.add(new ColumnNames(Age, Age));
        sampleColumnNames.add(new ColumnNames(Tissue, Tissue));
        sampleColumnNames.add(new ColumnNames(BioCellType, BioCellType));
        sampleColumnNames.add(new ColumnNames(Genotype, Genotype));
        sampleColumnNames.add(new ColumnNames(FamilyRelations, FamilyRelations));
        sampleColumnNames.add(new ColumnNames(Phenotype, Phenotype));
        sampleColumnNames.add(new ColumnNames(Disease, Disease));
        sampleColumnNames.add(new ColumnNames(MaterialType, MaterialType));
        sampleColumnNames.add(new ColumnNames(Source, Source));
        sampleColumnNames.add(new ColumnNames(AcquisitionDate,false,ColumnNames.DataType.DATE, AcquisitionDate));
        sampleColumnNames.add(new ColumnNames(SampleGroup, SampleGroup));
        sampleColumnNames.add(new ColumnNames(OriginalSampleID, OriginalSampleID));
        
        //library column names
        libraryColumnNames = new ArrayList<ColumnNames>();
        libraryColumnNames.add(new ColumnNames(Order,true,ColumnNames.DataType.INTEGER, Order));
        libraryColumnNames.add(new ColumnNames(LibraryName,true, LibraryName));
        libraryColumnNames.add(new ColumnNames(LibraryLabel, LibraryLabel));
        libraryColumnNames.add(new ColumnNames(SampleName,true, SampleName));
        libraryColumnNames.add(new ColumnNames(BarcodeSequencei7,true, BarcodeSequence1, BarcodeSequencei7, BarcodeSequence, BarcodeSequenceForward, "N7 Barcode Sequence", "Sequences Index primer 1 (i7)"));
        libraryColumnNames.add(new ColumnNames(LibraryAdapteri7,true, LibraryAdapter1, LibraryAdapteri7, LibraryAdapter, "N7 Library Adapter"));
        libraryColumnNames.add(new ColumnNames(BarcodeSequencei5, BarcodeSequence2, BarcodeSequencei5, BarcodeSequenceReverse, "N5 Barcode Sequence", "Sequences Index primer 2 (i5)"));
        libraryColumnNames.add(new ColumnNames(LibraryAdapteri5, LibraryAdapter2, LibraryAdapteri5, "N5 Library Adapter"));
        libraryColumnNames.add(new ColumnNames(SequencingPrimerType, SequencingPrimer, SequencingPrimerType));
        libraryColumnNames.add(new ColumnNames(CustomSequencingPrimerName, CustomSequencingPrimerNameold, CustomSequencingPrimerName));
        libraryColumnNames.add(new ColumnNames(CustomSequencingPrimerSequence, CustomSequencingPrimerSequence));
        libraryColumnNames.add(new ColumnNames(LibraryType, LibraryType));
        libraryColumnNames.add(new ColumnNames(LibraryKits, LibraryKits));
        libraryColumnNames.add(new ColumnNames(LibraryDetails, LibraryDetails));
        libraryColumnNames.add(new ColumnNames(LibraryPerson, LibraryPerson));
        libraryColumnNames.add(new ColumnNames(LibraryDate,false,ColumnNames.DataType.DATE,LibraryDate));
        libraryColumnNames.add(new ColumnNames(LibraryVolume,false,ColumnNames.DataType.DOUBLE, LibraryVolume));
        libraryColumnNames.add(new ColumnNames(LibraryDNAConcentration,false,ColumnNames.DataType.DOUBLE, LibraryDNAConcentration));
        libraryColumnNames.add(new ColumnNames(LibraryTotalSize,false,ColumnNames.DataType.DOUBLE, LibraryTotalSize, LibraryTotalSizeold));
        libraryColumnNames.add(new ColumnNames(LibraryInsertSize,false,ColumnNames.DataType.DOUBLE, LibraryInsertSize));
        libraryColumnNames.add(new ColumnNames(LibraryComment, LibraryComment));
        libraryColumnNames.add(new ColumnNames(AdditionalComment, AdditionalComment));
        libraryColumnNames.add(new ColumnNames(BioinformaticsProtocol, BioinformaticsProtocol));
        libraryColumnNames.add(new ColumnNames(BioinformaticsGenome, BioinformaticsGenome));
        libraryColumnNames.add(new ColumnNames(BioinformaticsGermlineControl, BioinformaticsGermlineControl));
        libraryColumnNames.add(new ColumnNames(BioinformaticsComment, BioinformaticsComment));
        libraryColumnNames.add(new ColumnNames(LibraryDNAAmount,false,ColumnNames.DataType.DOUBLE, LibraryDNAAmount, LibraryDNAAmountng));
        
        //request column names
        requestColumnNames = new ArrayList<ColumnNames>();
        requestColumnNames.add(new ColumnNames(Order,true,ColumnNames.DataType.INTEGER, Order));
        requestColumnNames.add(new ColumnNames(libraryName,true, libraryName));
        requestColumnNames.add(new ColumnNames(sequencingType,true, sequencingType));
        requestColumnNames.add(new ColumnNames(readLength,true,ColumnNames.DataType.INTEGER, readLength));
        requestColumnNames.add(new ColumnNames(sequencer, sequencer));
        requestColumnNames.add(new ColumnNames(numberofLanes,true,ColumnNames.DataType.INTEGER, numberofLanes, numberofHiSeqLanes));
        requestColumnNames.add(new ColumnNames(specialRequirements, specialRequirements));
        requestColumnNames.add(new ColumnNames(AdditionalComment, AdditionalComment));
        requestColumnNames.add(new ColumnNames(receivingPerson, receivingPerson));
        requestColumnNames.add(new ColumnNames(receivingDate,false,ColumnNames.DataType.DATE, receivingDate));
        requestColumnNames.add(new ColumnNames(receivingComment, receivingComment));
        requestColumnNames.add(new ColumnNames(qualityControlPerson, qualityControlPerson));
        requestColumnNames.add(new ColumnNames(qualityControlDate,false,ColumnNames.DataType.DATE, qualityControlDate));
        requestColumnNames.add(new ColumnNames(qualityControlSummary, qualityControlSummary));
        requestColumnNames.add(new ColumnNames(qualityControlFiles, qualityControlFiles));
        requestColumnNames.add(new ColumnNames(qualityControlStatus, qualityControlStatus));
        
    }
}
