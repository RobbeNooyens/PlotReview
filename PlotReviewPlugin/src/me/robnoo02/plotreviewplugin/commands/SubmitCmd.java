package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.submit.SubmitManager;
import me.robnoo02.plotreviewplugin.utils.PermissionUtil;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

/**
 * This class handles the /submit command. A submit will be saved in a queue on
 * /submit. A submit can be confirmed with /submit confirm and cancelled with
 * /submit cancel. Submit will be removed from queue after
 * confirming/cancelling.
 * 
 * @author Robnoo02
 *
 */
public class SubmitCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true; // Sender should be a Player, not Console
		Player p = (Player) sender; // Casting sender to Player
		if (!cmd.getName().equalsIgnoreCase("submit")) // Stop if command doesn't start with /submit
			return true; // Command is /submit
		if (SubmitManager.isSubmitQueued(p)) {
			if (args.length == 0)
				return SendMessageUtil.CONFIRM_OR_CANCEL.send(p, true); // Player should type /submit <confirm/cancel>
			String confirmCancel = args[0];
			if (confirmCancel.equalsIgnoreCase("confirm")) // /submit confirm -> submit plot -> remove from queue
				SubmitManager.submitPlot(p); // Creates new ticket
			else if (confirmCancel.equalsIgnoreCase("cancel")) // /submit cancel -> remove from queue
				SendMessageUtil.CANCELLED.send(p); // No return because submit should still be removed from queue
			else
				return SendMessageUtil.CONFIRM_OR_CANCEL.send(p, true); // Stop method to make sure submit won't be removed
			SubmitManager.removeSubmitQueue(p);
		} else {
			if (PermissionUtil.SUBMIT_PLOT.hasAndWarn((Player) sender))
				return true;
			if (SubmitManager.possibleToSubmit(p))
				SubmitManager.addSubmitQueue(p); // Wait for confirmation
			SendMessageUtil.SUBMIT.send(sender, true);
		}
		return true;
	}

}
