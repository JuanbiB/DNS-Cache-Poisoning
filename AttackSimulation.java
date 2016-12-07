import java.util.LinkedList;
import java.util.List;

/*
 * This is a simulation of DNS Cache poisoning.
 * Unlike the unfettered simulation, here we have an attacker
 * who guesses message ids and is successful thanks to the birthday paradox.
 */

public class AttackSimulation {

	class Target implements Runnable {
		private Simulation simulation;
		private List<Url> pagesToVisit;
		
		public Target(List<Url> pagesToVisit) {
			this.pagesToVisit = pagesToVisit;
			this.simulation = new Simulation(pagesToVisit, "addresses.txt");
		}

		public Simulation getSimulation() {
			return simulation;
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

		// Step (2): start an unfettered simulation in a thread
		Target unfettered = new Target(pagesToVisit);
		new Thread(unfettered).start();
		// Step (3): start the attacker
		while (!unfettered.getSimulation().isReady()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		new Attacker(unfettered.getSimulation().getDns(), 
				unfettered.getSimulation().getRoot()).attack(
						new Url("www.muwahaha.com", Url.MALICIOUS_ADDRESS));
		}

	public static void main(String[] args) {
		new AttackSimulation().attackSimulation();
	}
}
