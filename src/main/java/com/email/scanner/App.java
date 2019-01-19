package com.email.scanner;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

public class App {
    public static SimpleGUI simpleGUI;
    //public static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        simpleGUI = new SimpleGUI();
        simpleGUI.setVisible(true);
    }

    public static void startread(String path) {
        try {
            InputStream excelFile = new BufferedInputStream(new FileInputStream(new File(path)));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                DataFormatter formatter = new DataFormatter();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    //logger.info(formatter.formatCellValue(currentCell));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
