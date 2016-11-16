
public class Message {

	private String type;
	private String[] query;
	private String answer;
	private NameServer nextServer;
	
	public Message(String[] query, String answer) {
		this.type = MessageTypes.FINAL;
		this.query = query;
		this.answer = answer;
	}

	public Message(String[] query, NameServer answer) {
		this.type = MessageTypes.TRY;
		this.query = query;
		this.nextServer = answer;
	}
	
	public Message(String[] query) {
		this.type = MessageTypes.WHERE;
		this.query = query;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getQuery() {
		return query;
	}

	public void setQuery(String[] query) {
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
