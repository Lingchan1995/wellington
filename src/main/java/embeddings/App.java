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
        //File testlist=new File("src/main/java/testData/predictset.xlsx");
        
        List<Message> dataStart=new ArrayList<Message>();
        //List<Message> testStart=new ArrayList<Message>();
        dataStart =CollectData.readExcel(filelist);
        //testStart =CollectData.readExcel(testlist);
        
        List<Message> datasegments=new ArrayList<Message>();
       // List<Message> testsegments=new ArrayList<Message>();
		datasegments=CollectData.getSegmentation(dataStart);
		//testsegments=CollectData.getSegmentation(testStart);
		
		CollectData.outputsen(datasegments, new ArrayList<Message>());
		
		List<EmbeddingA> dataattri=new ArrayList<EmbeddingA>();
		//List<EmbeddingA> testattri=new ArrayList<EmbeddingA>();
		dataattri=CollectData.getAttributes(datasegments);	
		//testattri=CollectData.getAttributes(testsegments);		
		
		String material;
		material=CollectData.getMaterial(dataattri);
		//material=material+CollectData.getMaterial(testattri);
		//material=material+GetNewSentences.getNSentence();
		
		Embedding.buildModel(material, new File("testData/vec.txt"));
		
		List<Vec> dataprepare=new ArrayList<Vec>();
		//List<Vec> testprepare=new ArrayList<Vec>();
		dataprepare=Transfer.transferVec(dataattri);
		//testprepare=Transfer.transferVec(testattri);
		
		SVM.writeText(dataprepare);
		//SVM.writeTst(testprepare);
		SVM.svmtrain();
    	
    }
}
