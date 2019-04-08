package me.robnoo02.plotreviewplugin.files;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.intellectualcrafters.plot.object.Plot;

import me.robnoo02.plotreviewplugin.review.ReviewID;
import me.robnoo02.plotreviewplugin.review.ReviewReference;
import me.robnoo02.plotreviewplugin.utils.PlotUtil;

public class DataFileManager {

	private static final String REVIEWPATH = "reviews";
	private static final String IDPATH = "id-counter";
	
	/**
	 * Extracts all unreviewed reviews saved in yml.
	 * @return HashMap containing Review ID and reference from datafile
	 */
	public static HashMap<Integer, String> getUnreviewedReferences() {
		Set<String> keys = DataFile.getInstance().getYmlFile().getConfigurationSection(REVIEWPATH).getKeys(false); // All saved Review ID's
		HashMap<Integer, String> uuidOutput = new HashMap<>();
		for(String key: keys) { // Loops through keys to determine which Reviews aren't reviewed yet
			String value = getValue(Integer.valueOf(key));
			if(value.contains("false"))
				uuidOutput.put(Integer.valueOf(key), ReviewReference.getUUID(value));
		}
		return uuidOutput;
	}
	
	public static String getReviewInfo(int reviewId, int infoPos) {
		String[] info = getValue(reviewId).split("\\+");
		return (infoPos >= info.length) ? null : info[infoPos];
	}

	public static String getValue(int reviewId) {
		return DataFile.getInstance().getCustomYml().getString(REVIEWPATH + "." + String.valueOf(reviewId));
	}

	public static String getUUID(int reviewId) {
		return getReviewInfo(reviewId, 0);
	}

	public static void addReview(int id, String uuid) {
		DataFile.getInstance().getCustomYml().set(REVIEWPATH + "." + String.valueOf(id), uuid);
	}

	public static String getReviewID(final Plot plot) {
		ConfigurationSection section = DataFile.getInstance().getYmlFile().getConfigurationSection("reviews");
		String formattedPlot = PlotUtil.formatPlot(plot);
		for (String s : section.getKeys(false))
			if (section.getString(s).contains(formattedPlot))
				return s;
		return null;
	}
	
	public static void setReviewed(int plotId, boolean bool) {
		String current = getValue(plotId);
		if(current.contains("false"))
			current = current.replaceAll("false", String.valueOf(bool));
		else 
			current = current.replaceAll("true", String.valueOf(bool));
		addReview(plotId, current);
	}
	
	/*
	 * ID Methods 
	 */
	
	public static int getIDProgress() {
		return (int) DataFile.getInstance().getCustomYml().getInt(IDPATH);
	}

	public static void updateIDProgress() {
		DataFile.getInstance().getCustomYml().set(IDPATH, ReviewID.getCurrentCount());
	}
	
}
