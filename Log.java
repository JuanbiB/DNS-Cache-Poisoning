
public class Log {
	
	public static boolean enabled = true;

	public static void w(String tag, String warning) {
		if (enabled) {
			System.err.println(warning);
		}
	}
	
	public static void e(String tag, String error) {
		if (enabled) {
			System.err.println(error);
			System.exit(1);
		}
	}
	
	public static void i(String tag, String info) {
		if (enabled) {
			System.out.println(tag + " -- " + info + "\n------------------------------------------");
		}
	}
}
