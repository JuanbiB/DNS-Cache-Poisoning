import java.util.Comparator;

public class UrlComparator implements Comparator<Url> {

	@Override
	public int compare(Url url1, Url url2) {
		while (!url1.isEmpty() && !url2.isEmpty()) {
			// check last entry
			String f1 = url1.first();
			String f2 = url2.first();
			if (!f1.equals(f2)) {
				return f1.compareTo(f2);
			}

			// recurse on remaining
			else {
				url1.removeFirst();
				url2.removeFirst();
			}
		}

		// base case: one is empty
		if (url1.isEmpty() && url2.isEmpty()) {
			return 0;
		} else if (url1.isEmpty()) {
			return 1;
		} else if (url2.isEmpty()) {
			return -1;
		}
		return 0;
	}
}
