package comm.twitter;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import main.Alfred;
import main.Constants;

import apps.alarmclock.AlarmClock;

public class TwitterCmdHandler {

	ArrayList<String> commands;
	String listOfCommands = "";

	public TwitterCmdHandler() {
		// Add all the implemented commands to a list.
		commands = new ArrayList<String>();
		commands.add("Help");
		commands.add("Location");
		commands.add("Weather");
		commands.add("Yahooweather");
		commands.add("Alarm arg0 arg1 arg2");
		commands.add("Mute");
		commands.add("Sound");
		commands.add("Spotify");
		commands.add("Utorrent");
		commands.add("Torrentscrape");
		commands.add("Off");
		commands.add("Restart");
		commands.add("Shutdown");

		// Make a string representation of the list of commands.
		listOfCommands = commands.toString();
	}

	public void executeCommand(String[] command) {
		switch (command[0].toLowerCase()) {
		// --- Sends back information ---
		case "help":
			help(command);
			break;
		case "location":
			location(command);
			break;
		case "weather":
			weather();
			break;
		case "yahooweather":
			yahooWeather();
			break;
		// --- Remote control ---
		case "alarm":
			alarm(command);
			break;
		case "mute":
			mute(command);
			break;
		case "sound":
			sound(command);
			break;
		case "spotify":
			spotify(command);
			break;
		case "utorrent":
			uTorrent(command);
			break;
		case "torrentscrape":
			torrentScrape();
			break;
		case "off":
			Alfred.shutdownAlfred();
			break;
		case "restart":
			restart(command);
			break;
		case "shutdown":
			shutdown(command);
			break;
		default:
			System.out.println("Command not found in the list of commands.");
			break;
		}
	}

	/**
	 * Manually scrape eztv for wanted torrents.
	 */
	public void torrentScrape() {
		Alfred.getTorrentScraper().scrape();
	}

	/**
	 * Sends a list of available commands.
	 * 
	 * @param command
	 */
	private void help(String[] command) {
		System.out.println("Sending list of available commands via Twitter.");
		TwitterComm.sendDirectMessage(Constants.AUTH_USER, listOfCommands);
	}

	/**
	 * Sends the last known location.
	 * 
	 * @param command
	 */
	private void location(String[] command) {
		System.out
				.println("Sending last known location information via Twitter.");
		TwitterComm.sendDirectMessage(Constants.AUTH_USER,
				Constants.CH_LOCATIONTEXT + "\n"
						+ Alfred.getLocation().toString());
	}

	/**
	 * Sends a small weather report generated from info from yr.
	 */
	private void weather() {
		// System.out.println("Sending weather information via Twitter.");
		// TwitterComm.sendDirectMessage(Constants.AUTH_USER,
		// Alfred.getYrParser()
		// .getWeatherReport().twitterForecastToString());
	}

	/**
	 * Sends a small weather report generated from info from yahoo.
	 * 
	 * @param command
	 */
	private void yahooWeather() {
		System.out.println("Sending weather information via Twitter.");
		try {
			TwitterComm.sendDirectMessage(Constants.AUTH_USER, Alfred
					.getWeatherService().getTwitterWeather());
		} catch (JAXBException | IOException e) {
			TwitterComm
					.sendDirectMessage(
							Constants.AUTH_USER,
							"I was unable to retrieve weather information from Yahoo. Check out: "
									+ "\nhttp://www.yr.no/sted/Norge/Sør-Trøndelag/Trondheim/Trondheim/");
		}
	}

