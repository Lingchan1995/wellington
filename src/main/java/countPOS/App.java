package countPOS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import beans.Message;
import beans.SegDiv;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	PropertyConfigurator.configure("src/main/java/log4j.properties");
    	
        File filelist=new File("src/main/java/testData/Weibo_classification_test1.xlsx");  
        
        List<Message> dataStart=new ArrayList<Message>();
        dataStart =CollectData.readExcel(filelist);
        
        List<Pos> segments=new ArrayList<Pos>();
		segments=CollectData.getSegmentation(dataStart);
		
		Map<String,StringBuffer> temp =new HashMap<String,StringBuffer>();
	    for(int i=0;i<segments.size();i++){
	    	Map<String,String> temp1=new HashMap<String,String>();
	    	 temp1=segments.get(i).getSegment();
	    	 StringBuffer newContains=new StringBuffer();
	    	 
	    	for(String index:temp1.keySet()) {
	    		if(temp.containsKey(temp1.get(index))) {
	    			System.out.println(temp1.get(index)+":"+index);
	    		}
	    		
	    		else 
	    			temp.put(temp1.get(index),new StringBuffer(index));	
	    	}
	    }
	    System.out.println("总体词性");
	    for(String index:temp.keySet()) 
          System.out.println(index+" "+temp.get(index));
    }
}
