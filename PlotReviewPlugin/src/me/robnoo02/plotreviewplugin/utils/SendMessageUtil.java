package me.robnoo02.plotreviewplugin.utils;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public enum SendMessageUtil {

	PLUGIN_INFO, HELP, SUBMIT, CANT_SUBMIT, NOT_ON_PLOT, ALREADY_SUBMITTED, PLOT_SUBMITTED, NO_PERMS, CANCELLED, CONFIRM_OR_CANCEL;

	private ArrayList<String> list = new ArrayList<>();

	public void set(ArrayList<String> list) {
		this.list = list;
	}

	public void send(CommandSender p) {
		for (String s : list)
			if (!s.equalsIgnoreCase("none"))
				p.sendMessage(TextManipulation.toColor(replacePlaceholders(p, s)));
	}

	public boolean send(CommandSender p, boolean retValue) {
		for (String s : list)
			if (!s.equalsIgnoreCase("none"))
				p.sendMessage(TextManipulation.toColor(replacePlaceholders(p, s)));
		return retValue;
	}

	private String replacePlaceholders(CommandSender p, String input) {
		for (Placeholder pH : Placeholder.values())
			input = replace(input, pH.placeholder(), pH.value());
		input = replace(input, "%target%", p.getName());
		return input;
	}

	private String replace(String original, String replaceThis, String replaceWithThis) {
		if (original.contains(replaceThis))
			return original.replaceAll(replaceThis, replaceWithThis);
		return original;
	}

}
