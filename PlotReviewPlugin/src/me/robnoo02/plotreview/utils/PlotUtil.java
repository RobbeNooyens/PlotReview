package me.robnoo02.plotreview.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;

/**
 * This class handles all PlotSquared activities and Plot formatting.
 * 
 * @author Robnoo02
 *
 */
public class PlotUtil {

	/**
	 * @return a Plot from a String containing a worldname and a PlotId.
	 */
	public static Plot getPlot(String plotLocation) {
		String world = getWorld(plotLocation);
		String id = getPlotId(plotLocation);
		return PS.get().getPlot(PlotArea.createGeneric(world), PlotId.fromString(id));
	}
	
	/**
	 * @return a Plot from a given world and a given PlotId.
	 */
	public static Plot getPlot(String world, String id) {
		return PS.get().getPlot(PS.get().getPlotArea(world, id), PlotId.fromString(id));
	}
	
	/**
	 * @return the Plot a Player is currently standing on.
	 */
	public static Plot getCurrentPlot(Player p) {
		return PlotPlayer.wrap(p).getCurrentPlot();
	}
	
	/**
	 * Returns String containing Plot info: {worldname}:{plotid}
	 * @return String containing a Plots world and a Plots Id
	 */
	public static String formatPlot(Plot plot) {
		return plot.getWorldName() + ":" + plot.getId().toString();
	}
	
	/**
	 * @return String with the name of the world of a Plot
	 */
	public static String getWorld(String plotLocation) {
		return plotLocation.substring(0, plotLocation.indexOf(":"));
	}
	
	/**
	 * @return String with the id of the world of a Plot
	 */
	public static String getPlotId(String plotLocation) {
		return plotLocation.substring(plotLocation.indexOf(":") + 1, plotLocation.length());
	}
	
	/**
	 * 
	 */
	public static int getRankedPlotCount(OfflinePlayer player) {
		int count = 0;
		for(Plot plot: PlotPlayer.wrap(player).getPlots()) {
			if(!plot.getWorldName().equalsIgnoreCase("freebuild"))
				count++;
		}
		return count;
	}
	
}
