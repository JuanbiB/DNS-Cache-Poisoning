public class Attacker implements Node {

	private DNS target;
	private NameServer authoritative;
	private boolean successful;
	private Url malicious;
	private Url legitimite;
	
	public static final String TAG = "Attacker";
	
	public Attacker(DNS target, NameServer authoritative) {
		this.target = target;
		this.authoritative = authoritative;
	}
	
	public void attack(Url fake, Url legitimite) {
		this.malicious = fake;
		this.legitimite = legitimite;
		
		// send requests and spoofed responses until successful
		while (!successful) {
			new Thread(new Request(this)).start();
			new Thread(new Response()).start();
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
				Log.i(TAG, "Sending a request for " + legitimite.getAddress());
				String TXID = Txid.genTxid();
				Message request = new Message(legitimite, TXID);
				target.message(attacker, request);
			}
		}
	}
	
	class Response implements Runnable {
		
		public void run() {
			while (!successful) {
				Log.i(TAG, "Sending a fake response, spoofing authoritative name server.");
				Message response = createFakeResponse(malicious);
				target.message(authoritative, response);
			}
		}
	}

	private Message createFakeResponse(Url fake) {
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
			if (message.getAnswer().equals(malicious.getAddress())) {
				//Log.enabled = false;
				this.successful = true;
				//System.exit(1);
			}
		}
	}

	@Override
	public String getAddress() {
		return "222.222.222.222";
	}
	
}
