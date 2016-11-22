import java.util.*;

public class Client implements Node {
    
    private DNS dns_;
    private Cache cache_;
    	
    public Client(DNS dns, String address) {
		this.dns_ = dns;
		this.cache_ = new Cache();
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
     * Sleeps until it has received an answer
     */
    public String makeRequest(Url query) throws InterruptedException{
		// Check cache first of all
		if (cache_.containsEntry(query)){
		    return cache_.lookupEntry(query);
		}
		// If not in cache, send request and wait for reply
		Random rand = new Random();
		int TXID = rand.nextInt(1001) + 5000;
		
		Message request = new Message(query, TXID);
		dns_.message(this, request);

		while (true){
		    Thread.sleep(1000);
		    if (!cache_.isEmpty()){
		    	break;
		    }
		}
		// When it breaks out of the loop, it will have received an answer.
		return cache_.lookupEntry(query);
    }
	
}

