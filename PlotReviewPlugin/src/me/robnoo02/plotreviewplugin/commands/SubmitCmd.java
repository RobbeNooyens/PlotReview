package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.submit.SubmitManager;
import me.robnoo02.plotreviewplugin.utils.PermissionUtil;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class SubmitCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if (!cmd.getName().equalsIgnoreCase("submit"))
			return true; // Command is /submit
		if (SubmitManager.getInstance().isSubmitQueued(p)) {
			if(args.length > 0) {
				String subCmd = args[0];
				if(subCmd.equalsIgnoreCase("confirm")) { // /submit confirm -> submit plot -> remove from queue
					SubmitManager.getInstance().submitPlot(p);
					SubmitManager.getInstance().removeSubmitQueue(p);
				} else if(subCmd.equalsIgnoreCase("cancel")) { // /submit cancel -> remove from queue
					SubmitManager.getInstance().removeSubmitQueue(p);
					SendMessageUtil.CANCELLED.send(p);
				} else {
					SendMessageUtil.CONFIRM_OR_CANCEL.send(p);
				}
			} else {
				SendMessageUtil.CONFIRM_OR_CANCEL.send(p);
			}
			
		} else {
			if (SubmitManager.getInstance().possibleToSubmit(p)) {
				SubmitManager.getInstance().addSubmitQueue(p); // Wait for confirmation
				if (PermissionUtil.SUBMIT_PLOT.hasAndWarn((Player) sender))
					return SendMessageUtil.SUBMIT.send(sender, true);
			}
		}
		return true;
	}

}
