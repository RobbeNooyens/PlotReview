package me.robnoo02.plotreviewplugin.files;

import java.util.ArrayList;
import java.util.List;

import me.robnoo02.plotreviewplugin.Main;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

/**
 * This singleton writes and reads data from the config.yml.
 * 
 * @author Robnoo02
 *
 */
public final class ConfigManager {

	private static final ConfigManager INSTANCE = new ConfigManager(); // Singleton instance
	private static final String MESSAGES_PATH = "messages."; 
	private final Main plugin; // Main instance

	/**
	 * Constructor
	 * Private for singleton
	 */
	private ConfigManager() {
		this.plugin = Main.getInstance();
	}

	/**
	 * Static factory method to get singleton
	 */
	public static ConfigManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Returns string from Yml
	 */
	public String getString(String key) {
		return plugin.getConfig().getString(key);
	}

	/**
	 * Saves message block to config
	 */
	public void setList(String path, List<String> list) {
		plugin.getConfig().set(path, list);
		plugin.saveConfig();
	}

	/**
	 * Reads all messages from config and sets Enum values for SendMessageUtil
	 */
	public void setEnumMessages() {
		for (SendMessageUtil messageVar : SendMessageUtil.values()) {
			String path = MESSAGES_PATH + messageVar.toString().toLowerCase();
			if (plugin.getConfig().contains(path)) {
				messageVar.set((ArrayList<String>) plugin.getConfig().getStringList(path));
			} else {
				ArrayList<String> none = new ArrayList<>();
				none.add("none");
				setList(path, none);
				messageVar.set(none);
			}
		}
	}
}
