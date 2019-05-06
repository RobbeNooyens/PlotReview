package me.robnoo02.plotreview.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.lucko.luckperms.LuckPerms;
import me.robnoo02.plotreview.files.ConfigManager;

public class RankUtil implements DebugUtil {

	public static String getRankName(Player p) {
		if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms"))
			return LuckPerms.getApi().getUser(p.getUniqueId()).getPrimaryGroup();
		Bukkit.getLogger()
				.severe("§4Please make sure to install LuckPerms! Default rank is used for player " + p.getName());
		return "default";
	}

	public static String getRankFormatted(String rankName) {
		return ColorableText.toColor(ConfigManager.getString("ranks." + rankName));
	}

	public static enum BuildRank {
		DEFAULT, NOVICE, APPRENTICE, DESIGNER, ARCHITECT, ARTISAN, MASTER;

		private static final String PERMISSION = "rank.build.";
		private static final BuildRank[] ranking = {MASTER, ARTISAN, ARCHITECT, DESIGNER, APPRENTICE, NOVICE, DEFAULT};

		public static BuildRank getHeighest(Player player) {
			for (BuildRank rank : ranking) {
				if (rank.hasRankPerm(player)) return rank;
			}
			return BuildRank.DEFAULT;

		}
		
		public boolean hasRankPerm(Player player) {
			return player.hasPermission(PERMISSION + lowerCase());
		}
		
		public String lowerCase() {
			return this.toString().toLowerCase();
		}
	}

}
