import java.util.Map;
import java.util.Set;

public class NameServer implements Node {
	private String[] type;
	private Cache cache;
	private String address;
	private Map<String, NameServer> children;
	private Set<Node> trustedServers;
	
	/*
	 * Ideally, the simulator will take as input a list of URLs and create the tree structure of name servers
	 * from the URLs.
	 */
	public NameServer(String type, String address, Map<String, NameServer> children, Set<Node> trustedServers) {
		this.type = type.split("\\.");
		this.address = address;
		this.children = children;
		this.trustedServers= trustedServers;
		this.cache = new Cache();
	}
	
	@Override
	public void message(Node src, Message message) {
		String[] query = message.getQuery();
		// skip if not trusted server, invalid message, or wrong name server
		if (!trustedServers.contains(src) || message.getType() != MessageTypes.WHERE ||
				!rightServer(query)) {
			return;
		}
		
		// check if we are the final server fot this query
		if (matches(query)) {
			src.message(this, new Message(query, address(query)));
		}
		
		// return next server
		String nextServer = nextServer(query);
		if (!children.containsKey(nextServer)) {
			// skip if not a child
			return;
		}
		src.message(this, new Message(query, children.get(nextServer(query))));
	}
	
	private String nextServer(String[] query) {
		if (rightServer(query)) {
			return nextPart(query);
		} else {
			return null;
		}
	}
	
	private String nextPart(String[] query) {
		return type[type.length - query.length - 1];
	}

	private boolean rightServer(String[] query) {
		if (type.length > query.length) {
			return false;
		}
		
		int typeIndex = type.length - 1;
		int queryIndex = query.length - 1;
		
		while (queryIndex >= 0) {
			if (!query[queryIndex].equals(type[typeIndex])) {
				return false;
			}
			typeIndex--;
			queryIndex--;
		}
		return true;
	}
	
	private String address(String[] query) {
		if (matches(query)) {
			cache.addEntry(query, this.address);
			return this.address;
		} else {
			return null;
		}
	}

	private boolean matches(String[] query) {
		return rightServer(query) && query.length == type.length;
	}
	
}
