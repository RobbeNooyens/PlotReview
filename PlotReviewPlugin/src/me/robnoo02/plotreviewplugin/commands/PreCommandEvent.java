package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.robnoo02.plotreviewplugin.submit.SubmitManager;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class PreCommandEvent implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) { // Removes submit from queue + end convo
		Player p = e.getPlayer();
		try {
			if (SubmitManager.getInstance().isSubmitQueued(p) && !e.getMessage().toLowerCase().startsWith("/submit")
					&& !e.getMessage().toLowerCase().startsWith("/prdebug")) {
				SubmitManager.getInstance().removeSubmitQueue(p);
				SendMessageUtil.CANCELLED.send(p);
			}
		} catch (Exception ignore) {
		}
	}

}
