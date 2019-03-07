package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.robnoo02.plotreviewplugin.submit.SubmitManager;
import me.robnoo02.plotreviewplugin.utils.DebugUtil;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class PreCommandEvent implements Listener, DebugUtil {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		debug(this, e.getMessage());
		try {
			if (SubmitManager.getInstance().isSubmitQueued(p) && !e.getMessage().toLowerCase().startsWith("/submit")) {
				SubmitManager.getInstance().removeSubmitQueue(p);
				SendMessageUtil.CANCELLED.send(p);
			}
		} catch (Exception ignore) {
		}
	}

}
