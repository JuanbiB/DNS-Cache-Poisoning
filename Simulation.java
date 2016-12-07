import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Simulation {
	
	private List<Url> pagesToVisit;
	private String knownWebPages;
	private Client client;
	private NameServer root;
	private DNS dns;
	private boolean ready = false;

	public Simulation(List<Url> pagesToVisit, String knownWebPages) {
		this.pagesToVisit = pagesToVisit;
		this.knownWebPages = knownWebPages;
	}
	
	public NameServer getRoot() {
		return root;
	}
	
	public DNS getDns() {
		return dns;
	}

	public boolean isReady() {
		return ready;
	}

	public void unfettered() {
		init();
		
		for (Url url : pagesToVisit) {
			String answer = client.visitWebPage(url);
			if (!answer.equals(url.getAddress())){
				Log.i("Simulation", "Cache has been poisoned!");
			}
		}
	}

	private void init() {
		DNS dns = new DNS();
		this.dns = dns;
		Set<Node> dnsServers = new HashSet<Node>();
		dnsServers.add(dns);
		NameServer rootNS = new NameServer(knownWebPages, dnsServers, "128.532.543.645");
		this.root = rootNS;
		this.ready = true;
		dns.init(rootNS);
		this.client = new Client(dns);
	}
	
	public static void main(String[] args) {
		System.out.println("---------- Unfettered Simulation ----------");
		List<Url> pagesToVisit = new LinkedList<Url>();
		pagesToVisit.add(new Url("www.oberlin.edu", "192.168.1.1"));
		new Simulation(pagesToVisit, "addresses.txt").unfettered();
		System.out.println("---------- Unfettered Simulation End -------");
		
		System.out.println("---------- Cache Poisoning Simulation ----------");
	}
}
