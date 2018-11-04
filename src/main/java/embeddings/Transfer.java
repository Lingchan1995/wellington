package embeddings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import beans.EmbeddingA;
import beans.Vec;

public class Transfer {
	private static Logger logger  = Logger.getLogger(Transfer.class);
            /**
             * transfer double array to list
             * @param vec
             * @return
             */
	        public static List<Double> double2List(double[] vec){
	        	List<Double> temp=new ArrayList<Double>();
	        	
	        	for(int i=0;i<vec.length;i++) {
	        		temp.add(i, vec[i]);
	        	}
	        	return temp;
	        }
	
            @SuppressWarnings("null")
            /**
             * count the result of two vectors
             * @param a1 vector1
             * @param a2 vector2
             * @return
             */
			public static List<Double> countVec(List<Double> a1,List<Double> a2){
            	 double temp;
            	 List<Double> vectemp =new ArrayList<Double>();
            	 
            	 for(int i=0;i<a1.size();i++) {
            		 temp=a1.get(i)+a2.get(i);
            		 vectemp.add(temp);
            	 }
            	 return vectemp;
             }
             
            /**
             * 
             * @param dic vector dictionary
             * @param attributes vector expression of each 
             *        word in the data item
             * @param sysDoc
             * @param separator
             * @return document vec representation of each item
             * @throws IOException
             */
             public static List<Vec> transferVec(String dic,
            		 List<EmbeddingA> attributes,
            		 String sysDoc,String separator) throws IOException{
            	  
            	 File gModel;
            	 if(dic.equals("-1"))
            	    gModel = new File(sysDoc+separator+"vec.txt"); 
            	 else
            		gModel = new File(sysDoc+separator+dic);
            	 
            	 WordVectors wordVectors = WordVectorSerializer.loadTxtVectors(gModel);
            	 List<Vec> vectemp=new ArrayList<Vec>();
            	 
            	 for(EmbeddingA temp:attributes) {
            		List<Double> vecTemp=new ArrayList<Double>();
            		Vec stm=new Vec();
            		
            		for(int i=0;i<300;i++)
            			vecTemp.add(i,0.0);
            		
            		for(int i=0;i<temp.getAttributes().size();i++) {
            			List<Double> vecTe=new ArrayList<Double>();
            			String word=temp.getAttributes().get(i);
            			
            			if(wordVectors.hasWord(word)) {
            				vecTe=Transfer.double2List(wordVectors.getWordVector(word));
            				vecTemp=Transfer.countVec(vecTe,vecTemp);
            			}
            		}
            		
            		stm.setClassification(temp.getClassification());
            		stm.setNum(temp.getNum());
            		stm.setVector(vecTemp);
            		vectemp.add(stm);
            	 }
               return vectemp;
             }
}
