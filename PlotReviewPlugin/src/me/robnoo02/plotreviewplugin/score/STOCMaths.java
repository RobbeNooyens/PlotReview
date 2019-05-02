package me.robnoo02.plotreviewplugin.score;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.robnoo02.plotreviewplugin.files.UserDataFile;
import me.robnoo02.plotreviewplugin.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;

public class STOCMaths {

	public static double calcTotalStoc(String uuid) {
		UserDataFile file = UserDataManager.getUserDataFile(uuid);
		double totalStoc = 0;
		for(String key: file.getCustomYml().getConfigSection("tickets").getKeys(false)) {
			totalStoc += Double.valueOf(file.getString(Integer.valueOf(key), TicketDataField.STOC));
		}
		return totalStoc;
	}

	public static double calcAverageScore() {
		// == calcAverage
		return 0;
	}
	
	public static double calcAverage(HashMap<STOC, String> scores, int ticketId) {
		Set<Double> usedVals = new HashSet<>();
		for(String s: scores.values())
			if(!(Double.valueOf(s) == 0)) usedVals.add(Double.valueOf(s));
		int divider = usedVals.size();
		if(divider <= 0)
			return 0;
		DecimalFormat df = new DecimalFormat("#.#");
		return Double.valueOf(df.format((calcSum(usedVals, ticketId)) / divider).replaceAll(",", "."));
	}
	
	public static double calcStocSum(HashMap<STOC, String> scores) {
		double output = 0;
		for(String score: scores.values()) {
			try {
				output += Double.valueOf(score);
			} catch(ClassCastException ignore) {
				// ignore
			}
		}
		return output;
	}
	
	private static double calcSum(Set<Double> values, int ticketId) {
		double output = 0;
		for(double dbl: values)
			output += dbl;
		return output;
	}

}
