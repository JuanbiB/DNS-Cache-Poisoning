
public class Message {

	private String type;
	private Url query;
	private String answer;
	private NameServer nextServer;
	
	public Message(Url query, String answer) {
		this.type = MessageTypes.FINAL;
		this.query = query;
		this.answer = answer;
	}

	public Message(Url query, NameServer answer) {
		this.type = MessageTypes.TRY;
		this.query = query;
		this.nextServer = answer;
	}
	
	public Message(Url query) {
		this.type = MessageTypes.WHERE;
		this.query = query;
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
}
