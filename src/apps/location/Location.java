package apps.location;

import main.Constants;

import org.joda.time.DateTime;
import twitter4j.GeoLocation;
import twitter4j.Status;

public class Location {

	private GeoLocation geoLocation;
	private double latitude, longitude;
	private DateTime dateTime;

	/**
	 * A location object holding information about the last known whereabouts of
	 * the user. TODO: Connect this to google latitude. This way Alfred knows
	 * the location of the cell phone at all times.
	 */
	public Location() {
		setDateTime(new DateTime());
		setLatitude(Constants.INITIAL_LATITUDE);
		setLongitude(Constants.INITIAL_LONGITUDE);
		setGeoLocation(new GeoLocation(getLatitude(), getLongitude()));

	}

	public void upDateLocation(Status status) {
		setDateTime(new DateTime(status.getCreatedAt()));
		if (status.getGeoLocation() != null) {
			setGeoLocation(status.getGeoLocation());
			setLatitude(status.getGeoLocation().getLatitude());
			setLongitude(status.getGeoLocation().getLongitude());
		}
		printLog();
	}

	public void printLog() {
		System.out.println(getDateTime().toString());
		System.out.println("Updated location info:");
		System.out.println("Latitude: " + getLatitude() + "N");
		System.out.println("Longitude: " + getLongitude() + "E");
		System.out.println("GeoLocation: " + getGeoLocation().toString());
	}

	public String toString() {
		return "Last known location \n" + getDateTime().toString() + "\n"
				+ getLatitude() + "N " + getLongitude() + "E";

	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

}
