package com.nagarro.driven.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelParser2 {

    // ========================
    // READ EXCEL
    // ========================
    public static List<Map<String, String>> readExcel(String filePath) {
        List<Map<String, String>> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Read header
            if (!rowIterator.hasNext()) return rows; // empty sheet
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue().trim()));

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    rowData.put(headers.get(i), cell.getStringCellValue().trim());
                }

                // Skip row if required columns Team or Match Format are empty
                if (rowData.getOrDefault("Team", "").isEmpty() ||
                        rowData.getOrDefault("Match Format", "").isEmpty()) {
                    System.out.println("⚠️ Skipping incomplete row: " + rowData);
                    continue;
                }
                rows.add(rowData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // ========================
    // WRITE EXCEL (multi-sheet)
    // ========================
    public static void writeExcel(String filePath, Map<String, List<List<String>>> sheetData) {
        Workbook workbook = new XSSFWorkbook();

        sheetData.forEach((sheetName, data) -> {
            Sheet sheet = workbook.createSheet(sheetName);

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i);
                List<String> rowData = data.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }
        });

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            System.out.println("✅ Excel written successfully: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ========================
    // UTILITY: Sheet Name
    // ========================
    public static String getSheetName(String matchFormat, String statsBy) {
        if (statsBy == null || statsBy.isEmpty()) {
            statsBy = "ALL_STATS";
        }
        return (matchFormat + "_" + statsBy).toUpperCase().replace(" ", "_");
    }

    // ========================
    // WRITE EXCEL FOR SINGLE TEAM (CONVENIENCE)
    // ========================
    public static void writeExcel(String team, String matchFormat, String statsBy, List<List<String>> tableData) {
        // Create sheet data map
        Map<String, List<List<String>>> sheetData = new LinkedHashMap<>();
        sheetData.put(getSheetName(matchFormat, statsBy), tableData);

        // Ensure output folder exists
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Full file path
        String filePath = "output/" + team + ".xlsx";

        // Write Excel
        writeExcel(filePath, sheetData);
    }
}
