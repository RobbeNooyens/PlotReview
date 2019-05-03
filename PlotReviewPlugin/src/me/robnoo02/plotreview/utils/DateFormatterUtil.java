package me.robnoo02.plotreview.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class formats dates which can be used in userfiles.
 * @author Robnoo02
 *
 */
public class DateFormatterUtil {

	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
}
