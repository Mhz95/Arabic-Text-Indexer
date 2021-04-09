package assignment.obj;

public class Term {
	
	private int termID;
	private String term;
	private char type;
	public Term(int termID, String term, char type) {
		super();
		this.termID = termID;
		this.term = term;
		this.type = type;
	}
	public int getTermID() {
		return termID;
	}
	public void setTermID(int termID) {
		this.termID = termID;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "\nPosition: " + termID + " | term: " + term + " | type: " + (type == 't'? "Title": "Body") + "";
	}
	
}
