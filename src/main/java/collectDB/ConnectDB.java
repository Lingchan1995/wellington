package collectDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class ConnectDB {
         public static Map<String,String> connectDB(String ipaddr,int portnum,
        		 String username,String password,String dbname,String collect,
        		 String txtid,String txt,int limitation,int skip) {  
                 //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
                 //ServerAddress()两个参数分别为 服务器地址 和 端口  
                 ServerAddress serverAddress = new ServerAddress(ipaddr,portnum);  
                 List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
                 addrs.add(serverAddress);  
                   
                 //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
                 MongoCredential credential = MongoCredential.createScramSha1Credential
                		                                       (username, dbname, password.toCharArray());  
                 List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
                 credentials.add(credential);  
                   
                 //通过连接认证获取MongoDB连接  
                 MongoClient mongoClient = new MongoClient(serverAddress,credentials);  
                   
                 //连接到数据库  
                 MongoDatabase mongoDatabase = mongoClient.getDatabase(dbname);  
                 System.out.println("Connect to database successfully");  
                 
                 MongoCollection<Document> collection = mongoDatabase.getCollection(collect);
                 
                 List<Document> results = new ArrayList<Document>(); 
                 FindIterable<Document> iterables = collection.find().limit(limitation).skip(skip);  
                 MongoCursor<Document> cursor = iterables.iterator();
               
                 Map<String,String> data=new HashMap<String,String>();
                 
                 while (cursor.hasNext()) {  
                	 Document txttemp=cursor.next();
                     results.add(txttemp);  
                 } 
                
                 for(Document parse:results) {
                	JSONObject temp=new JSONObject(parse.toJson());
                	String txttemp=temp.get(txt).toString();
                	String idtx=temp.get(txtid).toString();
                    data.put(idtx, txttemp);
                 }
                 return data;
         }
         
         public static void Connect(String ipaddr,int portnum,String username,
        		 String password,String dbname, String collection,
        		 String txtid,String txt,int limitation,int skip,
        		 String sysDoc,String separator,String testFile) {
        	 Map<String,String> data=new HashMap<String,String>();
        	 
        	 data=ConnectDB.connectDB(ipaddr,portnum,username,password,
      			   dbname,collection,txtid,txt,limitation,skip);
        	 WriteExcel.wirte2exl(testFile,sysDoc,separator,data);
         }
}

