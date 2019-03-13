package me.robnoo02.plotreviewplugin.files;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

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
	 * Private in combination with static factory
	 * @param uuid The uuid of a Reviewee
	 */
	private UserDataFile(UUID uuid) {
		this.uuid = uuid.toString();
		this.yml = CustomYml.createFileInFolder("userdata", this.uuid, true);
	}
	
	/**
	 * Returns an instance of a UserDataFile for a player
	 * @return UserDataFile instance for given Player
	 */
	public static UserDataFile getUserDataFile(OfflinePlayer player) {
		return getUserDataFile(player.getUniqueId());
	}
	
	/**
	 * Returns an instance of a UserDataFile for a player with UUID
	 * @return UserDataFile instance for given Player
	 */
	public static UserDataFile getUserDataFile(UUID uuid) {
		UserDataFile file = new UserDataFile(uuid);
		file.yml.setup();
		file.yml.set("latest-name", Bukkit.getOfflinePlayer(uuid).getName());
		return file;
	}
	
	/**
	 * Each enum value represents a key in a userdate yml.
	 * Placeholder is used to replace the yml's value with.
	 * @param pH Placeholder
	 * @author Robnoo02
	 *
	 */
	public static enum UserDataField {
		RANK("%rank%"), DATE("%date%"), WORLD("%world%"), PLOT("%plot%"), RESULT("%result%"), SCORE("%score%"), STAFF("%staff%");
		
		private String placeholder;
		
		private UserDataField(String pH) {
			this.placeholder = pH;
		}
		
		/**
		 * Returns path to get a value in a userdata yml.
		 * @param id Review ticket ID
		 * @return Path for yml
		 */
		public String getPath(String id) {
			return "tickets." + id + "." + this.toString().toLowerCase();
		}
		public String getPlaceHolder() {
			return placeholder;
		}
	}
	
	/**
	 * Gets a yml value obtainable with a UserDataField key.
	 * @param id Review ticket ID
	 * @param field Key for Reviewinfo value
	 * @return String representing a part of the info from the Review ticket
	 */
	public String getString(String id, UserDataField field) {
		return (String) yml.get(field.getPath(id));
	}
	
	/**
	 * Sets a value for a userdata key.
	 * @param id Review ticket ID
	 * @param field Key for Reviewinfo
	 * @param value Value to be set for the key
	 */
	public void setString(String id, UserDataField field, String value) {
		yml.set(field.getPath(id), value);
	}
	
	/**
	 * Gets the ymlFile used by this instance.
	 * @return YamlConfiguration for userdata configfile
	 */
	public YamlConfiguration getYml() {
		return yml.getYml();
	}
}
