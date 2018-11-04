package mainOper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import beans.Message;
import embeddings.App_em;
import fileoper.getUrl;
import tfidf.App_tf;
import collectDB.ConnectDB;
import combinexml.Combination;
/*
 * working with selected models
 */
public class Main {
   private static boolean td=false;
   private static boolean ed=false;
   private static String dic="sgns.weibo.word";
   private static String trainFile="train.xlsx";
   private static String testFile="test.xlsx";
   
   private static boolean db=false;
   private static String username="healthvis";
   private static String password="2018healthvis";
   private static String ipaddr="115.146.92.242";
   private static int portnum=27017;
   private static String dbname="healthvis";
   private static String collection="wap_posts_data";
   private static String txtid="_id";
   private static String txt="post_plaintext";
   private static int limitation=50000; 
   private static int skip=0;
   
   private static boolean cb=false;
   private static String trainold;
   private static String trainnew;
   
   private static String url=getUrl.getCurrentPath();
   private static String separator=getUrl.getSeparator();
	
   public static void main(String[] Args) throws IOException {
	   Main.getArguements(Args);
	   
	   if(db)
	   collectDB.ConnectDB.Connect(ipaddr,portnum,username,password,
			   dbname,collection,txtid,txt,limitation,skip,
			   url,separator,testFile);
	   
	   if(td) {
	   tfidf.App_tf.Tfidf(url,separator,trainFile,testFile);
	   mainOper.Main.write(new File(url+separator+"out_r.txt"),
			   "tt_result.xlsx",
			   "nt_result.xlsx",
			   url,separator,testFile);
	   }
	   
	   if(ed) {
	   embeddings.App_em.Embeddings(url,separator,
			   trainFile,testFile,dic);
	   mainOper.Main.write(new File(url+separator+"outE_r.txt"),
    		   "te_result.xlsx",
    		   "ne_result.xlsx",
    		   url,separator,testFile);
	
	   }
	   
	   if(td&&ed) {
	     mainOper.Main.write(url,separator,testFile);
       }
	   
       if(cb)
       Combination.combine(trainold,trainnew,url,separator);
   }

   /**
    * 
    * @param url running root document
    * @param separator system file separator in the OS
    * @param testFile test file data to be classified
    * @throws IOException
    */
   public static void write(String url, String separator,String testFile) throws IOException {
	   File fe=new File(url+separator+"out_r.txt");
	   File ft=new File(url+separator+"outE_r.txt");
	   File excelFile=new File(url+separator+testFile);
	   
	   BufferedReader r1=new BufferedReader(new FileReader(fe));
 	   BufferedReader r2=new BufferedReader(new FileReader(ft));
 	   List<String[]> userList = ReadingXls.readExcel(excelFile);
 	   
 	   String temp1,temp2;
 	   List<Message> rt=new ArrayList<Message>();
 	   List<Message> rf=new ArrayList<Message>();
 	   
 	   int index=0;
 	   while((temp1=r1.readLine())!=null&&(temp2=r2.readLine())!=null) {   
 		   double s1,s2;
 		   Message temp=new Message();
 		   String[] txt=userList.get(index);
 		   
 		   s1=Double.parseDouble(temp1);
 		   s2=Double.parseDouble(temp2); 
 		   
 		   temp.setNum(txt[0]);
 		   temp.setMessage(txt[1]);
 		   index++;
 		   
 		   if(s1==s2){             
 			   if(s1==1.0) {
 				 temp.setClassification("Y");  
 				 rt.add(temp);
 			   }
 			   else {
 				   temp.setClassification("N");
 				   rf.add(temp);
 			   }
 		   }
 	   }
 	   
 	  mainOper.WriteExcel.wirte2exl(rt,
 			      new File(url+separator+"t_result.xlsx"));
 	  mainOper.WriteExcel.wirte2exl(rf,
 			      new File(url+separator+"n_result.xlsx"));
   
   r1.close();
   r2.close();
   }
   
   /**
    * 
    * @param file result file
    * @param fs1 output file store negative result
    * @param fs2 output file store positive result
    * @param url running root document
    * @param separator system file separator in the OS
    * @param testFile test file data to be classified
    * @throws IOException
    */
   public static void write(File file,String fs1,String fs2,
		   String url, String separator,String testFile) throws IOException {
	   File excelFile=new File(url+separator+testFile);
	   
	   BufferedReader r1=new BufferedReader(new FileReader(file));
 	   List<String[]> userList = ReadingXls.readExcel(excelFile);
 	   
 	   String temp1;
 	   List<Message> rt=new ArrayList<Message>();
 	   List<Message> rf=new ArrayList<Message>();
 	   
 	   int index=0;
 	   while((temp1=r1.readLine())!=null) {   
 		   double s1;
 		   Message temp=new Message();
 		   String[] txt=userList.get(index);
 		   
 		   s1=Double.parseDouble(temp1);
 		   
 		   temp.setNum(txt[0]);
 		   temp.setMessage(txt[1]);
 		   index++;
 		   
            
 			   if(s1==1.0) {
 				 temp.setClassification("Y");  
 				 rt.add(temp);
 			   }
 			   else {
 				   temp.setClassification("N");
 				   rf.add(temp);
 			   }
 		   }
 	  mainOper.WriteExcel.wirte2exl(rt,
 			      new File(url+separator+fs1));
 	  mainOper.WriteExcel.wirte2exl(rf,
 			      new File(url+separator+fs2));
   
   r1.close();
   }
   
   /**
    * 
    * @param args input arguement
    */
   private static void getArguements(String[] args) {
	    for(int i=0;i<args.length;) {
		     if(args[i].charAt(0)!= '-') {
		    	 System.err.println("wrong arguement "+ args[i]);
		    	 System.exit(1);
		     }
		     
		     else {
		    	 switch(args[i].charAt(1)) {
		    	    case 'c': 
		    	    	db=true;
		    	    	ipaddr=args[i+1];
		    	    	portnum=Integer.parseInt(args[i+2]);
		    	    	username=args[i+3];
		    	    	password=args[i+4];	    	
		    	        dbname=args[i+5];
		    	    	collection=args[i+6];
		    	    	txtid=args[i+7];
		    	    	txt=args[i+8];
		    	    	i=i+9;
		    	    	break;
		    	    	
		    	    case 'b':
		    	    	td=true;
		    	    	ed=true;
		    	    	trainFile=args[i+1];
		    	    	testFile=args[i+2];
		    	    	dic=args[i+3];
			    	    i=i+4;
		    	    	break;
		    	    	
		    	    case 't':
		    	    	 td=true;
		    	    	 trainFile=args[i+1];
			    	     testFile=args[i+2];
			    	     i=i+3;
			    	     break;
			    	     
		    	    case 'e':
		    	    	 ed=true;
		    	    	 trainFile=args[i+1];
			    	     testFile=args[i+2];
			    	     dic=args[i+3];
			    	     i=i+4;
			    	     break;
			    	     
		    	    case 'n':
		    	    	cb=true;
		    	    	trainold=args[i+1];
		    	    	trainnew=args[i+2];
		    	    	i=i+3;
		    	    	break;
			    	     
		    	    default:
						System.err.println("wrong arguement " + args[i]);
						System.exit(1);
		    	 }
		     }
	    }
  }
}
