public class Attacker implements Node {

	private DNS target;
	private NameServer root;
	private boolean successful;
	private Url malicious;
	private Url legitimite;
	
	public static final String TAG = "Attacker";
	
	public Attacker(DNS target, NameServer root) {
		this.target = target;
		this.root = root;
	}
	
	public void attack(Url fake, Url legitimite) {
		this.malicious = fake;
		this.legitimite = legitimite;
		
		// send requests and spoofed responses until successful
		new Thread(new Response()).start();
		
		while (!successful) {
			new Thread(new Request(this)).start();
		}
	}
	
	// this class implements a runnable that continuously sends 
	class Request implements Runnable {
		private Attacker attacker;
		
		public Request(Attacker attacker) {
			this.attacker = attacker;
		}

		public void run() {
			while (!successful) {
				String TXID = Txid.genTxid();
				Message request = new Message(legitimite, TXID);
				target.message(attacker, request);
			}
		}
	}
	
	class Response implements Runnable {
		
		public void run() {
			
		}
	}

	private Message createFakeMessage(Url fake) {
		Message message = new Message(new Url(fake.getRaw()), fake.getAddress(), genTxid());
		return message;
	}

	private String genTxid() {
		return Txid.genTxid();
	}

	public boolean successful() {
		return successful;
	}

	@Override
	public void message(Node src, Message message) {
		if (message.getType() == MessageTypes.FINAL) {
			if (message.getAnswer() == malicious.getAddress()) {
				this.successful = true;
			}
		}
	}

	@Override
	public String getAddress() {
		return "222.222.222.222";
	}
	
}
