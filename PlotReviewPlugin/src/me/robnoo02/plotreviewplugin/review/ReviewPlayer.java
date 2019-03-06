package me.robnoo02.plotreviewplugin.review;

import java.util.Date;

import org.bukkit.OfflinePlayer;

public interface ReviewPlayer {
	
	OfflinePlayer getOfflinePlayer();
	String getName();
	Date getDate();
	
}
