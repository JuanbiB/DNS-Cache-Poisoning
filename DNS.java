import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class DNS implements Node {
	//Instance Variables: Root name server and cache
	private static final String TAG = "DNS";	
	private NameServer root_;
	private Cache cache_;
	private Node client_;
	private Map<String, Integer> expectedServers_;
	private Set<String> TXIDS_;
	private final ReentrantLock lock_;

	/**
	 * Constructor for DNS server object.
	 * @param root the root name server
	 */
	public DNS() {
		cache_ = new Cache();
		expectedServers_ = new HashMap<String, Integer>();
		TXIDS_ = new HashSet<String>();
		lock_ = new ReentrantLock();
	}

	public void init(NameServer root){
		root_ = root;
	}

	/**
	 * Keeps track of the name server's we're expecting an answer from, this is done to 
	 * make it harder for an attacker to just flood us with answers when we're not expecting them.
	 * @param address the address of the name server we're expecting an answer from
	 */
	public void addToExpected(String address){
		int count = expectedServers_.containsKey(address) ? expectedServers_.get(address) : 0;
		expectedServers_.put(address, count + 1);
	}

	/**
	 * This is blank since we'll never be asking a DNS server to return it's address,
	 * that's only for Name Servers.
	 */
	public String getAddress(){
		return "";
	}

	/**
	 * After getting a legitimate answer, we decrement the expectancy
	 */

	public void decrementExpected(String address){
		int count = expectedServers_.get(address);
		expectedServers_.put(address, count - 1);
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

		if (TXIDS_.contains(message.getTXID())) {
			Log.i(TAG, "Received a message of type: " + message.getType());
		}

		// If the requested Url is already contained within the cache, send that one back
		if (cache_.containsEntry(message.getQuery())){
			if (message.getType() == MessageTypes.WHERE) {
				Url query = message.getQuery();
				Message toSend = new Message(query, cache_.lookupEntry(query), message.getTXID());
				src.message(this, toSend);
			} else {
				return;
			}
		}
		// This is the initial message received by the client	
		if (message.getType()==MessageTypes.WHERE){
			Log.i(TAG, "Received a message of type: " + message.getType());

			String TXID = Txid.genTxid();
			lock_.lock();
			while (TXIDS_.contains(TXID)){
				TXID = Txid.genTxid();
			}
			TXIDS_.add(TXID);
			lock_.unlock();

			message.setTXID(TXID);
			this.client_ = src;

			// Now expecting a message back from root
			addToExpected(root_.getAddress());
			root_.message(this, message);

		}
		// If a NS returns another NS to ask and check that we're expecting an answer
		if (message.getType()==MessageTypes.TRY) {
			if (TXIDS_.contains(message.getTXID())) {
				if (expectedServers_.get(src.getAddress()) > 0){
					NameServer nextServer = message.getNextServer();
					// Got message back from expected server, add next to the map 
					decrementExpected(src.getAddress());
					addToExpected(nextServer.getAddress());

					nextServer.message(this, new Message(message.getQuery(), message.getTXID()));
				}
				else{
					Log.i(TAG, "The following address tried to contact me, but I was not" +
							" expecting it: " + src.getAddress());
				}
			}
			else {
				//Log.i(TAG, "Got a request with wrong TXID.");
				return;
			}
		}
		// If we get the final answer from the name server, that is, an IP address
		if (message.getType() == MessageTypes.FINAL){
			// Add entry to cache	
			if (TXIDS_.contains(message.getTXID())){
				if (expectedServers_.get(src.getAddress()) > 0){
					Log.i(TAG, "Adding the following to cache: " + "{" + message.getQuery() + 
							":" + message.getAnswer() + "}");

					decrementExpected(src.getAddress());
					this.cache_.addEntry(message.getQuery(), message.getAnswer());
					this.client_.message(this, message);
					lock_.lock();
					TXIDS_.remove(message.getTXID());
					lock_.unlock();
				}
				else{
					Log.i(TAG, "The following address tried to contact me, but I was not" +	// 
							" expecting it: " + src.getAddress());
				}
			}
			else{
				Log.i(TAG, "Got a request with wrong TXID.");
				return;
			}
		}
	}
}