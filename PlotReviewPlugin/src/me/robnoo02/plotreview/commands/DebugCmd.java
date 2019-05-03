package me.robnoo02.plotreview.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.robnoo02.plotreview.Main;
import me.robnoo02.plotreview.files.DataFile;
import me.robnoo02.plotreview.files.UserDataFile;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.submit.SubmitManager;
import me.robnoo02.plotreview.utils.PermissionUtil;

/**
 * This class handles the /prdebug command.
 * The command can display current queued submits and yml-file contents.
 * 
 * @author Robnoo02
 *
 */
public class DebugCmd implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player || sender instanceof ConsoleCommandSender))
			return true; // sender is Player or Console
		if (!cmd.getName().equalsIgnoreCase("prdebug"))
			return true;  // Command is /prdebug
		if (sender instanceof Player && !PermissionUtil.DEBUG.hasAndWarn((Player) sender))
			return true; // If sender is Player, he has required permissions
		if (args.length == 0)
			return true; // There are arguments
		switch (args[0]) {
		case "submits": // Shows queue of submits
			sender.sendMessage("§aSubmits:");
			sender.sendMessage(SubmitManager.getSubmits());
			break;
		case "config":
			FileConfiguration config = Main.getInstance().getConfig();
			sender.sendMessage("§3ranks§8:");
			for(String s: config.getConfigurationSection("ranks").getKeys(false))
				sender.sendMessage("  §3" + s + "§8: §7" + config.getString("ranks." + s));
			sender.sendMessage("§3messages§8:");
			for(String s: config.getConfigurationSection("messages").getKeys(false)) {
				sender.sendMessage("  §3" + s + "§8:");
				for(String line: config.getStringList("messages." + s))
					sender.sendMessage("  §8- §7" + line);
			}
			break;
		case "datafile": // Prints content of datafile.yml in console or chat
			YamlConfiguration datafile = DataFile.getCustomYml().getYml();
			sender.sendMessage("§3id-counter§8: §7" + datafile.getString("id-counter"));
			sender.sendMessage("§3reviews:");
			if(datafile.getConfigurationSection("reviews") == null) {
				break;
			}
			for (String key : datafile.getConfigurationSection("reviews").getKeys(false)) {
				sender.sendMessage("  §3" + key + "§8: §7" + datafile.getString("reviews." + key));
			}
			break;
		case "userdata": // Prints content of userdatafile in console or chat
			if (args.length == 1)
				return true; // args[1] represents target player to get info for
			String uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString();
			File file = new File(Main.getInstance().getDataFolder() + File.separator + "userdata", uuid + ".yml");
			if (!file.exists()) {
				sender.sendMessage("§cFile doesn't exist");
				return true;
			}
			UserDataFile userFile = UserDataManager.getUserDataFile(uuid);
			sender.sendMessage("§3latest-name§8: §7" + userFile.getCustomYml().getYml().getString("latest-name"));
			sender.sendMessage("§3tickets§8:");
			for(String key: userFile.getCustomYml().getYml().getConfigurationSection("tickets").getKeys(false)) {
				sender.sendMessage("  §3'" + key + "'§8:");
				for(String subKey: userFile.getCustomYml().getYml().getConfigurationSection("tickets." + key).getKeys(false)) {
					sender.sendMessage("    §3" + subKey + "§8: §7" + userFile.getCustomYml().getYml().getString("tickets." + key + "." + subKey));
				}
			}
			break;
		}
		return true;
	}
}
