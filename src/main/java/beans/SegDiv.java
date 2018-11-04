package beans;

import java.util.HashMap;
import java.util.Map;

public class SegDiv {
	private String num;
	private Map<String,Integer> segment;
	private String classification;
	
	public SegDiv() {
		this.segment = new HashMap<String,Integer>();
		this.num="";
		this.classification="";
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Map<String, Integer> getSegment() {
		return segment;
	}

	public void setSegment(Map<String, Integer> segment) {
		this.segment = segment;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
}
