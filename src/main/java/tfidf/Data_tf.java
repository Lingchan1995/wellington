package tfidf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import beans.Message;
import beans.SegDiv;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

import mainOper.ReadingXls;
public class Data_tf {
	private static Logger logger  = Logger.getLogger(Data_tf.class);
	
	/**
     * 
     * @param MultipartFile file path
     * @return data read
	 * @throws FileNotFoundException 
     */
	public static List<Message> readExcel(File excelFile) throws FileNotFoundException{
	    List<Message> allUsers = new ArrayList<Message>();
	     
	    logger.info("正在读取文件");
	    
	    try {
		    List<String[]> userList = ReadingXls.readExcel(excelFile);
		    for(int i = 0;i<userList.size();i++){
			  String[] users = userList.get(i);
			  Message user = new Message();
			  
			  user.setNum(users[0]);
			  user.setMessage(users[1]);
			  
			  if(users.length==3)
			  user.setClassification("N");  
			  else user.setClassification("Y");
 
			  allUsers.add(user);
		     } 

		   } catch (IOException e) {
		    logger.info("读取excel文件失败", e);
	       }
	   return allUsers;
	}
	
	/**
     * 
     * @param allData original data with id and content
     * @return data after segmentation
	 * @throws IOException 
     */
	public static List<SegDiv> getSegmentation(
			List<Message> allData,String sysDoc,
			String separator) throws IOException{
		
		List<SegDiv> collected = new ArrayList<SegDiv>();
		File file1=new File(sysDoc+separator+"segments.txt");
		FileAct.checkFile(file1);
		OutputStream f1= new FileOutputStream(file1);
		
		logger.info("segmenting");
		
        // set up self Properties
		 /*StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");*/
        StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos",
                //"ssplit.isOneSentence", "true",
                "customAnnotatorClass.segment","edu.stanford.nlp.pipeline.ChineseSegmenterAnnotator",
                "tokenize.language", "zh",
                "segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
                "segment.sighanCorporaDict", "edu/stanford/nlp/models/segmenter/chinese",
                "segment.serDictionary", "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
                "segment.sighanPostProcessing", "true",
                "ssplit.boundaryTokenRegex","[.]|[!?]+|[\u3002]|[\uFF01\uFF1F]+",
                "pos.model","edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger"
        ));
		       for(Message token:allData) {
                if(token.getMessage().equals(null)==false
                		&&token.getMessage().isEmpty()==false){
		    	  Map<String,Integer> temp = new HashMap<String,Integer>();
		    	  String txt;
                  temp=Segmentation_tf.getSegmentation(token.getMessage(),pipeline);
		    	  
		    	  SegDiv te=new SegDiv();
		    	  te.setSegment(temp);
		    	  te.setClassification(token.getClassification());
		    	  te.setNum(token.getNum());
		    	  
		    	  txt=token.getNum()+" "+temp.toString()+" "+token.getClassification()+"\n";
		    	  f1.write(txt.getBytes());
                  collected.add(te);
                }
                else {
                	collected.add(new SegDiv());
                }
		       }
		   logger.info("achieved segmentation");
		   f1.close();;
		return collected;
	}

	/**
     * 
     * @param collected data dealt with id content and classification
     * @return label of each words' tf-idf values
	 * @throws IOException 
     */
	public static Map<String,Float> getTFIDF(
			String sysDoc,String separator,
			List<SegDiv> collected) throws IOException{
		int size=collected.size();
		Map<String,Float> label=new HashMap<String,Float>();
		File file1=new File(sysDoc+separator+"label.txt");
		FileAct.checkFile(file1);
		OutputStream f1= new FileOutputStream(file1);
		
		logger.info("getting TFIDF value of label");
		
		for(SegDiv tem:collected) {
			//count each word's tf
			Map<String, Float> tf = new HashMap<String, Float>();
			 tf=TFIDF.tfCalculate((HashMap<String, Integer>)tem.getSegment());
			
			//count single word's tf-idf
			 Map<String,Float> tfidf=new HashMap<String, Float>();
			 tfidf=TFIDF.tfidfCalculate(size, collected, tf);
			 
			//put the single result in the object
			 for(String key:tfidf.keySet()) {
				 if(label.containsKey(key)) {
					 if(label.get(key)<tfidf.get(key))
						 label.replace(key, tfidf.get(key));
				 }
				 else label.put(key, tfidf.get(key));
			 }		
		}
			 f1.write(label.toString().getBytes());
			 f1.close();
		
		logger.info("label achieved");
		return label;
	}
}
