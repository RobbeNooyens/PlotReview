package me.robnoo02.plotreviewplugin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.files.DataFileManager;
import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;

public class Query {

	/**
	 * A QueryElement is a single piece of info of a ticket. Information can be
	 * requested with the request() method.
	 */
	public static enum QueryElement {

		/**
		 * String format of the review ID.
		 */
		TICKET_ID,
		/**
		 * The name of the Player who submitted the review.
		 */
		REVIEWEE_NAME,
		/**
		 * The UUID of the Player who submitted the review.
		 */
		REVIEWEE_UUID,
		/**
		 * The ID of the plot that is being reviewed in String format.
		 */
		PLOT_ID,
		/**
		 * The name of the world the reviewed plot is in.
		 */
		WORLD,
		/**
		 * String format of boolean: true if the plot has been reviewed.
		 */
		REVIEWED_BY_STAFF,
		/**
		 * Rank of the player who submitted the review which he had when he submitted
		 * the review.
		 */
		RANK,
		/**
		 * Date the review has been created.
		 */
		CREATION_DATE,
		/**
		 * Represents the score of the structure in general
		 */
		STRUCTURE_SCORE,
		/**
		 * Represents the score of the terrain the player made
		 */
		TERRAIN_SCORE,
		/**
		 * Represents the score of the organics the player added
		 */
		ORGANICS_SCORE,
		/**
		 * Represents the score of the composition in general
		 */
		COMPOSITION_SCORE,
		/**
		 * Average score of the individual scores
		 */
		RESULT,
		/**
		 * UUID of the staffmember who gave points
		 */
		STAFF_UUID,
		/**
		 * Name of the staffmember who gave points
		 */
		STAFF_NAME;

