package mainOper;
// Writen by Linghan Zhou
// use for transfer data to excel

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import beans.Message;

public class WriteExcel {
	public static void wirte2exl(List<Message> data, File file) {
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

    //writing data to the excel file
     int i=1;
    for (Message temp:data) {
        XSSFRow row1 = sheet.createRow(i++);
        //build the cell and the value in each line
        row1.createCell(0).setCellValue(temp.getNum());
        row1.createCell(1).setCellValue(temp.getMessage());
    }

    //save data in the pointed place
    try {
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();
    } catch (IOException e) {
        e.printStackTrace();
      }
   }
}
