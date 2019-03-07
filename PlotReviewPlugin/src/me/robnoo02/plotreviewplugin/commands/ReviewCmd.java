package me.robnoo02.plotreviewplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class ReviewCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player || sender instanceof ConsoleCommandSender))
			return true;
		if(!cmd.getName().equalsIgnoreCase("review"))
			return true;
		if(args.length == 0)
			return SendMessageUtil.PLUGIN_INFO.send(sender, true);
		switch(args[0]) {
		case "help":
			SendMessageUtil.HELP.send(sender);
			break;
		}
		return true;
	}

	
	
}
