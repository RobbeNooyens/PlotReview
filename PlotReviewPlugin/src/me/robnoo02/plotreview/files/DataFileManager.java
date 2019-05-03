package me.robnoo02.plotreview.files;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.intellectualcrafters.plot.object.Plot;

import me.robnoo02.plotreview.review.ReviewID;
import me.robnoo02.plotreview.utils.PlotUtil;

public class DataFileManager {

	private static final String REVIEWPATH = "reviews.";
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
				uuidOutput.put(Integer.valueOf(key), getUUID(Integer.valueOf(key))); // Adds if not reviewed
		}
		return uuidOutput;
	}

	public static boolean containsId(final int ticketId) {
		return DataFile.getCustomYml().getConfigSection(REVIEWPATH).getKeys(false)
				.contains(String.valueOf(ticketId));
	}
	
	public static void addReview(int id, String value) {
		DataFile.getCustomYml().set(REVIEWPATH + String.valueOf(id), value);
	}

	public static void setReviewed(int plotId, boolean bool) {
		String current = getValue(plotId);
		if (current.contains("false"))
			current = current.replaceAll("false", String.valueOf(bool));
		else
			current = current.replaceAll("true", String.valueOf(bool));
		addReview(plotId, current);
	}
	
	public static String toDataFileFormat(String... strings) {
		StringBuilder builder = new StringBuilder();
		for(String s: strings) {
			builder.append(s);
			builder.append("+");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	
	/********************
	 * Methods used to get info from the datafile.yml
	 ********************/
	
	public static String getValue(final int ticketId) {
		return DataFile.getCustomYml().getString(REVIEWPATH + String.valueOf(ticketId));
	}
	
	public static String strip(final String input, final String regex, final int index) {
		String[] info = input.split(regex);
		return (index >= info.length) ? null : info[index];
	}
	
	public static String getReviewInfo(int ticketId, int infoPos) {
		return strip(getValue(ticketId), "\\+", infoPos);
	}
	
	public static String getUUID(final int ticketId) {
		return getReviewInfo(ticketId, 0);
	}
	
	public static String getPlotInfo(final int ticketId) {
		return getReviewInfo(ticketId, 1);
	}

	public static String getIsReviewed(final int ticketId) {
		return getReviewInfo(ticketId, 2);
	}
	
	
	public static String idFromPlot(final Plot plot) {
		ConfigurationSection section = DataFile.getCustomYml().getYml()
				.getConfigurationSection("reviews");
		if(section == null)
			return null;
		String formattedPlot = PlotUtil.formatPlot(plot);
		for (String s : section.getKeys(false))
			if (section.getString(s).contains(formattedPlot))
				return s;
		return null;
	}

	/**************
	 * Methods related to the ID-counter field
	 **************/

	public static int getIDProgress() {
		return (int) DataFile.getCustomYml().getInt(IDPATH);
	}

	public static void updateIDProgress() {
		DataFile.getCustomYml().set(IDPATH, ReviewID.getCurrentCount());
	}

}
