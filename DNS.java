public class DNS implements Node{
    //Instance Variables: Root name server and cache
	private static final String TAG = "DNS";	
    private NameServer root_;
    private Cache cache_;
    private String TXID_;
    private Node client_;

    /**
     * Constructor for DNS server object.
     * @param root the root name server
     */
    public DNS() {
        cache_ = new Cache();
    }
    
    public void init(NameServer root){
    	root_ = root;
    }

    /**
     * Takes in domain name (currently), asks root_ for IP address
     * @param domain the domain name to search for
     * @return address address corresponding to the domain parameter
     */
    @Override
    public void message(Node src, Message message) {
    	try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Log.i(TAG, "Received a message of type: " + message.getType());
    	
    	//System.out.println("Got message: " + message.getType());
    	// If the requested Url is already contained within the cache, send that one back
        if (cache_.containsEntry(message.getQuery())){
        	Url query = message.getQuery();
        	Message toSend = new Message(query, cache_.lookupEntry(query), message.getTXID());
            src.message(this, toSend);
        }
        // This is the initial message received by the client	
        if (message.getType()==MessageTypes.WHERE){
            this.TXID_ = message.getTXID();
            this.client_ = src;
            root_.message(this, message);
        }
        // If a NS returns another NS to ask	
        if (message.getType()==MessageTypes.TRY){
        	if (message.getTXID()==this.TXID_){
        		NameServer nextServer = message.getNextServer();
                nextServer.message(this, new Message(message.getQuery(), this.TXID_));
            }
        	else {
                return;
            }
        }
        // If we get the final answer from the name server, that is, an IP address
        if (message.getType() == MessageTypes.FINAL){
            // Add entry to cache	
            if (message.getTXID().equals(this.TXID_)){
                this.cache_.addEntry(message.getQuery(), message.getAnswer());
                this.client_.message(this, message);
            }
        }
        //System.out.println("DNS: all done handling message");
    }
}