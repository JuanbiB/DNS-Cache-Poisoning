import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class NameServer implements Node {
	private static final String TAG = "NameServer";	
	private Url type;
	private Cache cache;
	private String address;
	private Map<String, NameServer> children;
	private Set<Node> trustedServers;
	
	/*
	 * This constructor takes a file name
	 * and builds a name server tree based
	 * on the URLs in the file.
	 */
	public NameServer(String file, Set<Node> trustedServers, String address) {
		// init scanner for file
		Scanner input = null;
		try {
			input = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			System.err.println("Error: could not build name servers, invalid file");
		}
		
		// read in URLs
		List<Url> urls = new LinkedList<Url>();
		while (input.hasNextLine()) {
			String line = input.nextLine();
			String[] parts = line.split(" "); // TODO error handling
			urls.add(new Url(parts[0], parts[1]));
		}

		// set instance variables
		this.type = new Url("");
		this.cache = new Cache();
		this.trustedServers = trustedServers;
		this.address = address;
		this.children = new HashMap<String, NameServer>();
		// build tree
		for (Url url : urls) {
			addEntry(url);
		}
	}
	
	private void addEntry(Url url) {
		if (url.isFinished()) {
			this.address = url.getAddress();
			return;
		}
		String first = url.first();
		if (children.containsKey(first)) {
			url.removeFirst();
			NameServer child = children.get(first);
			child.addEntry(url);
		} else {
			url.removeFirst();
			NameServer child = new NameServer(first + "." + type.getRaw(), this.address+"-"+first, this.trustedServers);
			children.put(first, child);
			child.addEntry(url);
		}
	}

	/*
	 * Ideally, the simulator will take as input a list of URLs and create the tree structure of name servers
	 * from the URLs.
	 */
	public NameServer(String type, String address, Set<Node> trustedServers) {
		this.type = new Url(type);
		this.address = address;
		this.trustedServers= trustedServers;
		this.cache = new Cache();
		this.children = new HashMap<String, NameServer>();
	}
	
	@Override
	public void message(Node src, Message message) {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Url query = message.getQuery();
		// skip if not trusted server, invalid message, or wrong name server
		if (!trustedServers.contains(src) || message.getType() != MessageTypes.WHERE ||
				!rightServer(query)) {
			System.out.println("doesn't contain key");
			return;
		}
		
		// check if we are the final server for this query
		if (matches(query)) {
			Log.i(TAG, "Found final server.");
			src.message(this, new Message(query, address(query), message.getTXID()));
			return;
		}
		
		// return next server
		String nextServer = nextServer(query);
		Log.i(TAG, "Next name server to ask: " + nextServer + "\nWhole Domain: " + query.toString());
		if (!children.containsKey(nextServer)) {
			// skip if not a child
			return;
		}

		src.message(this, new Message(query, children.get(nextServer(query)), message.getTXID()));
	}
	
	private String nextServer(Url query) {
		if (rightServer(query)) {
			return nextPart(query);
		} else {
			return null;
		}
	}
	
	private String nextPart(Url query) {
		return query.nextPart(type);
	}

	private boolean rightServer(Url query) {
		if (type.length() > query.length()) {
			System.out.println("len");
			return false;
		}
		
		int typeIndex = type.length() - 1;
		int queryIndex = query.length() - 1;
		
		while (typeIndex >= 0) {
			if (!query.get(queryIndex).equals(type.get(typeIndex))) {
				return false;
			}
			typeIndex--;
			queryIndex--;
		}
		return true;
	}
	
	private String address(Url query) {
		if (matches(query)) {
			cache.addEntry(query, this.address);
			return this.address;
		} else {
			return null;
		}
	}

	private boolean matches(Url query) {
		return rightServer(query) && query.length() == type.length();
	}
	
}
