package me.robnoo02.plotreviewplugin.utils;

import org.bukkit.Bukkit;

/**
 * Debugs Strings in the console.
 * Methods are named debugUtil to avoid confusion with other debug methods.
 * @author Robnoo02
 *
 */
public interface DebugUtil {
	
	public static boolean debug = false;

	/**
	 * Debugs classname with message if debugging is enabled
	 * @param obj Classname is used to start debug message with
	 * @param message Message to be printed
	 */
	default void debugUtil(Object obj, String message) {
		if(debug)
			Bukkit.getConsoleSender().sendMessage("§3[Debug] §b" + obj.getClass().getSimpleName() + ": §2" + message);
	}
	
	/**
	 * Simply debugs a message if debugging is enabled
	 * @param message Message to be printed
	 */
	default void debugUtil(String message) {
		if(debug)
			Bukkit.getConsoleSender().sendMessage("§3[Debug] §2" + message);
	}
}
