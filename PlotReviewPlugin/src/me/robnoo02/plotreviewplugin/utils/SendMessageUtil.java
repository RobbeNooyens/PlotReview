package me.robnoo02.plotreviewplugin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.robnoo02.plotreviewplugin.files.UserDataFileFields.TicketDataField;;

public enum SendMessageUtil {

	CREDITS, HELP, SUBMIT, NOT_PLOTOWNER, NOT_ON_PLOT, ALREADY_SUBMITTED, PLOT_SUBMITTED, NO_PERMS, CANCELLED, 
	CONFIRM_OR_CANCEL, REVIEW_INFO, NO_PAST_REVIEWS, ONLINE_REVIEWED, OFFLINE_REVIEWED;

	private ArrayList<String> list = new ArrayList<>();

	public void set(ArrayList<String> list) {
		this.list = list;
	}

	public void send(CommandSender p) {
		for (String s : list)
			if (!s.equalsIgnoreCase("none")) p.sendMessage(ColorableText.toColor(replacePlaceholders(p, s)));
	}

	public boolean send(CommandSender p, boolean retValue) {
		send(p);
		return retValue;
	}

	public void sendReview(CommandSender p, String id, String uuid, HashMap<TicketDataField, String> data) {
		for (String s : list) {
			String send = ColorableText.toColor(reviewPlaceHolders(s, id, uuid, data));
			if (!send.equalsIgnoreCase("none")) p.sendMessage(send);
		}
	}

	private String replacePlaceholders(CommandSender p, String input) {
		for (Placeholder pH : Placeholder.values())
			input = replace(input, pH.placeholder(), pH.value());
		input = replace(input, "%target%", p.getName());
		return input;
	}

	private String reviewPlaceHolders(String input, String id, String uuid, HashMap<TicketDataField, String> data) {
		input = replace(input, "%review_id%", id);
		for (TicketDataField field : data.keySet())
			if (data.get(field) != null)
				input = replace(input, field.getPlaceHolder(), data.get(field));
			else if (input.contains(field.getPlaceHolder())) return "none";
		input = replace(input, "%reviewee%", Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
		return input;
	}

	private String replace(String original, String replaceThis, String replaceWithThis) {
		if (original.contains(replaceThis)) return original.replaceAll(replaceThis, replaceWithThis);
		return original;
	}

}
