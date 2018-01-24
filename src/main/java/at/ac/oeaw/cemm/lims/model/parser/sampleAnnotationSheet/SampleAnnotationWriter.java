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
import org.apache.poi.ss.util.CellUtil;

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
    
    public void writeToFile() throws ParsingException {

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
            
            fos = new FileOutputStream(orginalFile);
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
        
        Font font = getFont(sheet.getWorkbook(),false,false);
        CellStyle defaultCellStyle = sheet.getWorkbook().getCellStyleAt(0);
        defaultCellStyle.setFont(font);
        defaultCellStyle.setBorderBottom(BorderStyle.NONE);
        defaultCellStyle.setBorderTop(BorderStyle.NONE);
        defaultCellStyle.setBorderLeft(BorderStyle.NONE);
        defaultCellStyle.setBorderRight(BorderStyle.NONE);

        Font boldFont = getFont(sheet.getWorkbook(),true,false);
        Font italicFont =  getFont(sheet.getWorkbook(),false,true);

        CellStyle topRowStyle=defaultCellStyle;
        CellStyle topSummaryRowStyle=defaultCellStyle;
        CellStyle leftSummaryColumnStyle=defaultCellStyle;
        
        int rowNumber = 0;
        int headerLength = 0;
        for (ArrayList<String> row : rows) {
            Row currentRow = sheet.createRow(rowNumber);
            
            if (!isTransposed && rowNumber == headerIndex) {
                topRowStyle = currentRow.getRowStyle();
                if (topRowStyle == null){
                    topRowStyle = sheet.getWorkbook().createCellStyle();
                }
                topRowStyle.setFont(boldFont);
                topRowStyle.setBorderBottom(BorderStyle.MEDIUM);
                currentRow.setRowStyle(topRowStyle);

                headerLength = row.size();
            } else if (isTransposed && rowNumber == 0) {
                topSummaryRowStyle = currentRow.getRowStyle();
                if (topSummaryRowStyle == null){
                    topSummaryRowStyle = sheet.getWorkbook().createCellStyle();
                }
                topSummaryRowStyle.setFont(italicFont);
                topSummaryRowStyle.setBorderBottom(BorderStyle.MEDIUM);
                currentRow.setRowStyle(topSummaryRowStyle);
                
                leftSummaryColumnStyle = sheet.getColumnStyle(0);
                if (leftSummaryColumnStyle==null){
                    leftSummaryColumnStyle = sheet.getWorkbook().createCellStyle();
                }
                leftSummaryColumnStyle.setFont(boldFont);
                leftSummaryColumnStyle.setBorderRight(BorderStyle.MEDIUM);
                sheet.setDefaultColumnStyle(0,leftSummaryColumnStyle );
            } 
            
            
            int colNumber = 0;
            for (String value : row) {
                Cell currentCell = currentRow.createCell(colNumber, CellType.STRING);
                
                if (!isTransposed && rowNumber == headerIndex) {
                    currentCell.setCellStyle(topRowStyle);
                } else if (isTransposed && rowNumber == 0) {
                   currentCell.setCellStyle(topSummaryRowStyle);
                } else if (isTransposed && colNumber == 0){
                    currentCell.setCellStyle(leftSummaryColumnStyle);
                }else {
                    currentCell.setCellStyle(defaultCellStyle);
                }
                
                if (!isTransposed && rowNumber > headerIndex && header.containsValue(colNumber)) {
                    switch (header.getKey(colNumber).getColumnType()) {
                        case DOUBLE:                            
                            if (!value.isEmpty()){
                               currentCell.setCellType(CellType.NUMERIC);
                               currentCell.setCellValue(Double.parseDouble(ExcelParserUtils.removeUnits(value)));
                            }else{
                                currentCell.setCellType(CellType.BLANK);
                            }
                            break;
                        case INTEGER:
                             if (!value.isEmpty()){
                                currentCell.setCellType(CellType.NUMERIC);
                                currentCell.setCellValue(new Double(ExcelParserUtils.removeUnits(value)).intValue());
                            }else{
                                currentCell.setCellType(CellType.BLANK);
                            }
                            break;
                        case DATE:                          
                            if (!value.isEmpty()) {
                                currentCell.setCellType(CellType.NUMERIC);
                                currentCell.setCellValue(ExcelParserUtils.getStringAsDate(value));
                                CellUtil.setCellStyleProperty(currentCell, CellUtil.DATA_FORMAT, sheet.getWorkbook().getCreationHelper().createDataFormat().getFormat("dd.MM.YYYY"));
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
   
    private Font getFont (Workbook wb, boolean bold, boolean italic) {
        Font theFont = wb.findFont(bold, IndexedColors.BLACK.getIndex(), (short) 11, "Arial", italic, false, Font.SS_NONE, Font.U_NONE);
        if (theFont == null) {
            theFont = wb.createFont();
        }
        
        theFont.setFontHeightInPoints((short) 11);
        theFont.setFontName("Arial");
        theFont.setColor(IndexedColors.BLACK.getIndex());
        theFont.setBold(bold);
        theFont.setItalic(italic);

        return theFont;
    }

    private void updateSubmissionSummary(RequestFormDTO requestForm) {
         UserDTO requestor = requestForm.getRequestor().getUser();
         for (ArrayList<String>row : summarySheet){
            if (row.contains(ExcelParserConstants.ContactPersonName)){
                row.set(1, (requestor.getFirstName()+", "+requestor.getLastName()));
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
            } else if (row.contains(ExcelParserConstants.BillingContact)){
                row.set(1, requestForm.getBillingInfo().getContact());
            } else if (row.contains(ExcelParserConstants.BillingAddress)){
                row.set(1, requestForm.getBillingInfo().getAddress());
            } else if (row.contains(ExcelParserConstants.BillingCode)){
                row.set(1, requestForm.getBillingInfo().getBillingCode());
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
        row.set(header.get(getColumnName(ExcelParserConstants.LibraryType)), sample.getApplicationName());
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
