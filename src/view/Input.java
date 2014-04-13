package view;

public class Input {

	static String inString;

	public static String getString() {
		inString = "";
		// Wait for input
		while (inString == "") {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		return inString;
	}
}
