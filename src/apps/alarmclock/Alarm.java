package apps.alarmclock;

import java.util.TimerTask;

import main.Alfred;

import org.joda.time.DateTime;

public class Alarm extends TimerTask {

	DateTime created, schedueled;

	public Alarm() {
		created = null;
		schedueled = null;
	}

	public Alarm(DateTime now, DateTime date) {
		created = now;
		schedueled = date;
	}

	@Override
	public void run() {
		System.out.println("Alarm went off.");
		Alfred.getAlarmList().remove(this);
		AlarmClock.getTimer().cancel();
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getSchedueled() {
		return schedueled;
	}
}
