package proj_wellington.sml;

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

public class SVM {
	private static Logger logger  = Logger.getLogger(SVM.class);
	
	public static void svmtrain() throws IOException {
	
	String[] arg = { "testData/train2.txt", // 存放SVM训练模型用的数据的路径
	                 "testData/model_r.txt" }; // 存放SVM通过训练数据训/ //练出来的模型的路径

    String[] parg = { "testData/train1.txt", // 这个是存放测试数据
	                  "testData/model_r.txt", // 调用的是训练以后的模型
	                  "testData/out_r.txt" }; // 生成的结果的文件的路径
    
    String[] scale1= {"-l","-1.0",
    		         "-u","1.0",
                     "testData/train1.txt"};
    
    String[] scale2= {"-l","-1.0",
	                  "-u","1.0",
                      "testData/train2.txt"};
    svm_train st=new svm_train();
    svm_predict sp=new svm_predict();
    svm_scale ss=new svm_scale();
     
           System.out.println("........SVM运行开始..........");
           ss.main(scale1);
           ss.main(scale2);
           
         // 创建一个训练对象
           logger.info("正在训练");
           st.main(arg); // 调用
           logger.info("正在预测");
           sp.main(parg); // 调用
           logger.info("预测完成");
	}
	
	/**
     * 将数据转化为libsvm接受的格式
     * @param dataVec 转化为向量的text数据
     */
	public static void writeText(List<SegDiv> dataVec) throws IOException {
		File file1=new File("testData/train1.txt");
		File file2=new File("testData/train2.txt");
		
		FileAct.checkFile(file1);
		FileAct.checkFile(file2);
		
		OutputStream f1= new FileOutputStream(file1);
		OutputStream f2= new FileOutputStream(file2);
		
		logger.info("正在输出训练数据");
		//输出训练数据
		for(int i=0;i<dataVec.size()/2;i++) {
			
			String temp;
			
			if(dataVec.get(i).getClassification().equals("Y"))
			     temp="0 ";		
			else temp="1 ";
			
			f1.write(temp.getBytes());
			
			Map<String,Integer> keys=new HashMap<String, Integer>();
			keys=dataVec.get(i).getSegment();
			int labelnum=1; 
			
			   for(String key:keys.keySet()) {
				temp=labelnum+":"+keys.get(key).toString()+" ";
				f1.write(temp.getBytes());
				labelnum++;
			   }
			   
			temp="\n";
			f1.write(temp.getBytes());
		}
		
		logger.info("正在输出测试数据");
		//输出测试数据
		for(int i=dataVec.size()/2;i<dataVec.size();i++) {
			
            String temp;
			
			if(dataVec.get(i).getClassification().equals("Y"))
			     temp="0 ";		
			else temp="1 ";
			
			f2.write(temp.getBytes());
			
			Map<String,Integer> keys=new HashMap<String, Integer>();
			keys=dataVec.get(i).getSegment();
			int labelnum=1; 
			
			   for(String key:keys.keySet()) {
				temp=labelnum+":"+keys.get(key).toString()+" ";
				f2.write(temp.getBytes());
				labelnum++;
			   }
			   
			temp="\n";
			f2.write(temp.getBytes());
		}
		f1.close();
		f2.close();
		logger.info("数据输出准备完成");
	}
}
