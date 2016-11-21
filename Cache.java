import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cache {

	private Map<Url, String> cache;
	private Map<TTL, Url> ttls;

	/*
	 * Initializes the cache and
	 * starts a new thread to remove expired entries
	 */
	public Cache() {
		this.cache = new HashMap<Url, String>();

		new Date().getTime();

		// start a new thread that will continuously purge obsolete cache entries
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					for (TTL ttl : ttls.keySet()) {
						if (getCurrentTime() >= ttl.getExpTime()) {
							Url expired = ttls.get(ttl);
							cache.remove(expired);
							ttls.remove(ttl);
						}
					}
					// Only do this once per second
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						run();
					}
				}
			}
		}).start();
	}

	/*
	 * Method for adding an entry to the cache
	 * Takes as input a query, address, and ttl in milliseconds
	 */
	public void addEntry(Url query, String address, long millis) {
		TTL ttl = new TTL(millis);
		cache.put(query, address);
		ttls.put(ttl, query);
	}

	public void addEntry(Url query, String address) {
		TTL ttl = new TTL();
		cache.put(query, address);
		ttls.put(ttl, query);
	}

	/*
	 * Returns the address of the query if it is in the cache,
	 * null otherwise
	 */
	public String lookupEntry(String[] query) {
		return cache.get(query);
	}

	/*
	 * Checks if an entry is within the cache
	 */
	public boolean containsEntry(String[] query) {
		return cache.containsKey(query);
	}

	/*
	 * Checks if the cache is empty
	 */
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	/*
	 * Returns the current system time in milliseconds
	 */
	long getCurrentTime() {
		return new Date().getTime();
	}

	/*
	 * Returns an expiration date given millis
	 */
	long genTTL(long millis) {
		return new Date().getTime() + millis;
	}

}