	/**
	 * Creates a new alarm clock for Alfred with a given schedule. This can be
	 * either a time in the format "0730", seconds, minutes & seconds, hours &
	 * minutes & seconds.
	 * 
	 * @param command
	 */
	private void alarm(String[] command) {
		// If no arguments set default 5 seconds alarm.
		if (command.length == 1) {
			Alfred.setAlarmClock(new AlarmClock(5));
			// If one argument.
		} else if (command.length == 2) {
			// If argument is in the format of 4 digits.
			if (command[1].toLowerCase().matches("\\d{4}")) {
				if (Integer.parseInt(command[1]) < 2400) {
					Alfred.setAlarmClock(new AlarmClock(command[1]));
				} else
					System.out.println("Time must be between 0000 and 2359.");
			} else if (command[1].toLowerCase().matches("^[0-9]\\d*$")) {
				Alfred.setAlarmClock(new AlarmClock(Integer
						.parseInt(command[1])));
			}
		}
		// If two arguments.
		else if (command.length == 3) {
			if (command[1].toLowerCase().matches("^[0-9]\\d*$")
					|| command[2].toLowerCase().matches("^[0-9]\\d*$")) {
				// Alarm set with minutes, seconds arguments.
				Alfred.setAlarmClock(new AlarmClock(Integer
						.parseInt(command[1]), Integer.parseInt(command[2])));
			}

			// If three arguments.
			else if (command.length == 4) {
				if (command[1].toLowerCase().matches("^[0-9]\\d*$")
						&& command[2].toLowerCase().matches("^[0-9]\\d*$")
						&& command[3].toLowerCase().matches("^[0-9]\\d*$")) {
					System.out
							.println("Three arguments currently not supported.");
					// Alarm set with hours, minutes, seconds arguments.

					// This crashes with java.lang.NumberFormatException: radix
					// 1
					// less than Character.MIN_RADIX.

					// Alfred.setAlarmClock(new AlarmClock(Integer
					// .parseInt(command[1]), Integer.parseInt(command[2],
					// Integer.parseInt(command[3]))));
				}
			}

		} else
			System.out.println("Too many arguments.");
	}

	// --------- Remote control methods ---------

	/**
	 * Opens the utorrent application
	 * 
	 * @param command
	 */
	private void uTorrent(String[] command) {
		try {
			new ProcessBuilder(Constants.CH_PATH_UTORRENT, "").start();
			System.out.println("Launching uTorrent application.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// /**
	// * Downloads the torrent given by the input parameter. This must be a
	// * .torrent file name in the path set by Constants.TORRENT_PATH. Example:
	// to
	// * download the torrent "thriftshop.torrent", pass the parameter
	// * "thriftshop".
	// *
	// * @param command
	// */
	// private void torrent(String[] command) {
	// // Currently downloads the torrent named "thriftshop". This file
	// // is in the downloads folder. Creates a new
	// // thread so I can still communicate with Alfred while he is
	// // downloading.
	// if (command.length == 2) {
	// Alfred.getTorrentDownloader().runJob(command[1]);
	// } else
	// TwitterComm.sendDirectMessage(Constants.AUTH_USER,
	// "I was unable to retrieve weather information from Yahoo.");
	// }

	private void sound(String[] command) {
		// turns on the system sound
		try {
			new ProcessBuilder(Constants.CH_PATH_NIRCMD, "mutesysvolume", "0")
					.start();
			System.out.println("Unmuting sound.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void mute(String[] command) {
		// mutes the system sound
		try {
			new ProcessBuilder(Constants.CH_PATH_NIRCMD, "mutesysvolume", "1")
					.start();
			System.out.println("Muting sound.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void spotify(String[] command) {
		// opens spotify application. Starts playing after 8 seconds.
		try {
			new ProcessBuilder(Constants.CH_PATH_SPOTIFY, "").start();
			System.out.println("Launching Spotify application.");
			// Format for å spille av text-to-speech fra fil:
			// new ProcessBuilder(
			// "nircmd",
			// "speak",
			// "file",
			//
			// "C:\\Users\\Andreas\\Workspaces\\Alfred\\assets\\texttospeech\\spotify_start.txt",
			// "-2").start();
			Thread.sleep(8000);
			new ProcessBuilder(Constants.CH_PATH_NIRCMD, "sendkeypress", "spc")
					.start();
			System.out.println("Pressed the spacebar.");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void shutdown(String[] command) {
		// shutdown computer
		try {
			new ProcessBuilder("shutdown", "-s").start();
			System.out.println("Beginning shutdown process.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void restart(String[] command) {
		// restarts computer. Add Alfred to startup programs so he comes
		// back online.
		try {
			new ProcessBuilder("shutdown", "-r").start();
			System.out.println("Beginning restart process.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ------------ Currently unimplemented methods

	// private void torrentInfo(String[] command) {
	// Alfred.getTorrentDownloader().print();
	// }

	// private void latestNews(String[] command) {
	// TwitterComm.sendDirectMessage(Constants.AUTH_USER,
	// Alfred.getLatestNewsString());
	// }

}
