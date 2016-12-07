import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cache {

	private Map<Url, String> cache;
	private Map<TTL, Url> ttls;
	private boolean purging = false;
	private boolean entering = false;

	/*
	 * Initializes the cache and
	 * starts a new thread to remove expired entries
	 */
	public Cache() {
		this.cache = new HashMap<Url, String>();
		this.ttls = new HashMap<TTL, Url>();

		// start a new thread that will continuously purge obsolete cache entries
		new Thread(new CachePurger(this)).start();
	}

	/*
	 * Method for adding an entry to the cache
	 * Takes as input a query, address, and ttl in milliseconds
	 */
	public void addEntry(Url query, String address, long millis) {
		this.entering = true;
		sleepUntilUnset(purging);
		TTL ttl = new TTL(millis);
		cache.put(query, address);
		ttls.put(ttl, query);
		this.entering = false;
	}
	
	public void sleepUntilUnset(boolean param) {
		while (!param) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addEntry(Url query, String address) {
		addEntry(query, address, TTL.DEFAULT_TTL);
	}

	/*
	 * Returns the address of the query if it is in the cache,
	 * null otherwise
	 */
	public String lookupEntry(Url query) {
		return cache.get(query);
	}

	/*
	 * Checks if an entry is within the cache
	 */
	public boolean containsEntry(Url query) {
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

	// purges expired entries from the cache periodically
	class CachePurger implements Runnable {
		private Cache master;
		
		public CachePurger(Cache cache) {
			this.master = cache;
		}

		@Override
		public void run() {
			while (true) {
				master.purging = true;
				sleepUntilUnset(entering);
				for (TTL ttl : ttls.keySet()) {
					if (getCurrentTime() >= ttl.getExpTime()) {
						Url expired = ttls.get(ttl);
						cache.remove(expired);
						ttls.remove(ttl);
					}
				}
				master.purging = false;
				// Only do this once per second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					run();
				}
			}
		}
	}

}
