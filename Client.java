import java.util.*;
public class Client {
	
	private DNS dns_;
	private Cache cache_;
	
	public Client(DNS dns) {
		dns_ = dns;
		cache_ = new Cache();
	}

	/* 
	 * Returns the IP address associated with the domain 
	 */
	public int Visit(String[] query){
		// First check cache to see if answer is already in there 
		if (cache_.checkEntry(query)){
			return cache_.lookupEntry(query);
		}
		
		// Else have to ask the DNS server for a response 
		Message req = new Message(query);
		Message answer = dns_.Answer(req);
		
		// Add entry to cache
		cache_.addEntry(query, answer.ip(), answer.TTL());
	}
	
}
