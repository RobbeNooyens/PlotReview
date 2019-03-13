package me.robnoo02.plotreviewplugin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.intellectualcrafters.plot.object.Plot;

/**
 * This class formats certain values, especially for ReviewReferences.
 * @author Robnoo02
 *
 */
public class FormatterUtil {

	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	public static String formatPlot(Plot plot) {
		return plot.getWorldName() + ":" + plot.getId().toString();
	}
	
	public static String getWorld(String plotLocation) {
		return plotLocation.substring(0, plotLocation.indexOf(":"));
	}
	
	public static String getPlotId(String plotLocation) {
		return plotLocation.substring(plotLocation.indexOf(":") + 1, plotLocation.length());
	}
	
}
