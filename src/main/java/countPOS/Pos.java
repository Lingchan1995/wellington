package countPOS;

import java.util.HashMap;
import java.util.Map;

public class Pos {
		private String num;
		private Map<String,String> segment;
		private String classification;
		
		public Pos() {
			this.segment = new HashMap<String,String>();
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public Map<String, String> getSegment() {
			return segment;
		}

		public void setSegment(Map<String, String> segment) {
			this.segment = segment;
		}

		public String getClassification() {
			return classification;
		}

		public void setClassification(String classification) {
			this.classification = classification;
		}
}
