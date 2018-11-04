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

import beans.SegDiv;

public class LibS_tf {
	private static Logger logger  = Logger.getLogger(LibS_tf.class);
	/**
     * 
     * @param num scale of attributes
     * @param labels labels preparing to be selected
     * @return attributes selected
	 * @throws IOException 
     */
       public static List<String> getLabel(int num,Map<String,Float> labels, 
    		                               String sysDoc,String separator) 
    		                               throws IOException{
    	   
            	  List<String> labelF=new ArrayList<String>();
            	  logger.info("choosing label, number is "+num);
            	  
            	File file1=new File(sysDoc+separator+"labelF.txt");
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
            	logger.info("achieved with "+num);  
            	f1.close();
            	
            	return labelF;
              }
         
              /**
               * 
               * @param collected data item with segmented words and words' frequencies
               * @param label attributes
               * @param file where to store transfering result
               * @throws IOException
               */
              public static void dataTransf(List<SegDiv> collected,List<String> label,
            		                        File file) throws IOException{
            	  FileAct.checkFile(file);
            	  OutputStream f1= new FileOutputStream(file);
            	  logger.info("transfering to vector");
            	  
            	  //transfer
            	  for(SegDiv token:collected) {
            		  
            		  SegDiv temp=new SegDiv();
            		  Map<String,Integer> vector=new HashMap<String,Integer>();
            		  
            		  temp.setNum(token.getNum());
            		  temp.setClassification(token.getClassification()); 
            		  
            		   //set attributes with words'frequencies
            		  for(String take:label) {
                        //if text have the word, update the frequency
            			  if(token.getSegment().containsKey(take))
            				  vector.put(take, token.getSegment().get(take));
            			 //no word means 0
            			  else vector.put(take, 0);
            		  }
            		 temp.setSegment(vector);
            		 //store in temp variable
            		 
            		String classifi;
         			if(temp.getClassification().equals("Y"))
         			     classifi="1 ";		
         			else classifi="0 ";
         			
         			f1.write(classifi.getBytes());
         			
         			Map<String,Integer> keys=new HashMap<String, Integer>();
         			keys=temp.getSegment();
         			int labelnum=1; 
         			
         			   for(String key:keys.keySet()) {
         				classifi=labelnum+":"+keys.get(key).toString()+" ";
         				f1.write(classifi.getBytes());
         				labelnum++;
         			   }
         			   
         			classifi="\n";
         			f1.write(classifi.getBytes());
            	  }
            	  logger.info("achieved transfering");  
              }
}
