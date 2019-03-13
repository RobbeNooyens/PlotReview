package me.robnoo02.plotreviewplugin.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.files.DataFile;
import me.robnoo02.plotreviewplugin.files.UserDataManager;
import me.robnoo02.plotreviewplugin.guis.GuiFactory;
import me.robnoo02.plotreviewplugin.guis.GuiUtil.Gui;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class ReviewCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender))
			return true;
		if (!cmd.getName().equalsIgnoreCase("review"))
			return true;
		if (args.length == 0)
			return SendMessageUtil.PLUGIN_INFO.send(sender, true);
		switch (args[0]) { // /review args[0]
		case "help": // Help cmd
			SendMessageUtil.HELP.send(sender);
			break;
		case "list": // Opens Gui with all unreviewed reviews
			if (!(sender instanceof Player))
				return true;
			Gui gui = GuiFactory.reviewGui((Player) sender, 1, null);
			gui.open();
			break;
		case "info": // Displays info for a review
			if (args.length < 2)
				return true; // args[2] is the Review ticket ID
			String id = args[1];
			if (!StringUtils.isNumeric(id))
				return true; // Prevent Cast exception
			if (DataFile.getInstance().getValue(Integer.valueOf(id)) == null)
				return true; // Prevent nullpointer exception
			String uuid = DataFile.getInstance().getUUIDString(Integer.valueOf(id)); // extracts Player UUID from datafile
			SendMessageUtil.REVIEW_INFO.sendReview(sender, id, uuid,
					UserDataManager.getInstance().getUserData(uuid, id));
			break;

		}
		return true;
	}

}
