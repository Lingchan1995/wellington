package embeddings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import beans.EmbeddingA;
import beans.Message;
import beans.Vec;

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
        
        List<Message> segments=new ArrayList<Message>();
		segments=CollectData.getSegmentation(dataStart);
		
		List<EmbeddingA> attributes=new ArrayList<EmbeddingA>();
		attributes=CollectData.getAttributes(segments);		
		
		String material;
		material=CollectData.getMaterial(attributes);
		
		Embedding.buildModel(material, new File("testData/vec.txt"));
		
		List<Vec> prepare=new ArrayList<Vec>();
		prepare=Transfer.transferVec(attributes);
		
		SVM.writeText(prepare);
		SVM.svmtrain();
    }
}
