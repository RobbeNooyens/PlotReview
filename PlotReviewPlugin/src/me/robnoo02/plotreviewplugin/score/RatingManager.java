package me.robnoo02.plotreviewplugin.score;

import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.utils.PlotUtil;

public class RatingManager {
	
	public static double getRating(OfflinePlayer player) {
		return STOCManager.calcTotalStoc() * calcStocPerPlot(player) * (STOCManager.calcAverageScore() / 1000);
	}
	
	private static double calcStocPerPlot(OfflinePlayer player) {
		return STOCManager.calcTotalStoc() / PlotUtil.getRankedPlotCount(player);
	}

}
