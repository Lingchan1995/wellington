package proj_wellington.sml;

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

import beans.SegDiv;

public class LibS {
	private static Logger logger  = Logger.getLogger(LibS.class);
	/**
     * 
     * @param num 选取的label数量
     * @param labels 备选label表
     * @return 选取的label组
	 * @throws IOException 
     */
              public static List<String> getLabel(int num,Map<String,Float> labels) throws IOException{
            	  List<String> labelF=new ArrayList<String>();
            	  logger.info("正在选取label，数目"+num);
            	  
            	File file1=new File("testData/labelF.txt");
          		FileAct.checkFile(file1);
          		OutputStream f1= new FileOutputStream(file1);
            	  
            	  for(int i=0;i<num;i++) {
            		  float comp=(float)-1.0;
            		  String temp="";
            		  
            		  for(String key:labels.keySet()) {
            			  if(labels.get(key)>=comp){ 
            				temp=key;
            				comp=labels.get(key);
            			  }
            		  }
                    f1.write(temp.getBytes());
                    f1.write((labels.get(temp).toString()+"\n").getBytes());
                    labelF.add(temp);
                    labels.remove(temp);
            	  }
            	logger.info("完成，数目"+num);  
            	f1.close();
            	
            	return labelF;
              }
         
              /**
               * 
               * @param num 所有分词后的text的信息，包括编号，分词，classification
               * @param label label参数表
               * @return label化之后的text的信息
               */
              public static List<SegDiv> dataTransf(List<SegDiv> collected,List<String> label){
            	  List<SegDiv> Deals = new ArrayList<SegDiv>();
            	  logger.info("正在转化text向量");
            	  
            	  //转移每条text的信息
            	  for(SegDiv token:collected) {
            		  
            		  SegDiv temp=new SegDiv();
            		  Map<String,Integer> vector=new HashMap<String,Integer>();
            		  
            		  temp.setNum(token.getNum());
            		  temp.setClassification(token.getClassification()); 
            		  
            		   //按label给每条text设定词向量
            		  for(String take:label) {
                        //如果text分词中有该词则写入该词频率
            			  if(token.getSegment().containsKey(take))
            				  vector.put(take, token.getSegment().get(take));
            			 //无该词则写入为0
            			  else vector.put(take, 0);
            		  }
            		 temp.setSegment(vector);
            		 //转化后存储在临时表中
            		 Deals.add(temp);
            	  }
            	  logger.info("完成转化向量");  
               return Deals;
              }
}
