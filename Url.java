import java.util.Arrays;


public class Url {

	private static final String TAG = "Url";
	private String raw;
	private String[] url;
	private int first;
	// only for the purpose of building name servers, not dns resolution
	// will not be set for request URLs
	private String address;
	
	public Url(String url) {
		this(url, "");
	}
	
	public Url(String url, String address) {
		this.raw = url;
		this.url = url.split("\\.");
		if (raw.equals("")) {
			this.url = new String[0];
		}
		this.first = this.url.length - 1;
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public boolean isEmpty() {
		return url.length == 0;
	}

	public String first() {
		if (first < 0) {
			return null;
		} else {
			return url[first];
		}
	}

	public void removeFirst() {
		first--;
	}
	
	public String toString() {
		return raw;
	}

	// TODO: bounds check
	public String nextPart(Url query) {
		return url[url.length - query.length() - 1];
	}

	public int length() {
		return url.length;
	}

	public String get(int i) {
		if (i >= url.length || i < 0) {
			return null;
		}
		return url[i];
	}

	public boolean isFinished() {
		return first < 0;
	}
	
	public String getRaw(){
		return raw;
	}

}
