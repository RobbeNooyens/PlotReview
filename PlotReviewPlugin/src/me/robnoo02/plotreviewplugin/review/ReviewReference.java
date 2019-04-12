package me.robnoo02.plotreviewplugin.review;

/**
 * This class handles the values for each key in the config file.
 * It extracts information and builds String with given variables.
 * 
 * For example: c7a48d91-709d-4e60-9ba4-d4c37447c0ce+plotsworld:1;-1\+false is a reference
 * 
 * @author Robnoo02
 *
 */
public final class ReviewReference {
//	/*
//	 * PLEASE NOTE:
//	 * This class is bad designed and contains methods which
//	 * belong better to other classes.
//	 * The class should be rewritten and methods should
//	 * be moved to other classes.
//	 */
//
//	private static final String SPLIT = "\\+"; // 2x backslash since a +-sign is a special sign
//
//	// Static methods
//	/**
//	 * @return UUID of the Player who owns the reviewed plot
//	 */
//	public static String getUUID(String reference) {
//		String[] info = reference.split(SPLIT);
//		if (info.length == 3)
//			return info[0];
//		return null;
//	}
//
//	/**
//	 * @return world and PlotId of the reviewed plot
//	 */
//	public static String getPlotLocation(String reference) {
//		String[] info = reference.split(SPLIT);
//		if (info.length == 3)
//			return info[1];
//		return null;
//	}
//
//	/**
//	 * @return boolean formatted as a String whether or not the plot is reviewed yet or not
//	 */
//	public static String getReviewed(String reference) {
//		String[] info = reference.split(SPLIT);
//		if (info.length == 3)
//			return info[2];
//		return null;
//	}
//	
//	/**
//	 * Combines information to 1 String which can be saved in the datafile.
//	 * @return Reference as a String which contains a user UUID, a plot and a boolean
//	 */
//	public static String stringFormat(String uuid, String plotLocation, String reviewed) {
//		return uuid + "+" + plotLocation + "+" + reviewed;
//	}

}
