package mainOper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
	
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	/**
	 * excel tool
	 * @author sun.kai
	 * 2016年8月21日
	 */
	public class ReadingXls {
		private static Logger logger  = Logger.getLogger(ReadingXls.class);
		private final static String xls = "xls";
		private final static String xlsx = "xlsx";
		
		/**
		 * read excel file
		 * @param file
		 * @throws IOException 
		 */
		public static List<String[]> readExcel(File file) throws IOException{
			//checkfile
			checkFile(file);
	    	//get Workbook obj
	    	Workbook workbook = getWorkBook(file);
	    	//create return obj and store each line of the 
	    	//result in string array
	    	List<String[]> list = new ArrayList<String[]>();
	    	if(workbook != null){
	    		for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
	    			//get sheet
	        		Sheet sheet = workbook.getSheetAt(sheetNum);
	        		if(sheet == null){
	        			continue;
	        		}
	        		//get start row
	        		int firstRowNum  = sheet.getFirstRowNum();
	        		//get end row
	        		int lastRowNum = sheet.getLastRowNum();
	        		//iterate each row except start
	        		for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
	        			//get current row
	        			Row row = sheet.getRow(rowNum);
	        			if(row == null){
	        				continue;
	        			}
	        			//get the start column
	        			int firstCellNum = row.getFirstCellNum();
	        			//get column number
	        			int lastCellNum = row.getPhysicalNumberOfCells();
	        			
	        			String[] cells = new String[row.getPhysicalNumberOfCells()];
	        			//get each cell of the column
	        			for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
	        				Cell cell = row.getCell(cellNum);
	                        cells[cellNum] = getCellValue(cell);
	        			}
	        			list.add(cells);
	        		}
	    		}
	    		workbook.close();
	    	}
			return list;
	    }
		public static void checkFile(File file) throws IOException{
			//file exist?
	    	if(null == file){
	    		logger.error("file no exist！");
	    		throw new FileNotFoundException("file no exist！");
	    	}
			//get file name
	    	String fileName = file.getName();
	    	//judge whether excel file
	    	if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
	    		logger.error(fileName + "not excel");
	    		throw new IOException(fileName + "not excel");
	    	}
		}
		public static Workbook getWorkBook(File file) {
			//get file name
	    	String fileName = file.getName();
	    	//create workbook obj
			Workbook workbook = null;
			try {
				InputStream is = new FileInputStream(file);
				//create file with different ends
				if(fileName.endsWith(xls)){
					//2003
					workbook = new HSSFWorkbook(is);
				}else if(fileName.endsWith(xlsx)){
					//2007
					workbook = new XSSFWorkbook(is);
				}
			} catch (IOException e) {
				logger.info(e.getMessage());
			}
			return workbook;
		}
		public static String getCellValue(Cell cell){
			String cellValue = "";
			if(cell == null){
				return cellValue;
			}
			//reading each cell value
			if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
			//judge cell value class
	        switch (cell.getCellType()){
		        case Cell.CELL_TYPE_NUMERIC: //number
		            cellValue = String.valueOf(cell.getNumericCellValue());
		            break;
		        case Cell.CELL_TYPE_STRING: //string
		            cellValue = String.valueOf(cell.getStringCellValue());
		            break;
		        case Cell.CELL_TYPE_BOOLEAN: //Boolean
		            cellValue = String.valueOf(cell.getBooleanCellValue());
		            break;
		        case Cell.CELL_TYPE_FORMULA: //formular
		            cellValue = String.valueOf(cell.getCellFormula());
		            break;
		        case Cell.CELL_TYPE_BLANK: //empty
		            cellValue = "";
		            break;
		        case Cell.CELL_TYPE_ERROR: //error
		            cellValue = "illegal";
		            break;
		        default:
		            cellValue = "unknow";
		            break;
	        }
			return cellValue;
		}
}
