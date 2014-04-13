package apps.alarmclock;

import java.util.TimerTask;

import main.Alfred;

public class WakeupAlarm extends TimerTask {

	@Override
	public void run() {
		System.out.println("Alarm went off.");
		Alfred.printTime();
		Alfred.printGreeting();
		AlarmClock.getTimer().cancel();
	}
}
