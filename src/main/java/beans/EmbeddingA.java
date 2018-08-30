package beans;

import java.util.ArrayList;
import java.util.List;

public class EmbeddingA {
	private String num;
	private List<String> attributes;
	private String classification;
	
	public EmbeddingA() {
		attributes=new ArrayList<String>();
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
}
