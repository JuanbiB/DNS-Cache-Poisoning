
public class Url {

	private static final String TAG = "Url";
	private String raw;
	private String[] url;
	private int first;
	// only for the purpose of building name servers, not dns resolution
	// will not be set for request URLs
	private String address;
	
	public Url(String line) {
		String[] parts = line.split(" ");
		if (parts.length != 2) {
			Log.e(TAG, "invalid addresses in file");
		}
		this.address = parts[1];
		this.url = parts[1].split("\\.");
		this.first = url.length - 1;
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

	public String getRaw() {
		return raw;
	}

	public boolean isFinished() {
		return first < 0;
	}

}
