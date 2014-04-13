package apps.weather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBException;

import main.Constants;

import org.fedy2.weather.binding.RSSParser;
import org.fedy2.weather.data.Channel;
import org.fedy2.weather.data.Rss;
import org.fedy2.weather.data.unit.DegreeUnit;

public class YahooWeatherService {

	protected RSSParser parser;

	public YahooWeatherService() {
		try {
			parser = new RSSParser();
		} catch (JAXBException e) {
			System.out.println("Parser JAXBException");
		}
	}

	/**
	 * Retrieves and prints the weather in Trondheim.
	 * 
	 * @throws IOException
	 * @throws JAXBException
	 */
	public void printDefaultWeather() throws JAXBException, IOException {
		Channel ch;
		ch = getForecast(Constants.CODE_ALFREDLOCATION, DegreeUnit.CELSIUS);
		printToConsole(ch);
	}

	/**
	 * Method returns the most important features of the weather in a nice,
	 * stringy format.
	 * 
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	public String getTwitterWeather() throws JAXBException, IOException {
		Channel ch;
		StringBuilder tweet = new StringBuilder();
		ch = getForecast(Constants.CODE_ALFREDLOCATION, DegreeUnit.CELSIUS);
		tweet.append("Conditions: ");
		tweet.append(ch.getItem().getCondition().getText());
		tweet.append("\nTemp: ");
		tweet.append(ch.getItem().getCondition().getTemp());
		tweet.append("\n");
		tweet.append("Tmrw: ");
		tweet.append("Low: ");
		tweet.append(ch.getItem().getForecasts().get(0).getLow());
		tweet.append(" High: ");
		tweet.append(ch.getItem().getForecasts().get(0).getHigh());
		tweet.append(" Conditions: ");
		tweet.append(ch.getItem().getForecasts().get(0).getText());
		tweet.append("\n");
		tweet.append("Day aft tmrw: ");
		tweet.append("Low: ");
		tweet.append(ch.getItem().getForecasts().get(1).getLow());
		tweet.append(" High: ");
		tweet.append(ch.getItem().getForecasts().get(1).getHigh());
		tweet.append(" Conditions: ");
		tweet.append(ch.getItem().getForecasts().get(1).getText());
		return tweet.toString();
	}

	public Channel getForecast(String woeid, DegreeUnit unit)
			throws JAXBException, IOException {
		String url = composeUrl(woeid);
		// System.out.println("url: " + url);
		String xml;
		xml = retrieveRSS(url);
		// System.out.println("xml: " + xml);
		Rss rss = parser.parse(xml);
		return rss.getChannel();
	}

	protected String composeUrl(String woeid) {
		StringBuilder url = new StringBuilder(
				Constants.WEATHER_SERVICE_BASE_URL);
		url.append('?');
		url.append("w");
		url.append('=');
		url.append(woeid);
		url.append('&');
		url.append("u");
		url.append('=');
		url.append('c');
		return url.toString();
	}

	protected String retrieveRSS(String serviceUrl) throws IOException {
		URL url = new URL(serviceUrl);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(is);
		StringWriter writer = new StringWriter();
		copy(reader, writer);
		reader.close();
		is.close();

		return writer.toString();
	}

	private long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[Constants.DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public void printToConsole(Channel ch) {
		System.out.println("Information gathered from: " + ch.getLink());
		System.out.println("\n----------Current information----------");
		System.out.println("" + ch.getItem().getTitle());

		System.out.println("Conditions: "
				+ ch.getItem().getCondition().getText());
		System.out.println("Temperature: "
				+ ch.getItem().getCondition().getTemp());
		System.out.println("Wind chill: " + ch.getWind().getChill());
		System.out.println("Wind direction: " + ch.getWind().getDirection());
		System.out.println("Wind speed: " + ch.getWind().getSpeed() + " km/h");
		System.out.println("\n----------Forecast information----------");
		System.out.println("Tomorrow:           " + "Low: "
				+ ch.getItem().getForecasts().get(0).getLow() + " High: "
				+ ch.getItem().getForecasts().get(0).getHigh()
				+ " Conditions: "
				+ ch.getItem().getForecasts().get(0).getText());
		System.out.println("Day after tomorrow: " + "Low: "
				+ ch.getItem().getForecasts().get(1).getLow() + " High: "
				+ ch.getItem().getForecasts().get(1).getHigh()
				+ " Conditions: "
				+ ch.getItem().getForecasts().get(1).getText());

		System.out.println("\n-----------Other current information-----------");
		System.out.println("Sunrise: "
				+ ch.getAstronomy().getSunrise().getHours() + ":"
				+ ch.getAstronomy().getSunrise().getMinutes());
		System.out.println("Sunset: "
				+ ch.getAstronomy().getSunset().getHours() + ":"
				+ ch.getAstronomy().getSunset().getMinutes());
		System.out.println("Latitude: " + ch.getItem().getGeoLat());
		System.out.println("Longitude: " + ch.getItem().getGeoLong());
		System.out.println("Humidity: " + ch.getAtmosphere().getHumidity());
		System.out.println("Pressure: " + ch.getAtmosphere().getPressure());
		System.out.println("Pressure State: " + ch.getAtmosphere().getRising());
		System.out.println("Visibility: " + ch.getAtmosphere().getVisibility());
	}
}
