/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.analysis.impl.model;

/**
 *
 * @author dbarreca
 */
public enum SampleSheetCSVHeader {
    lane,
    barcode_sequence_1,
    barcode_sequence_2,
    sample_name,
    library_name,
    library_size,
    barcode_comment_1,
    barcode_comment_2,
    sample_comment;

    public static String getLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(lane);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(barcode_sequence_1);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(barcode_sequence_2);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(sample_name);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(library_name);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(library_size);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(barcode_comment_1);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(barcode_comment_2);
        sb.append(SampleSheet.SEPARATOR);
        sb.append(sample_comment);
        return sb.toString();
    }

}
