package embeddings;

import java.io.File;
import java.io.FileNotFoundException;
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
import mainOper.ReadingXls;

public class Data_em {
	private static Logger logger  = Logger.getLogger(Data_em.class);
	
	/**
     * 
     * @param MultipartFile file path
     * @return data read
	 * @throws IOException 
     */
	public static List<Message> readExcel(File excelFile) throws IOException{
	    List<Message> allUsers = new ArrayList<Message>();
	    
	    logger.info("reading excel file");
	    
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
		    logger.info("fail to read excel", e);
	       }

	   return allUsers;
	}
	
	/**
     * 
     * @param allData original data with id and content
     * @return data after segmentation
	 * @throws IOException 
     */
	public static List<Message> getSegmentation(List<Message> allData) throws IOException{
		List<Message> collected = new ArrayList<Message>();
   
		logger.info("Segementing");
		
        // 载入自定义的Properties文件
		//StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties"); 
		
        /*StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit",
                "ssplit.isOneSentence", "true",
                "tokenize.language", "zh",
                "segment.model", "edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
                "segment.sighanCorporaDict", "edu/stanford/nlp/models/segmenter/chinese",
                "segment.serDictionary", "edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
                "segment.sighanPostProcessing", "true"               
        ));*/
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
		       for(Message token:allData){
		        Message temp=new Message();
		    	   
                 if(token.getMessage().equals(null)==false
                		 &&token.getMessage().isEmpty()==false){
		    	  temp.setNum(token.getNum()); 
		    	  temp.setMessage(Segmentation_em.getSegmentation
		    			  (token.getMessage(),pipeline).toString());

		    	  temp.setClassification(token.getClassification());
		    	} 
		    	  collected.add(temp);
		       }
		   logger.info("achieve segmentation");
		    
		   
		return collected;
	}
	/**
	 * output the embedding material 
	 * @param datatra training data
	 * @param datatst testing data
	 * @param sysDoc
	 * @param separator
	 * @throws IOException
	 */
	public static void outputsen(List<Message> datatra, 
			List<Message> datatst,String sysDoc,
            String separator) throws IOException {
		
	    File file=new File(sysDoc+separator+"sentences.txt");
	    OutputStream f1= new FileOutputStream(file);
	    
	    for(Message temp:datatra)
	    f1.write((temp.getMessage()+"\n").getBytes());
	    
	    for(Message temp:datatst)
		f1.write((temp.getMessage()+"\n").getBytes());
	    
	    f1.close(); 
	}
	/**
     * 
     * @param text data to be segmented
     * @return result of segmentation
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
     * @param text segmentation result, each word is splitted with space
     * @return single word attribute
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
