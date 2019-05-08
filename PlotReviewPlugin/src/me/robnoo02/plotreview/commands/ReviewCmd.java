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

import me.robnoo02.plotreview.Query.QueryElement;
import me.robnoo02.plotreview.Query.QueryGroup;
import me.robnoo02.plotreview.files.ConfigManager;
import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataFile;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreview.guis.HistoryGui;
import me.robnoo02.plotreview.guis.ReviewListGui;
import me.robnoo02.plotreview.handlers.ScoreHandler;
import me.robnoo02.plotreview.utils.PermissionUtil;
import me.robnoo02.plotreview.utils.PlotUtil;
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
		OfflinePlayer target;
		Player p;
		int ticketId;
		switch (args[0]) {
		case "help": // /review help
			return SendMessageUtil.HELP.send(sender, true); // Shows help page with possible (sub)commands
		case "list": // /review list
			return (!(sender instanceof Player)) ? true : ReviewListGui.show((Player) sender, 1, null); // Opens Gui
		case "credits":
			return SendMessageUtil.CREDITS.send(sender, QueryGroup.PLUGIN.get(0));
		case "info": // /review info <id>
			if (args.length < 2) return SendMessageUtil.WRONG_SYNTAX.send(sender, true);
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
			p = (Player) sender;
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
				items.add(HistoryGui.getHistoryItem(p, QueryGroup.HISTORY.get(Integer.valueOf(idKey)),
						target.getUniqueId().toString(), Integer.valueOf(idKey)));
			}
			return HistoryGui.show(p, 1, items, null, target);
		case "score":
			if (!(sender instanceof Player)) return true;
			if (args.length < 3) return SendMessageUtil.WRONG_SYNTAX.send(sender, true);
			ticketId = Integer.valueOf(args[1]);
			p = (Player) sender;
			if (PlotUtil.getPlot(QueryElement.WORLD.request(ticketId, p), QueryElement.PLOT_ID.request(ticketId, p))
					.getOwners().contains(p.getUniqueId()))
				if (!PermissionUtil.BYPASS_REVIEW_OWN_PLOT.has(p))
					return SendMessageUtil.CANT_REVIEW_OWN_PLOT.send(p, true);
			ScoreHandler.handleNewReview(ticketId, args[2], ((Player) sender));
			DataFileManager.setReviewed(Integer.valueOf(ticketId), true);
			return SendMessageUtil.STAFF_REVIEWED_PLOT.send(sender, true);
		case "summary":
			if (args.length == 1) {
				if (!(sender instanceof Player)) return true;
				if (Boolean.valueOf(QueryElement.NEW_SCORES_AVAILABLE.request(-1, (Player) sender))) {
					int lastId = Integer.valueOf(QueryElement.LAST_TICKET_ID.request(-1, (Player) sender));
					if (lastId < 0) return SendMessageUtil.NO_PAST_REVIEWS.send(sender);
					SendMessageUtil.REVIEW_SUMMARY.send(sender, QueryGroup.REVIEW_SUMMARY.get(lastId));
				} else {
					return SendMessageUtil.NO_PENDING_REVIEW.send(sender);
				}
			} else {
				target = Bukkit.getOfflinePlayer(args[1]);
				if (target == null) return SendMessageUtil.INVALID_ARGUMENT.send(sender);
				if (Boolean.valueOf(QueryElement.NEW_SCORES_AVAILABLE.request(-1, target))) {
					// Casting will be succesfull: casted from int to String
					int lastId = Integer.valueOf(QueryElement.LAST_TICKET_ID.request(-1, target));
					if (lastId < 0) return SendMessageUtil.NO_PAST_REVIEWS.send(sender);
					SendMessageUtil.REVIEW_SUMMARY.send(sender, QueryGroup.REVIEW_SUMMARY.get(lastId));
				} else {
					return SendMessageUtil.NO_PENDING_REVIEW.send(sender);
				}
			}
			return true;
		case "comment":
			if(!(sender instanceof Player)) return true;
			if(args.length < 3) return SendMessageUtil.WRONG_SYNTAX.send(sender, true);
			if(!StringUtils.isNumeric(args[1])) return SendMessageUtil.INVALID_ARGUMENT.send(sender, true);
			ticketId = Integer.valueOf(args[1]);
			p = (Player) sender;
			if(!DataFileManager.getValue(ticketId).contains("failed"))
				return SendMessageUtil.SOMETHING_WENT_WRONG.send(sender);
			if(!QueryElement.STAFF_UUID.request(ticketId, p).equalsIgnoreCase(p.getUniqueId().toString()))
				return SendMessageUtil.SOMETHING_WENT_WRONG.send(sender);
			//if(!QueryElement.COMMENT.request(ticketId, p).equalsIgnoreCase("none"))
			//	return true;
			StringBuilder comment = new StringBuilder();
			for(int i = 0; i < args.length; i++) {
				if(i == 0 || i == 1)
					continue;
				comment.append(args[i] + ((i == (args.length - 1)) ? "" : " "));
			}
			UserDataManager.getUserDataFile(ticketId).setString(ticketId, TicketDataField.COMMENT, comment.toString());
			return true;
		case "rl":
		case "reload":
			ConfigManager.reload();
			SendMessageUtil.CONFIG_RELOADED.send(sender);
			return true;
		default:
			return true;
		}
	}

}
