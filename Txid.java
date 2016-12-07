import java.util.Random;

public class Txid {
	private static final int NUM_BITS = 16;

	public static String genTxid() {
		StringBuilder result = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < NUM_BITS; i++) {
			result.append(rand.nextInt(2));
		}
		return result.toString();
	}
	
}
