package me.robnoo02.plotreviewplugin.utils;

import org.bukkit.Bukkit;

public interface DebugUtil {
	
	public static boolean debug = false;

	default void debug(Object obj, String message) {
		if(debug)
			Bukkit.getConsoleSender().sendMessage("§3[Debug] §b" + obj.getClass().getSimpleName() + ": §2" + message);
	}
	
}
