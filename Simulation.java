import java.util.HashSet;
import java.util.LinkedList;
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
		
		for (Url url : pagesToVisit) {
			System.out.println("visiting: " + url);
			String result = client.visitWebPage(url);
			Log.i("IP address for ".concat(url.toString()), result);
		}
	}

	private void init() {
		DNS dns = new DNS();
		Set<Node> dnsServers = new HashSet<Node>();
		dnsServers.add(dns);
		NameServer rootNS = new NameServer(knownWebPages, dnsServers, "128.532.543.645");
		dns.init(rootNS);
		this.client = new Client(dns);
	}
	
	public static void main(String[] args) {
		List<Url> pagesToVisit = new LinkedList<Url>();
		pagesToVisit.add(new Url("www.oberlin.edu"));
		new Simulation(pagesToVisit, "addresses.txt").unfettered();
	}
}
