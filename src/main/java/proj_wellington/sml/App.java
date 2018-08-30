package proj_wellington.sml;

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
        
        List<SegDiv> segments=new ArrayList<SegDiv>();
		segments=CollectData.getSegmentation(dataStart);
		
		Map<String,Float> labels=new HashMap<String, Float>();
		labels=CollectData.getTFIDF(segments);
		
		List<String> reLabel=new ArrayList<String>();
		reLabel=LibS.getLabel(labels.size()/4, labels);
        
		List<SegDiv> dataVec=new ArrayList<SegDiv>();
		dataVec=LibS.dataTransf(segments, reLabel);
		
		SVM.writeText(dataVec);
		SVM.svmtrain();
    }
}
