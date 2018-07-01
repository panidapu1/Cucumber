//package com.automation.cucumber.helper;
package com.automation.cucumber.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.NumberToTextConverter;


public class ExcelReader {
	
	private XSSFWorkbook excelbook;
	
    public List<Map<String, String>> getData(String excelFilePath, String sheetName)
            throws InvalidFormatException, IOException {
    	XSSFSheet sheet = getSheetByName(excelFilePath, sheetName);
        
		return readSheet(sheet);
    }

    public List<Map<String, String>> getData(String excelFilePath, int sheetNumber)
            throws InvalidFormatException, IOException {
    	XSSFSheet sheet = getSheetByIndex(excelFilePath, sheetNumber);
        return readSheet(sheet);
    }

    private XSSFSheet getSheetByName(String excelFilePath, String sheetName) throws IOException, InvalidFormatException {
    	excelbook = getWorkBook(excelFilePath);
		return excelbook.getSheet(sheetName);

    }

    private XSSFSheet getSheetByIndex(String excelFilePath, int sheetNumber) throws IOException, InvalidFormatException {
    	XSSFSheet sheet = getWorkBook(excelFilePath).getSheetAt(sheetNumber);
        return sheet;
    }

    private XSSFWorkbook getWorkBook(String excelFilePath) throws IOException, InvalidFormatException {
    	return new XSSFWorkbook(new File(excelFilePath).getPath());
    }

    private List<Map<String, String>> readSheet(XSSFSheet sheet) {
    	XSSFRow row;
        int totalRow = sheet.getPhysicalNumberOfRows();
        List<Map<String, String>> excelRows = new ArrayList<Map<String, String>>();
        int headerRowNumber = getHeaderRowNumber(sheet);
        if (headerRowNumber != -1) {
            int totalColumn = sheet.getRow(headerRowNumber).getLastCellNum();
            int setCurrentRow = 1;
            for (int currentRow = setCurrentRow; currentRow <= totalRow; currentRow++) {
                row = getRow(sheet, sheet.getFirstRowNum() + currentRow);
                LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<String, String>();
                for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
                    columnMapdata.putAll(getCellValue(sheet, row, currentColumn));
                }
                excelRows.add(columnMapdata);
            }
        }
        return excelRows;
    }


    private int getHeaderRowNumber(XSSFSheet sheet) {
    	XSSFRow row;
        int totalRow = sheet.getLastRowNum();
        for (int currentRow = 0; currentRow <= totalRow + 1; currentRow++) {
            row = getRow(sheet, currentRow);
            if (row != null) {
                int totalColumn = row.getLastCellNum();
                for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
                    Cell cell;
                    cell = row.getCell(currentColumn, XSSFRow.CREATE_NULL_AS_BLANK);
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        return row.getRowNum();

                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        return row.getRowNum();

                    } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                        return row.getRowNum();
                    } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                        return row.getRowNum();
                    }
                }
            }
        }
        return (-1);
    }

    private XSSFRow getRow(XSSFSheet sheet, int rowNumber) {
        return sheet.getRow(rowNumber);
    }

    private LinkedHashMap<String, String> getCellValue(XSSFSheet sheet, XSSFRow row, int currentColumn) {
        LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<String, String>();
        Cell cell;
        if (row == null) {
            if (sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn, XSSFRow.CREATE_NULL_AS_BLANK)
                    .getCellType() != Cell.CELL_TYPE_BLANK) {
                String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn)
                        .getStringCellValue();
                columnMapdata.put(columnHeaderName, "");
            }
        } else {
            cell = row.getCell(currentColumn, XSSFRow.CREATE_NULL_AS_BLANK);
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), XSSFRow.CREATE_NULL_AS_BLANK)
                        .getCellType() != Cell.CELL_TYPE_BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, cell.getStringCellValue());
                }
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), XSSFRow.CREATE_NULL_AS_BLANK)
                        .getCellType() != Cell.CELL_TYPE_BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, NumberToTextConverter.toText(cell.getNumericCellValue()));
                }
            } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                if (sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), XSSFRow.CREATE_NULL_AS_BLANK)
                        .getCellType() != Cell.CELL_TYPE_BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, "");
                }
            } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                if (sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), XSSFRow.CREATE_NULL_AS_BLANK)
                        .getCellType() != Cell.CELL_TYPE_BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, Boolean.toString(cell.getBooleanCellValue()));
                }
            } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                if (sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex(), XSSFRow.CREATE_NULL_AS_BLANK)
                        .getCellType() != Cell.CELL_TYPE_BLANK) {
                    String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
                            .getStringCellValue();
                    columnMapdata.put(columnHeaderName, Byte.toString(cell.getErrorCellValue()));
                }
            }
        }
        return columnMapdata;
    }
}
