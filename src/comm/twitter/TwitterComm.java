package comm.twitter;

import main.Constants;
import org.joda.time.DateTime;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterComm {

	public static Twitter twitter;
	public static TwitterStream twitterStream;
	public static TwitterCmdHandler commandHandler;

	public TwitterComm() {
		// Setup the stream.
		twitterStream = new TwitterStreamFactory().getInstance();
		// Add the customized listener.
		twitterStream.addListener(new AccountListener());
		// Requests startup back-fill from the REST API and then
		// transitions to streaming for nearly all subsequent reads. Rate limits
		// and latency are practically eliminated.
		twitterStream.user();
	}

	/**
	 * Opens up a connection and sends a direct message.
	 * 
	 * @param recipient
	 *            : The wanted receiver of the message.
	 * @param message
	 *            : The message to be sent.
	 */
	public static void sendDirectMessage(String recipient, String message) {
		DateTime now = new DateTime();
		twitter = new TwitterFactory().getInstance();
		try {
			DirectMessage msg = twitter.sendDirectMessage(recipient, message);
			System.out.println(now + " Direct message successfully sent to "
					+ msg.getRecipientScreenName());
			System.out.println("Message was: \n" + message);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println(now + "Failed to send a direct message: "
					+ te.getMessage());
		}
	}

	/**
	 * Fetches tweets from the timeline.
	 * 
	 * @param page
	 *            : from which page. 1 is the newest page.
	 * @param count
	 *            : how many tweets. 1 will retrieve only the last.
	 */
	public static void fetchTweets(int page, int count) {
		try {
			ResponseList<Status> responseList;
			responseList = twitter.getHomeTimeline(new Paging(page, count));
			for (Status status : responseList) {
				System.out.println("@" + status.getUser().getScreenName()
						+ " - " + status.getText());
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to fetch tweets: " + te.getMessage());
		}
	}

	/**
	 * Fetches the last command from the authenticated user.
	 */
	public static void fetchLastAuthTweet() {
		try {
			ResponseList<Status> responseList;
			responseList = twitter.getHomeTimeline();
			for (Status status : responseList) {
				if (status.getUser().getScreenName()
						.compareTo(Constants.AUTH_USER) == 0
						&& status.getText().startsWith(
								"@" + Constants.TWITTER_ACCOUNT)) {
					// Print only the command.
					System.out.println(status.getText().split(" ")[1]);
				}
			}
			System.out
					.println("No commands from authenticated user on the timeline.");

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to fetch the timeline: "
					+ te.getMessage());
		}
	}

	/**
	 * Gets the messages from the direct message inbox.
	 */
	public static void getDirectMessages() {
		try {
			ResponseList<DirectMessage> responseList;
			responseList = twitter.getDirectMessages();
			for (DirectMessage msg : responseList) {
				System.out.println("@" + msg.getSenderScreenName() + " - "
						+ msg.getText());
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to fetch the direct messages: "
					+ te.getMessage());
		}
	}

	/**
	 * Gets the last message from a authenticated user.
	 */
	public static void getLastAuthDirectMessage() {
		try {
			ResponseList<DirectMessage> responseList;
			responseList = twitter.getDirectMessages();
			for (DirectMessage msg : responseList) {
				if (msg.getSenderScreenName().compareTo(Constants.AUTH_USER) == 0) {
					// Print only the command.
					System.out.println(msg.getText().split(" ")[0]);
				}
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to fetch the direct messages: "
					+ te.getMessage());
		}
	}
}
