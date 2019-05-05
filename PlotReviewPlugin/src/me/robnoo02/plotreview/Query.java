package me.robnoo02.plotreview;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.files.UserDataFileFields.PlayerInfoField;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;

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
		DATE,
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
		STOC,
		/**
		 * UUID of the staffmember who gave points
		 */
		STAFF_UUID,
		/**
		 * Name of the staffmember who gave points
		 */
		STAFF_NAME,
		/**
		 * Avgerage stoc score for that plot
		 */
		AVG_STOC,
		/**
		 * Whether or not the build passes
		 */
		PASSES,
		/**
		 * Rating of a player's builds
		 */
		RATING,
		/**
		 * Version of the plugin
		 */
		PLUGIN_VERSION,
		/**
		 * Description of the plugin in plugin.yml
		 */
		PLUGIN_DESCRIPTION,
		/**
		 * Developers of the plugin
		 */
		PLUGIN_DEVELOPERS,
		/**
		 * Name of the plugin
		 */
		PLUGIN_NAME;

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
			if (!DataFileManager.containsId(ticketId)) return null;
			switch (this) {
			case TICKET_ID:
				return String.valueOf(ticketId);
			case REVIEWED_BY_STAFF:
				return DataFileManager.getIsReviewed(ticketId);
			case REVIEWEE_UUID:
				return DataFileManager.getUUID(ticketId);
			case REVIEWEE_NAME:
				return Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.getUUID(ticketId))).getName();
			case DATE:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.DATE);
			case PLOT_ID:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.PLOT);
			case RANK:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.RANK);
			case STOC:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.STOC);
			case COMPOSITION_SCORE:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.COMPOSITION_SCORE);
			case ORGANICS_SCORE:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.ORGANICS_SCORE);
			case STRUCTURE_SCORE:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.COMPOSITION_SCORE);
			case TERRAIN_SCORE:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.COMPOSITION_SCORE);
			case WORLD:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.COMPOSITION_SCORE);
			case STAFF_UUID:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.STAFF);
			case PASSES:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.PASSES);
			case STAFF_NAME:
				String uuid = UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.STAFF);
				OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				return (staff == null) ? "" : staff.getName();
			case RATING:
				return UserDataManager.getUserDataFile(ticketId).getString(ticketId, PlayerInfoField.RATING);
			case PLUGIN_VERSION:
				return Main.getInstance().getDescription().getVersion();
			case PLUGIN_DESCRIPTION:
				return Main.getInstance().getDescription().getDescription();
			case PLUGIN_NAME:
				return Main.getInstance().getDescription().getName();
			case PLUGIN_DEVELOPERS:
				return "Robnoo02, Mirass";
			default:
				return null;
			}
		}

		public String getPlaceHolder() {
			return "%" + this.toString().toLowerCase() + "%";
		}

	}

	public static enum QueryGroup {
		INFO, HISTORY, GUI, SCORES, PLUGIN, PLOT_REVIEWED;

		public HashMap<QueryElement, String> get(int ticketId) {
			switch (this) {
			case GUI:
				return requestReviewsGui(ticketId);
			case HISTORY:
				return requestHistoryCommand(ticketId);
			case INFO:
				return requestInfoCommand(ticketId);
			case SCORES:
				return requestScores(ticketId);
			case PLUGIN:
				return requestPluginInfo(ticketId);
			case PLOT_REVIEWED:
				return requestReviewedClick(ticketId);
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
	private static HashMap<QueryElement, String> requestInfoCommand(int ticketId) {

		HashMap<QueryElement, String> output = new HashMap<>();
		HashMap<TicketDataField, String> userData = UserDataManager.getUserDataFile(ticketId).getUserData(ticketId);

		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.getUUID(ticketId)));
		OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(userData.get(TicketDataField.STAFF)));

		output.put(QueryElement.TICKET_ID, String.valueOf(ticketId));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.RANK, userData.get(TicketDataField.RANK));
		output.put(QueryElement.DATE, userData.get(TicketDataField.DATE));
		output.put(QueryElement.WORLD, userData.get(TicketDataField.WORLD));
		output.put(QueryElement.PLOT_ID, userData.get(TicketDataField.PLOT));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(TicketDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(TicketDataField.TERRAIN_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(TicketDataField.ORGANICS_SCORE));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(TicketDataField.COMPOSITION_SCORE));
		output.put(QueryElement.STOC, userData.get(TicketDataField.STOC));
		output.put(QueryElement.AVG_STOC, userData.get(TicketDataField.AVERAGE_STOC));
		output.put(QueryElement.STAFF_NAME, (staff == null) ? null : staff.getName());

		return output;
	}

	/**
	 * Contains: TicketID, Reviewee's name, Rank, Date, World, Plot, Score
	 * 
	 * @return ArrayList containing info for /review history command
	 */
	private static HashMap<QueryElement, String> requestHistoryCommand(int ticketId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		String idValue = DataFileManager.getValue(ticketId);
		HashMap<TicketDataField, String> userData = UserDataManager.getUserDataFile(ticketId).getUserData(ticketId);

		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(userData.get(TicketDataField.STAFF)));

		output.put(QueryElement.TICKET_ID, String.valueOf(ticketId));
		output.put(QueryElement.REVIEWED_BY_STAFF, DataFileManager.strip(idValue, "\\+", 2));
		output.put(QueryElement.REVIEWEE_UUID, DataFileManager.strip(idValue, "\\+", 0));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.DATE, userData.get(TicketDataField.DATE));
		output.put(QueryElement.PLOT_ID, userData.get(TicketDataField.PLOT));
		output.put(QueryElement.RANK, userData.get(TicketDataField.RANK));
		output.put(QueryElement.STOC, userData.get(TicketDataField.STOC));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(TicketDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(TicketDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(TicketDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(TicketDataField.TERRAIN_SCORE));
		output.put(QueryElement.WORLD, userData.get(TicketDataField.WORLD));
		output.put(QueryElement.STAFF_UUID, userData.get(TicketDataField.STAFF));
		if (staff != null) output.put(QueryElement.STAFF_UUID, staff.getName());
		return output;
	}

	/**
	 * Contains: TicketID, Reviewee's name, Rank, Date, World, Plot, Score
	 * 
	 * @return ArrayList containing info for /review info command
	 */
	private static HashMap<QueryElement, String> requestReviewsGui(int ticketId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		String idValue = DataFileManager.getValue(ticketId);
		HashMap<TicketDataField, String> userData = UserDataManager.getUserDataFile(ticketId).getUserData(ticketId);

		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		OfflinePlayer staff = Bukkit.getOfflinePlayer(UUID.fromString(userData.get(TicketDataField.STAFF)));

		output.put(QueryElement.TICKET_ID, String.valueOf(ticketId));
		output.put(QueryElement.REVIEWED_BY_STAFF, DataFileManager.strip(idValue, "\\+", 2));
		output.put(QueryElement.REVIEWEE_UUID, DataFileManager.strip(idValue, "\\+", 0));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.DATE, userData.get(TicketDataField.DATE));
		output.put(QueryElement.PLOT_ID, userData.get(TicketDataField.PLOT));
		output.put(QueryElement.RANK, userData.get(TicketDataField.RANK));
		output.put(QueryElement.STOC, userData.get(TicketDataField.STOC));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(TicketDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(TicketDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(TicketDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(TicketDataField.TERRAIN_SCORE));
		output.put(QueryElement.WORLD, userData.get(TicketDataField.WORLD));
		output.put(QueryElement.STAFF_UUID, userData.get(TicketDataField.STAFF));
		if (staff != null) output.put(QueryElement.STAFF_UUID, staff.getName());
		return output;
	}

	/**
	 * Contains: ID, Structure, Composition, Organics, Terrain, Result
	 * 
	 * @return ArrayList containing scores
	 */
	private static HashMap<QueryElement, String> requestScores(int ticketId) {
		HashMap<QueryElement, String> output = new HashMap<>();

		HashMap<TicketDataField, String> userData = UserDataManager.getUserDataFile(ticketId).getUserData(ticketId);

		output.put(QueryElement.TICKET_ID, String.valueOf(ticketId));
		output.put(QueryElement.STOC, userData.get(TicketDataField.STOC));
		output.put(QueryElement.COMPOSITION_SCORE, userData.get(TicketDataField.COMPOSITION_SCORE));
		output.put(QueryElement.ORGANICS_SCORE, userData.get(TicketDataField.ORGANICS_SCORE));
		output.put(QueryElement.STRUCTURE_SCORE, userData.get(TicketDataField.STRUCTURE_SCORE));
		output.put(QueryElement.TERRAIN_SCORE, userData.get(TicketDataField.TERRAIN_SCORE));
		return output;
	}

	private static HashMap<QueryElement, String> requestPluginInfo(int ticketId) {
		HashMap<QueryElement, String> output = new HashMap<>();
		output.put(QueryElement.PLUGIN_DESCRIPTION, QueryElement.PLUGIN_DESCRIPTION.request(ticketId));
		output.put(QueryElement.PLUGIN_DEVELOPERS, QueryElement.PLUGIN_DEVELOPERS.request(ticketId));
		output.put(QueryElement.PLUGIN_NAME, QueryElement.PLUGIN_NAME.request(ticketId));
		output.put(QueryElement.PLUGIN_VERSION, QueryElement.PLUGIN_VERSION.request(ticketId));
		return output;
	}

	private static HashMap<QueryElement, String> requestReviewedClick(int ticketId) {
		HashMap<QueryElement, String> output = new HashMap<>();
		output.put(QueryElement.TICKET_ID, String.valueOf(ticketId));
		output.put(QueryElement.STAFF_NAME, QueryElement.STAFF_NAME.request(ticketId));
		return output;
	}

}
