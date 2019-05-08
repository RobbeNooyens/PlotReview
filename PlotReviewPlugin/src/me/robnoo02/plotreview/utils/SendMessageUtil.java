package me.robnoo02.plotreview.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.robnoo02.plotreview.Query.QueryElement;;

public enum SendMessageUtil {

	CREDITS, HELP, SUBMIT, NOT_PLOTOWNER, NOT_ON_PLOT, ALREADY_SUBMITTED, PLOT_SUBMITTED, NO_PERMS, CANCELLED, 
	CONFIRM_OR_CANCEL, REVIEW_INFO, NO_PAST_REVIEWS, ONLINE_REVIEWED, OFFLINE_REVIEWED, CONFIG_RELOADED, 
	STAFF_REVIEWED_PLOT, REVIEW_SUMMARY, NO_PENDING_REVIEW, INVALID_ARGUMENT, CLICK_TO_REVIEW, WRONG_SYNTAX, 
	ALREADY_HAS_SUBMISSION, WRITE_COMMENT, CANT_REVIEW_OWN_PLOT, SOMETHING_WENT_WRONG;

	private ArrayList<String> list = new ArrayList<>();

	public void set(ArrayList<String> list) {
		this.list = list;
	}
	
	/*public ArrayList<String> get(CommandSender p, HashMap<QueryElement, String> data) {
		ArrayList<String> output = new ArrayList<>();
		Placeholder pH = new Placeholder(data);
		for (String s : list) {
			String send = ColorableText.toColor(pH.replace(s));
			if (send.equalsIgnoreCase("none")) continue;
			if (!send.toLowerCase().startsWith("@json"))
				output.add(send);
			else
				sendJson(p, send.replaceFirst("@json", ""));
		}
		return output;
	}*/

	public boolean send(CommandSender p) {
		for (String s : list) {
			if (s.equalsIgnoreCase("none")) continue;
			String send = Placeholder.replaceCustom(s);
			if (!s.toLowerCase().startsWith("@json"))
				p.sendMessage(ColorableText.toColor(send));
			else
				sendJson(p, send.replaceFirst("@json", ""));
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
	
	public boolean send(CommandSender p, QueryElement data, String value) {
		for (String s : list) {
			String send = ColorableText.toColor(s.replaceAll(data.getPlaceHolder(), value));
			send = Placeholder.replaceCustom(send);
			if (send.equalsIgnoreCase("none")) continue;
			if (!send.toLowerCase().startsWith("@json"))
				p.sendMessage(send);
			else
				sendJson(p, send.replaceFirst("@json", ""));
		}
		return true;
	}

	public static void sendJson(CommandSender p, String json) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + json);
	}
	
	public static void send(CommandSender p, ArrayList<String> list) {
		for(String s: list) {
			if(s == null || s.equalsIgnoreCase("none"))
				continue;
			String send = ColorableText.toColor(s);
			if (!send.toLowerCase().startsWith("@json"))
				p.sendMessage(send);
			else
				sendJson(p, send.replaceFirst("@json", ""));
		}
	}

}
