import java.util.*;
public class Client {
	
	private DNS dns_;
	private Map<String, Integer> domainIP;
	private int TTL; 
	
	// Constructor 
	public Client(DNS dns) {
		dns_ = dns;
	}

	// Returns the IP address associated with the domain 
	public int Visit(String domain){
		// first check cache to see if answer is already in there
		if (domainIP.containsKey(domain)){
			return domainIP.get(domain);
		}
		
		// else have to ask the DNS server for a response
		Response answer = dns_.SendRequest(Request req);
		
		// update TTL 
		TTL = answer.TTL();
		
		// clear the cache of whatever was in it and put new value
		domainIP.clear();
		domainIP.put(domain, answer.ip());
	}
	
	// To be called periodically to clear cache if TTL has expired
	void Clear(){
		if (TTL <= 0){
			domainIP.clear();
		}
	}
}
