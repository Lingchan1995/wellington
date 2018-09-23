package countPOS;

import java.io.File;
import java.io.IOException;

public class FileAct {
       public static void checkFile(File file) throws IOException {
    	   if (!file.exists()) {	//文件不存在则创建文件，先创建目录
   			File dir = new File(file.getParent());
   			dir.mkdirs();
   			file.createNewFile();
          }
       }
}
