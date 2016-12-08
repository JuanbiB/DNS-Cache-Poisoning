import java.util.LinkedList;
import java.util.List;

/*
 * This is a simulation of DNS Cache poisoning.
 * Unlike the unfettered simulation, here we have an attacker
 * who guesses message ids and is successful thanks to the birthday paradox.
 */

public class AttackSimulation {
	public static final String TAG = "Cache Poisoning Simulation";
	
	class Target implements Runnable {
		private Simulation simulation;
		private List<Url> pagesToTarget;
		
		public Target(List<Url> pagesToTarget) {
			this.pagesToTarget = pagesToTarget;
			this.simulation = new Simulation(pagesToTarget, "addresses.txt");
		}

		public Simulation getSimulation() {
			return simulation;
		}
		
		public List<Url> getPagesToTarget() {
			return pagesToTarget;
		}

		@Override
		public void run() {
			this.simulation.unfettered();
		}
	}

	/*
	 * Creates a new thread for the attacker, and client
	 * Allows the attacker to attack after the client makes a request
	 * We assume that the attacker knows when the client makes the request
	 * and which DNS Server the client is talking to.
	 */
	public void attackSimulation() {
		// Step (1): init pages for the client to visit
		List<Url> pagesToVisit = new LinkedList<Url>();
		pagesToVisit.add(new Url("www.oberlin.edu", "192.168.1.1"));

		// Step (2): create an unfettered simulation
		Log.i(TAG, "Initializing unfeterred simulation.");
		Target unfettered = new Target(pagesToVisit);
		
		// Step (3): start the attacker
		Log.i(TAG, "Waiting for the unfettered simulation to initialize the DNS Server and Name Servers.");
		while (!unfettered.getSimulation().isReady()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.i(TAG, "Initializing the attacker.");
		Attacker attacker = new Attacker(unfettered.getSimulation().getDns(), 
				unfettered.getSimulation().getRoot());
		attacker.attack(new Url("www.muwahaha.com", Url.MALICIOUS_ADDRESS), new Url("www.oberlin.edu", Url.OBERLIN_ADDRESS));
	
		// Step (4): an unsuspecting client tries to visit
		while (!attacker.successful()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		unfettered.run();
	}

	public static void main(String[] args) {
		System.out.println("---------- Cache Poisoning Simulation ----------");
		new AttackSimulation().attackSimulation();
		System.out.println("---------- Cache Poisoning Simulation End ------");
	}
}
