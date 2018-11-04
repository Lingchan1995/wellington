package tfidf;

import svm_service.svm_train;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import beans.SegDiv;
import svm_service.svm_predict;
import svm_service.svm_scale;

public class SVM_tf {
	private static Logger logger  = Logger.getLogger(SVM_tf.class);
	
	public static void svmtrain(String sysDoc,String separator) throws IOException {
	
	String[] arg = { sysDoc+separator+"train1.txt", // path of training data
			         sysDoc+separator+"model_r.txt" }; //path of model

    String[] parg = { sysDoc+separator+"train2.txt", // path of testing data
    		          sysDoc+separator+"model_r.txt", // 
    		          sysDoc+separator+"out_r.txt" }; // result path
    
    String[] scale1= {"-l","-1.0",
    		         "-u","1.0",
    		         sysDoc+separator+"train1.txt"};
    
    String[] scale2= {"-l","-1.0",
	                  "-u","1.0",
	                  sysDoc+separator+"train2.txt"};
    svm_train st=new svm_train();
    svm_predict sp=new svm_predict();
    svm_scale ss=new svm_scale();
     
           System.out.println("........SVM start..........");
           ss.main(scale1);
           ss.main(scale2);
           
         // 创建一个训练对象
           logger.info("training");
           st.main(arg); // 调用
           logger.info("testing");
           sp.main(parg); // 调用
           logger.info("predicted");
	}
}
