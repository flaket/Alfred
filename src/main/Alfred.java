package main;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import apps.alarmclock.Alarm;
import apps.alarmclock.AlarmClock;
import apps.location.Location;
import apps.processhandler.SystemProcessHandler;
import apps.torrent.TorrentScraper;
import apps.weather.YahooWeatherService;
import apps.weather.YrWeatherParser;

import comm.console.ConsoleCmdHandler;
import comm.console.ConsoleComm;
import comm.twitter.TwitterCmdHandler;
import comm.twitter.TwitterComm;

public class Alfred {

	private static TwitterComm twitterComm;
	private static TwitterCmdHandler twitterCmdHandler;
	private static ConsoleComm consoleComm;
	private static ConsoleCmdHandler consoleCmdHandler;
	private static Location location;
	private static AlarmClock alarmClock;
	private static ArrayList<Alarm> alarmList;
	private static YahooWeatherService weatherService;
	private static YrWeatherParser yrParser;
	private static TorrentScraper torrentScraper;
	private static SystemProcessHandler systemProcessHandler;
	private static String latestNewsURL = Constants.NEWS_URL_DEFAULT;
	private static String latestNewsString = Constants.NEWS_STRING_DEFAULT;

	/**
	 * Program entry point.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		// Turn off console output from log4j.
		Logger.getRootLogger().setLevel(Level.OFF);
		// Initiate Alfred.
		new Alfred();
	}

	/**
	 * Constructor.
	 */
	public Alfred() {
		printTime();
		setLocation(new Location());
		setAlarmList(new ArrayList<Alarm>());
		setTwitterComm(new TwitterComm());
		setTwitterCmdHandler(new TwitterCmdHandler());
		setConsoleComm(new ConsoleComm());
		setConsoleCmdHandler(new ConsoleCmdHandler());
		Thread consoleComm = new Thread(getConsoleComm());
		consoleComm.start();
		setWeatherService(new YahooWeatherService());
		setYrParser(new YrWeatherParser());

		setTorrentScraper(new TorrentScraper());
		setSystemProcessHandler(new SystemProcessHandler());
		printGreeting();

		// TODO (SMALL): Create a timed event using ScheduledExecutorService.
		// Every day at ex 0900 execute torrentScraper.scrape() and launch
		// utorrent to download the latest wanted torrents.
		// TODO (SMALL): Create reload-methods capable of reloading every
		// component of Alfred individually. This way code could be updated
		// without restarting Alfred.
		// TODO: (SMALL): Create a watchdog that can monitor Alfred. This way
		// modules can be reloaded if needed.
		// TODO (SMALL): Create a "im going to bed"-function. This should
		// eventually
		// turn off apartment lights and set an alarm for the following day.
		// This alarm should be displayed so that I am reminded and can manually
		// override it if I want.
		// TODO (SMALL): Create a way for the alarms to signal besides
		// outputting "Alarm went off.".
		// TODO (SMALL): Format the output so
		// that it is more easily readable. i.e not 6 minutes and -17 seconds.
		// TODO: Create or modify a logging tool.
		// TODO: Continue work on the DriveCommandLine class. It can currently
		// save a file to the drive folder, but it
		// requires a code to be generated through browser interaction.
		// TODO (SMALL): Create a working google calendar implementation.
		// TODO (SMALL): Connect the location class to google latitude. This way
		// Alfred
		// knows
		// * the location of the cell phone at all times.
		// TODO (BIG): Create a system diagnostics tool. This could run on a
		// fixed
		// schedule and control that everything is up and running, help the
		// garbage
		// collector, collect statistics, run ccleaner, etc.
		// TODO: Make a gui so Alfred can live outside the console.
		// TODO (BIG): Implement communication via sound.
		// TODO (BIG): Hook Alfred up with the physical world. Get light dimmers
		// or something that Alfred can take control over.
		// TODO (BIG): Create a database and a webcrawler. Alfred should be able
		// to automatically "compile a database on -insert topic-" on command.
		// TODO (BIG): After a database has been created I will create viewing
		// software so the screen
		// is populated with images, articles, etc.
	}

	public static void printTime() {
		DateTime now = new DateTime();
		StringBuilder time = new StringBuilder();
		time.append(now.dayOfMonth().getAsShortText());
		time.append(".");
		time.append(now.monthOfYear().getAsShortText());
		time.append(" ");
		time.append(now.year().getAsShortText());
		time.append(" ");
		time.append(now.hourOfDay().getAsText());
		time.append(":");
		time.append(now.minuteOfHour().getAsText());
		time.append(":");
		time.append(now.secondOfMinute().getAsText());

		System.out.println(time.toString());
	}

