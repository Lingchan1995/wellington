package embeddings;

import svm_service.svm_train;
import tfidf.FileAct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import beans.SegDiv;
import beans.Vec;
import svm_service.svm_predict;
import svm_service.svm_scale;

public class SVM_em {
	private static Logger logger  = Logger.getLogger(SVM_em.class);
	
	@SuppressWarnings("static-access")
	/**
	 * 
	 * @param sysDoc
	 * @param separator
	 * @throws IOException
	 */
	public static void svmtrain(String sysDoc,String separator) throws IOException {
	
	String[] arg = { sysDoc+separator+"trainE1.txt", // training data path
			         sysDoc+separator+"modelE_r.txt" }; // training model path

    String[] parg = { sysDoc+separator+"trainE2.txt", // testing data path
    		          sysDoc+separator+"modelE_r.txt", // 
    		          sysDoc+separator+"outE_r.txt" }; // output result path
    
    String[] scale1= {"-l","-1.0",
    		         "-u","1.0",
    		         sysDoc+separator+"trainE1.txt"};
    
    String[] scale2= {"-l","-1.0",
	                  "-u","1.0",
	                  sysDoc+separator+"trainE2.txt"};
    svm_train st=new svm_train();
    svm_predict sp=new svm_predict();
    svm_scale ss=new svm_scale();
     
           System.out.println("........SVM start..........");
           ss.main(scale1);
           ss.main(scale2);
           
         // 创建一个训练对象
           logger.info("training");
           st.main(arg); // 调用
           logger.info("predicting");
           sp.main(parg); // 调用
           logger.info("achieved");
	}
	
	/**
	 * 
	 * @param sysDoc
	 * @param separator
	 * @param dataVec data to be transfered
	 * @throws IOException
	 */
	public static void writeTra(String sysDoc,String separator,
						List<Vec> dataVec) throws IOException {
		
		File file1=new File(sysDoc+separator+"trainE1.txt");
		FileAct.checkFile(file1);
		OutputStream f1= new FileOutputStream(file1);

		logger.info("transfering training data");
		//输出训练数据
		for(int i=0;i<dataVec.size();i++) {
			f1.write((dataVec.get(i).toString()+"\n")
					  .getBytes());
		}
		f1.close();
		logger.info("achieved");
	}
	
	public static void writeTst(String sysDoc,String separator,
			            List<Vec> dataVec) throws IOException {
		
		File file1=new File(sysDoc+separator+"trainE2.txt");
		FileAct.checkFile(file1);
		OutputStream f1= new FileOutputStream(file1);

		logger.info("trasfering testing data");
		//输出训练数据
		for(int i=0;i<dataVec.size();i++) {
			f1.write((dataVec.get(i).toString()+"\n")
					  .getBytes());
		}
		f1.close();
		logger.info("achieved");
	}
}
