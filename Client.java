import java.util.*;
public class Client {
	
	// Class variables should end with an underscore
	private DNS dns_;
	private Map<String, Integer> domainIP_;
	private int TTL_; 
	
	// Constructor 
	public Client(DNS dns) {
		dns_ = dns;
	}

	// Returns the IP address associated with the domain 
	public int Visit(String domain){
		/* first check cache to see if answer is already in there */
		if (domainIP_.containsKey(domain)){
			return domainIP_.get(domain);
		}
		
		/* Else have to ask the DNS server for a response */	
		// Build Request object
		Request req = new Request(domain);
		
		// Send object and store response
		Response answer = dns_.SendRequest(req);
		
		// Update TTL 
		TTL_ = answer.TTL();
		
		// clear the cache of whatever was in it and put new value
		domainIP_.clear();
		domainIP_.put(domain, answer.ip());
	}
	
	// To be called periodically to clear cache if TTL has expired
	void Clear(){
		if (TTL_ <= 0){
			domainIP_.clear();
		}
	}
}
