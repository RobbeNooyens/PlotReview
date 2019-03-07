package me.robnoo02.plotreviewplugin.utils;

import org.bukkit.ChatColor;

public class TextManipulation {

	// Gives color to string
		public static String toColor(String input) {
			String output = input;
			output = (ChatColor.translateAlternateColorCodes('&', output));
			return output;
		}

		// Removes color from string
		public static String removeColor(String input) {
			String output = input;
			output = (ChatColor.stripColor(output));
			return output;
		}
	
}
