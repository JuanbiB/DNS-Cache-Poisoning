import java.util.*;
public class Client {
	
	// Class variables should end with an underscore
	private DNS dns_;
	private Cache cache_;
	
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
		Response answer = dns_.Answer(req);
		
		// clear the cache of whatever was in it and put new value
		cache_.clear();
		cache_.domain = domain;
		cache_.ip = answer.ip();
		cache_.TTL = answer.TTL();
	}
	
	// To be called periodically to clear cache if TTL has expired
	void Clear(){
		if (cache_ <= 0){
			cache_.clear();
		}
	}
	
}
