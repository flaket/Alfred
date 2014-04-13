package apps.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import main.Constants;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class YrWeatherParser extends DefaultHandler {

	private YrWeatherReport weatherReport;
	private String temp;
	public int counter = 0;

	public YrWeatherParser() {
		super();
		setWeatherReport(new YrWeatherReport());
	}

	public void generateReport() throws UnsupportedEncodingException,
			IOException, SAXException {
		URL url;
		url = new URL(Constants.YR_URL);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				url.openStream(), "UTF-8"));
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(this);
		xr.setErrorHandler(this);
		xr.parse(new InputSource(in));
	}

	/*
	 * Every time the parser encounters the beginning of a new element, it calls
	 * this method, which resets the string buffer
	 */
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		temp = "";
		if (qName.toLowerCase().equals("link")) {
			getWeatherReport().setLinkText(atts.getValue("text"));
			getWeatherReport().setLinkText(atts.getValue("url"));
		} else if (qName.toLowerCase().equals("sun")) {
			getWeatherReport().setSunRise(atts.getValue("rise"));
			getWeatherReport().setSunSet(atts.getValue("set"));
		}
	}

	/*
	 * When the parser encounters the end of an element, it calls this method
	 */
	public void endElement(String uri, String name, String qName) {
		switch (qName.toLowerCase()) {
		case "name":
			getWeatherReport().setName(temp);
			break;
		case "type":
			getWeatherReport().setType(temp);
			break;
		case "country":
			getWeatherReport().setCountry(temp);
			break;
		case "lastupdate":
			getWeatherReport().setLastupdate(temp);
			break;
		case "nextupdate":
			getWeatherReport().setNextupdate(temp);
			break;
		case "title":
			counter++;
			if (counter == 1)
				getWeatherReport().setForecast1(temp);
			else if (counter == 2)
				getWeatherReport().setForecast2(temp);
			else if (counter == 3)
				getWeatherReport().setForecast3(temp);
			else if (counter == 4)
				getWeatherReport().setForecast4(temp);
			else if (counter == 5)
				getWeatherReport().setForecast5(temp);
			break;
		case "body":
			if (counter == 1)
				getWeatherReport().setText1(temp);
			else if (counter == 2)
				getWeatherReport().setText2(temp);
			else if (counter == 3)
				getWeatherReport().setText3(temp);
			else if (counter == 4)
				getWeatherReport().setText4(temp);
			else if (counter == 5)
				getWeatherReport().setText5(temp);
		}

	}

	/*
	 * When the parser encounters plain text (not XML elements), it calls(this
	 * method, which accumulates them in a string buffer
	 */
	public void characters(char[] ch, int start, int length) {
		temp = new String(ch, start, length);
	}

	public void startDocument() {
		// System.out.println("Start document");
	}

	public void endDocument() {
		// System.out.println("End document");
	}

	public YrWeatherReport getWeatherReport() {
		return weatherReport;
	}

	public void setWeatherReport(YrWeatherReport weatherReport) {
		this.weatherReport = weatherReport;
	}

}