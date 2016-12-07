
public class TTL {
	private long expirationTime;
	public static final long DEFAULT_TTL = 10000;
	
	public TTL() {
		this.expirationTime = DEFAULT_TTL;
	}

	public TTL(long millis) {
		this.expirationTime = millis;
	}
	
	public long getExpTime() {
		return expirationTime;
	}
}
