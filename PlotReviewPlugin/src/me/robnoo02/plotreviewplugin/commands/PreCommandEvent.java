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
 * The method cancels the event when the player should confirm the submit, but doesn't do so. (Convo)
 * 
 * @author Robnoo02
 *
 */
public class PreCommandEvent implements Listener {

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		try {
			if (SubmitManager.isSubmitQueued(p) && !e.getMessage().toLowerCase().startsWith("/submit")
					&& !e.getMessage().toLowerCase().startsWith("/prdebug")) { // Command isn't /submit or /prdebug
				SubmitManager.removeSubmitQueue(p); // Player's UUID will be removed from Submit Queue
				SendMessageUtil.CANCELLED.send(p); // Confirmation that Submit has been cancelled
			}
		} catch (Exception ignore) {
		}
	}

}
