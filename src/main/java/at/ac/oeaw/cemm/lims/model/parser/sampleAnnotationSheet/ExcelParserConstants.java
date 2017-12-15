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
    public static final String CellType                             = "Cell Type";	
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
    //public static final String BarcodeSequenceForward               = "Barcode Sequence Forward";
    
    public static final String LibraryAdapteri7                     = "Library Adapter i7";
    public static final String LibraryAdapter                       = "Library Adapter";
    public static final String LibraryAdapter1                      = "Library Adapter 1";
    
    public static final String BarcodeSequencei5                    = "Barcode Sequence i5";
    public static final String BarcodeSequence2                     = "Barcode Sequence 2 (Dual Index)";
    //public static final String BarcodeSequenceReverse               = "Barcode Sequence Reverse";
    
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
    public static final String additionalComment ="Additional Comment";
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
        summaryColumnNames.add(new ColumnNames("Submission Name", "Submission Name"));
        summaryColumnNames.add(new ColumnNames("Submission Title", "Submission Title"));
        summaryColumnNames.add(new ColumnNames("Submission Context", "Submission Context"));
        summaryColumnNames.add(new ColumnNames("Submission Description", "Submission Description"));
        summaryColumnNames.add(new ColumnNames("Submission Comment", "Submission Comment"));
        summaryColumnNames.add(new ColumnNames("Submission Date", "Submission Date"));
        summaryColumnNames.add(new ColumnNames("Submission Template", "Submission Template"));
        summaryColumnNames.add(new ColumnNames("Related Publications", "Related Publications"));
        summaryColumnNames.add(new ColumnNames("Related Datasets", "Related Datasets"));
        summaryColumnNames.add(new ColumnNames("Submitting Organization Department", "Submitting Organization Department"));
        summaryColumnNames.add(new ColumnNames("Submitting Organization Name", "Submitting Organization Name"));
        summaryColumnNames.add(new ColumnNames("Submitting Organization Address", "Submitting Organization Address"));
        summaryColumnNames.add(new ColumnNames("Submitting Organization URL", "Submitting Organization URL"));
        summaryColumnNames.add(new ColumnNames("Submitting Organization UID", "Submitting Organization UID"));
        summaryColumnNames.add(new ColumnNames("Contact Person Name", "Contact Person Name"));
        summaryColumnNames.add(new ColumnNames("Contact Person Email", "Contact Person Email"));
        summaryColumnNames.add(new ColumnNames("Contact Person Phone", "Contact Person Phone"));
        summaryColumnNames.add(new ColumnNames("Lab URL", "Lab URL"));
        summaryColumnNames.add(new ColumnNames("Lab Head Contact", "Lab Head Contact"));
        summaryColumnNames.add(new ColumnNames("Lab Administrative Contact", "Lab Administrative Contact"));
        summaryColumnNames.add(new ColumnNames("Lab Experimental Contact", "Lab Experimental Contact"));
        summaryColumnNames.add(new ColumnNames("Lab Bioinformatics Contact", "Lab Bioinformatics Contact"));
        summaryColumnNames.add(new ColumnNames("Billing Contact", "Billing Contact"));
        summaryColumnNames.add(new ColumnNames("Billing Address", "Billing Address"));
        summaryColumnNames.add(new ColumnNames("Billing Code", "Billing Code"));
        
        //samples column names
        sampleColumnNames = new ArrayList<ColumnNames>();
        sampleColumnNames.add(new ColumnNames("Sample Name", "Sample Name"));
        sampleColumnNames.add(new ColumnNames("Sample Description", "Sample Description"));
        sampleColumnNames.add(new ColumnNames("Organism", "Organism"));
        sampleColumnNames.add(new ColumnNames("Sex", "Sex"));
        sampleColumnNames.add(new ColumnNames("Age", "Age"));
        sampleColumnNames.add(new ColumnNames("Tissue", "Tissue"));
        sampleColumnNames.add(new ColumnNames("Cell Type", "Cell Type"));
        sampleColumnNames.add(new ColumnNames("Genotype", "Genotype"));
        sampleColumnNames.add(new ColumnNames("Family Relations", "Family Relations"));
        sampleColumnNames.add(new ColumnNames("Phenotype", "Phenotype"));
        sampleColumnNames.add(new ColumnNames("Disease", "Disease"));
        sampleColumnNames.add(new ColumnNames("Material Type", "Material Type"));
        sampleColumnNames.add(new ColumnNames("Source", "Source"));
        sampleColumnNames.add(new ColumnNames("Acquisition Date", "Acquisition Date"));
        sampleColumnNames.add(new ColumnNames("Sample Group (custom column)", "Sample Group (custom column)"));
        sampleColumnNames.add(new ColumnNames("Original Sample ID (custom column)", "Original Sample ID (custom column)"));
        sampleColumnNames.add(new ColumnNames("", ""));
        sampleColumnNames.add(new ColumnNames("", ""));
        
        //library column names
        libraryColumnNames = new ArrayList<ColumnNames>();
        libraryColumnNames.add(new ColumnNames("Library Name", "Library Name"));
        libraryColumnNames.add(new ColumnNames("Library Label", "Library Label"));
        libraryColumnNames.add(new ColumnNames("Sample Name", "Sample Name"));
        libraryColumnNames.add(new ColumnNames("Barcode Sequence i7", "Barcode Sequence 1", "Barcode Sequence i7", "Barcode Sequence", "Barcode Sequence Forward", "N7 Barcode Sequence", "Sequences Index primer 1 (i7)"));
        libraryColumnNames.add(new ColumnNames("Library Adapter i7", "Library Adapter 1", "Library Adapter i7", "Library Adapter", "N7 Library Adapter"));
        libraryColumnNames.add(new ColumnNames("Barcode Sequence i5", "Barcode Sequence 2 (Dual Index)", "Barcode Sequence i5", "Barcode Sequence Reverse", "N5 Barcode Sequence", "Sequences Index primer 2 (i5)"));
        libraryColumnNames.add(new ColumnNames("Library Adapter i5", "Library Adapter 2 (Dual Index)", "Library Adapter i5", "N5 Library Adapter"));
        libraryColumnNames.add(new ColumnNames("Sequencing Primer Type", "Sequencing Primer", "Sequencing Primer Type"));
        libraryColumnNames.add(new ColumnNames("Custom Sequencing Primer Name ", "Name of Custom Sequencing Primer ", "Custom Sequencing Primer Name"));
        libraryColumnNames.add(new ColumnNames("Custom Sequencing Primer Sequence", "Custom Sequencing Primer Sequence"));
        libraryColumnNames.add(new ColumnNames("Library Type", "Library Type"));
        libraryColumnNames.add(new ColumnNames("Library Kits", "Library Kits"));
        libraryColumnNames.add(new ColumnNames("Library Details", "Library Details"));
        libraryColumnNames.add(new ColumnNames("Library Person", "Library Person"));
        libraryColumnNames.add(new ColumnNames("Library Date", "Library Date"));
        libraryColumnNames.add(new ColumnNames("Library Volume", "Library Volume"));
        libraryColumnNames.add(new ColumnNames("Library DNA Concentration", "Library DNA Concentration"));
        libraryColumnNames.add(new ColumnNames("Library Total Size", "Library Total Size", "Library Size"));
        libraryColumnNames.add(new ColumnNames("Library Insert Size", "Library Insert Size"));
        libraryColumnNames.add(new ColumnNames("Library Comment", "Library Comment"));
        libraryColumnNames.add(new ColumnNames("Additional Comment", "Additional Comment"));
        libraryColumnNames.add(new ColumnNames("Bioinformatics Protocol", "Bioinformatics Protocol"));
        libraryColumnNames.add(new ColumnNames("Bioinformatics Genome", "Bioinformatics Genome"));
        libraryColumnNames.add(new ColumnNames("Bioinformatics Germline Control", "Bioinformatics Germline Control"));
        libraryColumnNames.add(new ColumnNames("Bioinformatics Comment", "Bioinformatics Comment"));
        libraryColumnNames.add(new ColumnNames("Library DNA Amount", "Library DNA Amount", "Library DNA Amount [ng]"));
        
        //request column names
        requestColumnNames = new ArrayList<ColumnNames>();
        requestColumnNames.add(new ColumnNames("Library Name", "Library Name"));
        requestColumnNames.add(new ColumnNames("Sequencing Type", "Sequencing Type"));
        requestColumnNames.add(new ColumnNames("Read Length", "Read Length"));
        requestColumnNames.add(new ColumnNames("Sequencer", "Sequencer"));
        requestColumnNames.add(new ColumnNames("Number of Lanes", "Number of Lanes", "Number of HiSeq Lanes"));
        requestColumnNames.add(new ColumnNames("Special Requirements", "Special Requirements"));
        requestColumnNames.add(new ColumnNames("Additional Comment", "Additional Comment"));
        requestColumnNames.add(new ColumnNames("Receiving Person", "Receiving Person"));
        requestColumnNames.add(new ColumnNames("Receiving Date", "Receiving Date"));
        requestColumnNames.add(new ColumnNames("Receiving Comment", "Receiving Comment"));
        requestColumnNames.add(new ColumnNames("Quality Control Person", "Quality Control Person"));
        requestColumnNames.add(new ColumnNames("Quality Control Date", "Quality Control Date"));
        requestColumnNames.add(new ColumnNames("Quality Control Summary", "Quality Control Summary"));
        requestColumnNames.add(new ColumnNames("Quality Control Files", "Quality Control Files"));
        requestColumnNames.add(new ColumnNames("Quality Control Status", "Quality Control Status"));
        
    }
}
