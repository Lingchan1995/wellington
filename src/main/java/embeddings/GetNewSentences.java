package embeddings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;

public class GetNewSentences {
	private static Logger logger  = Logger.getLogger(GetNewSentences.class);
	/**
	 * 训练外部语料
	 * @return 外部语料分词内容集合
	 * @throws IOException
	 */
         public static String getNSentence() throws IOException {
        	 File senFile=new File("weibo.txt");
        	 File segFile=new File("testData/Nsen.txt");
        	 
        	 BufferedReader senIn=new BufferedReader(new FileReader(senFile));
        	 FileOutputStream segOut=new FileOutputStream(segFile);
        	 
        	 StringBuffer materialTxt=new StringBuffer();
        	 String temptxt;
        	 StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
 	                "annotators", "tokenize,ssplit",
 	                "ssplit.isOneSentence", "true",
 	                "tokenize.language", "zh",
 	                "segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
 	                "segment.sighanCorporaDict", "edu/stanford/nlp/models/segmenter/chinese",
 	                "segment.serDictionary", "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
 	                "segment.sighanPostProcessing", "true"               
 	        ));
        	 
        	 while((temptxt=senIn.readLine())!=null) {

        		 temptxt=temptxt.replaceAll("[^\u4e00-\u9fa5]*(\\s)+", "");
        		 temptxt=temptxt.replaceAll("rel=\"nofollow\">", "");
        		 String segments;
        		
        		 segments=Segmentation.getSegmentation(temptxt,pipeline).toString();
        		 logger.info(temptxt);
        		 
        		 materialTxt.append(segments+" ");
        		 segOut.write((segments+"\n").getBytes());
        	 }
         senIn.close();
         segOut.close();
         
         return materialTxt.toString();
        }
}
