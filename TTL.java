import java.util.Date;

public class TTL {
	private long expirationTime;
	public static final long DEFAULT_TTL = 100000;
	
	public TTL() {
		this.expirationTime = DEFAULT_TTL;
	}

	public TTL(long millis) {
		this.expirationTime = millis + new Date().getTime();
	}
	
	public long getExpTime() {
		return expirationTime;
	}
}
