package apps.weather;

public class YrWeatherReport {

	private String name;
	private String type;
	private String country;
	private String forecast1, forecast2, forecast3, forecast4, forecast5;
	private String text1, text2, text3, text4, text5;
	private String lastupdate, nextupdate;

	private String linkText, linkUrl;
	private String sunRise, sunSet;

	public YrWeatherReport() {
	}

	public String fullForecastToString() {
		StringBuilder str = new StringBuilder();
		str.append(getName());
		str.append(" ");
		str.append(getCountry());
		str.append("\nLast update: ");
		str.append(getLastupdate().substring(11));
		str.append("\nNext update: ");
		str.append(getNextupdate().substring(11));
		str.append("\n");
		str.append(getForecast1());
		str.append(" - ");
		str.append(getText1());
		str.append("\n");
		str.append(getForecast2());
		str.append(" - ");
		str.append(getText2());
		str.append("\n");
		str.append(getForecast3());
		str.append(" - ");
		str.append(getText3());
		str.append("\n");
		str.append(getForecast4());
		str.append(" - ");
		str.append(getText4());
		str.append("\n");
		str.append(getForecast5());
		str.append(" - ");
		str.append(getText5());
		str.append("\n");
		return str.toString();
	}

	// TODO: Create a shorthand format to return for twitter use. max 140 char.
	public String twitterForecastToString() {
		StringBuilder str = new StringBuilder();
		str.append(getName());
		str.append("\n");
		str.append(getForecast1());
		str.append(" - ");
		str.append(getText1());
		return str.toString();

	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getSunRise() {
		return sunRise;
	}

	public void setSunRise(String sunRise) {
		this.sunRise = sunRise;
	}

	public String getSunSet() {
		return sunSet;
	}

	public void setSunSet(String sunSet) {
		this.sunSet = sunSet;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public String getText3() {
		return text3;
	}

	public void setText3(String text3) {
		this.text3 = text3;
	}

	public String getText4() {
		return text4;
	}

	public void setText4(String text4) {
		this.text4 = text4;
	}

	public String getText5() {
		return text5;
	}

	public void setText5(String text5) {
		this.text5 = text5;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String latestupdate) {
		this.lastupdate = latestupdate;
	}

	public String getNextupdate() {
		return nextupdate;
	}

	public void setNextupdate(String nextupdate) {
		this.nextupdate = nextupdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getForecast1() {
		return forecast1;
	}

	public void setForecast1(String forecast1) {
		this.forecast1 = forecast1;
	}

	public String getForecast2() {
		return forecast2;
	}

	public void setForecast2(String forecast2) {
		this.forecast2 = forecast2;
	}

	public String getForecast3() {
		return forecast3;
	}

	public void setForecast3(String forecast3) {
		this.forecast3 = forecast3;
	}

	public String getForecast4() {
		return forecast4;
	}

	public void setForecast4(String forecast4) {
		this.forecast4 = forecast4;
	}

	public String getForecast5() {
		return forecast5;
	}

	public void setForecast5(String forecast5) {
		this.forecast5 = forecast5;
	}

}
