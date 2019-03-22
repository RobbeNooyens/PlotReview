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

/**
 * This class handles the /review command.
 * 
 * @author Robnoo02
 *
 */
public class ReviewCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender))
			return true; // Sender is a Player or Console
		if (!cmd.getName().equalsIgnoreCase("review"))
			return true; // Command is /review
		if (args.length == 0)
			return SendMessageUtil.PLUGIN_INFO.send(sender, true); // Command has at least 1 argument: /review <arg>
		switch (args[0]) { // /review args[0]
		case "help": // Help cmd
			SendMessageUtil.HELP.send(sender); // Sends textblock with commands and other help to player
			break;
		case "list": // Opens Gui with all unreviewed reviews
			if (!(sender instanceof Player))
				return true; // Sender should be a Player in order to open up a Gui
			Gui gui = GuiFactory.reviewGui((Player) sender, 1, null);
			gui.open();
			break;
		case "info": // Displays info for a review
			if (args.length < 2)
				return true; // args[2] is the Review ticket ID
			String id = args[1];
			if (!StringUtils.isNumeric(id))
				return true; // Prevent Cast exception; exits when not a valid number is given
			if (DataFile.getInstance().getValue(Integer.valueOf(id)) == null)
				return true; // Prevent nullpointer exception
			String uuid = DataFile.getInstance().getUUIDString(Integer.valueOf(id)); // extracts Player UUID from datafile
			SendMessageUtil.REVIEW_INFO.sendReview(sender, id, uuid,
					UserDataManager.getInstance().getUserData(uuid, id));
			break;

		}
		return true; // Returns true instead of false to prevent that the plugins sends annoying messages to the player
	}

}
