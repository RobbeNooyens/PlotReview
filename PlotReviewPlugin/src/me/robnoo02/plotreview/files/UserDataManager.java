package me.robnoo02.plotreview.files;

import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreview.files.UserDataFileFields.PlayerInfoField;

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
		if(StringUtils.isNumeric(uuidString)) // String is accidently String.valueof(ticketId)
			return getUserDataFile(String.valueOf(uuidString));
		final UUID uuid = UUID.fromString(uuidString); // Converts String uuid to UUID object
		if (!files.containsKey(uuid)) // Checks if UserDataFile object for specific UUID exists.
			files.put(uuid, getUserDataFile(uuid)); // Creates new instance if not existed.
		return files.get(uuid); // Returns UserDataFile for UUID
	}
	
	public static UserDataFile getUserDataFile(final int ticketId) {
		return getUserDataFile(DataFileManager.getUUID(ticketId));
	}
	
	private static UserDataFile getUserDataFile(UUID uuid) {
		UserDataFile file = new UserDataFile(uuid);
		file.setString(0, PlayerInfoField.LATEST_NAME, Bukkit.getOfflinePlayer(uuid).getName());
		return file;
	}
	
	public static UserDataFile getUserDataFile(final OfflinePlayer player) {
		return getUserDataFile(player.getUniqueId().toString());
	}
	
}
