package com.example.fieldforce.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUtils {

    public static XSSFWorkbook getNewXSSFWorkbook() {
        return new XSSFWorkbook();
    }

    public static boolean isSheetExists(XSSFWorkbook xssfWorkbook, String sheetName) {
        return xssfWorkbook.getSheet(sheetName) != null;
    }

    public static void writeDataToSheet(XSSFSheet xssfSheet, String[] headers, List<String[]> dataList) throws Exception {

        //Write Headers
        XSSFRow xssfHeaderRow = xssfSheet.createRow(0);
        writeRow(xssfHeaderRow, headers);

        //Write Data
        for(int i = 0; i < dataList.size(); i++) {
            XSSFRow currRow = xssfSheet.createRow(i+1);
            writeRow(currRow, dataList.get(i));
        }
    }


    public static void writeHeaderToSheet(XSSFSheet xssfSheet, String[] headers) throws Exception {

        //Write Headers
        XSSFRow xssfHeaderRow = xssfSheet.createRow(0);
        writeRow(xssfHeaderRow, headers);
    }

    public static void writeHeaderToSheet(XSSFSheet xssfSheet, String[] headers,int startRow) throws Exception {
        //Write Headers
        XSSFRow xssfHeaderRow = xssfSheet.createRow(startRow);
        writeRow(xssfHeaderRow, headers);
    }

    private static void writeRow(XSSFRow xssfRow, String[] data) {
        for(int colIndex = 0; colIndex < data.length; colIndex++) {
            if(NumberUtils.isCreatable(data[colIndex]))
                xssfRow.createCell(colIndex).setCellValue(Double.parseDouble(data[colIndex]));
            else
                xssfRow.createCell(colIndex).setCellValue(data[colIndex]);
        }
    }

    public static void writeDataToSheet(XSSFSheet xssfSheet, String[] headers, List<String[]> dataList,int startColumn, int startRow) throws Exception {

        //Write Headers
        int rowNumber=startRow;
        XSSFRow xssfHeaderRow = xssfSheet.getRow(rowNumber)==null?xssfSheet.createRow(rowNumber):xssfSheet.getRow(rowNumber);
        rowNumber++;
        writeRow(xssfHeaderRow, headers,startColumn);

        //Write Data
        for(int i = 0; i < dataList.size(); i++) {
            XSSFRow currRow =xssfSheet.getRow(rowNumber)==null?xssfSheet.createRow(rowNumber):xssfSheet.getRow(rowNumber);
            rowNumber++;
            writeRow(currRow, dataList.get(i),startColumn);
        }
    }

    private static void writeRow(XSSFRow xssfRow, String[] data,int startColumn) {
        for(int colIndex = startColumn; (colIndex-startColumn) < data.length; colIndex++) {
            if(NumberUtils.isCreatable(data[colIndex-startColumn]))
                xssfRow.createCell(colIndex).setCellValue(Double.parseDouble(data[colIndex-startColumn]));
            else
                xssfRow.createCell(colIndex).setCellValue(data[colIndex-startColumn]);
        }
    }
}
