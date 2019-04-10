package me.robnoo02.plotreviewplugin.commands;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.files.DataFileManager;
import me.robnoo02.plotreviewplugin.files.UserDataFile;
import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;
import me.robnoo02.plotreviewplugin.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreviewplugin.guis.HistoryGui;
import me.robnoo02.plotreviewplugin.guis.ReviewListGui;
import me.robnoo02.plotreviewplugin.review.ReviewScore;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

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
		if (args.length == 0) return SendMessageUtil.PLUGIN_INFO.send(sender, true); // Subcommand required
		switch (args[0]) {
		case "help": // /review help
			return SendMessageUtil.HELP.send(sender, true); // Shows help page with possible (sub)commands
		case "list": // /review list
			return (!(sender instanceof Player)) ? true : ReviewListGui.show((Player) sender, 1, null); // Opens Gui
		case "info": // /review info <id>
			if (args.length < 2) return true;
			String id = args[1]; // Review ticket ID
			if (!StringUtils.isNumeric(id)) return true; // Prevent Cast exception; exits when not a valid number is given
			if (DataFileManager.containsId(Integer.valueOf(id))) return true; // Prevent nullpointer exception
			String uuid = DataFileManager.getUUID(Integer.valueOf(id)); // extracts Player UUID from datafile
			SendMessageUtil.REVIEW_INFO.sendReview(sender, id, uuid,
					UserDataManager.getUserData(Integer.valueOf(id)));
			/*
			 * ^^^^ This is a big problem. SendMessageUtil replaces all placeholders from
			 * the message with the values of the parameters.
			 */
			break;
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
			if (args.length < 3) return true;
			int scoreId = Integer.valueOf(args[1]);
			String score = args[2];
			ReviewScore reviewScore = ReviewScore.fromString(score);
			String userUUID = DataFileManager.getUUID(Integer.valueOf(scoreId));
			UserDataFile userFile = UserDataManager.getUserDataFile(userUUID);
			userFile.setString(scoreId, UserDataField.STRUCTURE_SCORE,
					String.valueOf(reviewScore.getStructurePoints()));
			userFile.setString(scoreId, UserDataField.TERRAIN_SCORE, String.valueOf(reviewScore.getTerrainPoints()));
			userFile.setString(scoreId, UserDataField.ORGANICS_SCORE, String.valueOf(reviewScore.getOrganicsPoints()));
			userFile.setString(scoreId, UserDataField.COMPOSITION_SCORE,
					String.valueOf(reviewScore.getCompositionPoints()));
			userFile.setString(scoreId, UserDataField.RESULT, String.valueOf(reviewScore.calculateOverall()));
			userFile.setString(scoreId, UserDataField.STAFF, sender.getName().toString());
			DataFileManager.setReviewed(Integer.valueOf(scoreId), true);
			break;
		}
		return true; // Returns true instead of false to prevent that the plugins sends annoying messages to the player
	}

}
