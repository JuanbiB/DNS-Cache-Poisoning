public class Message {

	private String type;
	private int TXID;
	private String answer;
	private NameServer nextServer;
	
	public Message(Url query, String answer, int TXID) {
		this.type = MessageTypes.FINAL;
		this.query = query;
		this.answer = answer;
		this.TXID = TXID;
	}

	public Message(Url query, NameServer answer, int TXID) {
		this.type = MessageTypes.TRY;
		this.query = query;
		this.nextServer = answer;
		this.TXID = TXID;
	}
	
	public Message(Url query, int TXID) {
		this.type = MessageTypes.WHERE;
		this.query = query;
		this.TXID = TXID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Url getQuery() {
		return query;
	}

	public void setQuery(Url query) {
		this.query = query;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public NameServer getNextServer() {
		return nextServer;
	}

	public void setNextServer(NameServer nextServer) {
		this.nextServer = nextServer;
	}
	
	public int getTXID(){
		return this.TXID;
	}
}