	/**
	 * Shutdown the JVM.
	 */
	public static void shutdownAlfred() {
		System.out.println("Going offline, sir.");
		System.exit(0);
	}

	public static void printGreeting() {
		DateTime now = new DateTime();
		int h = now.getHourOfDay();
		if (h < 6)
			System.out
					.println("\nIt's nighttime, sir. Shouldn't you be sleeping?");
		else if (h < 12)
			System.out.println("\nGood morning, sir.");
		else if (h < 18)
			System.out.println("\nGood afternoon, sir.");
		else
			System.out.println("\nGood evening, sir.");
	}

	public static void disableWeatherService() {
		setWeatherService(null);
		System.out.println("The weather service has been disabled, sir.");
	}

	// public static void disableTorrentDownloader() {
	// setTorrentDownloader(null);
	// System.out.println("The torrent downloader has been disabled.");
	// }

	public static TorrentScraper getTorrentScraper() {
		return torrentScraper;
	}

	public static YrWeatherParser getYrParser() {
		return yrParser;
	}

	public static void setYrParser(YrWeatherParser yrParser) {
		Alfred.yrParser = yrParser;
	}

	public static void setTorrentScraper(TorrentScraper torrentScraper) {
		Alfred.torrentScraper = torrentScraper;
	}

	public static SystemProcessHandler getSystemProcessHandler() {
		return systemProcessHandler;
	}

	public static void setSystemProcessHandler(
			SystemProcessHandler systemProcessHandler) {
		Alfred.systemProcessHandler = systemProcessHandler;
	}

	public static String getLatestNewsString() {
		return latestNewsString;
	}

	public static void setLatestNewsString(String latestNewsString) {
		Alfred.latestNewsString = latestNewsString;
	}

	public static ConsoleComm getConsoleComm() {
		return consoleComm;
	}

	public static void setConsoleComm(ConsoleComm consoleComm) {
		Alfred.consoleComm = consoleComm;
	}

	public static ConsoleCmdHandler getConsoleCmdHandler() {
		return consoleCmdHandler;
	}

	public static void setConsoleCmdHandler(ConsoleCmdHandler consoleCmdHandler) {
		Alfred.consoleCmdHandler = consoleCmdHandler;
	}

	public static String getLatestNewsURL() {
		return latestNewsURL;
	}

	public static void setLatestNewsURL(String latestNewsURL) {
		Alfred.latestNewsURL = latestNewsURL;
	}

	// public static TorrentDownloader getTorrentDownloader() {
	// return torrentDownloader;
	// }
	//
	// public static void setTorrentDownloader(TorrentDownloader
	// torrentDownloader) {
	// Alfred.torrentDownloader = torrentDownloader;
	// }

	public static YahooWeatherService getWeatherService() {
		return weatherService;
	}

	public static void setWeatherService(YahooWeatherService weatherService) {
		Alfred.weatherService = weatherService;
	}

	public static AlarmClock getAlarmClock() {
		return alarmClock;
	}

	public static void setAlarmClock(AlarmClock alarmClock) {
		Alfred.alarmClock = alarmClock;
	}

	public static Location getLocation() {
		return location;
	}

	public static void setLocation(Location location) {
		Alfred.location = location;
	}

	public static TwitterComm getTwitterComm() {
		return twitterComm;
	}

	public static void setTwitterComm(TwitterComm twitterComm) {
		Alfred.twitterComm = twitterComm;
	}

	public static TwitterCmdHandler getTwitterCmdHandler() {
		return twitterCmdHandler;
	}

	public static void setTwitterCmdHandler(TwitterCmdHandler twitterCmdHandler) {
		Alfred.twitterCmdHandler = twitterCmdHandler;
	}

	public static void restartTwitterComm() {
		setTwitterComm(new TwitterComm());
	}

	public static void restartConsoleComm() {
		setConsoleComm(new ConsoleComm());
		Thread consoleComm = new Thread(getConsoleComm());
		consoleComm.start();
	}

	public static ArrayList<Alarm> getAlarmList() {
		return alarmList;
	}

	public static void setAlarmList(ArrayList<Alarm> alarmList) {
		Alfred.alarmList = alarmList;
	}

}
