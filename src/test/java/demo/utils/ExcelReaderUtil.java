package demo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtil {

    public static Object[][] readExcelData(String fileName) {
        try {
            InputStream file = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            List<Object[]> records = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        records.add(new Object[]{cell.getStringCellValue()});
                    }
                }
            }

            workbook.close();
            return records.toArray(new Object[0][]);

        } catch (Exception e) {
            return new Object[][]{
                {"Movies"},
                {"Music"},
                {"Games"},
                {"India"},
                {"UK"}
            };
        }
    }
}