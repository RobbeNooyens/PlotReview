package me.robnoo02.plotreview.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.robnoo02.plotreview.Query.QueryElement;;

public enum SendMessageUtil {

	CREDITS, HELP, SUBMIT, NOT_PLOTOWNER, NOT_ON_PLOT, ALREADY_SUBMITTED, PLOT_SUBMITTED, NO_PERMS, CANCELLED, CONFIRM_OR_CANCEL, REVIEW_INFO, NO_PAST_REVIEWS, ONLINE_REVIEWED, OFFLINE_REVIEWED, CONFIG_RELOADED, PLOT_REVIEWED, STAFF_REVIEWED_PLOT;

	private ArrayList<String> list = new ArrayList<>();

	public void set(ArrayList<String> list) {
		this.list = list;
	}

	public boolean send(CommandSender p) {
		for (String s : list) {
			if (s.equalsIgnoreCase("none")) continue;
			if (!s.toLowerCase().startsWith("@json"))
				p.sendMessage(ColorableText.toColor(s));
			else
				sendJson(p, s.replaceFirst("@json", ""));
		}
		return true;
	}

	public boolean send(CommandSender p, boolean retValue) {
		send(p);
		return retValue;
	}

	public boolean send(CommandSender p, HashMap<QueryElement, String> data) {
		Placeholder pH = new Placeholder(data);
		for (String s : list) {
			String send = ColorableText.toColor(pH.replace(s));
			if (send.equalsIgnoreCase("none")) continue;
			if (!send.toLowerCase().startsWith("@json"))
				p.sendMessage(send);
			else
				sendJson(p, send.replaceFirst("@json", ""));
		}
		return true;
	}

	private void sendJson(CommandSender sender, String json) {
		Bukkit.dispatchCommand(sender, "tellraw " + sender.getName() + " " + json);
	}

}
