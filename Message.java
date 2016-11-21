
public class Message {

	private String type;
<<<<<<< HEAD
	private String[] query;
	private int TXID;
	private String answer;
	private NameServer nextServer;
	
	public Message(String[] query, String answer, int TXID) {
=======
	private Url query;
	private String answer;
	private NameServer nextServer;
	
	public Message(Url query, String answer) {
>>>>>>> origin/master
		this.type = MessageTypes.FINAL;
		this.query = query;
		this.answer = answer;
		this.TXID = TXID;
	}

<<<<<<< HEAD
	public Message(String[] query, NameServer answer, int TXID) {
=======
	public Message(Url query, NameServer answer) {
>>>>>>> origin/master
		this.type = MessageTypes.TRY;
		this.query = query;
		this.nextServer = answer;
		this.TXID = TXID;
	}
	
<<<<<<< HEAD
	public Message(String[] query, int TXID) {
=======
	public Message(Url query) {
>>>>>>> origin/master
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
