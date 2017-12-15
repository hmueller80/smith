/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.oeaw.cemm.lims.model.parser.sampleAnnotationSheet;

import at.ac.oeaw.cemm.lims.model.parser.ParsingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 *
 * @author hMueller
 */
public class ExcelParser{
    
    private String fileName;
    
    protected ArrayList<ArrayList<String>> summarySheet = new ArrayList<>();
    protected ArrayList<ArrayList<String>>  samplesSheet = new ArrayList<>();
    protected ArrayList<ArrayList<String>> librariesSheet = new ArrayList<>();
    protected ArrayList<ArrayList<String>> requestsSheet = new ArrayList<>();
    
    public ExcelParser(String filename) throws ParsingException {
        this.fileName = filename;
        parseFile();        
    }
     
    private void parseFile() throws ParsingException {
        boolean isExcel2007 = false;
        
         if(fileName.endsWith(".xlsx")){
            isExcel2007 = true;
        }else if(!fileName.endsWith(".xls")){
            throw new ParsingException("Excel parsing","Invalid format: only xls or xlsx files are accepted");
        }
        
        try {
            Workbook wb;
            
            if (isExcel2007){
                wb = new XSSFWorkbook(fileName);
            }else{
                FileInputStream fis = new FileInputStream(fileName);            
                POIFSFileSystem fileSystem = new POIFSFileSystem(fis);   
                wb = new HSSFWorkbook(fileSystem);
            }
            
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
            
        } catch (IOException ex) {
            Logger.getLogger(ExcelParser.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
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
                ArrayList<String> parsedRow = new ArrayList<>();
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

                    parsedRow.add(content.trim());
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
                case _NONE:
                case BLANK:
                case ERROR:
                    break;
                case NUMERIC:
                    content = String.valueOf(c.getNumericCellValue());
                    break;
                case STRING:
                    content = c.getStringCellValue();
                    break;
                case FORMULA:
                default:
                    c.toString();
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
