package embeddings;

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

public class Segmentation_em {
    
	private static Logger logger  = Logger.getLogger(Segmentation_em.class);
	/**
     * 
     * @param text detailed content of one data item
     * @return result of segmentation of one data item, 
     *         filtered only keep noun and verb
     *          
     */
	public static StringBuffer getSegmentation(String text,StanfordCoreNLP pipeline) {
		            //store original data item
		            StringBuffer segment = new StringBuffer();           

					// construct the object with data item
					Annotation annotation;
					annotation = new Annotation(text);
				
					// running segmentation
					pipeline.annotate(annotation);
					
					List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

			        //get the result detail
			        for (CoreMap sentence : sentences){
			            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){
			                String word = token.get(CoreAnnotations.TextAnnotation.class);
			                String pos = token.getString(PartOfSpeechAnnotation.class);
							
			                if(pos.equals("NN")||pos.equals("VV")||pos.equals("JJ")||pos.equals("AD")||pos.equals("VA")) {
						        segment.append(word+" ");
			                }
			            }
			          }
					
			return segment;
	}
}