		/**
		 * Reads one specific element of a ticket. I've chosen to call the
		 * getUserDataField method every single time instead of collecting a HashMap
		 * first so that it won't unnecessary be called for non-userdata related
		 * requests.
		 * 
		 * @param ticketId
		 *            is the id number of the ticket
		 */
		public String request(int ticketId) {
			if (DataFileManager.containsId(ticketId)) return null;
			switch (this) {
			case TICKET_ID:
				return String.valueOf(ticketId);
			case REVIEWED_BY_STAFF:
				return DataFileManager.getIsReviewed(ticketId);
			case REVIEWEE_UUID:
				return DataFileManager.getUUID(ticketId);
			case REVIEWEE_NAME:
				return Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.getUUID(ticketId))).getName();
			case CREATION_DATE:
				return UserDataManager.getUserDataField(ticketId, UserDataField.DATE);
			case PLOT_ID:
				return UserDataManager.getUserDataField(ticketId, UserDataField.PLOT);
			case RANK:
				return UserDataManager.getUserDataField(ticketId, UserDataField.RANK);
			case RESULT:
				return UserDataManager.getUserDataField(ticketId, UserDataField.RESULT);
			case COMPOSITION_SCORE:
				return UserDataManager.getUserDataField(ticketId, UserDataField.COMPOSITION_SCORE);
			case ORGANICS_SCORE:
				return UserDataManager.getUserDataField(ticketId, UserDataField.ORGANICS_SCORE);
			case STRUCTURE_SCORE:
				return UserDataManager.getUserDataField(ticketId, UserDataField.COMPOSITION_SCORE);
			case TERRAIN_SCORE:
				return UserDataManager.getUserDataField(ticketId, UserDataField.COMPOSITION_SCORE);
			case WORLD:
				return UserDataManager.getUserDataField(ticketId, UserDataField.COMPOSITION_SCORE);
			case STAFF_UUID:
				return UserDataManager.getUserDataField(ticketId, UserDataField.STAFF);
			case STAFF_NAME:
				OfflinePlayer staff = Bukkit
						.getPlayer(UUID.fromString(UserDataManager.getUserDataField(ticketId, UserDataField.STAFF)));
				return (staff == null) ? "" : staff.getName();
			default:
				return null;
			}
		}

	}

	public static enum QueryGroup {
		INFO, HISTORY, GUI, SCORES;

		public HashMap<QueryElement, String> group(int reviewId) {
			switch (this) {
			case GUI:
				return requestReviewsGui(reviewId);
			case HISTORY:
				return requestHistoryCommand(reviewId);
			case INFO:
				return requestInfoCommand(reviewId);
			case SCORES:
				return requestScores(reviewId);
			default:
				return null;
			}
		}
	}

	/**
	 * Contains: TicketID, Reviewee's name, Rank, Date, World, Plot, Score
	 * 
	 * @return ArrayList containing info for /review info command
	 */
	private static HashMap<QueryElement, String> requestInfoCommand(int reviewId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		String idValue = DataFileManager.getValue(reviewId);
		HashMap<UserDataField, String> userData = UserDataManager.getUserData(reviewId);

		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(userData.get(UserDataField.STAFF)));

		output.put(QueryElement.TICKET_ID, String.valueOf(reviewId));
		output.put(QueryElement.REVIEWED_BY_STAFF, DataFileManager.strip(idValue, "\\+", 2));
		output.put(QueryElement.REVIEWEE_UUID, DataFileManager.strip(idValue, "\\+", 0));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.CREATION_DATE, userData.get(UserDataField.DATE));
		output.put(QueryElement.PLOT_ID, userData.get(UserDataField.PLOT));
		output.put(QueryElement.RANK, userData.get(UserDataField.RANK));
		output.put(QueryElement.RESULT, userData.get(UserDataField.RESULT));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(UserDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(UserDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(UserDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(UserDataField.TERRAIN_SCORE));
		output.put(QueryElement.WORLD, userData.get(UserDataField.WORLD));
		output.put(QueryElement.STAFF_UUID, userData.get(UserDataField.STAFF));
		if (staff != null) output.put(QueryElement.STAFF_UUID, staff.getName());
		return output;
	}
	
	/**
	 * Contains: TicketID, Reviewee's name, Rank, Date, World, Plot, Score
	 * 
	 * @return ArrayList containing info for /review history command
	 */
	private static HashMap<QueryElement, String> requestHistoryCommand(int reviewId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		String idValue = DataFileManager.getValue(reviewId);
		HashMap<UserDataField, String> userData = UserDataManager.getUserData(reviewId);

		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(userData.get(UserDataField.STAFF)));

		output.put(QueryElement.TICKET_ID, String.valueOf(reviewId));
		output.put(QueryElement.REVIEWED_BY_STAFF, DataFileManager.strip(idValue, "\\+", 2));
		output.put(QueryElement.REVIEWEE_UUID, DataFileManager.strip(idValue, "\\+", 0));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.CREATION_DATE, userData.get(UserDataField.DATE));
		output.put(QueryElement.PLOT_ID, userData.get(UserDataField.PLOT));
		output.put(QueryElement.RANK, userData.get(UserDataField.RANK));
		output.put(QueryElement.RESULT, userData.get(UserDataField.RESULT));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(UserDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(UserDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(UserDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(UserDataField.TERRAIN_SCORE));
		output.put(QueryElement.WORLD, userData.get(UserDataField.WORLD));
		output.put(QueryElement.STAFF_UUID, userData.get(UserDataField.STAFF));
		if (staff != null) output.put(QueryElement.STAFF_UUID, staff.getName());
		return output;
	}
	
	/**
	 * Contains: TicketID, Reviewee's name, Rank, Date, World, Plot, Score
	 * 
	 * @return ArrayList containing info for /review info command
	 */
	private static HashMap<QueryElement, String> requestReviewsGui(int reviewId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		String idValue = DataFileManager.getValue(reviewId);
		HashMap<UserDataField, String> userData = UserDataManager.getUserData(reviewId);

		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(userData.get(UserDataField.STAFF)));

		output.put(QueryElement.TICKET_ID, String.valueOf(reviewId));
		output.put(QueryElement.REVIEWED_BY_STAFF, DataFileManager.strip(idValue, "\\+", 2));
		output.put(QueryElement.REVIEWEE_UUID, DataFileManager.strip(idValue, "\\+", 0));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.CREATION_DATE, userData.get(UserDataField.DATE));
		output.put(QueryElement.PLOT_ID, userData.get(UserDataField.PLOT));
		output.put(QueryElement.RANK, userData.get(UserDataField.RANK));
		output.put(QueryElement.RESULT, userData.get(UserDataField.RESULT));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(UserDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(UserDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(UserDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(UserDataField.TERRAIN_SCORE));
		output.put(QueryElement.WORLD, userData.get(UserDataField.WORLD));
		output.put(QueryElement.STAFF_UUID, userData.get(UserDataField.STAFF));
		if (staff != null) output.put(QueryElement.STAFF_UUID, staff.getName());
		return output;
	}

	/**
	 * Contains: ID, Structure, Composition, Organics, Terrain, Result
	 * 
	 * @return ArrayList containing scores
	 */
	private static HashMap<QueryElement, String> requestScores(int reviewId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		HashMap<UserDataField, String> userData = UserDataManager.getUserData(reviewId);

		output.put(QueryElement.TICKET_ID, String.valueOf(reviewId));
		output.put(QueryElement.RESULT, userData.get(UserDataField.RESULT));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(UserDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(UserDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(UserDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(UserDataField.TERRAIN_SCORE));
		return output;
	}

}
