package me.robnoo02.plotreview.files;

import java.util.HashMap;
import java.util.UUID;

import me.robnoo02.plotreview.files.UserDataFileFields.NewScoresField;
import me.robnoo02.plotreview.files.UserDataFileFields.PlayerInfoField;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.files.UserDataFileFields.UserDataField;

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
		this.yml.setup();
		fillMissingInfo();
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
	
	public void fillMissingInfo() {
		for(PlayerInfoField field: PlayerInfoField.values()){
			if(!yml.containsKey(field.getPath()))
				setString(0, field, field.getDefaultValue());
		}
		for(NewScoresField field: NewScoresField.values()){
			if(!yml.containsKey(field.getPath()))
				setString(0, field, field.getDefaultValue());
		}
	}
	

	/**
	 * @param id is ID of Review
	 * @return HashMap containing all fields with review data
	 */
	public HashMap<TicketDataField, String> getUserData(int id) {
		HashMap<TicketDataField, String> fields = new HashMap<>();
		for (TicketDataField field : TicketDataField.values())
			fields.put(field, getString(id, field));
		for(TicketDataField field: fields.keySet())
			if(fields.get(field) == null)
				fields.put(field, field.getDefaultValue());
		return fields;
	}
	
	public HashMap<PlayerInfoField, String> getPlayerInfo(){
		HashMap<PlayerInfoField, String> fields = new HashMap<>();
		for (PlayerInfoField field : PlayerInfoField.values())
			fields.put(field, getString(0, field));
		for(PlayerInfoField field: fields.keySet())
			if(fields.get(field) == null)
				fields.put(field, field.getDefaultValue());
		return fields;
	}
	
	public HashMap<NewScoresField, String> getNewScores(){
		HashMap<NewScoresField, String> fields = new HashMap<>();
		for (NewScoresField field : NewScoresField.values())
			fields.put(field, getString(0, field));
		for(NewScoresField field: fields.keySet())
			if(fields.get(field) == null)
				fields.put(field, field.getDefaultValue());
		return fields;
	}
	
	/**
	 * Gets a yml value obtainable with a UserDataField key.
	 * @param id Review ticket ID
	 * @param field Key for Reviewinfo value
	 * @return String representing a part of the info from the Review ticket
	 */
	public String getString(int id, UserDataField field) {
		return yml.getString(field.getPath(id));
	}

	/**
	 * Sets a value for a userdata key.
	 * @param id Review ticket ID
	 * @param field Key for Reviewinfo
	 * @param value Value to be set for the key
	 */
	public void setString(int id, UserDataField field, String value) {
		yml.set(field.getPath(id), value);
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
				setString(0, field, data.get(field)); // Sets value for each key in yml
	}
	
	public void setOldScores(final HashMap<NewScoresField, String> data) {
		for(NewScoresField field: NewScoresField.values()) // Loops through its keys
			if(data.containsKey(field) && data.get(field) != null) // Checks if given data contains requested data
				setString(0, field, data.get(field)); // Sets value for each key in yml
	}
}
