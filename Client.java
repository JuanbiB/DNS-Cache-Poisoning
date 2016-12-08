public class Client implements Node {
    
	private static final String TAG = "Client";	
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
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		// If our cache isn't empty, then we shut off from receiving messages
		if (!cache_.isEmpty()){
		    return; 
		}
		// Check that this is indeed a final answer from the DNS
		if (message.getType() == MessageTypes.FINAL){
		    // Add entry to cache
			Log.i(TAG, "Adding the following to cache: " + "{" + message.getQuery().toString() + 
					" : " + message.getAnswer() + "}");
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
		String TXID = Txid.genTxid();
		
		Message request = new Message(query, TXID);
		dns_.message(this, request);
    }
    
    public String getAddress(){
		return "333.333.333.333";
	}
}

