package comm.console;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import main.Alfred;
import main.Constants;

import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import apps.alarmclock.Alarm;
import apps.alarmclock.AlarmClock;

/**
 * Class handles all input from the console.
 * 
 * @author Andreas
 * 
 */
public class ConsoleCmdHandler {

	ArrayList<String> commands;
	String listOfCommands = "";

	public ConsoleCmdHandler() {
		// Add all the implemented commands to a list.
		commands = new ArrayList<String>();
		commands.add("Help");
		commands.add("Location");
		commands.add("Latestnews");
		commands.add("Mute");
		commands.add("Sound");
		commands.add("Utorrent");
		commands.add("Torrentscrape");
		commands.add("Spotify");
		commands.add("Weather");
		commands.add("Yahooweather");
		commands.add("\nAlarm arg0 arg1 arg2");
		commands.add("Alarminfo");
		commands.add("Stopalarm arg0");
		commands.add("Snooze");
		commands.add("Processes");
		commands.add("Off");
		commands.add("Restart");
		commands.add("Shutdown");
		// Make a string representation of the list of commands.
		listOfCommands = commands.toString();
	}

	public void executeCommand(String[] command) {
		switch (command[0].toLowerCase()) {
		case "help":
			help(command);
			break;
		case "location":
			location();
			break;
		case "latestnews":
			latestNews(command);
			break;
		case "mute":
			mute(command);
			break;
		case "sound":
			sound(command);
			break;
		case "torrentscrape":
			torrentScrape();
			break;
		case "utorrent":
			uTorrent(command);
			break;
		case "spotify":
			spotify(command);
			break;
		case "yahooweather":
			yahooWeather();
			break;
		case "weather":
			weather();
			break;
		case "alarm":
			alarm(command);
			break;
		case "snooze":
			snooze();
			break;
		case "processes":
			processes();
		case "alarminfo":
			alarmInfo();
			break;
		case "stopalarm":
			stopAlarm(command);
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
	 * Print the running system processes.
	 */
	public void processes() {
		System.out.println(Alfred.getSystemProcessHandler()
				.getRunningProcesses());
	}

	/**
	 * Manually scrape eztv for wanted torrents.
	 */
	public void torrentScrape() {
		Alfred.getTorrentScraper().scrape();
	}

	/**
	 * Set a 5 minute alarm.
	 * 
	 * @param command
	 */
	private void snooze() {
		Alfred.setAlarmClock(new AlarmClock(5, 0));
	}

	/**
	 * Kill a schedueled alarm.
	 * 
	 * @param command
	 */
	private void stopAlarm(String[] command) {
		if (command.length == 2) {
			int r = Integer.parseInt(command[1]);
			if (r <= Alfred.getAlarmList().size() - 1)
				Alfred.getAlarmList().remove(r).cancel();
		} else
			System.out
					.println("Stopalarm takes one argument, the numbered alarm in the schedueled alarms list viewable by command: alarminfo");
		;
	}

	/**
	 * Print info about alarms currently schedueled. TODO: Format the output so
	 * that it is more easily readable. i.e not 6 minutes and -17 seconds.
	 * 
	 * @param command
	 */
	private void alarmInfo() {
		DateTime now = new DateTime();
		System.out.println("Number of alarms schedueled: "
				+ Alfred.getAlarmList().size());
		int number = 0;
		for (Alarm alarm : Alfred.getAlarmList()) {
			if (alarm.getCreated() != null) {
				System.out.println(number + " Created:    "
						+ (alarm.getCreated()) + "\n  Schedueled: "
						+ (alarm.getSchedueled()));
				System.out.println("Alarm goes off in: "
						+ (alarm.getSchedueled().dayOfMonth().get() - now
								.dayOfMonth().get())
						+ " day, "
						+ (alarm.getSchedueled().hourOfDay().get() - now
								.hourOfDay().get())
						+ " hours, "
						+ (alarm.getSchedueled().minuteOfHour().get() - now
								.minuteOfHour().get())
						+ " minutes and "
						+ (alarm.getSchedueled().secondOfMinute().get() - now
								.secondOfMinute().get()) + " seconds.");

			} else
				System.out
						.println(number
								+ " Detailed alarm info only viewable on alarms set with string argument.");
			number += 1;
		}
	}

	/**
	 * Prints the latest news Alfred has received from his feed. If the
	 * parameter "expand" is passed the browser opens with the news url.
	 * 
	 * @param command
	 */
	private void latestNews(String[] command) {
		System.out.println(Alfred.getLatestNewsString()
				+ "\nTo check out the link: 'latestnews expand'");
		if (command.length == 2 && command[1].equals("expand")) {
			try {
				String url = Alfred.getLatestNewsURL();
				new ProcessBuilder(Constants.CH_PATH_BROWSER, url).start();
				System.out.println("Launching browser with url: " + url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Print the last known location of the authorized user.
	 * 
	 * @param command
	 */
	private void location() {
		System.out.println(Alfred.getLocation().toString());
	}

	/**
	 * Generate a full weather report from yahoo and outputs it to the console.
	 * 
	 * @param command
	 */
	private void yahooWeather() {
		try {
			Alfred.getWeatherService().printDefaultWeather();
		} catch (JAXBException | IOException | NumberFormatException e) {
			System.out
					.println("The weather service caught an error from the yahoo weather library, sir. It is that time of the day.");
			// Alfred.setWeatherService(new WeatherService());
		}
	}

	/**
	 * Outputs a textual forecast for the next 5 days. Generated from yr.no
	 */
	private void weather() {
		try {
			Alfred.getYrParser().generateReport();
			System.out.println(Alfred.getYrParser().getWeatherReport()
					.fullForecastToString());
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Open the utorrent application.
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

	/**
	 * Turns on the system sound
	 * 
	 * @param command
	 */
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

	/**
	 * Mutes the system sound.
	 * 
	 * @param command
	 */
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

	/**
	 * Opens spotify application. Presses the spacebar after 8 seconds.
	 * 
	 * @param command
	 */
	private void spotify(String[] command) {
		try {
			new ProcessBuilder(Constants.CH_PATH_SPOTIFY, "").start();
			System.out.println("Launching Spotify application.");
			// Format for å spille av text-to-speech fra fil:
			// new ProcessBuilder(
			// "nircmd",
			// "speak",
			// "file",
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

	/**
	 * Shuts down the computer.
	 * 
	 * @param command
	 */
	private void shutdown(String[] command) {
		try {
			new ProcessBuilder("shutdown", "-s").start();
			System.out.println("Beginning shutdown process.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Restarts the computer.
	 * 
	 * @param command
	 */
	private void restart(String[] command) {
		try {
			new ProcessBuilder("shutdown", "-r").start();
			System.out.println("Beginning restart process.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the list of available commands.
	 * 
	 * @param command
	 */
	private void help(String[] command) {
		System.out.println(listOfCommands);
	}

	/**
	 * Creates a new alarm clock with a given schedule. This can be either a
	 * time in the format "0730", seconds, minutes & seconds, hours & minutes &
	 * seconds.
	 * 
	 * @param command
	 */
	private void alarm(String[] command) {
		// If no arguments set default 5 seconds alarm.
		if (command.length == 1) {
			Alfred.setAlarmClock(new AlarmClock(5));
			// ------------------If one argument.
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
		// ------------------If two arguments.
		else if (command.length == 3) {
			if (command[1].toLowerCase().matches("^[0-9]\\d*$")
					|| command[2].toLowerCase().matches("^[0-9]\\d*$")) {
				// Alarm set with minutes, seconds arguments.
				Alfred.setAlarmClock(new AlarmClock(Integer
						.parseInt(command[1]), Integer.parseInt(command[2])));
			}

			// ------------------If three arguments.
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

	// ------------- no longer used -------------------

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
	// System.out
	// .println("Too few or too many arguments. Ex use: 'torrent thriftshop'");
	// }
	//
	// /**
	// * Prints the current downloading torrent info.
	// *
	// * @param command
	// */
	// private void torrentInfo(String[] command) {
	// Alfred.getTorrentDownloader().print();
	// }
	//
	// /**
	// * Outputs the torrents found in the folder Constants.TORRENT_PATH.
	// */
	// private void torrentsInFolder() {
	// System.out
	// .println(Alfred.getTorrentDownloader().getAvailableTorrents());
	// }
}
