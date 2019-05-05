package me.robnoo02.plotreview.commands;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreview.Query.QueryGroup;
import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataFile;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.guis.HistoryGui;
import me.robnoo02.plotreview.guis.ReviewListGui;
import me.robnoo02.plotreview.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreview.handlers.ScoreHandler;
import me.robnoo02.plotreview.utils.SendMessageUtil;

/**
 * This class handles the /review command.
 * 
 * @author Robnoo02
 *
 */
public class ReviewCmd implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true; // Sender should be Player or Console
		if (!cmd.getName().equalsIgnoreCase("review")) return true; // Command starts with /review
		if (args.length == 0) return SendMessageUtil.HELP.send(sender, true); // Subcommand required
		switch (args[0]) {
		case "help": // /review help
			return SendMessageUtil.HELP.send(sender, true); // Shows help page with possible (sub)commands
		case "list": // /review list
			return (!(sender instanceof Player)) ? true : ReviewListGui.show((Player) sender, 1, null); // Opens Gui
		case "credits":
			return SendMessageUtil.CREDITS.send(sender, QueryGroup.PLUGIN.get(0));
		case "info": // /review info <id>
			if (args.length < 2) return true;
			if (!StringUtils.isNumeric(args[1])) return true; // Prevent Cast exception; exits when not a valid number is given
			int id = Integer.valueOf(args[1]); // Review ticket ID
			if (!DataFileManager.containsId(id)) return true; // Prevent nullpointer exception
			SendMessageUtil.REVIEW_INFO.send(sender, QueryGroup.INFO.get(id));
			/*
			 * ^^^^ This is a big problem. SendMessageUtil replaces all placeholders from
			 * the message with the values of the parameters.
			 */
			return true;
		case "history":
			if (!(sender instanceof Player)) return true; // Sender should be a Player
			Player p = (Player) sender;
			OfflinePlayer target; // Target is the person whose history is requested
			if (args.length < 2)
				target = p;
			else
				target = Bukkit.getOfflinePlayer(args[1]);
			if (target == null) target = p;
			UserDataFile file = UserDataManager.getUserDataFile(String.valueOf(target.getUniqueId()));
			if (!file.getCustomYml().getYml().getKeys(false).contains("tickets"))
				return SendMessageUtil.NO_PAST_REVIEWS.send(p, true); // No review tickets saved for this player
			ArrayList<GuiItem> items = new ArrayList<>();
			for (String idKey : file.getCustomYml().getYml().getConfigurationSection("tickets").getKeys(false)) {
				items.add(HistoryGui.getHistoryItem(p, file.getUserData(Integer.valueOf(idKey)),
						target.getUniqueId().toString(), Integer.valueOf(idKey)));
			}
			return HistoryGui.show(p, 1, items, null);
		case "score":
			if(!(sender instanceof Player))
				return true;
			if (args.length < 3) return true;
			int ticketId = Integer.valueOf(args[1]);
			
			ScoreHandler.handleNewReview(ticketId, args[2], ((Player) sender));
			
			/*String userUUID = DataFileManager.getUUID(Integer.valueOf(ticketId));
			UserDataFile userFile = UserDataManager.getUserDataFile(userUUID);
			HashMap<STOC, String> scores = STOC.fromStringStrings(args[2]);
			userFile.setString(ticketId, TicketDataField.STRUCTURE_SCORE, scores.get(STOC.STRUCTURE));
			userFile.setString(ticketId, TicketDataField.TERRAIN_SCORE, scores.get(STOC.TERRAIN));
			userFile.setString(ticketId, TicketDataField.ORGANICS_SCORE, scores.get(STOC.ORGANICS));
			userFile.setString(ticketId, TicketDataField.COMPOSITION_SCORE, scores.get(STOC.COMPOSITION));
			userFile.setString(ticketId, TicketDataField.STOC, String.valueOf(STOCMaths.calcAverage(scores, ticketId)));
			userFile.setString(ticketId, TicketDataField.STAFF, ((Player) sender).getUniqueId().toString());*/
			DataFileManager.setReviewed(Integer.valueOf(ticketId), true);
			return true;
		case "reload":
			return true;
		default:
			return true;
		}
	}

}
