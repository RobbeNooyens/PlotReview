package me.robnoo02.plotreviewplugin.review;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;

import me.robnoo02.plotreviewplugin.utils.FormatterUtil;

/**
 * This class handles the values for each key in the config file.
 * It extracts information and builds String with given variables.
 * @author Robnoo02
 *
 */
public final class ReviewReference {
	
	/*
	 * PLEASE NOTE:
	 * This class is bad designed and contains methods which
	 * belong better to other classes.
	 * The class should be rewritten and methods should
	 * be moved to other classes.
	 */

	private static final String SPLIT = "\\+";

	// Static methods
	public static String getUUID(String reference) {
		String[] info = reference.split(SPLIT);
		if (info.length == 3)
			return info[0];
		return null;
	}

	public static String getPlotLocation(String reference) {
		String[] info = reference.split(SPLIT);
		if (info.length == 3)
			return info[1];
		return null;
	}

	public static String getReviewed(String reference) {
		String[] info = reference.split(SPLIT);
		if (info.length == 3)
			return info[2];
		return null;
	}
	
	public static Plot getPlot(String plotLocation) {
		String world = FormatterUtil.getWorld(plotLocation);
		String id = FormatterUtil.getPlotId(plotLocation);
		return PS.get().getPlot(PlotArea.createGeneric(world), PlotId.fromString(id));
	}
	
	public static Plot getPlot(String world, String id) {
		return PS.get().getPlot(PS.get().getPlotArea(world, id), PlotId.fromString(id));
	}
	
	public static String stringFormat(String uuid, String plotLocation, String reviewed) {
		return uuid + "+" + plotLocation + "+" + reviewed;
	}

}
