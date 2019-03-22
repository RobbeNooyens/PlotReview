package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.robnoo02.plotreviewplugin.submit.SubmitManager;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

/**
 * This class handles the PlayerCommandPreProcess-event.
 * This event is called before a command is executed.
 * The method cancels the event when the player should confirm the submit, but doesn't do so.
 * 
 * @author Robnoo02
 *
 */
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
