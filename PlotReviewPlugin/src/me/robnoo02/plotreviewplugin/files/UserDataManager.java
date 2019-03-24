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
	
	private final HashMap<UUID, UserDataFile> files = new HashMap<>(); // Stores references to UserDataFiles
	private static final UserDataManager INSTANCE = new UserDataManager(); // Singleton instance
	
	/**
	 * Singleton > private constructor
	 */
	private UserDataManager() {
	}
	
	/**
	 * Singleton > public static factory method
	 * @return instance
	 */
	public static UserDataManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Registers UserDataFile if not yet existed and returns one based on UUID.
	 * @param uuidString Player UUID which represents key for UserDataFile
	 * @return UserDataFile object for given Players UUID
	 */
	public UserDataFile getUserDataFile(String uuidString) {
		UUID uuid = UUID.fromString(uuidString); // Converts String uuid to UUID object
		if (!files.containsKey(uuid)) // Checks if UserDataFile object for specific UUID exists.
			files.put(uuid, UserDataFile.getUserDataFile(uuid)); // Creates new instance if not existed.
		return files.get(uuid); // Returns UserDataFile for UUID
	}
	
	/**
	 * Writes given data to userdata file.
	 * @param userUUID UUID of player to save data for
	 * @param id Review ticket ID
	 * @param data HashMap containing UserDataField as a key and its corresponding value as a String
	 */
	public void setUserData(String userUUID, String id, HashMap<UserDataField, String> data) {
		UserDataFile file = getUserDataFile(userUUID); // Gets UserDataFile object for Player
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
	public HashMap<UserDataField, String> getUserData(String uuid, String id) {
		UserDataFile file = getUserDataFile(uuid); // Gets 
		return file.getUserData(id);
	}
}
