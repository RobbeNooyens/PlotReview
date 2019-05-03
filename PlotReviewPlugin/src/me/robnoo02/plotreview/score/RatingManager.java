package me.robnoo02.plotreview.score;

import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreview.utils.PlotUtil;

public class RatingManager {
	
	public static double getRating(OfflinePlayer player) {
		return STOCMaths.calcTotalStoc(player.getUniqueId().toString()) * calcStocPerPlot(player) * (STOCMaths.calcAverageScore() / 1000);
	}
	
	private static double calcStocPerPlot(OfflinePlayer player) {
		return STOCMaths.calcTotalStoc(player.getUniqueId().toString()) / PlotUtil.getRankedPlotCount(player);
	}

}
