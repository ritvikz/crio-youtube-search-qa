package demo.utils;

import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

    @DataProvider(name = "fetchData")
    public static Object[][] fetchData() {
        String fileLocation = System.getProperty("user.dir") + "/src/test/resources/data.xlsx";
        return ExcelReaderUtil.readExcelData(fileLocation);
    }
}