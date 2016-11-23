import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation {
	
	private List<Url> pagesToVisit;
	private String knownWebPages;
	private Client client;

	public Simulation(List<Url> pagesToVisit, String knownWebPages) {
		this.pagesToVisit = pagesToVisit;
		this.knownWebPages = knownWebPages;
	}

	public void unfettered() {
		init();
		
		for (Url url : urls) {
			client.visitWebPage(url);
		}
	}

	private void init() {
		DNSServer dns = new DNSServer();
		Set<Node> dnsServers = new HashSet<Node>();
		dnsServers.add(dns);
		NameServer nameServer = new NameServer(knownWebPages, dnsServers, "128.532.543.645");
		DNSServer dnsServer = new DNSServer(nameServer);
		this.client = new Client(dnsServer);
	}
}
