package me.robnoo02.plotreview.files;

import java.util.ArrayList;
import java.util.List;

import me.robnoo02.plotreview.Main;
import me.robnoo02.plotreview.utils.SendMessageUtil;

/**
 * This class represents the config.yml.
 * It reads and writes data to that file.
 * All fields and methods are static since there's only one config.
 * 
 * @author Robnoo02
 *
 */
public final class ConfigManager {

	private static final String MESSAGES_PATH = "messages."; // Path in config file to messages
	private static Main plugin; // Main instance

	public static void setup() {
		if(plugin == null) {
			plugin = Main.getInstance();
			setEnumMessages();
		}
	}
	
	public static void reload() {
		plugin.reloadConfig();
		setEnumMessages();
	}
	
	/**
	 * Returns string from Yml
	 * @param key is the path in the yml
	 * @return the value of the given key
	 */
	public static String getString(String key) {
		return plugin.getConfig().contains(key) ? plugin.getConfig().getString(key) : null;
	}

	/**
	 * Saves message block to config
	 * @param path is the path where the list should be saved to
	 * @param list is the list of String that should be saved
	 */
	public static void setList(String path, List<String> list) {
		plugin.getConfig().set(path, list);
		plugin.saveConfig();
	}

	/**
	 * Reads all messages from config and sets Enum values for SendMessageUtil
	 * Only called on setup
	 */
	private static void setEnumMessages() {
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

	/*public static HashMap<ScoreAspect, Double> getScore(String score) {
		String scoreFormat = plugin.getConfig().getString("score");
		String separator = plugin.getConfig().getString("score-separator");
		HashMap<ScoreAspect, Double> scores = new HashMap<>();
		String[] givenPoints = score.split(separator);
		String[] format = scoreFormat.split(separator);
		if (givenPoints.length != format.length)
			return null;
		for (int i = 0; i < format.length; i++) {
			String s = format[i];
			if (s.equalsIgnoreCase("%STRUCTURE%")) {
				scores.put(ScoreAspect.STRUCTURE, Double.valueOf(givenPoints[i]));
			} else if (s.equalsIgnoreCase("%TERRAIN%")) {
				scores.put(ScoreAspect.TERRAIN, Double.valueOf(givenPoints[i]));
			} else if (s.equalsIgnoreCase("%ORGANICS%")) {
				scores.put(ScoreAspect.ORGANICS, Double.valueOf(givenPoints[i]));
			} else if (s.equalsIgnoreCase("%COMPOSITION%")) {
				scores.put(ScoreAspect.COMPOSITION, Double.valueOf(givenPoints[i]));
			}
		}
		return scores;
	}*/
}
