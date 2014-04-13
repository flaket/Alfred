package apps.torrent;

import java.io.IOException;

import main.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author Andreas
 * 
 */
public class TorrentScraper {

	public TorrentScraper() {
	}

	// For testing
	public static void main(String[] args) {

		new TorrentScraper().scrape();

	}

	/**
	 * Method searches the front page of eztv.it for interesting torrents.
	 */
	public void scrape() {
		try {
			// Extract html page to doc.
			Document doc = Jsoup.connect("http://eztv.it/").get();
			// Find link elements by class.
			Elements torrents = doc.getElementsByClass("download_1");
			// Extract only the http
			for (Element torrent : torrents) {
				// If the link contains any of the wanted keywords select that
				// link.
				for (int i = 0; i < Constants.KEYWORDS.length; i++) {
					if (torrent.toString().toLowerCase().contains(Constants.KEYWORDS[i].toLowerCase())) {
						// Each torrent link from the page containts only two
						// ""s so let's split on those.
						String[] str = torrent.toString().split("\"", 3);
						download(str[1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Helper, saves torrents to drive and adds new tasks to download queue.
	 * 
	 * @param url
	 */
	private void download(String url) {
		try {
			new ProcessBuilder(Constants.CH_PATH_BROWSER, url).start();
			System.out.println("Saving torrent to drive: " + url);
			// // Remove leading http...
			// url = url.substring(24);
			// // Remove trailing .torrent
			// url = url.replace(".torrent", "");
			// // Add the torrent to the TorrentDownloader queue.
			// System.out
			// .println("Adding the downloaded torrent to the download queue.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
