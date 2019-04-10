package me.robnoo02.plotreviewplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.lucko.luckperms.LuckPerms;
import me.robnoo02.plotreviewplugin.files.ConfigManager;

public class RankUtil implements DebugUtil {

	public static String getRankName(Player p) {
		if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms"))
			return LuckPerms.getApi().getUser(p.getUniqueId()).getPrimaryGroup();
		Bukkit.getLogger().severe("§4Please make sure to install LuckPerms! Default rank is used for player " + p.getName());
		return "default";
	}

	public static String getRankFormatted(String rankName) {
		return ColorableText.toColor(ConfigManager.getString("ranks." + rankName));
	}

}
