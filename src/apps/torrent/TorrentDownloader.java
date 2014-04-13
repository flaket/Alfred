package apps.torrent;

import java.io.File;
import java.util.ArrayList;

import main.Constants;

/**
 * This class is no longer used. Utorrent will automatically start downloading
 * any .torrent file found in the downloads/torrents folder. The download
 * scraper should visit eztv once a day and download any new wanted torrents to
 * this folder.
 * 
 * @author Andreas
 * 
 */
public class TorrentDownloader {

	private Torrent currentTorrent;
	private ArrayList<Torrent> torrentList;
	private boolean isDownloading;

	public TorrentDownloader() {
		System.out.println("...enabling TorrentDownloader");
		setDownloading(false);
		setCurrentTorrent(new Torrent());
		torrentList = new ArrayList<Torrent>();
		System.out.println("Torrent downloader enabled.");
	}

	public void runJob(String name) {
		Thread torrentThread = new Thread(new Torrent(name));
		torrentThread.run();
	}

	public String getAvailableTorrents() {
		File folder = new File(Constants.TORRENT_PATH);
		File[] listOfFiles = folder.listFiles();
		String files = "";
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files = files
						+ listOfFiles[i].getName().replace(".torrent", "");
			}
			// else if (listOfFiles[i].isDirectory()) {
			// System.out.println("Directory " + listOfFiles[i].getName());
			// }
		}
		return files;
	}

	public void print() {
		if (isDownloading()) {
			System.out.println("Name: " + currentTorrent.torrentName);
			System.out.println("Status: "
					+ currentTorrent.client.getState().toString());
			System.out
					.println("Size: "
							+ String.format("%.2f", currentTorrent.torrentSize)
							+ " MB");
			System.out.println("Completion: "
					+ (int) currentTorrent.client.getTorrent().getCompletion()
					+ "%");
		} else
			System.out
					.println("There are no active torrents downloading, sir.");
	}

	public ArrayList<Torrent> getTorrentList() {
		return torrentList;
	}

	public Torrent getCurrentTorrent() {
		return currentTorrent;
	}

	public void setCurrentTorrent(Torrent currentTorrent) {
		this.currentTorrent = currentTorrent;
	}

	public boolean isDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}

}
