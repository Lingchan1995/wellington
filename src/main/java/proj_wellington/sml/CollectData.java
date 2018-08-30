package proj_wellington.sml;

import java.io.File;
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

public class CollectData {
	private static Logger logger  = Logger.getLogger(CollectData.class);
	
	/**
     * 
     * @param MultipartFile 文件路径
     * @return 读取后的文件数据，分别为编号，内容，classification
     */
	public static List<Message> readExcel(File excelFile){
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
     * @param allData 读取后的初始文件数据，分别为编号，内容，classification
     * @return 处理后分词数据，分别为编号，分词内容，classification
	 * @throws IOException 
     */
	public static List<SegDiv> getSegmentation(List<Message> allData) throws IOException{
		List<SegDiv> collected = new ArrayList<SegDiv>();
		File file1=new File("testData/segments.txt");
		FileAct.checkFile(file1);
		OutputStream f1= new FileOutputStream(file1);
		
		logger.info("正在进行分词");
		
        // 载入自定义的Properties文件
		/*StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties"); */
		
        StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit",
                "ssplit.isOneSentence", "true",
                "tokenize.language", "zh",
                "segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
                "segment.sighanCorporaDict", "edu/stanford/nlp/models/segmenter/chinese",
                "segment.serDictionary", "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
                "segment.sighanPostProcessing", "true"
        ));
		       for(Message token:allData) {
                if(token.getMessage().equals(null)==false){
		    	  Map<String,Integer> temp = new HashMap<String,Integer>();
		    	  String txt;
                  temp=Segmentation.getSegmentation(token.getMessage(),pipeline);
		    	  
		    	  SegDiv te=new SegDiv();
		    	  te.setSegment(temp);
		    	  te.setClassification(token.getClassification());
		    	  te.setNum(token.getNum());
		    	  
		    	  txt=token.getNum()+" "+temp.toString()+" "+token.getClassification()+"\n";
		    	  f1.write(txt.getBytes());
		    	  
		    	  collected.add(te);
                } 
		       }
		   logger.info("分词完成");
		   f1.close();;
		return collected;
	}

	/**
     * 
     * @param collected 处理后分词数据，分别为编号，分词内容，classification
     * @return label表，内含每个单词对应的最大tfidf值，作为备选label
	 * @throws IOException 
     */
	public static Map<String,Float> getTFIDF(List<SegDiv> collected) throws IOException{
		int size=collected.size();
		Map<String,Float> label=new HashMap<String,Float>();
		File file1=new File("testData/label.txt");
		FileAct.checkFile(file1);
		OutputStream f1= new FileOutputStream(file1);
		
		logger.info("正在计算TFIDF值并捕捉label");
		
		for(SegDiv tem:collected) {
			//计算单个文档每个分词的TF
			Map<String, Float> tf = new HashMap<String, Float>();
			 tf=TFIDF.tfCalculate((HashMap<String, Integer>)tem.getSegment());
			
			//计算单个文档每个的TFIDF
			 Map<String,Float> tfidf=new HashMap<String, Float>();
			 tfidf=TFIDF.tfidfCalculate(size, collected, tf);
			 
			//将计算好的单词放入label表中备选
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
		
		logger.info("label捕捉完成");
		return label;
	}
}
