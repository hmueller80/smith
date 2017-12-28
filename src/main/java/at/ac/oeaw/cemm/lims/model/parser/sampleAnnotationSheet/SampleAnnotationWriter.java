/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.lims.api.dto.lims.UserDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestFormDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestLibraryDTO;
import at.ac.oeaw.cemm.lims.api.dto.request_form.RequestSampleDTO;
import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import static at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet.SampleAnnotationParser.findColumnIndices;
import at.ac.oeaw.cemm.lims.util.NameFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.BidiMap;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author dbarreca
 */
public class SampleAnnotationWriter extends ExcelParser {
    private BidiMap<ColumnNames, Integer> samplesHeader;
    private Integer samplesHeaderIndex;
    
    private BidiMap<ColumnNames, Integer> librariesHeader;
    private Integer librariesHeaderIndex;
    
    private BidiMap<ColumnNames, Integer> requestHeader;
    private Integer requestHeaderIndex;
    
    public SampleAnnotationWriter(File file, RequestFormDTO requestForm) throws ParsingException {
       super(file);
        
       updateSubmissionSummary(requestForm);
       updateSamplesSheet(requestForm);
       updateLibrariesSheet(requestForm);
       updateRequestSheet(requestForm);        
    }
    
    public void writeToFile(String fileName, File folder) throws ParsingException {
        if (!folder.exists()) {
            throw new ParsingException("Excel Writing", "The specified directory " + folder.getName() + " does not exists");
        }
        if (!folder.isDirectory()) {
            throw new ParsingException("Excel Writing", "The specified file " + folder.getName() + " is not a directory");
        }

        Workbook wb = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(orginalFile);
            
            wb= WorkbookFactory.create(fis);
            
            wb.removeSheetAt(wb.getSheetIndex(ExcelParserConstants.summary));
            Sheet summaryWbSheet= wb.createSheet(ExcelParserConstants.summary);
            writeSheet(summaryWbSheet,summarySheet);
            
            wb.removeSheetAt(wb.getSheetIndex(ExcelParserConstants.samples));
            Sheet samplesWbSheet= wb.createSheet(ExcelParserConstants.samples);
            writeSheet(samplesWbSheet,samplesSheet,samplesHeader,samplesHeaderIndex);
            
            wb.removeSheetAt(wb.getSheetIndex(ExcelParserConstants.libraries));
            Sheet librariesWbSheet= wb.createSheet(ExcelParserConstants.libraries);
            writeSheet(librariesWbSheet,librariesSheet,librariesHeader,librariesHeaderIndex);
            
            wb.removeSheetAt(wb.getSheetIndex(ExcelParserConstants.requests));
            Sheet requestsWbSheet= wb.createSheet(ExcelParserConstants.requests);
            writeSheet(requestsWbSheet,requestsSheet,requestHeader,requestHeaderIndex);
            
            fis.close();
            
            fos = new FileOutputStream(new File(folder,fileName));
            wb.write(fos);
            
        } catch (IOException | EncryptedDocumentException | InvalidFormatException  ex) {
            Logger.getLogger(SampleAnnotationWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {}
            }
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException ex) {}
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {}
            }

        }

    }
    
    private void writeSheet(Sheet sheet, ArrayList<ArrayList<String>> rows){
        writeSheet(sheet,rows,null,null,true);
    }
    
    private void writeSheet(Sheet sheet, ArrayList<ArrayList<String>> rows,BidiMap<ColumnNames, Integer> header, Integer headerIndex){
        writeSheet(sheet,rows,header,headerIndex,false);
    }
    
    private void writeSheet(Sheet sheet, ArrayList<ArrayList<String>> rows, BidiMap<ColumnNames, Integer> header, Integer headerIndex, boolean isTransposed) {

        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(false);
        font.setItalic(false);
        sheet.getWorkbook().getCellStyleAt(0).setFont(font);
        
        Font boldFont = sheet.getWorkbook().createFont();
        boldFont.setFontHeightInPoints((short) 11);
        boldFont.setFontName("Arial");
        boldFont.setColor(IndexedColors.BLACK.getIndex());
        boldFont.setBold(true);
        boldFont.setItalic(false);

        Font italicFont = sheet.getWorkbook().createFont();
        italicFont.setFontHeightInPoints((short) 11);
        italicFont.setFontName("Arial");
        italicFont.setColor(IndexedColors.BLACK.getIndex());
        italicFont.setBold(false);
        italicFont.setItalic(true);

        int rowNumber = 0;
        int headerLength = 0;
        for (ArrayList<String> row : rows) {
            Row currentRow = sheet.createRow(rowNumber);
          
            if (!isTransposed && rowNumber == headerIndex) {
                currentRow.setRowStyle(sheet.getWorkbook().createCellStyle());
                currentRow.getRowStyle().setFont(boldFont);
                currentRow.getRowStyle().setBorderBottom(BorderStyle.MEDIUM);
                headerLength = row.size();
            } else if (isTransposed && rowNumber == 0) {
                currentRow.setRowStyle(sheet.getWorkbook().createCellStyle());
                currentRow.getRowStyle().setFont(italicFont);
                currentRow.getRowStyle().setBorderBottom(BorderStyle.MEDIUM);
                sheet.setDefaultColumnStyle(0, sheet.getWorkbook().createCellStyle());
                sheet.getColumnStyle(0).setFont(boldFont);
                sheet.getColumnStyle(0).setBorderRight(BorderStyle.MEDIUM);
            } 
            
            
            int colNumber = 0;
            for (String value : row) {
                Cell currentCell = currentRow.createCell(colNumber, CellType.STRING);
                CellStyle style = sheet.getWorkbook().createCellStyle();
                currentCell.setCellStyle(style);             
                style.setFont(font);
                
                if (!isTransposed && rowNumber == headerIndex) {
                    style.setFont(boldFont);
                    style.setBorderBottom(BorderStyle.MEDIUM);
                } else if (isTransposed && rowNumber == 0) {
                    style.setFont(italicFont);
                    style.setBorderBottom(BorderStyle.MEDIUM);
                } else if (isTransposed && colNumber ==0){
                    style.setFont(boldFont);
                    style.setBorderRight(BorderStyle.MEDIUM);
                }
                
                if (!isTransposed && rowNumber > headerIndex && header.containsValue(colNumber)) {
                    switch (header.getKey(colNumber).getColumnType()) {
                        case DOUBLE:                            
                            if (!value.isEmpty()){
                               currentCell.setCellType(CellType.NUMERIC);
                               currentCell.setCellValue(Double.parseDouble(value));
                            }else{
                                currentCell.setCellType(CellType.BLANK);
                            }
                            break;
                        case INTEGER:
                             if (!value.isEmpty()){
                                currentCell.setCellType(CellType.NUMERIC);
                                currentCell.setCellValue(new Double(value).intValue());
                            }else{
                                currentCell.setCellType(CellType.BLANK);
                            }
                            break;
                        case DATE:                          
                            if (!value.isEmpty()) {
                                currentCell.setCellType(CellType.NUMERIC);
                                currentCell.setCellValue(ExcelParserUtils.getStringAsDate(value));
                                currentCell.getCellStyle().setDataFormat(sheet.getWorkbook().getCreationHelper().createDataFormat().getFormat("dd.MM.YYYY"));
                            }else{
                                currentCell.setCellType(CellType.BLANK);
                            }
                            break;
                        default:
                            currentCell.setCellValue(value);
                            break;
                    }
                } else {
                    currentCell.setCellValue(value);
                }
                
                colNumber++;
            }
            rowNumber++;
        }
        
        if (isTransposed){
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
        }else{
            for (int i=0;i<headerLength;i++){
                sheet.autoSizeColumn(i);
            }
        }

    }
    
    
    private void updateSubmissionSummary(RequestFormDTO requestForm) {
         UserDTO requestor = requestForm.getRequestor().getUser();
         for (ArrayList<String>row : summarySheet){
            if (row.contains(ExcelParserConstants.ContactPersonName)){
                row.set(1, requestor.getUserName());
            } else if (row.contains(ExcelParserConstants.ContactPersonEmail)){
                row.set(1, requestor.getMailAddress());
            } else if (row.contains(ExcelParserConstants.ContactPersonPhone)){
                row.set(1, requestor.getPhone());
            } else if (row.contains(ExcelParserConstants.SubmittingOrganizationName)){
                row.set(1, requestor.getAffiliation().getOrganizationName());
            } else if (row.contains(ExcelParserConstants.SubmittingOrganizationDepartment)){
                row.set(1, requestor.getAffiliation().getDepartmentName());
            } else if (row.contains(ExcelParserConstants.SubmittingOrganizationURL)){
                row.set(1, requestor.getAffiliation().getUrl());
            } else if (row.contains(ExcelParserConstants.SubmittingOrganizationAddress)){
                row.set(1, requestor.getAffiliation().getAddress());
            } else if (row.contains(ExcelParserConstants.SubmissionDate)){
                row.set(1, ExcelParserUtils.getDateAsString(requestForm.getDate()));
            } else if (row.contains(ExcelParserConstants.LabHeadContact)){
                row.set(1, requestForm.getRequestor().getPi().getMailAddress());
            }
        }
    }
    
    private void updateSamplesSheet(RequestFormDTO requestForm) {
        samplesHeaderIndex = SampleAnnotationParser.findHeaderRows(ExcelParserConstants.sampleColumnNames, samplesSheet).get(0);
        ArrayList<String> headerRow = samplesSheet.get(samplesHeaderIndex);
        samplesHeader = findColumnIndices(ExcelParserConstants.sampleColumnNames, headerRow);
        
        Integer headerLength = headerRow.size();
        Set<String> samplesInSheet = new HashSet<>();
         
        Iterator <ArrayList<String>> sampleSheetIterator = samplesSheet.listIterator();
        int rowIndex = 0;
        while (sampleSheetIterator.hasNext()){
            ArrayList<String> row = sampleSheetIterator.next();
            if (rowIndex<=samplesHeaderIndex){
                rowIndex++;
                continue;
            }
            rowIndex++;
            
            fillToSize(row,headerLength);

            boolean found = false;
            
            String sampleName = NameFilter.legalizeSampleName(row.get(samplesHeader.get(getColumnName(ExcelParserConstants.SampleName))));

            for (RequestLibraryDTO library: requestForm.getLibraries()){
                for (RequestSampleDTO sample: library.getSamples()){
                    if (sample.getName().equals(sampleName)){
                        updateSamplesSheetRow(row,samplesHeader,sample);
                        found = true;
                    }
                }
            }
            
            if (!found){
                sampleSheetIterator.remove();
            } else {
                samplesInSheet.add(sampleName);
            }
        }
        
        for (RequestLibraryDTO library : requestForm.getLibraries()) {
            for (RequestSampleDTO sample : library.getSamples()) {
                if (!samplesInSheet.contains(sample.getName())) {
                    ArrayList<String> row = new ArrayList<>();
                    fillToSize(row,headerLength);
                    updateSamplesSheetRow(row, samplesHeader,sample);
                    samplesSheet.add(row);
                }
            }
        }  
    }

    private void updateSamplesSheetRow(ArrayList<String> row, Map<ColumnNames, Integer> header, RequestSampleDTO sample) {
        row.set(header.get(getColumnName(ExcelParserConstants.SampleName)), sample.getName());
        row.set(header.get(getColumnName(ExcelParserConstants.SampleDescription)), sample.getSampleDescription());
        row.set(header.get(getColumnName(ExcelParserConstants.Organism)), sample.getOrganism());
    }
    
    private void updateLibrariesSheet(RequestFormDTO requestForm) {
        librariesHeaderIndex = SampleAnnotationParser.findHeaderRows(ExcelParserConstants.libraryColumnNames, librariesSheet).get(0);
        ArrayList<String> headerRow = librariesSheet.get(librariesHeaderIndex);
        normalizeHeaderRow(headerRow);       
        librariesHeader = findColumnIndices(ExcelParserConstants.libraryColumnNames, headerRow);
        
        Integer headerLength = librariesHeader.size();
        Set<String> samplesInSheet = new HashSet<>();
         
        Iterator <ArrayList<String>> librarySheetIterator = librariesSheet.iterator();
        int rowIndex = 0;
        while (librarySheetIterator.hasNext()){
            ArrayList<String> row = librarySheetIterator.next();
            if (rowIndex<=librariesHeaderIndex){
                rowIndex++;
                continue;
            }
            rowIndex++;
            fillToSize(row,headerLength);

            boolean found = false;
            
            String sampleName =  NameFilter.legalizeSampleName(row.get(librariesHeader.get(getColumnName(ExcelParserConstants.SampleName))));
            for (RequestLibraryDTO library: requestForm.getLibraries()){
                for (RequestSampleDTO sample: library.getSamples()){
                    if (sample.getName().equals(sampleName)){
                        updateLibrariesSheetRow(row,librariesHeader,sample, library);
                        found = true;
                    }
                }
            }
            if (!found){
                librarySheetIterator.remove();
            } else {
                samplesInSheet.add(sampleName);
            }
        }
        
        for (RequestLibraryDTO library : requestForm.getLibraries()) {
            for (RequestSampleDTO sample : library.getSamples()) {
                if (!samplesInSheet.contains(sample.getName())) {
                    ArrayList<String> row = new ArrayList<>();
                    fillToSize(row,headerLength);
                    updateLibrariesSheetRow(row,librariesHeader,sample, library);
                    librariesSheet.add(row);
                }
            }
        }  
    }
    
    private void normalizeHeaderRow(ArrayList<String> headerRow) {
        for (ColumnNames column : ExcelParserConstants.libraryColumnNames) {
            if (!headerRow.contains(column)) {
                headerRow.add(column.getOutputColumnName());
            }
        }
    }

    private void updateLibrariesSheetRow(ArrayList<String> row, Map<ColumnNames, Integer> header, RequestSampleDTO sample, RequestLibraryDTO library) {
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryName)), library.getName());
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryLabel)), library.getName());
        row.set(header.get(getColumnName(ExcelParserConstants.SampleName)), sample.getName());
        row.set(header.get(getColumnName(ExcelParserConstants.BarcodeSequencei7)), sample.getI7Index());
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryAdapteri7)), sample.getI7Adapter());
        row.set(header.get(getColumnName(ExcelParserConstants.BarcodeSequencei5)), sample.getI5Index());
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryAdapteri5)), sample.getI5Adapter());
        row.set(header.get(getColumnName(ExcelParserConstants.SequencingPrimerType)), sample.getPrimerType());
        row.set(header.get(getColumnName(ExcelParserConstants.CustomSequencingPrimerName)), sample.getPrimerName());
        row.set(header.get(getColumnName(ExcelParserConstants.CustomSequencingPrimerSequence)), sample.getPrimerSequence());
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryType)), library.getApplicationName());
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryVolume)), String.valueOf(library.getVolume()));
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryDNAConcentration)),String.valueOf(library.getDnaConcentration()));
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryTotalSize)), String.valueOf(library.getTotalSize()));
    }
    
    private void updateRequestSheet(RequestFormDTO requestForm) {
        requestHeaderIndex = SampleAnnotationParser.findHeaderRows(ExcelParserConstants.requestColumnNames, requestsSheet).get(0);
        ArrayList<String> headerRow = requestsSheet.get(requestHeaderIndex);
        requestHeader = findColumnIndices(ExcelParserConstants.requestColumnNames, headerRow);
       
        Integer headerLength = requestHeader.size();
        Set<String> librariesInSheet = new HashSet<>();
         
        Iterator <ArrayList<String>> requestSheetIterator = requestsSheet.iterator();
        
        int rowIndex = 0;
        while (requestSheetIterator.hasNext()){
            ArrayList<String> row = requestSheetIterator.next();
            if (rowIndex<=requestHeaderIndex){
                rowIndex++;
                continue;
            }
            rowIndex++;
            fillToSize(row,headerLength);

            boolean found = false;
            
            String libraryName =  NameFilter.legalizeLibrary(row.get(requestHeader.get(getColumnName(ExcelParserConstants.LibraryName))));
            for (RequestLibraryDTO library: requestForm.getLibraries()){
                if (library.getName().equals(libraryName)) {
                    updateRequestSheetRow(row, requestHeader, library);
                    found = true;
                } 
            }
            
            if (!found){
                requestSheetIterator.remove();
            } else {
                librariesInSheet.add(libraryName);
            }
        }
        
        for (RequestLibraryDTO library : requestForm.getLibraries()) {
            if (!librariesInSheet.contains(library.getName())) {
                ArrayList<String> row = new ArrayList<>();
                fillToSize(row,headerLength);
                updateRequestSheetRow(row, requestHeader, library);
                requestsSheet.add(row);
            }
        }
    }

    private void updateRequestSheetRow(ArrayList<String> row, Map<ColumnNames, Integer> header, RequestLibraryDTO library) {
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryName)), library.getName());
        row.set(header.get(getColumnName(ExcelParserConstants.sequencingType)), library.getReadMode());
        row.set(header.get(getColumnName(ExcelParserConstants.readLength)), String.valueOf(library.getReadLength()));
        row.set(header.get(getColumnName(ExcelParserConstants.numberofLanes)), String.valueOf(library.getLanes()));
      }
    
    
    private void fillToSize(ArrayList<String> toFill, Integer size){
        for (int i = toFill.size()-1;i<size;i++){
            toFill.add("");
        }
    }
    
    private ColumnNames getColumnName(String columnName) {
        return new ColumnNames(columnName,columnName);
    }
}
