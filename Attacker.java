import java.util.Random;

public class Attacker {

	private DNS target;
	private NameServer root;
	
	public Attacker(DNS target, NameServer root) {
		this.target = target;
		this.root = root;
	}
	
	public void attack(Url fake) {
		while (true) {
			Message fakeMessage = createFakeMessage(fake);
			target.message(root, fakeMessage);
		}
	}

	private Message createFakeMessage(Url fake) {
		Message message = new Message(new Url(fake.getRaw()), fake.getAddress(), genTxid());
		return message;
	}

	private String genTxid() {
		return Txid.genTxid();
	}
	
}
