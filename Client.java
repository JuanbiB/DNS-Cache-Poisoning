import java.util.*;

public class Client implements Node {
    
    private DNS dns_;
    private Cache cache_;
    	
    public Client(DNS dns) {
		this.dns_ = dns;
		this.cache_ = new Cache();
    }
    
    /**
     * Sends request to visit message and waits for answer
     */
    
    public String visitWebPage(Url query){
    	String address = cache_.lookupEntry(query);
    	while (address == null){
    		makeRequest(query);
    		address = cache_.lookupEntry(query);
    	}
    	
    	return address;
    }

    /* 
     * Handles answer from the DNS server 
     */
    @Override 
    public void message(Node src, Message message){
		// If our cache isn't empty, then we shut off from receiving messages
		if (!cache_.isEmpty()){
		    return; 
		}
		// Check that this is indeed a final answer from the DNS
		if (message.getType() == MessageTypes.FINAL){
		    // Add entry to cache
		    cache_.addEntry(message.getQuery(), message.getAnswer());
		}
		else {
		    return;
		}
    }

    /*
     * Makes request for the IP address associated with the domain 
     */
    public void makeRequest(Url query){
		// If not in cache, send request and wait for reply
		Random rand = new Random();
		int TXID = rand.nextInt(1001) + 5000;
		
		Message request = new Message(query, TXID);
		dns_.message(this, request);
    }
}

