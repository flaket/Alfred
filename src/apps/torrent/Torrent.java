package apps.torrent;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import main.Constants;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

/**
 * This class is no longer used. Utorrent will automatically start downloading
 * any .torrent file found in the downloads/torrents folder. The download
 * scraper should visit eztv once a day and download any new wanted torrents to
 * this folder.
 * 
 * @author Andreas
 * 
 */
public class Torrent implements Runnable {

	String torrentPath = Constants.TORRENT_PATH;
	String outputPath = Constants.OUTPUT_PATH;
	public Client client;
	InetAddress ip;
	public File torrent;
	String torrentName;
	double torrentSize;

	public Torrent() {
	}

	public Torrent(String name) {
	}

	public void run() {
		try {
			setIp(InetAddress.getLocalHost());
			// Create a shared torrent.
			SharedTorrent sharedTorrent = initTorrent(torrent);
			// Initialize the client.
			initClient(ip, sharedTorrent);
			System.out.println("Beginning torrent download.");
			setTorrentSize(client.getTorrent().getSize() / 1048567.);
			setTorrent(client.getTorrent().getName());

			// Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			// Start downloading. Use client.share() to keep seeding after
			// finished.
//			Alfred.getTorrentDownloader().setDownloading(true);
			client.download();
		} catch (UnknownHostException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (IOException e) {
			System.out.println("No file named that in this folder.");
			e.printStackTrace();
		}
	}

	public void setTorrent(String name) {
		torrentPath = torrentPath + name + ".torrent";
		// Load in the downloaded torrent file.
		torrent = new File(torrentPath);
	}

	public void initClient(InetAddress ip, SharedTorrent torr)
			throws UnknownHostException, NoSuchAlgorithmException, IOException {
		client = new Client(ip, torr);
	}

	public SharedTorrent initTorrent(File torrent)
			throws NoSuchAlgorithmException, IOException {
		return SharedTorrent.fromFile(new File(getTorrentPath()), new File(
				getOutputPath()));
	}

	public String getTorrentPath() {
		return torrentPath;
	}

	public void setTorrentPath(String torrentPath) {
		this.torrentPath = torrentPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public File getTorrent() {
		return torrent;
	}

	public void setTorrent(File torrent) {
		this.torrent = torrent;
	}

	public String getTorrentName() {
		return torrentName;
	}

	public void setTorrentName(String torrentName) {
		this.torrentName = torrentName;
	}

	public double getTorrentSize() {
		return torrentSize;
	}

	public void setTorrentSize(double torrentSize) {
		this.torrentSize = torrentSize;
	}

	public void clear() {
		torrentPath = Constants.TORRENT_PATH;
	}
}
