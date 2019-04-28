package me.robnoo02.plotreviewplugin.files;

import java.util.HashMap;
import java.util.UUID;

/**
 * Each instance represents a userdata yml file.
 * A UserDataFile contains a Player uuid and a refernce to its yml in the userdata folder.
 * 
 * @author Robnoo02
 *
 */
public class UserDataFile {

	private final String uuid; // Stores Player UUID
	private final CustomYml yml; // Wraps customyml

	/**
	 * Constructor
	 * @param uuid The uuid of a Reviewee
	 */
	public UserDataFile(final UUID uuid) {
		this.uuid = uuid.toString();
		this.yml = CustomYml.createFileInFolder("userdata", this.uuid, true);
	}
	
	/**
	 * Gets the ymlFile used by this instance.
	 * @return YamlConfiguration for userdata configfile
	 */
	public CustomYml getCustomYml() {
		return yml;
	}
	
	public int getReviewedCount() {
		int count = 0;
		for(String ticketId: yml.getConfigSection("tickets.").getKeys(false)) {
			String path = TicketDataField.REVIEWED.getPath(Integer.valueOf(ticketId));
			count += (Boolean.valueOf(yml.getString(path)) ? 1 : 0);
		}
		return count;
	}

	/**
	 * Each enum value represents a key in a userdate yml.
	 * Placeholder is used to replace the yml's value with.
	 * @param pH Placeholder
	 * @author Robnoo02
	 *
	 */
	public static enum TicketDataField {
		RANK("%rank%"), DATE("%date%"), WORLD("%world%"), PLOT("%plot%"), STOC("%stoc%"), AVERAGE_STOC("%average_stoc%"), STRUCTURE_SCORE(
				"%structure_score%"), TERRAIN_SCORE("%terrain_score%"), ORGANICS_SCORE(
						"%organics_score%"), COMPOSITION_SCORE("%composition_score%"), STAFF("%staff%"), REVIEWED("%reviewed%");

		private String placeholder;

		private TicketDataField(String pH) {
			this.placeholder = pH;
		}

		/**
		 * Returns path to get a value in a userdata yml.
		 * @param id Review ticket ID
		 * @return Path for yml
		 */
		public String getPath(int id) {
			return "tickets." + String.valueOf(id) + "." + this.toString().toLowerCase();
		}

		public String getPlaceHolder() {
			return placeholder;
		}
	}
	
	public static enum PlayerInfoField {
		AVARAGE_STOC, TOTAL_STOC, RATING, TOTAL_PLOT_SCORE, NUMBER_OF_SUBMISSIONS, PENDING_TICKET, LATEST_NAME;
		
		public String getPath() {
			return "player-info." + this.toString().toLowerCase();
		}
	}

	/**
	 * Gets a yml value obtainable with a UserDataField key.
	 * @param id Review ticket ID
	 * @param field Key for Reviewinfo value
	 * @return String representing a part of the info from the Review ticket
	 */
	public String getString(int id, TicketDataField field) {
		return yml.getString(field.getPath(id));
	}
	
	public String getString(PlayerInfoField field) {
		return yml.getString(field.getPath());
	}

	/**
	 * Sets a value for a userdata key.
	 * @param id Review ticket ID
	 * @param field Key for Reviewinfo
	 * @param value Value to be set for the key
	 */
	public void setString(int id, TicketDataField field, String value) {
		yml.set(field.getPath(id), value);
	}
	
	public void setString(PlayerInfoField field, String value) {
		yml.set(field.getPath(), value);
	}

	/**
	 * @param id is ID of Review
	 * @return HashMap containing all fields with review data
	 */
	public HashMap<TicketDataField, String> getUserData(int id) {
		HashMap<TicketDataField, String> fields = new HashMap<>();
		for (TicketDataField field : TicketDataField.values())
			fields.put(field, getString(id, field));
		return fields;
	}
	
	public HashMap<PlayerInfoField, String> getPlayerInfo(){
		HashMap<PlayerInfoField, String> fields = new HashMap<>();
		for (PlayerInfoField field : PlayerInfoField.values())
			fields.put(field, getString(field));
		return fields;
	}
	
	/**
	 * Writes given data to userdata file.
	 * @param userUUID UUID of player to save data for
	 * @param id Review ticket ID
	 * @param data HashMap containing UserDataField as a key and its corresponding value as a String
	 */
	public void setUserData(final int id, final HashMap<TicketDataField, String> data) {
		for(TicketDataField field: TicketDataField.values()) // Loops through its keys
			if(data.containsKey(field) && data.get(field) != null) // Checks if given data contains requested data
				setString(id, field, data.get(field)); // Sets value for each key in yml
	}
	
	public void setPlayerInfo(final HashMap<PlayerInfoField, String> data) {
		for(PlayerInfoField field: PlayerInfoField.values()) // Loops through its keys
			if(data.containsKey(field) && data.get(field) != null) // Checks if given data contains requested data
				setString(field, data.get(field)); // Sets value for each key in yml
	}
}
