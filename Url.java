
public class Url {

	private String raw;
	private String[] url;
	private int first;
	
	public Url(String raw) {
		this.raw = raw;
		this.url = raw.split("\\.");
		this.first = url.length - 1;
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

}
