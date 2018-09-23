package collectDB;
// Writen by Linghan Zhou
// use for transfer data to excel

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
	public static void wirte2exl(Map<String,String> dataset) {
	//create a worksheet object
	Workbook workbook = new XSSFWorkbook();
    //create a worksheet
    XSSFSheet sheet = (XSSFSheet) workbook.createSheet("dataset1");
    //create the first name line
    XSSFRow row = sheet.createRow(0);
    //create each cell name of the line
    XSSFCell cell = row.createCell(0);
    cell.setCellValue("id");
    cell = row.createCell(1);
    cell.setCellValue("post_plaintxt");
    cell = row.createCell(2);
    cell.setCellValue("is_health_related");


    //writing data to the excel file
     int i=1;
    for (String key:dataset.keySet()) {
        XSSFRow row1 = sheet.createRow(i++);
        //build the cell and the value in each line
        row1.createCell(0).setCellValue(key);
        row1.createCell(1).setCellValue(dataset.get(key));
    }

    //save data in the pointed place
    try {
        FileOutputStream fos = new FileOutputStream("src/main/java/testData/predictset.xlsx");
        workbook.write(fos);
        fos.close();
    } catch (IOException e) {
        e.printStackTrace();
      }
   }
}
