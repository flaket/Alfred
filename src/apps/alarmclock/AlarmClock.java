package apps.alarmclock;

import java.util.Timer;

import main.Alfred;

import org.joda.time.DateTime;

public class AlarmClock {

	private static Timer timer;

	/**
	 * Set alarm clock in seconds.
	 * 
	 * @param minutes
	 * @param seconds
	 */
	public AlarmClock(int seconds) {
		setTimer(new Timer());
		Alarm alarm = new Alarm();
		timer.schedule(alarm, seconds * 1000);
		Alfred.getAlarmList().add(alarm);
		System.out.println("Alarm set.");
		System.out.println("Alarm goes off in: " + seconds + " seconds.");
	}

	/**
	 * Set alarm clock in minutes and seconds.
	 * 
	 * @param minutes
	 * @param seconds
	 */
	public AlarmClock(int minutes, int seconds) {
		setTimer(new Timer());
		Alarm alarm = new Alarm();
		timer.schedule(alarm, minutes * 60 * 1000 + seconds * 1000);
		Alfred.getAlarmList().add(alarm);
		System.out.println("Alarm set.");
		System.out.println("Alarm goes off in: " + minutes + " minutes and "
				+ seconds + " seconds.");
	}

	/**
	 * Set alarm clock in hours, minutes, seconds
	 * 
	 * @param hours
	 * @param minutes
	 * @param seconds
	 */
	public AlarmClock(int hours, int minutes, int seconds) {
		setTimer(new Timer());
		Alarm alarm = new Alarm();
		timer.schedule(alarm, hours * 60 * 60 * 1000 + minutes * 60 * 1000
				+ seconds * 1000);
		Alfred.getAlarmList().add(alarm);
		System.out.println("Alarm set.");
	}

	/**
	 * TODO: Some bugs with alarm not going off when several alarms are schedueled. 
	 * Accepts a future time parameter
	 * in the format 0000, ex: 0730. The input will always be between 0000 and
	 * 2359. This makes it possible to set non-sensical points in time like
	 * 0799. TODO: create a control in the CmdHandler for the third digit so it
	 * cannot be larger than 5.
	 * 
	 * @param time
	 */
	public AlarmClock(String time) {
		setTimer(new Timer());
		// Extract hours and minutes from string.
		int hour = Integer.parseInt(time.substring(0, 2));
		int minute = Integer.parseInt(time.substring(2, 4));
		// Create a new date object with nilled fields.
		DateTime date = new DateTime().toDateMidnight().toDateTime();
		// Set the hours and minutes of this new date.
		date = date.plusHours(hour);
		date = date.plusMinutes(minute);
		// Add a day if the time is earlier than the current time.
		if (date.isBeforeNow())
			date = date.plusDays(1);
		// Get the current time.
		DateTime now = new DateTime();
		// Calculate the difference in milliseconds between these datetimes.
		long diff = date.getMillis() - now.getMillis();
		// Set the alarm to go off after this difference.
		Alarm alarm = new Alarm(now, date);
		timer.schedule(alarm, diff);
		Alfred.getAlarmList().add(alarm);
		System.out.println("Alarm set.");
		System.out.println("Now:   " + now);
		System.out.println("Alarm: " + date);
		System.out.println("Alarm goes off in: "
				+ (alarm.getSchedueled().dayOfMonth().get() - alarm
						.getCreated().dayOfMonth().get())
				+ " day, "
				+ (alarm.getSchedueled().hourOfDay().get() - alarm.getCreated()
						.hourOfDay().get())
				+ " hours, "
				+ (alarm.getSchedueled().minuteOfHour().get() - alarm
						.getCreated().minuteOfHour().get())
				+ " minutes and "
				+ (alarm.getSchedueled().secondOfMinute().get() - alarm
						.getCreated().secondOfMinute().get()) + " seconds.");
	}

	public static Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		AlarmClock.timer = timer;
	}
}
