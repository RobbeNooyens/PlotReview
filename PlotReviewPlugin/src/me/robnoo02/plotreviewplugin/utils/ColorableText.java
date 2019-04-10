package me.robnoo02.plotreviewplugin.utils;

import org.bukkit.ChatColor;

public class ColorableText {

	// Gives color to string
	static String toColor(String input) {
		return (ChatColor.translateAlternateColorCodes('&', input));
	}

	// Removes color from string
	static String removeColor(String input) {
		return (ChatColor.stripColor(input));
	}

}
