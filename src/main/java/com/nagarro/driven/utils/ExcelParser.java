package com.nagarro.driven.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelParser {

    public static List<Map<String, String>> readExcel(String filePath, String teams) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> dataMap = new LinkedHashMap<>();
                for (int j = 0; j < colCount; j++) {
                    String header = headerRow.getCell(j).getStringCellValue().trim();
                    Cell cell = row.getCell(j);
                    String value = (cell == null) ? "" : cell.toString().trim();
                    dataMap.put(header, value);
                }
                dataList.add(dataMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void writeResult(String filePath, List<Map<String, String>> results) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Result");

            if (results.isEmpty()) return;
            Row header = sheet.createRow(0);
            List<String> headers = new ArrayList<>(results.get(0).keySet());
            for (int i = 0; i < headers.size(); i++) {
                header.createCell(i).setCellValue(headers.get(i));
            }

            int rowNum = 1;
            for (Map<String, String> rowMap : results) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String key : headers) {
                    row.createCell(colNum++).setCellValue(rowMap.getOrDefault(key, ""));
                }
            }

            for (int i = 0; i < headers.size(); i++) sheet.autoSizeColumn(i);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
