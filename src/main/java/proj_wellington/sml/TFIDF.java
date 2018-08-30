package proj_wellington.sml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.SegDiv;

/**
 * 计算每个文档的tf值
 * @param wordAll 每个文本的分词后单词组，用space隔开
 * @return Map<String,Float> key是单词 value是tf值
 */
public class TFIDF {
	public static Map<String,Float> tfCalculate(HashMap<String, Integer> dict){ //存放（单词，单词数量）
        //存放（单词，单词词频）
        HashMap<String, Float> tf = new HashMap<String, Float>();
        int wordCount=0;

        /**
         * 单词的tf=该单词出现的数量n/总的单词数wordCount
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
     * @param D 总文档数
     * @param doc_words 每个文档对应的分词
     * @param tf 计算好的tf,用这个作为基础计算tfidf
     * @return 每个文档中的单词的tfidf的值
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
