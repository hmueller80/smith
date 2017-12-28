/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
 
/**
 *
 * @author hMueller
 */
public class ExcelParser{
    
    
    protected ArrayList<ArrayList<String>> summarySheet = new ArrayList<>();
    protected ArrayList<ArrayList<String>>  samplesSheet = new ArrayList<>();
    protected ArrayList<ArrayList<String>> librariesSheet = new ArrayList<>();
    protected ArrayList<ArrayList<String>> requestsSheet = new ArrayList<>();
    protected File orginalFile;
    
    public ExcelParser(File file) throws ParsingException {
        String fileName = file.getName();
        
        
        if(!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")){
            throw new ParsingException("Excel parsing","Invalid format: only xls or xlsx files are accepted");
        }
        
        try {
            orginalFile = file;
            
            ZipSecureFile.setMinInflateRatio(0.005);
            Workbook wb = WorkbookFactory.create(file);
           
            int sheets = wb.getNumberOfSheets();

            for (int i = 0; i < sheets; i++) {
                
                String name = wb.getSheetName(i);
                if (!ExcelParserConstants.sheetsToParse.contains(name)) {
                    continue;
                }
                
            
                Sheet sheet = wb.getSheet(name);
                CellRangeAddress regionOfInterest = getRegionOfInterest(sheet);
                
                if (regionOfInterest==null) throw new ParsingException("Excel Parsing","Could not find data in sheet "+name);
                
                for (int mergedRegion = 0; mergedRegion < sheet.getNumMergedRegions(); ++mergedRegion) {
                    CellRangeAddress range = sheet.getMergedRegion(mergedRegion);
                    if (range.intersects(regionOfInterest)){
                        throw new ParsingException("Excel parsing","Invalid format: there is a merged region in sheet "+name
                            + " at row "+range.getFirstRow()+" and column "+range.getFirstColumn());
                    }
                }
            
                for (int rowNumber= regionOfInterest.getFirstRow();rowNumber<=regionOfInterest.getLastRow();rowNumber++){                    
                    Row row = sheet.getRow(rowNumber);
                  
                    ArrayList<String> parsedRow = new ArrayList<>();
                    if(!isRowEmpty(row)){
                        for (int colNumber = regionOfInterest.getFirstColumn();colNumber<=regionOfInterest.getLastColumn();colNumber++){
                            Cell c = row.getCell(colNumber);
                            String content = getCellContent(c);
                            parsedRow.add(content);
                        }
                    }
                    if (name.equalsIgnoreCase(ExcelParserConstants.summary)) {
                        summarySheet.add(parsedRow);
                    } else if (name.equalsIgnoreCase(ExcelParserConstants.samples)) {
                        samplesSheet.add(parsedRow);
                    } else if (name.equalsIgnoreCase(ExcelParserConstants.libraries)) {
                        librariesSheet.add(parsedRow);
                    } else if (name.equalsIgnoreCase(ExcelParserConstants.requests)) {
                        requestsSheet.add(parsedRow);
                    }
                }
    
            }
            wb.close();
            
        } catch (IOException| InvalidFormatException | EncryptedDocumentException ex) {
            throw new ParsingException("Error while reading Excel File "+file.getName(),ex.getMessage());
        }

    }
    
    private CellRangeAddress getRegionOfInterest(Sheet sheet) {
        Integer firstRowInSheet = null;
        Integer lastRowInSheet = null;
        Integer minColumnInSheet = null;
        Integer maxColumnInSheet = null;
        
        for (Row row : sheet) {
            boolean skipOthers = false;
            if (!isRowEmpty(row)) {
                if (firstRowInSheet == null) {
                    firstRowInSheet = row.getRowNum();
                }
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {

                    Cell c = row.getCell(j);
                    String content = getCellContent(c);

                    if (content.equalsIgnoreCase(ExcelParserConstants.commentsOnForm)) {
                        skipOthers = true;
                        break;
                    }
                    if (!content.equals("") && (minColumnInSheet == null || j < minColumnInSheet)) {
                        minColumnInSheet = j;
                    }
                    if (!content.equals("") && (maxColumnInSheet == null || j > maxColumnInSheet)) {
                        maxColumnInSheet = j;
                    }

                }

                if (skipOthers) {
                    break;
                } else {
                    lastRowInSheet = row.getRowNum();
                }
            }
        }
        
        if (firstRowInSheet!= null && lastRowInSheet!=null && minColumnInSheet!=null && maxColumnInSheet!=null){
            return new CellRangeAddress(firstRowInSheet,lastRowInSheet,minColumnInSheet,maxColumnInSheet);
        }else{
            return null;
        }
    }
    
    private String getCellContent(Cell c){
        String content = "";
        if (c != null) {
            CellType currentCellType = c.getCellTypeEnum();
            switch (currentCellType) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(c)){
                        content = ExcelParserUtils.getDateAsString(c.getDateCellValue());
                    }else{
                        content = String.valueOf(c.getNumericCellValue());
                    }
                    break;
                case STRING:
                    content = c.getStringCellValue();
                    break;
                case BOOLEAN:
                    content = String.valueOf(c.getBooleanCellValue());
                    break;
                case FORMULA:
                    switch(c.getCachedFormulaResultTypeEnum()){
                        case STRING:
                            content = c.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(c)) {
                                content = ExcelParserUtils.getDateAsString(c.getDateCellValue());
                            } else {
                                content = String.valueOf(c.getNumericCellValue());
                            }
                            break;
                        case BOOLEAN:
                            content = String.valueOf(c.getBooleanCellValue());
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }
        }

        content = removeWhiteSpace(content).trim();
        
        return content;
    }
    
    private String removeWhiteSpace(String s){
        char[] ca = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ca.length; i++){
            if((int)ca[i] != 10){
                sb.append(Character.toString(ca[i]));
            }
        }
        return sb.toString();
    }
    
    private boolean isRowEmpty(Row r) {
        if (r!=null) {
            for (int j = r.getFirstCellNum(); j < r.getLastCellNum(); j++) {
                Cell c = r.getCell(j);
                if(!"".equals(getCellContent(c))){
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<ArrayList<String>> getSamplesSheet() {
        return samplesSheet;
    }

    public ArrayList<ArrayList<String>> getLibrariesSheet() {
        return librariesSheet;
    }

    public ArrayList<ArrayList<String>> getRequestsSheet() {
        return requestsSheet;
    }
    
    public ArrayList<ArrayList<String>> getSummarySheet() {
        return summarySheet;
    }
    
    
    
}
