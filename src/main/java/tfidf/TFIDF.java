package tfidf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.SegDiv;

public class TFIDF {
	/**
	 * 
	 * @param dict word with frequency
	 * @return tf value of each word
	 */
	public static Map<String,Float> tfCalculate(HashMap<String, Integer> dict){
		
        HashMap<String, Float> tf = new HashMap<String, Float>();
        int wordCount=0;

        /**
         * tf=word num / total word num
         */
        for(String key:dict.keySet())
        	wordCount+=dict.get(key);
        
        for(Map.Entry<String, Integer> entry:dict.entrySet()){
            float wordTf=(float)entry.getValue()/wordCount;
            tf.put(entry.getKey(), wordTf);
        }
        return tf;
    } 
	
	/**
     * 
     * @param D document number
     * @param doc_words segmentation of each document
     * @param tf tf value
     * @return each word's tf-id in the document
     */
	 public static Map<String,Float> tfidfCalculate(int D, List<SegDiv> doc_words,Map<String,Float> tf){

	        HashMap<String,Float> tfidf=new HashMap<String, Float>();
	        for(String key:tf.keySet()){
	            int Dt=0;
	            for(SegDiv entry:doc_words){

	                Map<String,Integer> words= new HashMap<String,Integer>();
	                	words=entry.getSegment();

	                if(words.containsKey(key)){
	                    Dt++;
	                }
	            }
	            float idfvalue=(float) Math.log(Float.valueOf(D)/Dt);
	            tfidf.put(key, idfvalue * tf.get(key));
	        }       
	        return tfidf;
	    }
}
