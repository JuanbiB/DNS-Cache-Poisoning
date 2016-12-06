
public class Log {

	public static void w(String tag, String warning) {
		System.err.println(warning);
	}
	
	public static void e(String tag, String error) {
		System.err.println(error);
		System.exit(1);
	}
	
	public static void i(String tag, String info) {
		System.out.println(tag + " -- " + info);
	}
}
