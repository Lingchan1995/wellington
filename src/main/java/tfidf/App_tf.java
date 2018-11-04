package tfidf;

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
public class App_tf 
{
    public static void Tfidf(String sysDoc,String separator,String trainFile,String testFile) throws IOException
    {
        PropertyConfigurator.configure(sysDoc+separator+"src"+
                             separator+"main"+separator+"java"+
	                         separator+"log4j.properties");
        
        File filelist=new File(sysDoc+separator+trainFile);  
        File testlist=new File(sysDoc+separator+testFile);
        
        List<Message> dataStart=new ArrayList<Message>();
        List<Message> dataTest=new ArrayList<Message>();
        dataStart =Data_tf.readExcel(filelist);
        dataTest =Data_tf.readExcel(testlist);
        
        List<SegDiv> dataSeg=new ArrayList<SegDiv>();
        List<SegDiv> testSeg=new ArrayList<SegDiv>();
		dataSeg=Data_tf.getSegmentation(dataStart,sysDoc,separator);
		testSeg=Data_tf.getSegmentation(dataTest,sysDoc,separator);
		
		Map<String,Float> labels=new HashMap<String, Float>();
		labels=Data_tf.getTFIDF(sysDoc,separator,dataSeg);
		
		List<String> reLabel=new ArrayList<String>();
		reLabel=LibS_tf.getLabel(2500, labels,sysDoc,separator);
        
		LibS_tf.dataTransf(dataSeg, reLabel,
				new File(sysDoc+separator+"train1.txt"));
		LibS_tf.dataTransf(testSeg, reLabel,
				new File(sysDoc+separator+"train2.txt"));

		SVM_tf.svmtrain(sysDoc,separator);
    }
}
