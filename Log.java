
public class Log {

	public static void w(String warning) {
		System.err.println(warning);
	}
	
	public static void e(String error) {
		System.err.println(error);
		System.exit(1);
	}
	
	public static void i(String info) {
		System.out.println(info);
	}
}
