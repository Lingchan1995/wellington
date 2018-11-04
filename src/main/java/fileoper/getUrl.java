package fileoper;

public class getUrl {
	   
	   public static String getCurrentPath() {
		   return System.getProperty("user.dir");
	   }
	   
	   public static String getSeparator() {
		   return System.getProperty("file.separator");
	   }
}
