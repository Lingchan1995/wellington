package WriteData;
// Writen by Linghan Zhou
// use for transfer data to excel

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
	public static void write2exl() throws NumberFormatException, IOException {
		 FileInputStream fs=new FileInputStream("src/main/java/testData/predictset.xlsx");  
		 BufferedReader r2=new BufferedReader(new FileReader("testData/outE_r.txt"));

		 XSSFWorkbook wb = new XSSFWorkbook(fs);
	     XSSFSheet sheet=wb.getSheet("dataset1");  //获取到工作表，因为一个excel可能有多个工作表  
		
    //writing data to the excel file
	    String num;
		List<Double> predictres=new ArrayList<Double>() ;
		int index=1;
	    while((num=r2.readLine())!=null) {
	        predictres.add(Double.parseDouble(num));
	        index++;
	    }
	    
	    for (int i=1;i<=sheet.getLastRowNum();i++) {
	        XSSFRow row1 = sheet.getRow(i);
	        //build the cell and the value in each line
	        if(predictres.get(i-1)==1.0)
	        row1.createCell(2).setCellValue("N");
	    }
	
	    //save data in the pointed place
	    try {
	        FileOutputStream fos = new FileOutputStream("src/main/java/testData/predictset.xlsx");
	        fos.flush();
	        wb.write(fos);
	        fos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	      }
	   }
	
	 public static void main(String args[]) throws NumberFormatException, IOException {
		 WriteExcel.write2exl();
	 }
}
