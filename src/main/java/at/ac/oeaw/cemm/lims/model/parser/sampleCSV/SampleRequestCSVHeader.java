/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleCSV;

/**
 *
 * @author dbarreca
 */public enum SampleRequestCSVHeader {
    /*00*/ UserName, 
    /*01*/ UserLogin,
    /*02*/ UserTel,
    /*03*/ Institute,
    /*04*/ PI,
    /*05*/ SampleName,
    /*06*/ BarcodeByName,
    /*07*/ Application,
    /*08*/ ReadLength,
    /*09*/ Receipe,
    /*10*/ SampleType,
    /*11*/ LibrarySynthesis,
    /*12*/ Organism,
    /*13*/ Antibody,
    /*14*/ SampleDescription,
    /*15*/ BioAnalyzerDate,
    /*16*/ BioAnalyzerNanomolarity,
    /*17*/ SampleConcentration,
    /*18*/ TotalAmount,
    /*19*/ BulkFragmentSize,
    /*20*/ Comments,
    /*21*/ library,
    /*22*/ submissionId,
    /*23*/ date,
    /*24*/ volume;
    
    public static char getSeparator(){
        return ',';
    }
    
    public static String getHeaderLine(){
        StringBuilder sb = new StringBuilder();
        sb.append(UserName).append(getSeparator());
        sb.append(UserLogin).append(getSeparator());
        sb.append(UserTel).append(getSeparator());
        sb.append(Institute).append(getSeparator());
        sb.append(PI).append(getSeparator());
        sb.append(SampleName).append(getSeparator());
        sb.append(BarcodeByName).append(getSeparator());
        sb.append(Application).append(getSeparator());
        sb.append(ReadLength).append(getSeparator());
        sb.append(Receipe).append(getSeparator());
        sb.append(SampleType).append(getSeparator());
        sb.append(LibrarySynthesis).append(getSeparator());
        sb.append(Organism).append(getSeparator());
        sb.append(Antibody).append(getSeparator());
        sb.append(SampleDescription).append(getSeparator());
        sb.append(BioAnalyzerDate).append(getSeparator());
        sb.append(BioAnalyzerNanomolarity).append(getSeparator());
        sb.append(SampleConcentration).append(getSeparator());
        sb.append(TotalAmount).append(getSeparator());
        sb.append(BulkFragmentSize).append(getSeparator());
        sb.append(Comments).append(getSeparator());
        sb.append(library).append(getSeparator());
        sb.append(submissionId).append(getSeparator());
        sb.append(date).append(getSeparator());
        sb.append(volume);
 
        return sb.toString();
    }
}
