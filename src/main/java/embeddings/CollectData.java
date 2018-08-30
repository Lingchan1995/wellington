package embeddings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import beans.EmbeddingA;
import beans.Message;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;

import proj_wellington.sml.ReadingXls;

public class CollectData {
	private static Logger logger  = Logger.getLogger(CollectData.class);
	
	/**
     * 
     * @param MultipartFile 文件路径
     * @return 读取后的文件数据，分别为编号，内容，classification
	 * @throws IOException 
     */
	public static List<Message> readExcel(File excelFile) throws IOException{
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
	public static List<Message> getSegmentation(List<Message> allData) throws IOException{
		List<Message> collected = new ArrayList<Message>();
	    File file=new File("testData/sentences.txt");
	    OutputStream f1= new FileOutputStream(file);
	    
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
		       for(Message token:allData){
		        Message temp=new Message();
		    	   
                 if(token.getMessage().equals(null)==false){
		    	  temp.setNum(token.getNum()); 
		    	  temp.setMessage(Segmentation.getSegmentation
		    			  (token.getMessage(),pipeline).toString());
		    	  
				  f1.write((temp.getMessage()+"\n").getBytes());//写入所有文本备用embedding
		    	  
		    	  temp.setClassification(token.getClassification());
		    	 
		    	  collected.add(temp);
                }
		       }
		   logger.info("分词完成");
		    
		   f1.close(); 
		return collected;
	}
	
	/**
     * 
     * @param text 分词数据，分词用空格在String中隔开
     * @return 集合所有的分词语料
	 * @throws IOException 
     */
	public static String getMaterial(List<EmbeddingA> text) {
		StringBuffer txt=new StringBuffer();
		List<String> labels=new ArrayList<String>();
		
		for(EmbeddingA token:text)
           for(String attr: token.getAttributes())
              if(labels.contains(attr)==false)
            	  labels.add(attr);
		
		for(String label:labels)
			txt.append(label+" ");
		
		return txt.toString();
	}
	
	/**
     * 
     * @param text 分词数据，分词用空格在String中隔开
     * @return 每条数据的分词attribute
	 * @throws IOException 
     */
	public static List<EmbeddingA> getAttributes(List<Message> text) {
		List<EmbeddingA> emba=new ArrayList<EmbeddingA>();
		
		for(Message token:text) {
			List<String> attri=new ArrayList<String>();
			EmbeddingA t2=new EmbeddingA();
			String temp=token.getMessage();
			StringBuffer tem=new StringBuffer();
			
			for(int i=0;i<temp.length();i++) {
				if(temp.charAt(i)==' ') {
					if(tem.equals(null)==false)
					attri.add(tem.toString());
				
					tem.delete(0, tem.length());
				}
				else
				 tem.append(temp.charAt(i));
			}
		  t2.setNum(token.getNum());
		  t2.setClassification(token.getClassification());
		  t2.setAttributes(attri);
		  emba.add(t2);
		}
		return emba;
	}
}
