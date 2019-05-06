package me.robnoo02.plotreview.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.robnoo02.plotreview.Query.QueryElement;
import me.robnoo02.plotreview.files.UserDataFileFields.NewScoresField;
import me.robnoo02.plotreview.files.UserDataManager;

public class ReviewJoinLeaveEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if(Boolean.valueOf(QueryElement.NEW_SCORES_AVAILABLE.request(-1, p)))
			UserDataManager.getUserDataFile(p).setString(-1, NewScoresField.NEW_SCORES_AVAILABLE, "false");
	}
	
}
