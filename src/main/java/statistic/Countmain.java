package statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Countmain {
          @SuppressWarnings({ "unchecked", "rawtypes", "resource" })
		public static void main(String[] Args) throws IOException {
        	  File f1=new File("testData/out_r.txt");
        	  File f2=new File("testData/train1.txt");
        	  
        	  BufferedReader r1=new BufferedReader(new FileReader(f1));
        	  BufferedReader r2=new BufferedReader(new FileReader(f2));
        	  
        	  String temp=null;
              List<Double> t1=new LinkedList();
        	  while((temp=r2.readLine())!=null) {
        		  String[] te=temp.split(" ");
        		  t1.add(Double.parseDouble(te[0]));
        	  }
        	  
        	  List<Double> t2=new LinkedList();
        	  while((temp=r1.readLine())!=null) {
        		  t2.add(Double.parseDouble(temp));
        	  }
        	  
        	  long tp=0,tn=0,fp=0,fn=0;
        	  for(int i=0;i<t1.size();i++) {
        		  double i1=t1.get(i);
        		  double i2=t2.get(i);
        		  
        		  if(i1==i2) {
        			  if(i1==1.0)tp++;
        			  else tn++;
        		  }
        		  
        		  else {
        			  if(i1==0.0)fp++;
        			  else fn++;
        		  }
        	  }
        	  System.out.println("Accuracy:"+(double)(tp+tn)/(tp+tn+fn+fp));
        	  System.out.println("Sensitivity:"+(double)(tp)/(tp+fn));
        	  System.out.println("Sepcificity:"+(double)(tn)/(tn+fp));
        	  System.out.println("Precision:"+(double)(tp)/(tp+fp));       	  
          }
}
