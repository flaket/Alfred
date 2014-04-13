package comm.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import main.Alfred;

/**
 * 
 * @author Andreas
 * 
 */
public class ConsoleComm implements Runnable {

	private BufferedReader bufferedReader;

	public ConsoleComm() {
		setbufferedReader(new BufferedReader(new InputStreamReader(System.in)));
	}

	public void run() {
		while (true) {
			try {
				Alfred.getConsoleCmdHandler().executeCommand(
						getbufferedReader().readLine().split(" "));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeBufferedReader() throws IOException {
		getbufferedReader().close();
	}

	public BufferedReader getbufferedReader() {
		return bufferedReader;
	}

	public void setbufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}
}
