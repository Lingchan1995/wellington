package beans;

public class Message {
          
	  private String num;
	  private String message;
	  private String classification;
	 
	public Message() {
		this.num="";
		this.message="";
		this.classification="";
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	   
}
