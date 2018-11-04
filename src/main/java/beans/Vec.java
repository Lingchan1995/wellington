package beans;

import java.util.ArrayList;
import java.util.List;

public class Vec {
	private String num;
	private List<Double> vector;
	private String classification;
	
	public Vec() {
	 this.vector=new ArrayList<Double>();
	 this.classification="";
	 this.num="";
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public List<Double> getVector() {
		return vector;
	}
	public void setVector(List<Double> vector) {
		this.vector = vector;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	@Override
	public String toString() {
		StringBuffer temp=new StringBuffer();
		
		if(classification.equals("Y"))
		     temp.append("1"+" ");
		else temp.append("0"+" ");
		   
				
		for(int i=0;i<vector.size();i++)
		    temp.append((i+1)+":"+vector.get(i)+" ");
		
		return temp.toString();
	}
}
