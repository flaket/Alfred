package apps.processhandler;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemProcessHandler {

	// Checks if a process is running. Could be a helper to a diagnostics tool
	public SystemProcessHandler() {
	}

	/**
	 * Returns true if the process name parameter is running.
	 * 
	 * @param process
	 * @return
	 */
	public boolean checkProcess(String process) {
		System.out.println("Checking process...");
		try {
			Process p = new ProcessBuilder("tasklist").start();
			String s;
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((s = stdout.readLine()) != null) {
				System.out.println(s);
				if (s.contains(process)) {
					System.out.println("Process running");
					return true;
				}
			}
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out
				.println("Process not running. Try lower/upper-casing the first letter.");
		return false;
	}

	/**
	 * Returns a string of the currently running processes.
	 * 
	 * @return
	 */
	public String getRunningProcesses() {
		StringBuilder str = new StringBuilder();
		try {
			Process p = new ProcessBuilder("tasklist").start();
			String s;
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((s = stdout.readLine()) != null) {
				str.append(s);
				str.append("\n");
			}
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
			return str.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
