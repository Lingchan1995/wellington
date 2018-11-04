package combinexml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Combination {
	/**
	 * 
	 * @param file1 old file to combine
	 * @param file2 new file to combile
	 * @param sysDoc
	 * @param separator
	 * @throws IOException
	 */
    public static void combine(String file1,String file2,String sysDoc,String separator) throws IOException {
    	File f1= new File(sysDoc+separator+file1);
    	File f2= new File(sysDoc+separator+file2);
    	File f3= new File(sysDoc+separator+file1);
        List<String[]>tempf1=new ArrayList<String[]>();
        List<String[]>tempf2=new ArrayList<String[]>();
        
        tempf1=ReadingXls.readExcel(f1);
        tempf2=ReadingXls.readExcel(f2);
        
        Combination.combination(f3, tempf1, tempf2);
        
    }
    
    public static void combination(File file, List<String[]> s1, List<String[]> s2) throws IOException {
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
    	    for (String[] key:s1) {
    	        XSSFRow row1 = sheet.createRow(i++);
    	        //build the cell and the value in each line
    	        row1.createCell(0).setCellValue(key[0]);
    	        row1.createCell(1).setCellValue(key[1]);
    	        
    	        if(key.length==3)
    	        row1.createCell(2).setCellValue(key[2]);
    	    }
    	    
    	    for (String[] key:s2) {
    	        XSSFRow row1 = sheet.createRow(i++);
    	        //build the cell and the value in each line
    	        row1.createCell(0).setCellValue(key[0]);
    	        row1.createCell(1).setCellValue(key[1]);
    	        
    	        if(key.length==3)
    	        row1.createCell(2).setCellValue(key[2]);
    	    }
				FileOutputStream fos = new FileOutputStream(file);
    	        workbook.write(fos);
    	        fos.close();
    }
}
