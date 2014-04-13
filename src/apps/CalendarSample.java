package apps;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;


// TODO: Not working.
public class CalendarSample {

	/**
	 * Be sure to specify the name of your application. If the application name
	 * is {@code null} or blank, the application will log a warning. Suggested
	 * format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "Alfred";

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static com.google.api.services.calendar.Calendar client;

	static final java.util.List<Calendar> addedCalendarsUsingBatch = Lists
			.newArrayList();

	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorize() throws Exception {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, CalendarSample.class
						.getResourceAsStream("/client_secrets.json"));
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret()
						.startsWith("Enter ")) {
			System.out
					.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
							+ "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}
		// set up file credential store
		FileCredentialStore credentialStore = new FileCredentialStore(new File(
				System.getProperty("user.home"), ".credentials/calendar.json"),
				JSON_FACTORY);
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
				Collections.singleton(CalendarScopes.CALENDAR))
				.setCredentialStore(credentialStore).build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	public static void main(String[] args) {
		try {
			try {
				HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
				// authorization
				Credential credential = authorize();

				// set up global Calendar instance
				client = new com.google.api.services.calendar.Calendar.Builder(
						HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName(APPLICATION_NAME).build();
				// run commands
				showCalendars();
				addCalendarsUsingBatch();
				Calendar calendar = addCalendar();
				updateCalendar(calendar);
				addEvent(calendar);
				showEvents(calendar);
				deleteCalendarsUsingBatch();
				deleteCalendar(calendar);

			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
	}

	private static void showCalendars() throws IOException {
		System.out.println("Show Calendars");
		CalendarList feed = client.calendarList().list().execute();
		System.out.println(feed.toPrettyString());
	}

	private static void addCalendarsUsingBatch() throws IOException {
		System.out.println("Add Calendars using Batch");
		BatchRequest batch = client.batch();

		// Create the callback.
		JsonBatchCallback<Calendar> callback = new JsonBatchCallback<Calendar>() {

			@Override
			public void onSuccess(Calendar calendar, HttpHeaders responseHeaders) {
				try {
					System.out.println(calendar.toPrettyString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addedCalendarsUsingBatch.add(calendar);
			}

			@Override
			public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
				System.out.println("Error Message: " + e.getMessage());
			}
		};

		// Create 2 Calendar Entries to insert.
		Calendar entry1 = new Calendar().setSummary("Calendar for Testing 1");
		client.calendars().insert(entry1).queue(batch, callback);

		Calendar entry2 = new Calendar().setSummary("Calendar for Testing 2");
		client.calendars().insert(entry2).queue(batch, callback);

		batch.execute();
	}

	private static Calendar addCalendar() throws IOException {
		System.out.println("Add Calendar");
		Calendar entry = new Calendar();
		entry.setSummary("Calendar for Testing 3");
		Calendar result = client.calendars().insert(entry).execute();
		System.out.println(result.toPrettyString());
		return result;
	}

	private static Calendar updateCalendar(Calendar calendar)
			throws IOException {
		System.out.println("Update Calendar");
		Calendar entry = new Calendar();
		entry.setSummary("Updated Calendar for Testing");
		Calendar result = client.calendars().patch(calendar.getId(), entry)
				.execute();
		System.out.println(result.toPrettyString());
		return result;
	}

	private static void addEvent(Calendar calendar) throws IOException {
		System.out.println("Add Event");
		Event event = newEvent();
		Event result = client.events().insert(calendar.getId(), event)
				.execute();
		System.out.println(result.toPrettyString());
	}

	private static Event newEvent() {
		Event event = new Event();
		event.setSummary("New Event");
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 3600000);
		DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
		event.setStart(new EventDateTime().setDateTime(start));
		DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
		event.setEnd(new EventDateTime().setDateTime(end));
		return event;
	}

	private static void showEvents(Calendar calendar) throws IOException {
		System.out.println("Show Events");
		Events feed = client.events().list(calendar.getId()).execute();
		System.out.println(feed.toPrettyString());
	}

	private static void deleteCalendarsUsingBatch() throws IOException {
		System.out.println(("Delete Calendars Using Batch"));
		BatchRequest batch = client.batch();
		for (Calendar calendar : addedCalendarsUsingBatch) {
			client.calendars().delete(calendar.getId())
					.queue(batch, new JsonBatchCallback<Void>() {

						@Override
						public void onSuccess(Void content,
								HttpHeaders responseHeaders) {
							System.out.println("Delete is successful!");
						}

						@Override
						public void onFailure(GoogleJsonError e,
								HttpHeaders responseHeaders) {
							System.out.println("Error Message: "
									+ e.getMessage());
						}
					});
		}

		batch.execute();
	}

	private static void deleteCalendar(Calendar calendar) throws IOException {
		System.out.println(("Delete Calendar"));
		client.calendars().delete(calendar.getId()).execute();
	}
}
