package comm.twitter;

import main.Alfred;
import main.Constants;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class AccountListener implements UserStreamListener {

	public AccountListener() {
	}

	/**
	 * Called when a direct message has been found in the inbox.
	 */
	@Override
	public void onDirectMessage(DirectMessage msg) {
		if (msg.getSenderScreenName().compareTo(Constants.AUTH_USER) == 0) {
			Alfred.getTwitterCmdHandler().executeCommand(
					msg.getText().split(" "));
		} else if (msg.getSenderScreenName().compareTo("IvarFlakstad") == 0) {
			TwitterComm.sendDirectMessage("IvarFlakstad",
					"And a pleasant day to you, sir.");
			System.out.println("Sent a direct message to Ivar Flakstad.");
		} else if (msg.getSenderScreenName().compareTo("Maystorseth") == 0) {
			TwitterComm.sendDirectMessage("Maystorseth",
					"And a pleasant day to you, miss.");
			System.out.println("Sent a direct message to May-Britt Størseth.");
		} else
			System.out
					.println("I've received a direct message from someone I don't recognize, sir.");
	}

	/**
	 * Called when there's a new tweet in the timeline.
	 */
	@Override
	public void onStatus(Status status) {
		System.out.println("There's new status update in my twitter timeline:");
		switch (status.getUser().getScreenName()) {
		case Constants.AUTH_USER:
			Alfred.getLocation().upDateLocation(status);
			break;
		case "NRK_Nyheter":
			handleNews(status);
			break;
		case "Aftenposten":
			handleNews(status);
			break;
		case "110SorTrondelag":
			handleNews(status);
			break;
		case "politiOpsSTPD":
			handleNews(status);
			break;
		case "NRKno":
			handleNews(status);
			break;
		default:
			System.out
					.println("Status update was from an unrecognizable sender, so I did nothing.");
			break;
		}
	}

	/**
	 * Helper method taking care of all status updates relating to breaking
	 * news.
	 * 
	 * @param status
	 */
	private void handleNews(Status status) {
		System.out.println("@" + status.getUser().getScreenName() + ": "
				+ status.getText());
		Alfred.setLatestNewsString(status.getText());
		try {
			String url = status.getURLEntities()[0].getURL().toString();
			System.out.println("Updating the latest news url. Set to: " + url);
			Alfred.setLatestNewsURL(url);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out
					.println("Aftenposten tweeted news, but I couldnt extract the link.");
		}
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		System.out.println("Got a status deletion notice id:"
				+ statusDeletionNotice.getStatusId());
	}

	@Override
	public void onDeletionNotice(long directMessageId, long userId) {
		System.out.println("Got a direct message deletion notice id:"
				+ directMessageId);
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		System.out.println("Got a track limitation notice:"
				+ numberOfLimitedStatuses);
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Got scrub_geo event userId:" + userId
				+ " upToStatusId:" + upToStatusId);
	}

	@Override
	public void onStallWarning(StallWarning warning) {
		System.out.println("Got stall warning:" + warning);
	}

	@Override
	public void onFriendList(long[] friendIds) {
		// System.out.print("onFriendList");
		// for (long friendId : friendIds) {
		// System.out.print(" " + friendId);
		// }
	}

	@Override
	public void onFavorite(User source, User target, Status favoritedStatus) {
		System.out.println("onFavorite source:@" + source.getScreenName()
				+ " target:@" + target.getScreenName() + " @"
				+ favoritedStatus.getUser().getScreenName() + " - "
				+ favoritedStatus.getText());
	}

	@Override
	public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
		System.out.println("onUnFavorite source:@" + source.getScreenName()
				+ " target:@" + target.getScreenName() + " @"
				+ unfavoritedStatus.getUser().getScreenName() + " - "
				+ unfavoritedStatus.getText());
	}

	@Override
	public void onFollow(User source, User followedUser) {
		System.out.println("onFollow source:@" + source.getScreenName()
				+ " target:@" + followedUser.getScreenName());
	}

	@Override
	public void onUserListMemberAddition(User addedMember, User listOwner,
			UserList list) {
		System.out.println("onUserListMemberAddition added member:@"
				+ addedMember.getScreenName() + " listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserListMemberDeletion(User deletedMember, User listOwner,
			UserList list) {
		System.out.println("onUserListMemberDeleted deleted member:@"
				+ deletedMember.getScreenName() + " listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserListSubscription(User subscriber, User listOwner,
			UserList list) {
		System.out.println("onUserListSubscribed subscriber:@"
				+ subscriber.getScreenName() + " listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserListUnsubscription(User subscriber, User listOwner,
			UserList list) {
		System.out.println("onUserListUnsubscribed subscriber:@"
				+ subscriber.getScreenName() + " listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserListCreation(User listOwner, UserList list) {
		System.out.println("onUserListCreated listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserListUpdate(User listOwner, UserList list) {
		System.out.println("onUserListUpdated listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserListDeletion(User listOwner, UserList list) {
		System.out.println("onUserListDestroyed listOwner:@"
				+ listOwner.getScreenName() + " list:" + list.getName());
	}

	@Override
	public void onUserProfileUpdate(User updatedUser) {
		System.out.println("onUserProfileUpdated user:@"
				+ updatedUser.getScreenName());
	}

	@Override
	public void onBlock(User source, User blockedUser) {
		System.out.println("onBlock source:@" + source.getScreenName()
				+ " target:@" + blockedUser.getScreenName());
	}

	@Override
	public void onUnblock(User source, User unblockedUser) {
		System.out.println("onUnblock source:@" + source.getScreenName()
				+ " target:@" + unblockedUser.getScreenName());
	}

	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();
		System.out.println("onException:" + ex.getMessage());
	}

}
