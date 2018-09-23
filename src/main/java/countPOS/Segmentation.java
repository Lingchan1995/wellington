package countPOS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Segmentation {
    
	private static Logger logger  = Logger.getLogger(Segmentation.class);
	/**
     * 
     * @param text 单条文本内容
     * @return 处理后分词数据，排除标点符号后，每个单词的出现频率
     */
	public static Map<String,String> getSegmentation(String text,StanfordCoreNLP pipeline) {
		            //存储分词结果和对应数目的数组
		            Map<String,String> segment = new HashMap<String,String>();           

					// 用一些文本来初始化一个注释。文本是构造函数的参数。
					Annotation annotation;
					annotation = new Annotation(text);

					// 运行所有选定的代码在本文
					pipeline.annotate(annotation);
					
					List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

			        //从CoreMap 中取出CoreLabel List ,打印
			        for (CoreMap sentence : sentences){
			            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){
			                String word = token.get(CoreAnnotations.TextAnnotation.class);
			                String pos = token.getString(PartOfSpeechAnnotation.class);
								if(!segment.containsKey(word))
									segment.put(word, pos); 
			            }
			          }
					
			return segment;
	}
}