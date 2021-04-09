package assignment.obj;

public class Doc {

	private String stockCode;
	private long docID;
	private String title;
	private String content;
	
	public Doc(String stockCode, long docID, String title, String content) {
		super();
		this.stockCode = stockCode;
		this.docID = docID;
		this.title = title;
		this.content = content;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public long getDocID() {
		return docID;
	}

	public void setDocID(long docID) {
		this.docID = docID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Doc [stockCode=" + stockCode + ", docID=" + docID + ", title=" + title + ", content=...]";
	}
		
	
}
