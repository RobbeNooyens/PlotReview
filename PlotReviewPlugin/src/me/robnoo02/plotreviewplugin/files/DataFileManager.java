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
	 * 
	 * @return HashMap containing the ticketId as key and a String value from a datafile.
	 */
	public static HashMap<Integer, String> getUnreviewedReferences() {
		Set<String> keys = DataFile.getCustomYml().getYml().getConfigurationSection(REVIEWPATH)
				.getKeys(false);
		HashMap<Integer, String> uuidOutput = new HashMap<>();
		for (String key : keys) { // Loops through keys to determine which Reviews aren't reviewed yet
			String value = getValue(Integer.valueOf(key));
			if (value.contains("false"))
				uuidOutput.put(Integer.valueOf(key), ReviewReference.getUUID(value)); // Adds if not reviewed
		}
		return uuidOutput;
	}

	public static String getReviewInfo(int reviewId, int infoPos) {
		return strip(getValue(reviewId), "\\+", infoPos);
	}

	public static String strip(final String input, final String regex, final int index) {
		String[] info = input.split(regex);
		return (index >= info.length) ? null : info[index];
	}

	public static String getValue(int reviewId) {
		return DataFile.getCustomYml().getString(REVIEWPATH + "." + String.valueOf(reviewId));
	}

	public static boolean containsId(int reviewId) {
		return DataFile.getCustomYml().getConfigSection(REVIEWPATH).getKeys(false)
				.contains(String.valueOf(reviewId));
	}

	public static String getUUID(int reviewId) {
		return getReviewInfo(reviewId, 0);
	}

	public static void addReview(int id, String uuid) {
		DataFile.getCustomYml().set(REVIEWPATH + "." + String.valueOf(id), uuid);
	}

	public static String idFromPlot(final Plot plot) {
		ConfigurationSection section = DataFile.getCustomYml().getYml()
				.getConfigurationSection("reviews");
		String formattedPlot = PlotUtil.formatPlot(plot);
		for (String s : section.getKeys(false))
			if (section.getString(s).contains(formattedPlot))
				return s;
		return null;
	}

	public static void setReviewed(int plotId, boolean bool) {
		String current = getValue(plotId);
		if (current.contains("false"))
			current = current.replaceAll("false", String.valueOf(bool));
		else
			current = current.replaceAll("true", String.valueOf(bool));
		addReview(plotId, current);
	}

	/*
	 * ID Methods
	 */

	public static int getIDProgress() {
		return (int) DataFile.getCustomYml().getInt(IDPATH);
	}

	public static void updateIDProgress() {
		DataFile.getCustomYml().set(IDPATH, ReviewID.getCurrentCount());
	}

}
