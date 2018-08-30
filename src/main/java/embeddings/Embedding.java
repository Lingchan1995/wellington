package embeddings;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;


public class Embedding {
	private static Logger logger  = Logger.getLogger(Embedding.class);
	
	public static void buildModel(String material, File file) throws IOException {
	 int batchSize = 1000;
     int iterations = 3;
     int layerSize = 2500;
     
     /*加载语句语料，*/
     SentenceIterator iter=new LineSentenceIterator
    		 (new File("testData/sentences.txt"));
     /*String 转化为 tokenizer格式，加载分词结果*/
     logger.info("not confirmed code");
     TokenizerFactory tokenizer = new DefaultTokenizerFactory();
     tokenizer.create(material);
     
     logger.info("Build model....");
     Word2Vec vec = new Word2Vec.Builder()
             .batchSize(batchSize) //# words per minibatch.
             .minWordFrequency(1) //
             .useAdaGrad(false) //
             .layerSize(layerSize) // word feature vector size
             .iterations(iterations) // # iterations to train
             .learningRate(0.025) //
             .minLearningRate(1e-3) // learning rate decays wrt # words. floor learning
             .negativeSample(10) // sample size 10 words
             .iterate(iter) //
             .tokenizerFactory(tokenizer)
             .build();
     vec.fit();
     
     if (file != null) {
         logger.info("model will be write to path[{}]"+file.getAbsolutePath());
         WordVectorSerializer.writeWordVectors(vec, file);
       } else {
         logger.info("model will not be saved");
       }
     }
}
