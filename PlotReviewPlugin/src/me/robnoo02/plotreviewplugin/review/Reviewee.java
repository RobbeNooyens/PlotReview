package me.robnoo02.plotreviewplugin.review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class Reviewee implements ReviewPlayer {

	private final UUID uuid;
	private final String name, currentRank;
	private Date date;
	
	private Reviewee(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
		this.currentRank = "";
		this.date = new Date();
	}
	
	private Reviewee(String uuid, String rank, String date) {
		this.uuid = UUID.fromString(uuid);
		OfflinePlayer p = Bukkit.getOfflinePlayer(this.uuid);
		this.name = p.getName();
		this.currentRank = rank;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.date = format.parse(date);
		} catch (ParseException e) {
			this.date = new Date();
		} 
	}
	
	public static Reviewee wrap(OfflinePlayer p) {
		return new Reviewee(p.getUniqueId(), p.getName());
	}
	
	public static Reviewee load(String uuid, String rank, String date) {
		return new Reviewee(uuid, rank, date);
	}
	
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Date getDate() {
		return date;
	}
	
	public String getDateFormatted() {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}
	
	public String getCurrentRank() {
		return currentRank;
	}
}
