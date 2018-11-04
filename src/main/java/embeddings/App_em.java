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
public class App_em 
{
    public static void Embeddings(String sysDoc,String separator,
    		String trainFile,String testFile,String dic) throws IOException
    {
        PropertyConfigurator.configure(sysDoc+separator+"src"+
                             separator+"main"+separator+"java"+
                             separator+"log4j.properties");
    	
        File filelist=new File(sysDoc+separator+trainFile);  
        File testlist=new File(sysDoc+separator+testFile);
        
        List<Message> dataStart=new ArrayList<Message>();
        List<Message> testStart=new ArrayList<Message>();
        dataStart =Data_em.readExcel(filelist);
        testStart =Data_em.readExcel(testlist);
        
        List<Message> datasegments=new ArrayList<Message>();
        List<Message> testsegments=new ArrayList<Message>();
		datasegments=Data_em.getSegmentation(dataStart);
		testsegments=Data_em.getSegmentation(testStart);
		
		Data_em.outputsen(datasegments,testsegments,
				sysDoc,separator);
		
		List<EmbeddingA> dataattri=new ArrayList<EmbeddingA>();
		List<EmbeddingA> testattri=new ArrayList<EmbeddingA>();
		dataattri=Data_em.getAttributes(datasegments);	
		testattri=Data_em.getAttributes(testsegments);		
		
		if(dic=="-1") {
		String material;
		material=Data_em.getMaterial(dataattri);
		material=material+Data_em.getMaterial(testattri);
		Embedding.buildModel(material,
				   new File(sysDoc+separator+"vec.txt"),
				   sysDoc,separator);
		}
		
		List<Vec> dataprepare=new ArrayList<Vec>();
	    List<Vec> testprepare=new ArrayList<Vec>();
		dataprepare=Transfer.transferVec(dic,dataattri,sysDoc,separator);
		testprepare=Transfer.transferVec(dic,testattri,sysDoc,separator);
		
		SVM_em.writeTra(sysDoc,separator,dataprepare);
		SVM_em.writeTst(sysDoc,separator,testprepare);
		SVM_em.svmtrain(sysDoc,separator);
    	
    }
}
