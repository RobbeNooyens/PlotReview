package me.robnoo02.plotreview.utils;

import org.bukkit.ChatColor;

public class ColorableText {

	// Gives color to string
	static String toColor(String input) {
		try {
			return (ChatColor.translateAlternateColorCodes('&', input));
		} catch (Exception e) {
			return input;
		}
	}

	// Removes color from string
	static String removeColor(String input) {
		try {
			return (ChatColor.stripColor(input));
		} catch (Exception e) {
			return input;
		}
	}

}
