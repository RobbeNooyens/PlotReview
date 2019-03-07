package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.review.ReviewManager;
import me.robnoo02.plotreviewplugin.submit.SubmitManager;
import me.robnoo02.plotreviewplugin.utils.PermissionUtil;

public class DebugCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender))
			return true;
		if (!cmd.getName().equalsIgnoreCase("prdebug"))
			return true;
		if(sender instanceof Player && !PermissionUtil.DEBUG.hasAndWarn((Player) sender))
			return true;
		sender.sendMessage("§aSubmits:");
		sender.sendMessage(SubmitManager.getInstance().getSubmits());
		sender.sendMessage("§aReviews:");
		sender.sendMessage(ReviewManager.getInstance().getReviewsString());
		return true;
	}
}
