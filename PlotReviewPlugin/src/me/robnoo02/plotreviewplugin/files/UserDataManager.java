package me.robnoo02.plotreviewplugin.files;

import java.util.HashMap;
import java.util.UUID;

import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;

/**
 * This singleton stores UserDataFile objects.
 * It creates new instances if it doesn't exist yet.
 * @author Robnoo02
 *
 */
public class UserDataManager {
	
	private static final HashMap<UUID, UserDataFile> files = new HashMap<>(); // Stores references to UserDataFiles

	/**
	 * Registers UserDataFile if not yet existed and returns one based on UUID.
	 * @param uuidString Player UUID which represents key for UserDataFile
	 * @return UserDataFile object for given Players UUID
	 */
	public static UserDataFile getUserDataFile(final String uuidString) {
		final UUID uuid = UUID.fromString(uuidString); // Converts String uuid to UUID object
		if (!files.containsKey(uuid)) // Checks if UserDataFile object for specific UUID exists.
			files.put(uuid, UserDataFile.getUserDataFile(uuid)); // Creates new instance if not existed.
		return files.get(uuid); // Returns UserDataFile for UUID
	}
	
	public static UserDataFile getUserDataFile(final int reviewId) {
		return getUserDataFile(DataFileManager.getUUID(reviewId));
	}
	
	/**
	 * Writes given data to userdata file.
	 * @param userUUID UUID of player to save data for
	 * @param id Review ticket ID
	 * @param data HashMap containing UserDataField as a key and its corresponding value as a String
	 */
	public static void setUserData(final int id, final HashMap<UserDataField, String> data) {
		final UserDataFile file = getUserDataFile(id); // Gets UserDataFile object for Player
		for(UserDataField field: UserDataField.values()) // Loops through its keys
			if(data.containsKey(field) && data.get(field) != null) // Checks if given data contains requested data
				file.setString(id, field, data.get(field)); // Sets value for each key in yml
	}
	
	/**
	 * Reads information from a saved Review in yml file.
	 * @param uuid UUID of ticketowner
	 * @param id Review ticket ID
	 * @return HashMap containing all available reviewdata for a Player
	 */
	public static  HashMap<UserDataField, String> getUserData(final int id) {
		UserDataFile file = getUserDataFile(id); // Gets 
		return file.getUserData(id);
	}
}
