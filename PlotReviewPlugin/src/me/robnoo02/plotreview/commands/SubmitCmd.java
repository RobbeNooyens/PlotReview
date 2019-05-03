package me.robnoo02.plotreview.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreview.submit.SubmitManager;
import me.robnoo02.plotreview.utils.PermissionUtil;
import me.robnoo02.plotreview.utils.SendMessageUtil;

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
			return true;
		Player p = (Player) sender;
		if (!cmd.getName().equalsIgnoreCase("submit"))
			return true;
		if (SubmitManager.isSubmitQueued(p)) {
			if (args.length == 0)
				return SendMessageUtil.CONFIRM_OR_CANCEL.send(p, true);
			String confirmCancel = args[0];
			if (confirmCancel.equalsIgnoreCase("confirm"))
				SubmitManager.submitPlot(p);
			else if (confirmCancel.equalsIgnoreCase("cancel"))
				SendMessageUtil.CANCELLED.send(p);
			else
				return SendMessageUtil.CONFIRM_OR_CANCEL.send(p, true);
			SubmitManager.removeSubmitQueue(p);
			return true;
		} else {
			if (!PermissionUtil.SUBMIT_PLOT.hasAndWarn((Player) sender))
				return true;
			if (SubmitManager.possibleToSubmit(p))
				SubmitManager.addSubmitQueue(p);
			return SendMessageUtil.SUBMIT.send(sender, true);
		}
	}

}
